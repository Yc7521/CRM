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
    public HomeController(final HttpSession session,
                          final AuthenticationManager authenticationManager,
                          final LogoutSuccessHandler logoutSuccessHandler,
                          final LoginSuccessHandler loginSuccessHandler) {
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
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
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
    public String login(final LoginModel model) {
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getIdentity(), model.getPassword())));

            // save user to session
            session.setAttribute("name", getPrincipal());
            loginSuccessHandler.run();
            return "redirect:/index";
        } catch (AuthenticationException e) {
            return "redirect:/login?error=" + URLEncoder.encode("????????????????????????");
        }
    }

    /**
     * Logout. Redirect to {@code login} page.
     */
    @GetMapping("logout")
    public String logout(final HttpServletRequest request, final HttpServletResponse response) {
        logoutSuccessHandler.run();
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    /**
     * Info page.
     */
    @GetMapping("info")
    public void info(final ModelMap result) {
        final List<InfoModel> list = new ArrayList<>(5);
        final Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (user instanceof Client client) {
            result.addAttribute("id", client.getId());
            list.add(new InfoModel("??????", client.getName()));
            list.add(new InfoModel("????????????", client.getTel()));
            list.add(new InfoModel("?????????", client.getCredit()));
            result.addAttribute("role", "client");
        } else if (user instanceof Employee employee) {
            result.addAttribute("id", employee.getId());
            list.add(new InfoModel("??????", employee.getName()));
            list.add(new InfoModel("??????", employee.getDepartment()));
            list.add(new InfoModel("??????", employee.getSalary()));
//            list.add(new InfoModel("??????", employee.getEmployeeType().getDes()));
            result.addAttribute("role", "employee");
        } else throw new IllegalStateException("????????????: " + user);
        result.addAttribute("model", list);
    }
}
