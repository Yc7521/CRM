package com.crm.controller;

import com.crm.conf.LoginSuccessHandler;
import com.crm.conf.LogoutSuccessHandler;
import com.crm.model.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A controller for the home page.
 */
@Controller
public class HomeController {
    private final HttpSession session;
    private final AuthenticationManager authenticationManager;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final LoginSuccessHandler loginSuccessHandler;

    /**
     * Constructor
     */
    public HomeController(HttpSession session,
                          AuthenticationManager authenticationManager,
                          LogoutSuccessHandler logoutSuccessHandler,
                          LoginSuccessHandler loginSuccessHandler) {
        this.session = session;
        this.authenticationManager = authenticationManager;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    /**
     * optional: get user from {@link SecurityContextHolder}
     *
     * @return username
     */
    public static String getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }

    /**
     * Login page.
     */
    @GetMapping("/login")
    public void login() {
    }

    /**
     * Login.
     * Set parameter {@code error} if login failed.
     * Set session attribute {@code name} if login success.
     */
    @SuppressWarnings("deprecation")
    @PostMapping("login")
    public String login(LoginModel model) {
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getIdentity(), model.getPassword())));

            // save user to session
            this.session.setAttribute("name", HomeController.getPrincipal());
            this.loginSuccessHandler.run();
            return "redirect:/index";
        } catch (final AuthenticationException e) {
            return "redirect:/login?error=" + URLEncoder.encode("用户名或密码错误");
        }
    }

    /**
     * Logout. Redirect to {@code login} page.
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        this.logoutSuccessHandler.run();
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    /**
     * Info page.
     */
    @GetMapping("info")
    public void info(ModelMap result) {
        List<InfoModel> list = new ArrayList<>(5);
        Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (user instanceof Client client) {
            list.add(new InfoModel("姓名", client.getName()));
            list.add(new InfoModel("联系方式", client.getTel()));
            list.add(new InfoModel("信用度", client.getCredit()));
        } else if (user instanceof Employee employee) {
            list.add(new InfoModel("姓名", employee.getName()));
            list.add(new InfoModel("部门", employee.getDepartment()));
            list.add(new InfoModel("薪水", employee.getSalary()));
            list.add(new InfoModel("类别", employee.getEmployeeType().getDes()));
        } else throw new IllegalStateException("非法用户: " + user);
        result.addAttribute("model", list);
    }
}
