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

@Controller
@RequestMapping("client")
public class ClientController {
    private final PasswordEncoder passwordEncoder;
    private final DataSet dataSet;

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

        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client not found");

        return client.get();
    }

    @GetMapping({"", "/index"})
    public void index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Client> clientPage = dataSet.clients.findAll(id);
        model.addAttribute("model", clientPage);
    }

    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/client";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.clients.findAllByNameContains(id, search));
        return "client/index";
    }

    @GetMapping("create")
    public void create(Model model) {
        model.addAttribute("model", new Client());
    }

    @PostMapping("create")
    public String create(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        dataSet.clients.save(client);
        return "redirect:/client";
    }

    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    @PostMapping("edit")
    public String edit(Client client, Model model) {
        // check if exists
        getClient(client.getId(), dataSet);
        dataSet.clients.save(client);
        return "redirect:/client";
    }

    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getClient(id, dataSet));
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        dataSet.clients.delete(getClient(id, dataSet));
        return "redirect:/client";
    }
}
