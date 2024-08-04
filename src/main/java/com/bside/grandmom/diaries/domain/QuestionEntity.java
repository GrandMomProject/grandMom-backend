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
@Table(name = "QUESTION")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", nullable = false)
    private UserEntity user;

    @Lob
    @Column(name = "QUESTION", columnDefinition = "TEXT")
    private String question;

    @Lob
    @Column(name = "ANSWER", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "ANS_CNT")
    private Integer ansCnt;

    public void changeAnswer(String answer) {
        this.answer = answer;
    }
}