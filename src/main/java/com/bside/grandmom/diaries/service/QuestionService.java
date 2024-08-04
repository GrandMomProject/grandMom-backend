package com.bside.grandmom.diaries.service;

import com.bside.grandmom.diaries.domain.QuestionEntity;
import com.bside.grandmom.diaries.repository.QuestionRepository;
import com.bside.grandmom.users.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void createDiarySession(UserEntity user, String question) {
        clearSession(user);
        addQuestion(user, 1, question);
    }

    private void clearSession(UserEntity user) {
        questionRepository.deleteByUser(user);
    }

    public void addAnswer(UserEntity user, int answerCount, String answer) {
        QuestionEntity sessionRecord = questionRepository.findByUserAndAnsCnt(user, answerCount)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 다이어리 세션을 찾을 수 없음."));

        sessionRecord.changeAnswer(answer);
    }

    public void addQuestion(UserEntity user, int answerCount, String question) {
        questionRepository.save(QuestionEntity.builder()
                .user(user)
                .question(question)
                .ansCnt(answerCount)
                .build());
    }

    public List<QuestionEntity> getChatHistories(UserEntity user) {
        return questionRepository.findAllByUser(user);
    }

}
