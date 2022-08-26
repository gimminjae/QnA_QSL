package com.example.qnaquerydsl;

import com.example.qnaquerydsl.user.entity.SiteUser;
import com.example.qnaquerydsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원생성")
    void test1() {
        SiteUser siteUser1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
        SiteUser siteUser2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");

        userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));
    }
}