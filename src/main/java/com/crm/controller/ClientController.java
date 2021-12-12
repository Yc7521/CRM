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
    public ClientController(DataSet dataSet, PasswordEncoder passwordEncoder) {
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
    static Client getClient(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client not found");

        Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client not found");

        return client.get();
    }

    /**
     * [GET]
     * mapping to /client/[index]?page=0
     *
     * @param page a number of page
     * @return
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        Page<Client> clientPage = this.dataSet.clients.findAll(id);
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
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/client";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.clients.findAllByNameContains(id, search));
        return "client/index";
    }

    /**
     * [GET]
     * mapping to /client/create?id=0
     */
    @GetMapping("create")
    public void create(Model model) {
        model.addAttribute("model", new Client());
    }

    /**
     * [POST]
     * mapping to /client/create
     *
     * @param client the {@link Client}
     */
    @PostMapping("create")
    public String create(Client client) {
        client.setPassword(this.passwordEncoder.encode(client.getPassword()));
        this.dataSet.clients.save(client);
        return "redirect:/client";
    }

    /**
     * [GET]
     * mapping to /client/details?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", ClientController.getClient(id, this.dataSet));
    }

    /**
     * [GET]
     * mapping to /client/edit?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", ClientController.getClient(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /client/edit
     *
     * @param client the {@link Client}
     */
    @PostMapping("edit")
    public String edit(Client client, Model model) {
        // check if exists
        ClientController.getClient(client.getId(), this.dataSet);
        this.dataSet.clients.save(client);
        return "redirect:/client";
    }

    /**
     * [GET]
     * mapping to /client/delete?id=0
     *
     * @param id the id of {@link Client}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", ClientController.getClient(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /client/delete
     *
     * @param id the id of {@link Client}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        this.dataSet.clients.delete(ClientController.getClient(id, this.dataSet));
        return "redirect:/client";
    }
}
