package com.crm;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Employee;
import com.crm.model.EmployeeType;
import com.crm.model.LoginModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

import static com.crm.conf.Data.roles;

@SpringBootTest
class CrmApplicationTests {
    @Resource
    DataSet dataSet;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    AuthenticationManager authenticationManager;

    void clientsInit() {
        if (dataSet.clients.findByName("test") == null) {
            final Client client = new Client();
            client.setName("test");
            client.setPassword(passwordEncoder.encode("test"));
            client.setTel("12345678901");
            dataSet.clients.save(client);
        }
    }

    @Test
    void clients() {
        clientsInit();
        final Client test = dataSet.clients.findByName("test");
        assert test != null;
        final String password = test.getPassword();
        assert password != null;
        assert passwordEncoder.matches("test", password);
    }

    @Test
    void loginTest() {
        LoginModel model = new LoginModel();
        model.username = "test";
        model.password = "test";
        model.type = "客户";
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getIdentity(), model.getPassword()));
        assert authenticate.isAuthenticated();
        final Object principal = authenticate.getPrincipal();
        assert principal instanceof UserDetails;
        final UserDetails user = (UserDetails) principal;
        assert user.getUsername().equals("test");
        assert user.getAuthorities()
                .contains(new SimpleGrantedAuthority(roles[0]));
    }

    @Test
    void addManager() {
        if (dataSet.employees.findManagerByName("test") == null) {
            final Employee employee = new Employee();
            employee.setName("test");
            employee.setPassword(passwordEncoder.encode("test"));
            employee.setDepartment("test");
            employee.setEmployeeType(EmployeeType.MANAGER);
            employee.setSalary(1000.);
            dataSet.employees.save(employee);
        }
    }

}
