package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom {
}
