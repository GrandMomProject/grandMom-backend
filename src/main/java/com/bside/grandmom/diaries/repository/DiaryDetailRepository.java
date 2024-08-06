package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiaryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryDetailRepository extends JpaRepository<DiaryDetailEntity, Long> {
}
