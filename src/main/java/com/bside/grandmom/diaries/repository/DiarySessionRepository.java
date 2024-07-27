package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiarySessionEntity;
import com.bside.grandmom.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiarySessionRepository extends JpaRepository<DiarySessionEntity, Long> {
    void deleteByUser(UserEntity user);

    Optional<DiarySessionEntity> findByUserAndAnswerCount(UserEntity user, int answerCount);
}
