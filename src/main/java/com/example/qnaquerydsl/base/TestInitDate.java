package com.example.qnaquerydsl.base;

import com.example.qnaquerydsl.interestKeyword.entity.InterestKeyword;
import com.example.qnaquerydsl.user.entity.SiteUser;
import com.example.qnaquerydsl.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("test")
public class TestInitDate {
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            SiteUser siteUser1 = SiteUser.builder()
                    .username("user1")
                    .password("{noop}1234")
                    .email("user1@test.com")
                    .build();

            SiteUser siteUser2 = SiteUser.builder()
                    .username("user2")
                    .password("{noop}1234")
                    .email("user2@test.com")
                    .build();

            userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));

            siteUser1.addInterestKeywordContent("축구");
            siteUser1.addInterestKeywordContent("농구");

            siteUser2.addInterestKeywordContent("농구");
            siteUser2.addInterestKeywordContent("클라이밍");
            siteUser2.addInterestKeywordContent("마라톤");

            userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));

        };
    }
}
