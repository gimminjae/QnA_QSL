package com.example.qnaquerydsl;

import com.example.qnaquerydsl.user.entity.SiteUser;
import com.example.qnaquerydsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test") //테스트 모드 활성화
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원생성")
    void test1() {

        SiteUser siteUser3 = SiteUser.builder()
                .username("user3")
                .password("{noop}1234")
                .email("user3@test.com")
                .build();
        SiteUser siteUser4 = SiteUser.builder() //builder도입
                .username("user4")
                .password("{noop}1234")
                .email("user4@test.com")
                .build();
        //위와 같은 코드
//        SiteUser siteUser1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
//        SiteUser siteUser2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");

        userRepository.saveAll(Arrays.asList(siteUser3, siteUser4));
    }
    @Test
    @DisplayName("1번 회원 찾기")
    void test2() {
        SiteUser siteUser = userRepository.getQslUser(1L);

        assertThat(siteUser.getUsername()).isEqualTo("user1");
    }
    @Test
    @DisplayName("2번 회원 찾기")
    void test3() {
        SiteUser siteUser = userRepository.getQslUser(2L);

        assertThat(siteUser.getUsername()).isEqualTo("user2");
    }
    @Test
    @DisplayName("모든 회원의 수")
    void test4() {
        int count = userRepository.getQslCount();

        assertThat(count).isGreaterThan(0);
    }
}
