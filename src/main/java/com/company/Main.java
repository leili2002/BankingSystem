package com.company;//package com.company;


import Logic.AccountService;
import Logic.BankingService;
import Presentation.Presentation;
import Repository.AccountRepository;
import Repository.UserRepository;


public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        AccountRepository accountRepository = new AccountRepository();
        BankingService bankingService = new BankingService(userRepository);
        AccountService accountService = new AccountService(accountRepository);
        Presentation presentation = new Presentation(accountService,bankingService);


        presentation.presentation();

    }
}
