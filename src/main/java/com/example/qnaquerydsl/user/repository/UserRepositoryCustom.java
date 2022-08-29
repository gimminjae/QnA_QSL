package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
    int getQslCount();

    SiteUser getQslUserOrderByIdAscOne();

    List<SiteUser> getQslUsersOrderById();

    List<SiteUser> searchQsl(String username);
}
