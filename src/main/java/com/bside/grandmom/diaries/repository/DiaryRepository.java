package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
}
