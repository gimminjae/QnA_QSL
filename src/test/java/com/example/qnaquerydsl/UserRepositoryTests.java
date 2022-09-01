package com.example.qnaquerydsl;

import com.example.qnaquerydsl.interestKeyword.entity.InterestKeyword;
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
import java.util.Set;

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
    @Test
    @DisplayName("검색, Page 리턴, id DESC, pageSize=1, page=0")
    void t9() {
        long totalCount = userRepository.count();
        int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
        int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
        int page = 1;
        String kw = "user";

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

        assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> users = usersPage.get().toList();

        assertThat(users.size()).isEqualTo(pageSize);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");
    }

    @Test
    @DisplayName("검색, Page리턴, id DESC, pageSize=1, page=0")
    void test10() {
        SiteUser u2 = userRepository.getQslUser(2L);

        u2.addInterestKeywordContent("축구");
        u2.addInterestKeywordContent("롤");
        u2.addInterestKeywordContent("헬스");
        u2.addInterestKeywordContent("헬스"); //중복등록은 무시

        userRepository.save(u2);
        // 엔티티클래스 : InterestKeyword(interest_keyword 테이블)
        // 중간테이블도 생성되어야 함, 힌트 : @ManyToMany
        // interest_keyword 테이블에 축구, 롤, 헬스에 해당하는 row 3개 생성
    }

    @Test
    @DisplayName("querydsl : 축구에 관심이 있는 회원을 검색할 수 있다.")
    void t11() {
        List<SiteUser> siteUserList = userRepository.getQslUserByInterestKeyword("축구");

        SiteUser u = siteUserList.get(0);

        assertThat(siteUserList.size()).isEqualTo(1);
        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

        // 테스트 케이스 추가
        // 구현, QueryDSL 사용
    }

    @Test
    @DisplayName("jpa : 축구에 관심이 있는 회원을 검색할 수 있다.")
    void t12() {
        List<SiteUser> siteUserList = userRepository.findByInterestKeywords_content("축구");

        SiteUser u = siteUserList.get(0);

        assertThat(siteUserList.size()).isEqualTo(1);
        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

        // 테스트 케이스 추가
        // 구현, jpa 사용
    }
    @Test
    @DisplayName("u2=아이돌, u1=팬 u1은 u2의 팔로워 이다.")
    void t13() {
        SiteUser u1 = userRepository.getQslUser(1L);
        SiteUser u2 = userRepository.getQslUser(2L);

        u2.follow(u1);
        u1.follow(u2);
        userRepository.save(u2);
        userRepository.save(u1);
    }
    @Test
    @DisplayName("본인은 본인을 팔로우 할 수 없다.")
    void test14() {
        SiteUser siteUser = userRepository.getQslUser(1L);

        siteUser.follow(siteUser);

        assertThat(siteUser.getFollowers().size()).isEqualTo(0);
    }
}
