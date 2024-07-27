package com.bside.grandmom.diaries.service;

import com.bside.grandmom.diaries.domain.DiarySessionEntity;
import com.bside.grandmom.diaries.repository.DiarySessionRepository;
import com.bside.grandmom.users.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiarySessionService {
    private final DiarySessionRepository diarySessionRepository;

    public void createDiarySession(UserEntity user, String question) {
        clearSession(user);
        addQuestion(user, 1, question);
    }

    private void clearSession(UserEntity user) {
        diarySessionRepository.deleteByUser(user);
    }

    public void addAnswer(UserEntity user, int answerCount, String answer) {
        DiarySessionEntity sessionRecord = diarySessionRepository.findByUserAndAnswerCount(user, answerCount)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 다이어리 세션을 찾을 수 없음."));

        sessionRecord.changeAnswer(answer);
    }

    public void addQuestion(UserEntity user, int answerCount, String question) {
        diarySessionRepository.save(DiarySessionEntity.builder()
                .user(user)
                .question(question)
                .answerCount(answerCount)
                .build());
    }

    public List<DiarySessionEntity> getChatHistories(UserEntity user) {
        return diarySessionRepository.findAllByUser(user);
    }

}
