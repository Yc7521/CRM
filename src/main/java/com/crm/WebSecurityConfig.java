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

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(WebSecurityConfig.class);
    private static final String[] stu = {"Student", "Teacher", "Admin"};
    private static final String[] tea = {"Teacher", "Admin"};
    private static final String[] admin = {"Admin"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/**").permitAll()
                // .antMatchers("/", "/shared/**", "/error/**", "/home", "/login*", "/static/**").permitAll()
                // .antMatchers("/student", "/student/index", "/student/search").hasAnyAuthority(stu)
                // .antMatchers("/student/**").hasAnyAuthority(tea)
                // .antMatchers("/course/**").hasAnyAuthority(stu)
                // .antMatchers("/score", "/score/index", "/score/search").hasAnyAuthority(stu)
                // .antMatchers("/score/**").hasAnyAuthority(tea)
                // .antMatchers("/teacher", "/teacher/index", "/teacher/search").hasAnyAuthority(tea)
                // .antMatchers("/teacher/**").hasAnyAuthority(admin)
                // .antMatchers("/admin/**").hasAnyAuthority(admin)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/error/404")
                .and()
        ;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return () -> {
            try {
                User user = (User) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                logger.info("user(%s) : %s logout success !  ".formatted(user.getClass()
                        .getSimpleName(), user.getUsername()));
            } catch (Exception e) {
                logger.info("logout exception , e : " + e.getMessage());
            }
        };
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() { //登入处理
        return () -> {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            logger.info("user(%s) : %s login success !  ".formatted(user.getClass()
                    .getSimpleName(), user.getUsername()));
        };
    }

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private static class MyUserDetailsService implements UserDetailsService {
        @Autowired
        private DataSet dataSet;

        @Override
        public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
            final String[] split = name.split(Data.split);
            return loadUserByTypeAndUsername(split[0], split[1]);
        }

        /**
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