package com.example.qnaquerydsl;

import com.example.qnaquerydsl.user.entity.SiteUser;
import com.example.qnaquerydsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원생성")
    void test1() {

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
        //위와 같은 코드
//        SiteUser siteUser1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
//        SiteUser siteUser2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");

        userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));
    }
    @Test
    @DisplayName("1번 회원 찾기")
    void test2() {
        SiteUser siteUser = userRepository.getQslUser(1L);

        assertThat(siteUser.getUsername()).isEqualTo("user1");
    }
}
