package com.example.security.jwt.repo;

import com.example.security.jwt.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccRepositoryTest {

    @Autowired
    AccRepository accRepository;

    @Test
    void a() {
        var a = Account.builder()
                .email("adsd")
                .name("asda")
                .password("asd")
                .role(Account.Role.COMMON)
                .build();
        System.out.println(accRepository.save(a).getIdx());
    }
}