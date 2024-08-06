package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiaryDetailEntity;
import com.bside.grandmom.diaries.dto.DiaryResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryDetailRepository extends JpaRepository<DiaryDetailEntity, Long> {

    // userNo를 기준으로 DiaryDetailEntity의 정보와 DiaryInfoEntity의 img를 가져오는 메서드
    @Query("SELECT new com.bside.grandmom.diaries.dto.DiaryResDto(d.no, d.v1, d.v2, d.v3, di.img) " +
            "FROM DiaryDetailEntity d " +
            "JOIN d.diaryInfo di " +
            "WHERE di.user.userNo = :userNo")
    List<DiaryResDto> findAllWithImgByUserNo(@Param("userNo") Long userNo);
}
