package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Feedback;
import com.crm.model.FeedbackStatus;
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

@Controller
@RequestMapping("feedback")
public class FeedbackController {
    private final DataSet dataSet;

    public FeedbackController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Feedback} by id
     *
     * @param id      the id of {@link Feedback}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Feedback} not found
     */
    static Feedback getFeedback(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Feedback not found");

        final Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Feedback not found");

        return feedback.get();
    }

    @GetMapping({"", "/index"})
    public void index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Feedback> feedbackPage = dataSet.feedbacks.findAll(id);
        model.addAttribute("model", feedbackPage);
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
    public void create(@RequestParam(required = false) Integer id, Model model) {
        final Feedback feedback = new Feedback();
        feedback.setUserName(HomeController.getPrincipal());
        feedback.setStatus(FeedbackStatus.PENDING);
        if (id != null) {
            feedback.setProduct(ProductController.getProduct(id, dataSet));
        }
        model.addAttribute("model", feedback);
        model.addAttribute("products", dataSet.products.findAll());
    }

    @PostMapping("create")
    public String create(Feedback feedback, @RequestParam Integer product_id) {
        feedback.setProduct(ProductController.getProduct(product_id, dataSet));
        dataSet.feedbacks.save(feedback);
        return "redirect:/feedback";
    }

    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getFeedback(id, dataSet));
    }

    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getFeedback(id, dataSet));
        model.addAttribute("products", dataSet.products.findAll());
    }

    @PostMapping("edit")
    public String edit(Feedback feedback, @RequestParam Integer product_id, Model model) {
        // check if exists
        getFeedback(feedback.getId(), dataSet);
        feedback.setProduct(ProductController.getProduct(product_id, dataSet));
        dataSet.feedbacks.save(feedback);
        return "redirect:/feedback";
    }

    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getFeedback(id, dataSet));
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        dataSet.feedbacks.delete(getFeedback(id, dataSet));
        return "redirect:/feedback";
    }
}