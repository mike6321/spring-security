package me.choi.form;

import me.choi.account.Account;
import me.choi.account.AccountContext;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
        Account account = AccountContext.ACCOUNT_THREAD_LOCAL.get();
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(account.getUsername());
    }
}
