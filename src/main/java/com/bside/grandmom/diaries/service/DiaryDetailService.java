package com.bside.grandmom.diaries.service;

import com.bside.grandmom.diaries.domain.DiaryDetailEntity;
import com.bside.grandmom.diaries.dto.DiaryResDto;
import com.bside.grandmom.diaries.repository.DiaryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryDetailService {
    private final DiaryDetailRepository diaryDetailRepository;

    public void saveImage(DiaryDetailEntity diaryDetail) {
        diaryDetailRepository.save(diaryDetail);
    }

    public List<DiaryResDto> findAllWithImgByUserNo(long userNo) {
        List<DiaryResDto> diaryResDtoList = diaryDetailRepository.findAllWithImgByUserNo(userNo);

        for (DiaryResDto dto : diaryResDtoList) {
            if (dto.getImg() != null) {
                String[] parts = dto.getImg().split("/");
                String newImgPath = "/grandmom/images/" + parts[parts.length - 1];
                dto.setImg(newImgPath);
            }
        }
        return diaryResDtoList;
    }

}
