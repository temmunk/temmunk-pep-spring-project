package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(Account account){
        
        if (account.getUsername() == null || account.getUsername().isEmpty()){
            throw new IllegalArgumentException("Username can't be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password length must be longer than 4");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new DataIntegrityViolationException("Username taken");
        }

        return accountRepository.save(account);
    }

    public Optional<Account> login(String username, String password){
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public Account getAccount(Account account){
        return accountRepository.getById(account.getAccountId());
    }

}
