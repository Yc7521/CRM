package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Cost;
import com.crm.model.Employee;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;
import static com.crm.controller.CostController.getCost;
import static com.crm.controller.ProductController.getProduct;

/**
 * A controller for the shop page.
 */
@Controller
@RequestMapping("shop")
public class ShopController {
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
    public ShopController(final DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get an anonymous {@link Employee}
     *
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Employee} not found
     */
    static Employee getAnonymousEmployee(final DataSet dataSet) {
        final Employee anonymous = dataSet.employees.findManagerByName("Anonymous");
        if (anonymous == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Critical error");
        return anonymous;
    }

    /**
     * [GET]
     * mapping to /shop/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") final int page,
                        final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Product> productPage = dataSet.products.findAll(id);
        model.addAttribute("model", productPage);
        return "shop/index";
    }

    /**
     * [GET]
     * mapping to /shop/search?page=0&search=
     *
     * @param search the name of {@link Employee}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/shop";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.products.findAllByNameContains(id, search));
        return "shop/index";
    }

    /**
     * [GET]
     * mapping to /shop/buy?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("buy")
    public void buy(@RequestParam(required = false) final Integer id,
                    final Model model) throws Exception {
        final Cost cost = new Cost();
        // get a user who is logged in
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Client client) {
            cost.setClient(client);
        } else {
            throw new Exception("You are not a client");
        }
        cost.setEmployee(getAnonymousEmployee(dataSet));

        if (id != null) {
            final Product product = getProduct(id, dataSet);
            // may be not need to dec num here
            // if (product.getNum() > 0)
            //     product.decNum();
            // else
            //     throw new Exception("not found");
            cost.setProduct(product);
        }

        model.addAttribute("model", cost);
        model.addAttribute("clients", dataSet.clients.findAll());
        model.addAttribute("products", dataSet.products.findAll());
        model.addAttribute("employees", dataSet.employees.findAll());
    }

    /**
     * [POST]
     * mapping to /shop/buy
     *
     * @param shop       the {@link Cost}
     * @param client_id  the id of {@link Client}
     * @param product_id the id of {@link Product}
     */
    @PostMapping("buy")
    public String buy(final Cost shop,
                      @RequestParam final Integer client_id,
                      @RequestParam final Integer product_id) {
        shop.setClient(ClientController.getClient(client_id, dataSet));
        shop.setProduct(getProduct(product_id, dataSet));
        shop.setEmployee(getAnonymousEmployee(dataSet));
        dataSet.costs.save(shop);
        return "redirect:/shop";
    }

    /**
     * [GET]
     * mapping to /shop/details?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id,
                        final Model model) {
        model.addAttribute("model", getProduct(id, dataSet));
    }
}