package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Feedback;
import com.crm.model.FeedbackStatus;
import com.crm.model.Product;
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
@RequestMapping("feedback")
public class FeedbackController {
    private final DataSet dataSet;

    public FeedbackController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Feedback> feedbackPage = dataSet.feedbacks.findAll(id);
        model.addAttribute("model", feedbackPage);
        return "feedback/index";
    }

    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/feedback";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.feedbacks.findAllByProduct_Name(id, search));
        return "feedback/index";
    }

    @GetMapping("searchId")
    public String searchId(@RequestParam(required = false) Integer search,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        if (search == null) return "redirect:/feedback";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.feedbacks.findAllByProduct_Id(id, search));
        return "feedback/index";
    }

    @GetMapping("create")
    public String create(@RequestParam(required = false) Integer id,
                         Model model) {
        final Feedback feedback = new Feedback();
        feedback.setUserName(HomeController.getPrincipal());
        feedback.setStatus(FeedbackStatus.PENDING);
        if (id != null) {
            final Optional<Product> product = dataSet.products.findById(id);
            if (product.isPresent()) {
                feedback.setProduct(product.get());
            } else {
                return "redirect:/feedback/create";
            }
        }
        model.addAttribute("model", feedback);
        model.addAttribute("products", dataSet.products.findAll());
        return "feedback/create";
    }

    @PostMapping("create")
    public String create(Feedback feedback, @RequestParam Integer product_id) {
        final Optional<Product> product = dataSet.products.findById(product_id);
        if (product.isPresent()) {
            feedback.setProduct(product.get());
            dataSet.feedbacks.save(feedback);
            return "redirect:/feedback/";
        }
        return "redirect:/notfound";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id,
                          Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", feedback.get());
        return "feedback/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id,
                       Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", feedback.get());
        model.addAttribute("products", dataSet.products.findAll());
        return "feedback/edit";
    }

    @PostMapping("edit")
    public String edit(Feedback feedback,
                       @RequestParam Integer product_id,
                       Model model) {
        final Optional<Feedback> data = dataSet.feedbacks.findById(feedback.getId());
        final Optional<Product> product = dataSet.products.findById(product_id);
        if (data.isPresent() && product.isPresent()) {
            final Feedback feedback1 = data.get();
            feedback1.setUserName(feedback.getUserName());
            feedback1.setTime(feedback.getTime());
            feedback1.setContent(feedback.getContent());
            feedback1.setProduct(product.get());
            feedback1.setStatus(feedback.getStatus());
            dataSet.feedbacks.save(feedback1);
            model.addAttribute("model", feedback1);
            return "redirect:/feedback";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id,
                         Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", feedback.get());
        return "feedback/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty()) return "redirect:/notfound";

        dataSet.feedbacks.delete(feedback.get());
        return "redirect:/feedback";
    }
}