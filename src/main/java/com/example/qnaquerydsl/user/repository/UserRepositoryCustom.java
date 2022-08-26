package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.SiteUser;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
}
