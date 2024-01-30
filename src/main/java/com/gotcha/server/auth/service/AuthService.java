package com.gotcha.server.auth.service;

import com.gotcha.server.auth.domain.RefreshToken;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.auth.dto.response.RefreshTokenResponse;
import com.gotcha.server.auth.repository.RefreshTokenRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.dto.request.RefreshTokenRequest;
import com.gotcha.server.member.event.LoginEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final ApplicationContext applicationContext;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @EventListener(LoginEvent.class)
    @Transactional
    public void saveRefreshToken(final LoginEvent event) {
        RefreshToken newToken = new RefreshToken(event.socialId(), event.refreshToken());
        refreshTokenRepository.save(newToken);
        getSpringProxy().saveRefreshTokenToCache(newToken);
    }

    @CachePut(value = "refreshToken", key = "#token.socialId")
    public RefreshToken saveRefreshTokenToCache(final RefreshToken token) {
        return token;
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        jwtTokenProvider.validateRefreshToken(refreshToken);
        String socialId = jwtTokenProvider.getPayload(refreshToken);
        validateRefreshTokenRequest(socialId, refreshToken);
        return new RefreshTokenResponse(jwtTokenProvider.createAccessToken(socialId));
    }

    private void validateRefreshTokenRequest(final String socialId, final String refreshToken) {
        RefreshToken cachedToken = getSpringProxy().getRefreshToken(socialId);
        if(!cachedToken.getRefreshToken().equals(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    @Cacheable(value = "refreshToken")
    public RefreshToken getRefreshToken(final String socialId) {
        return refreshTokenRepository.findBySocialId(socialId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    @Transactional
    public void logout(final MemberDetails details) {
        String socialId = details.member().getSocialId();
        refreshTokenRepository.deleteBySocialId(socialId);
        getSpringProxy().removeRefreshTokenInCache(socialId);
        getSpringProxy().removeUserInCache(socialId);
    }

    @CacheEvict(value = "refreshToken")
    public void removeRefreshTokenInCache(final String socialId) {
    }

    @CacheEvict(value = "user")
    public void removeUserInCache(final String socialId) {
    }

    private AuthService getSpringProxy() {
        return applicationContext.getBean(AuthService.class);
    }
}
