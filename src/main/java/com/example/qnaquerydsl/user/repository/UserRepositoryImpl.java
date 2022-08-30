package com.example.qnaquerydsl.user.repository;

import com.example.qnaquerydsl.user.entity.QSiteUser;
import com.example.qnaquerydsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.qnaquerydsl.user.entity.QSiteUser.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        /*
        SELECT *
        FROM site_user
        WHERE id = {id}
        */
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(id))
                .fetchOne();


    }

    @Override
    public int getQslCount() {
        long count = jpaQueryFactory
                .select(siteUser.count())
                .from(siteUser)
                .fetchOne();
        return (int) count;
    }

    @Override
    public SiteUser getQslUserOrderByIdAscOne() {
            return jpaQueryFactory
                    .select(siteUser)
                    .from(siteUser)
                    .orderBy(siteUser.id.asc())
                    .limit(1)
                    .fetchOne();
    }

    @Override
    public List<SiteUser> getQslUsersOrderById() {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .orderBy(siteUser.id.asc())
                .fetch();
    }

    @Override
    public List<SiteUser> searchQsl(String username) {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.username.like("%" + username + "%").or(siteUser.email.like("%" + username + "%")))
                .fetch();
    }

    @Override
    public Page<SiteUser> searchQsl(String kw, Pageable pageable) {
        return (Page<SiteUser>) jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.username.like("%" + kw + "%").or(siteUser.email.like("%" + kw + "%")))
                .limit(pageable.getPageNumber())
                .fetch();
    }

}
