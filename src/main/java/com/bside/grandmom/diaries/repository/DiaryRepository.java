package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiaryEntity;
import com.bside.grandmom.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    void deleteByUser(UserEntity user);
}
