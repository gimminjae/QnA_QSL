package com.example.qnaquerydsl;

import com.example.qnaquerydsl.user.entity.SiteUser;
import com.example.qnaquerydsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Test
    @DisplayName("가장 오래된 회원 1명")
    void test5() {
        SiteUser siteUser = userRepository.getQslUserOrderByIdAscOne();

        assertThat(siteUser.getUsername()).isEqualTo("user1");
    }
    @Test
    @DisplayName("전체회원, 오래된 순")
    void test6() {
        List<SiteUser> siteUserList = userRepository.getQslUsersOrderById();

        SiteUser siteUser1 = siteUserList.get(0);
        assertThat(siteUser1.getUsername()).isEqualTo("user1");

        SiteUser siteUser2 = siteUserList.get(1);
        assertThat(siteUser2.getUsername()).isEqualTo("user2");
    }
    @Test
    @DisplayName("회원이름, 이메일로 조회")
    void test7() {
        List<SiteUser> siteUserList = userRepository.searchQsl("user");

        assertThat(siteUserList.size()).isEqualTo(2);
    }
    @Test
    @DisplayName("검색, Page 리턴, id DESC, pageSize=1, page=0")
    void t8() {
        long totalCount = userRepository.count();
        int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
        int totalPages = (int)Math.ceil(totalCount / (double)pageSize);
        int page = 1;
        String kw = "user";

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

        assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> users = usersPage.get().toList();

        assertThat(users.size()).isEqualTo(pageSize);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getUsername()).isEqualTo("user2");
        assertThat(u.getEmail()).isEqualTo("user2@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

        // 검색어 : user1
        // 한 페이지에 나올 수 있는 아이템 수 : 1개
        // 현재 페이지 : 1
        // 정렬 : id 역순

        // 내용 가져오는 SQL
        /*
        SELECT site_user.*
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
        ORDER BY site_user.id ASC
        LIMIT 1, 1
         */

        // 전체 개수 계산하는 SQL
        /*
        SELECT COUNT(*)
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
         */
    }
}
