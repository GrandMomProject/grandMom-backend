package com.bside.grandmom.diaries.service;

import com.bside.grandmom.diaries.domain.DiaryInfoEntity;
import com.bside.grandmom.diaries.repository.DiaryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryInfoService {
    private final DiaryInfoRepository diaryInfoRepository;

    public void saveImage(DiaryInfoEntity diaryInfo){
        diaryInfoRepository.save(diaryInfo);
    }

    public DiaryInfoEntity getLatestDiaryInfoByUserNo(Long userNo) {
        return diaryInfoRepository.findTopByUser_UserNoOrderByNoDesc(userNo)
                .orElse(null);
    }
}
