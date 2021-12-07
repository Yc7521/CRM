package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

@Controller
@RequestMapping("client")
public class clientController {
    private final DataSet dataSet;
    PasswordEncoder passwordEncoder;

    public clientController(DataSet dataSet, PasswordEncoder passwordEncoder) {
        this.dataSet = dataSet;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Client> clientPage = dataSet.clients.findAll(id);
        model.addAttribute("model", clientPage);
        return "client/index";
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
    public String create(Model model) {
        model.addAttribute("model", new Client());
        return "client/create";
    }

    @PostMapping("create")
    public String create(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        dataSet.clients.save(client);
        return "redirect:/client/";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id,
                          Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", client.get());
        return "client/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id,
                       Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", client.get());
        return "client/edit";
    }

    @PostMapping("edit")
    public String edit(Client client, Model model) {
        final Optional<Client> data = dataSet.clients.findById(client.getId());
        if (data.isPresent()) {
            final Client client1 = data.get();
            client1.setName(client.getName());
            client1.setTel(client.getTel());
            client1.setCredit(client.getCredit());
            dataSet.clients.save(client1);
            model.addAttribute("model", client1);
            return "redirect:/client";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id,
                         Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", client.get());
        return "client/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Client> client = dataSet.clients.findById(id);
        if (client.isEmpty()) return "redirect:/notfound";

        dataSet.clients.delete(client.get());
        return "redirect:/client";
    }
}
