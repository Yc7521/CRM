package com.crm;

import com.crm.conf.Data;
import com.crm.conf.LoginSuccessHandler;
import com.crm.conf.LogoutSuccessHandler;
import com.crm.dao.DataSet;
import com.crm.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(WebSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: to use antMatchers to set roles authorization
        http
            .authorizeRequests()
//                .antMatchers("/**").permitAll()
                .antMatchers("/*", "/shared/**", "/error/**", "/login*", "/static/**", "/static/img/**")
                    .permitAll()
                .antMatchers("/shop/**", "/feedback/create", "/feedback/searchId", "/cost/searchClientId", "/cost/searchUnhandled")
                    .hasAuthority(Data.roles[0])
                .antMatchers("/client/index", "/plan/index")
                    .hasAuthority(Data.roles[1])
                .antMatchers("/cost/edit", "/cost/delete")
                    .hasAuthority(Data.roles[2])
                .antMatchers("/cost/**", "/product/**", "/feedback/**")
                    .hasAuthority(Data.roles[1])
                .anyRequest().hasAuthority(Data.roles[2])
            .and()
                .exceptionHandling()
                .accessDeniedPage("/error/404").and();
    }

    /**
     * Register the {@link LoginSuccessHandler} bean for handling login success.
     *
     * @return the {@link LoginSuccessHandler} bean
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() { //登入处理
        return () -> {
            User user = (User) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            logger.info("user(%s) : %s login success !  ".formatted(user.getClass()
                    .getSimpleName(), user.getUsername()));
        };
    }

    /**
     * Register the {@link LogoutSuccessHandler} bean for handling logout success.
     *
     * @return a {@link LogoutSuccessHandler}
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return () -> {
            try {
                User user = (User) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                logger.info("user(%s) : %s logout success !  ".formatted(user.getClass()
                        .getSimpleName(), user.getUsername()));
            } catch (Exception e) {
                logger.info("logout exception , e : " + e.getMessage());
            }
        };
    }

    /**
     * Register the {@link PasswordEncoder} bean for encoding passwords.
     *
     * @return a {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    /**
     * Register the {@link UserDetailsService} bean for retrieving user details.
     *
     * @return a {@link MyUserDetailsService}
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    /**
     * Register the {@link AuthenticationManager} bean for authenticating users.
     *
     * @return a {@link AuthenticationManager} from {@link WebSecurityConfigurerAdapter#authenticationManager()}
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * A {@link UserDetailsService} class for retrieving user details.
     */
    private static class MyUserDetailsService implements UserDetailsService {
        @Autowired
        private DataSet dataSet;

        @Override
        public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
            final String[] split = name.split(Data.split);
            return loadUserByTypeAndUsername(split[0], split[1]);
        }

        /**
         * Retrieve user details by user type and username.
         *
         * @param type [员工, 客户, 管理员]
         * @param name username
         * @return a fully populated user record
         * @throws UsernameNotFoundException if not found
         */
        public UserDetails loadUserByTypeAndUsername(String type,
                                                     String name) throws UsernameNotFoundException {
            final User user = switch (type) {
                case "员工" -> dataSet.employees.findEmployeeByName(name);
                case "客户" -> dataSet.clients.findByName(name);
                case "管理员" -> dataSet.employees.findManagerByName(name);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
            if (user == null)
                throw new UsernameNotFoundException("Username " + name + " not found");
            return user;
        }
    }
}