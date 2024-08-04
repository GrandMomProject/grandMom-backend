package com.bside.grandmom.diaries.domain;

import com.bside.grandmom.users.domain.UserEntity;
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
@Builder
@Table(name = "DIARY_INFO")
public class DiaryInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private UserEntity user;

    @Lob
    @Column(name = "IMG")
    private String img;

    @Column(name = "REG_DT")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDt;

    @OneToMany(mappedBy = "diaryInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryDetailEntity> details = new ArrayList<>();
}
