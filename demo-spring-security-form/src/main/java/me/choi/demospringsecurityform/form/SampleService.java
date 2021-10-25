package me.choi.demospringsecurityform.form;

import me.choi.demospringsecurityform.account.Account;
import me.choi.demospringsecurityform.account.AccountContext;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
        Account account = AccountContext.getAccount();
        System.out.println("*****************************************");
        System.out.println(account.getUsername());
        System.out.println("*****************************************");
    }

}
