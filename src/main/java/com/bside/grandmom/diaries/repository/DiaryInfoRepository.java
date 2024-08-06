package com.bside.grandmom.diaries.repository;

import com.bside.grandmom.diaries.domain.DiaryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface DiaryInfoRepository extends JpaRepository<DiaryInfoEntity, Long> {
    // userNo를 기준으로 no가 가장 높은 DiaryInfoEntity를 가져오는 메서드
    @Query("SELECT d FROM DiaryInfoEntity d WHERE d.user.userNo = :userNo ORDER BY d.no DESC")
    Optional<DiaryInfoEntity> findTopByUser_UserNoOrderByNoDesc(@Param("userNo") Long userNo);
}
