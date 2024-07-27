package com.bside.grandmom.users.domain;

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
}
