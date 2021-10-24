package me.choi.demospringsecurityform.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Test
    @WithAnonymousUser
    void index_anonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    void index_user() throws Exception {
//        mockMvc.perform(get("/").with(user("user").roles("USER")))
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void admin_user() throws Exception {
        mockMvc.perform(get("/admin"))
//        mockMvc.perform(get("/admin").with(user("admin").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void login_success1() throws Exception {
        String username = "junwoo";
        String password = "123";
        Account account = createUser(username, password);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    void login_success2() throws Exception {
        String username = "junwoo";
        String password = "123";
        Account account = createUser(username, password);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    void login_fail() throws Exception {
        String username = "junwoo";
        String password = "123";
        Account account = createUser(username, password);

        mockMvc.perform(formLogin().user(account.getUsername()).password("1234444"))
                .andExpect(unauthenticated());
    }

    private Account createUser(String username, String password) {
        Account account = Account.builder()
                                 .username(username)
                                 .password(password)
                                 .role("USER")
                                 .build();

        accountService.createNew(account);

        return account;
    }
}
