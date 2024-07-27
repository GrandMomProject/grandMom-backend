package com.bside.grandmom.users.domain;

import com.bside.grandmom.diaries.domain.DiaryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "UID"),
        @UniqueConstraint(columnNames = "DID")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "UID", nullable = false)
    private String uid;

    @Column(name = "DID", nullable = false)
    private String did;

    @Column(name = "GENDER", nullable = false)
    private char gender;

    @Column(name = "AGREEMENT_YN")
    private char agreementYn;

    @Lob
    @Column(name = "IMG_DESC")
    private String imgDesc;

    @Column(name = "REG_DT")
    private Date regDt;

    @Column(name = "UPD_DT")
    private Date updDt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryEntity> diaries = new ArrayList<>();
}
