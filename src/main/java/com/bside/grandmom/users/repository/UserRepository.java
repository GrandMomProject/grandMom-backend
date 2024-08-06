package com.bside.grandmom.users.repository;

import com.bside.grandmom.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByDid(String did);

    Optional<UserEntity> findByUidAndDid(String uid, String did);

    UserEntity findByOauthId(String id);

    UserEntity findByUid(String uid);

    UserEntity findByUserNo(long userNo);
}
