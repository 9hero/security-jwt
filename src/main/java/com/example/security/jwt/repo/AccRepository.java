package com.example.security.jwt.repo;

import com.example.security.jwt.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface AccRepository extends JpaRepository<Account,Integer> {
    User findByEmail(String username);
}
