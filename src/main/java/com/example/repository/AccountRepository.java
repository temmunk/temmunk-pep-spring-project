package com.example.repository;
import com.example.entity.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    Optional<Account> findByUsernameAndPassword (String username, String password);


    Optional<Account> findByUsername (String username);
}
