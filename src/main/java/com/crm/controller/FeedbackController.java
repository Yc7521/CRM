package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Feedback;
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
 * A controller for the feedback page.
 */
@Controller
@RequestMapping("feedback")
public class FeedbackController {
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
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

        Optional<Feedback> feedback = dataSet.feedbacks.findById(id);
        if (feedback.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Feedback not found");

        return feedback.get();
    }

    /**
     * [GET]
     * mapping to /feedback/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        Page<Feedback> feedbackPage = this.dataSet.feedbacks.findAll(id);
        model.addAttribute("model", feedbackPage);
        return "feedback/index";
    }

    /**
     * [GET]
     * mapping to /feedback/search?page=0&search=
     *
     * @param search the name of {@link Feedback}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/feedback";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.feedbacks.findAllByProduct_Name(id, search));
        return "feedback/index";
    }

    /**
     * [GET]
     * mapping to /feedback/searchId?page=0&search=
     *
     * @param search the id of {@link Product}
     * @param page   a number of page
     */
    @GetMapping("searchId")
    public String searchId(@RequestParam(required = false) Integer search,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        if (search == null) return "redirect:/feedback";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.feedbacks.findAllByProduct_Id(id, search));
        return "feedback/index";
    }

    /**
     * [GET]
     * mapping to /feedback/create?id=0
     */
    @GetMapping("create")
    public void create(@RequestParam(required = false) Integer id, Model model) {
        Feedback feedback = new Feedback();
        feedback.setUserName(HomeController.getPrincipal());
        if (id != null) {
            feedback.setProduct(ProductController.getProduct(id, this.dataSet));
        }
        model.addAttribute("model", feedback);
        model.addAttribute("products", this.dataSet.products.findAll());
    }

    /**
     * [POST]
     * mapping to /feedback/create
     *
     * @param feedback   the {@link Feedback}
     * @param product_id the id of {@link Product}
     */
    @PostMapping("create")
    public String create(Feedback feedback, @RequestParam Integer product_id) {
        feedback.setProduct(ProductController.getProduct(product_id, this.dataSet));
        this.dataSet.feedbacks.save(feedback);
        return "redirect:/feedback";
    }

    /**
     * [GET]
     * mapping to /feedback/details?id=0
     *
     * @param id the id of {@link Feedback}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", FeedbackController.getFeedback(id, this.dataSet));
    }

    /**
     * [GET]
     * mapping to /feedback/edit?id=0
     *
     * @param id the id of {@link Feedback}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", FeedbackController.getFeedback(id, this.dataSet));
        model.addAttribute("products", this.dataSet.products.findAll());
    }

    /**
     * [POST]
     * mapping to /feedback/edit
     *
     * @param feedback   the {@link Feedback}
     * @param product_id the id of {@link Product}
     */
    @PostMapping("edit")
    public String edit(Feedback feedback, @RequestParam Integer product_id, Model model) {
        // check if exists
        final Feedback feedback1 = FeedbackController.getFeedback(feedback.getId(), this.dataSet);
        feedback1.setUserName(feedback.getUserName());
        feedback1.setTime(feedback.getTime());
        feedback1.setContent(feedback.getContent());
        feedback1.setProduct(ProductController.getProduct(product_id, this.dataSet));
        feedback1.setStatus(feedback.getStatus());
        this.dataSet.feedbacks.save(feedback1);
        return "redirect:/feedback";
    }

    /**
     * [GET]
     * mapping to /feedback/delete?id=0
     *
     * @param id the id of {@link Feedback}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", FeedbackController.getFeedback(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /feedback/delete
     *
     * @param id the id of {@link Feedback}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        this.dataSet.feedbacks.delete(FeedbackController.getFeedback(id, this.dataSet));
        return "redirect:/feedback";
    }
}