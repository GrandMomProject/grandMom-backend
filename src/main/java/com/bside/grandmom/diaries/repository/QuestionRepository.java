package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.QuestionEntity;
import com.bside.grandmom.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    void deleteByUser(UserEntity user);

    Optional<QuestionEntity> findByUserAndAnsCnt(UserEntity user, int ansCnt);

    List<QuestionEntity> findAllByUser(UserEntity user);
}
