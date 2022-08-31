package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
    int getQslCount();

    SiteUser getQslUserOrderByIdAscOne();

    List<SiteUser> getQslUsersOrderById();

    List<SiteUser> searchQsl(String username);

    Page<SiteUser> searchQsl(String kw, Pageable pageable);

    List<SiteUser> getQslUserByInterestKeyword(String interest);

}
