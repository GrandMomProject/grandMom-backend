package com.bside.grandmom.diaries.domain;

import com.bside.grandmom.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "DIARY_DETAIL")
public class DiaryDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRAY_INFO_NO")
    private DiaryInfoEntity diaryInfo;

    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "V1")
    private String v1;

    @Column(name = "V2")
    private String v2;

    @Column(name = "V3")
    private String v3;
}
