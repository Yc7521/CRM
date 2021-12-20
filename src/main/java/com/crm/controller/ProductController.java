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
    public ProductController(final DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Product} by id
     *
     * @param id      the id of {@link Product}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Product} not found
     */
    static Product getProduct(final Integer id, final DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found");

        final Optional<Product> product = dataSet.products.findById(id);
        if (product.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found");

        return product.get();
    }

    /**
     * [GET]
     * mapping to /product/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") final int page,
                        final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Product> productPage = dataSet.products.findAll(id);
        model.addAttribute("model", productPage);
        return "product/index";
    }

    /**
     * [GET]
     * mapping to /product/search?page=0&search=
     *
     * @param search the name of {@link Product}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/product";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.products.findAllByNameContains(id, search));
        return "product/index";
    }

    /**
     * [GET]
     * mapping to /product/create?id=0
     */
    @GetMapping("create")
    public void create(final Model model) {
        model.addAttribute("model", new Product());
    }

    /**
     * [POST]
     * mapping to /product/create
     *
     * @param product the {@link Product}
     */
    @PostMapping("create")
    public String create(final Product product) {
        dataSet.products.save(product);
        return "redirect:/product";
    }

    /**
     * [GET]
     * mapping to /product/details?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id,
                        final Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [GET]
     * mapping to /product/edit?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) final Integer id,
                     final Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /product/edit
     *
     * @param product the {@link Product}
     */
    @PostMapping("edit")
    public String edit(final Product product, final Model model) {
        // check if exists
        Product product1 = getProduct(product.getId(), dataSet);
        product1.setName(product.getName());
        product1.setTime(product.getTime());
        product1.setType(product.getType());
        product1.setPrice(product.getPrice());
        product1.setNum(product.getNum());
        dataSet.products.save(product1);
        return "redirect:/product";
    }

    /**
     * [GET]
     * mapping to /product/delete?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) final Integer id,
                       final Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /product/delete
     *
     * @param id the id of {@link Product}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam final int id) {
        dataSet.products.delete(getProduct(id, dataSet));
        return "redirect:/product";
    }
}