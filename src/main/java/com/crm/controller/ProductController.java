package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

@Controller
@RequestMapping("product")
public class ProductController {
    private final DataSet dataSet;

    public ProductController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Product> productPage = dataSet.products.findAll(id);
        model.addAttribute("model", productPage);
        return "product/index";
    }

    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/product";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.products.findAllByNameContains(id, search));
        return "product/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("model", new Product());
        return "product/create";
    }

    @PostMapping("create")
    public String create(Product product) {
        dataSet.products.save(product);
        return "redirect:/product/";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id,
                          Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", product.get());
        return "product/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id,
                       Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", product.get());
        return "product/edit";
    }

    @PostMapping("edit")
    public String edit(Product product, Model model) {
        final Optional<Product> data = dataSet.products.findById(product.getId());
        if (data.isPresent()) {
            final Product product1 = data.get();
            product1.setName(product.getName());
            product1.setTime(product.getTime());
            product1.setType(product.getType());
            product1.setPrice(product.getPrice());
            dataSet.products.save(product1);
            model.addAttribute("model", product1);
            return "redirect:/product";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id,
                         Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", product.get());
        return "product/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty()) return "redirect:/notfound";

        dataSet.products.delete(product.get());
        return "redirect:/product";
    }
}