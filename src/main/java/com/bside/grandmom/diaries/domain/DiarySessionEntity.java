package com.bside.grandmom.diaries.domain;

import com.bside.grandmom.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Diary")
public class DiarySessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIARY_NO")
    private Long diaryNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private UserEntity user;

    @Lob
    @Column(name = "QUESTION")
    private String question;

    @Lob
    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "ANSWERCOUNT")
    private Integer answerCount;

    public void changeAnswer(String answer) {
        this.answer = answer;
    }
}