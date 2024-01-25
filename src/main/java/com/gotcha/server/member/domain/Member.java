package com.gotcha.server.member.domain;

import com.gotcha.server.global.domain.BaseTimeEntity;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String profileUrl;

    @Column
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(
            final String socialId, final String email, final String name,
            final String profileUrl, final String refreshToken) {
        this.socialId = socialId;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.refreshToken = refreshToken;
        this.role = Role.ROLE_USER;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateEmail(final String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(final String email) {
        if(!email.endsWith("@gmail.com")) {
            throw new AppException(ErrorCode.INVALID_GOOGLE_EMAIL);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(Objects.isNull(other) || getClass() != other.getClass()) return false;
        Member anotherMember = (Member)other;
        return Objects.equals(id, anotherMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
