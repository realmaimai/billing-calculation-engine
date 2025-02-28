package com.maimai.billingcalculationengine.common.interceptor;

import com.maimai.billingcalculationengine.common.BaseContext;
import com.maimai.billingcalculationengine.common.constants.MessageConstants;
import com.maimai.billingcalculationengine.common.exception.UnauthorizedException;
import com.maimai.billingcalculationengine.common.properties.JwtProperties;
import com.maimai.billingcalculationengine.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        log.info("Incoming request: {} {} - Starting JWT validation", requestMethod, requestURI);

        // determine the method is from Controller or not, if not let it pass
        if (!(handler instanceof HandlerMethod)) return true;

        String header = request.getHeader("Authorization");
        if (header == null) {
            log.debug("Non-controller handler, skipping JWT validation");
            throw new UnauthorizedException(MessageConstants.Auth.MISSING_HEADER);
        }
        if (!header.startsWith("Bearer ")) {
            log.warn("Authorization header is missing for request: {} {}", requestMethod, requestURI);
            throw new UnauthorizedException(MessageConstants.Auth.INVALID_SCHEME);
        }

        // extract the token
        String token = header.substring(7); // "Bearer " is 7 chars
        if (token.isEmpty()) {
            log.warn("Invalid authorization scheme. Expected Bearer but got: {}", header);
            throw new UnauthorizedException(MessageConstants.Auth.EMPTY_TOKEN);
        }
        log.debug("JWT token received: {}", token);

        try {
            log.debug("Attempting to parse JWT token");
            Claims claims = JwtUtil.parseJwt(jwtProperties.getSecretKey(), token);
            Long userId = Long.parseLong(claims.get("userId").toString());
            BaseContext.setCurrentId(userId);
            log.info("JWT validation successful for user ID: {} - Request: {} {}",
                    userId, requestMethod, requestURI);

            return true;
        } catch (Exception e) {
            log.error("JWT validation failed for request {} {} - Error: {}",
                    requestMethod, requestURI, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new UnauthorizedException("token expired");
        }

    }
}
