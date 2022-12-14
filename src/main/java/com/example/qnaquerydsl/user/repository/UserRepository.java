package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom {

    List<SiteUser> findByInterestKeywords_content(String 축구);
}
