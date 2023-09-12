package com.tema26cwicz1.controllers;

import com.tema26cwicz1.account.AccountRepository;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


}
