package com.example.testSecurity.auth.entity;


import com.example.testSecurity.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_accesses")
public class MemberAccess {

    /**
     * 관리자 접속 로그 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_access_no")
    private Long id;


    /**
     * 토큰 발급 일시
     */
    @Column(length = 100)
    private LocalDateTime tokenCreateDate;

    /**
     * 토큰 만료 일시
     */
    private LocalDateTime tokenExpireDate;

    /**
     * 리프레쉬 토큰
     */
    private String refreshToken;

    /**
     * 재인증 일시
     */
    private LocalDateTime refreshDate;
    /**
     * 관리자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;


    public void expireJwtToken() {
        this.tokenExpireDate = LocalDateTime.now();
        this.refreshDate = null;
    }
}
