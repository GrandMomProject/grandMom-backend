package com.bside.grandmom.users.domain;

import com.bside.grandmom.diaries.domain.DiaryInfoEntity;
import com.bside.grandmom.diaries.domain.QuestionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "UID")
//        @UniqueConstraint(columnNames = "DID")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "DID")
    private String did;

    @Column(name = "UID")
    private String uid;

    @Column(name = "LOGIN_TYPE", nullable = false)
    private String loginType = "GENERAL";

    @Column(name = "OAUTH_ID")
    private String oauthId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "PROFILE_IMG")
    private String profileImg;

    @Column(name = "GENDER")
    private char gender;

    @Column(name = "USE_CNT")
    private int useCnt = 0;

    @Column(name = "AGREEMENT_YN", nullable = false)
    private char agreementYn = 'Y';

    @Lob
    @Column(name = "IMG_DESC", columnDefinition = "TEXT")
    private String imgDesc;

    @Column(name = "REG_DT")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDt;

    @Column(name = "LAST_DT")
    private Date lastDt;

    @Column(name = "UPD_DT")
    private Date updDt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryInfoEntity> infos = new ArrayList<>();

    public void updateImgDesc(String imgDesc, Date updDt) {
        this.imgDesc = imgDesc;
        this.updDt = updDt;
    }

    public void increaseUseCnt(){
        this.useCnt++;
    }

    public void resetUseCnt(){
        this.useCnt = 0;
    }
}
