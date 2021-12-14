package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * A controller for the client page.
 */
@Controller
@RequestMapping("client")
public class ClientController {
    private final PasswordEncoder passwordEncoder;
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
    public ClientController(final DataSet dataSet, final PasswordEncoder passwordEncoder) {
        this.dataSet = dataSet;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get {@link Client} by id
     *
     * @param id      the id of {@link Client}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Client} not found
     */
    static Client getClient(final Integer id, final DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client not found");

        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client not found");

        return client.get();
    }

    /**
     * [GET]
     * mapping to /client/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") final int page, final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Client> clientPage = dataSet.clients.findAll(id);
        model.addAttribute("model", clientPage);
        return "client/index";
    }

    /**
     * [GET]
     * mapping to /client/search?page=0&search=
     *
     * @param search the name of {@link Client}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/client";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.clients.findAllByNameContains(id, search));
        return "client/index";
    }

    /**
     * [GET]
     * mapping to /client/create?id=0
     */
    @GetMapping("create")
    public void create(final Model model) {
        model.addAttribute("model", new Client());
    }

    /**
     * [POST]
     * mapping to /client/create
     *
     * @param client the {@link Client}
     */
    @PostMapping("create")
    public String create(final Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        dataSet.clients.save(client);
        return "redirect:/client";
    }

    /**
     * [GET]
     * mapping to /client/details?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    /**
     * [GET]
     * mapping to /client/edit?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /client/edit
     *
     * @param client the {@link Client}
     */
    @PostMapping("edit")
    public String edit(final Client client, final Model model) {
        // check if exists
        Client client1 = getClient(client.getId(), dataSet);
        client1.setName(client.getName());
        client1.setTel(client.getTel());
        client1.setCredit(client.getCredit());
        dataSet.clients.save(client1);
        return "redirect:/client";
    }

    /**
     * [GET]
     * mapping to /client/resetPwd?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("resetPwd")
    public void resetPwd(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /client/resetPwd
     *
     * @param client the {@link Client}
     */
    @PostMapping("resetPwd")
    public String resetPwd(final Client client,
                           @RequestParam final String old_password,
                           final Model model) {
        // check if exists
        Client client1 = getClient(client.getId(), dataSet);
        if (this.passwordEncoder.matches(old_password, client1.getPassword())) {
            client1.setPassword(passwordEncoder.encode(client.getPassword()));
            dataSet.clients.save(client1);
        } else {
            model.addAttribute("model", getClient(client.getId(), dataSet));
            model.addAttribute("error", "请输入正确的密码");
            return "client/resetPwd";
        }
        return "redirect:/client";
    }

    /**
     * [GET]
     * mapping to /client/delete?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /client/delete
     *
     * @param id the id of {@link Client}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam final int id) {
        dataSet.clients.delete(getClient(id, dataSet));
        return "redirect:/client";
    }
}
