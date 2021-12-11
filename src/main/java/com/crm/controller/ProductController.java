package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

/**
 * A controller for the product page.
 */
@Controller
@RequestMapping("product")
public class ProductController {
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
    public ProductController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Product} by id
     *
     * @param id      the id of {@link Product}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Product} not found
     */
    static Product getProduct(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found");

        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found");

        return product.get();
    }

    /**
     * [GET]
     * mapping to /cost/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({ "", "/index" })
    public void index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Product> productPage = dataSet.products.findAll(id);
        model.addAttribute("model", productPage);
    }

    /**
     * [GET]
     * mapping to /cost/search?page=0&search=
     *
     * @param search the name of {@link Product}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        if (search.isEmpty())
            return "redirect:/product";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.products.findAllByNameContains(id, search));
        return "product/index";
    }

    /**
     * [GET]
     * mapping to /cost/create?id=0
     */
    @GetMapping("create")
    public void create(Model model) {
        model.addAttribute("model", new Product());
    }

    /**
     * [POST]
     * mapping to /cost/create
     *
     * @param product the {@link Product}
     */
    @PostMapping("create")
    public String create(Product product) {
        dataSet.products.save(product);
        return "redirect:/product";
    }

    /**
     * [GET]
     * mapping to /cost/details?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [GET]
     * mapping to /cost/edit?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /cost/edit
     *
     * @param product the {@link Product}
     */
    @PostMapping("edit")
    public String edit(Product product, Model model) {
        // check if exists
        getProduct(product.getId(), dataSet);
        dataSet.products.save(product);
        return "redirect:/product";
    }

    /**
     * [GET]
     * mapping to /cost/delete?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /cost/delete
     *
     * @param id the id of {@link Product}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        dataSet.products.delete(getProduct(id, dataSet));
        return "redirect:/product";
    }
}