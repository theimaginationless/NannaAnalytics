package com.theimless.nannaanalytics.authentication.user.security.service;

import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.authentication.user.security.property.JwtProperties;
import com.theimless.nannaanalytics.common.exception.rest.BadRequestException;
import com.theimless.nannaanalytics.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.GregorianCalendar;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final WebAuthenticationDetailsSource webAuthDetailsSource = new WebAuthenticationDetailsSource();

    /**
     * Generate JWT token
     * @param authentication
     * @return JWT token
     */
    public String generateToken(Authentication authentication) {
        var nowGC = new GregorianCalendar();
        var expirationGC = (GregorianCalendar) nowGC.clone();
        expirationGC.add(GregorianCalendar.DAY_OF_YEAR, jwtProperties.getTokenExpirationAfterDays());
        var token = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(nowGC.getTime())
                .setExpiration(expirationGC.getTime())
                .signWith(getSecuritySignKey())
                .compact();

        log.info("Key signed with '{}' value", authentication.getName());
        return token;
    }

    /**
     * Extract and parse JWT token
     * @param request
     * @return Authentication instance
     * @throws BadRequestException
     */
    public Authentication parseToken(HttpServletRequest request)
            throws BadRequestException {
        String authHeader = request.getHeader("Authorization");

        if (ObjectUtils.isEmpty(authHeader)) {
            String errMsg = "Authentication header with key not found";
            log.error(errMsg);
//            throw new BadRequestException(errMsg);
            return null;
        }

        String token = authHeader.replace("Bearer ", "");
        var authToken = getAuth(token);
        authToken.setDetails(webAuthDetailsSource.buildDetails(request));

        return authToken;
    }

    private UsernamePasswordAuthenticationToken getAuth(String token)
            throws BadRequestException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSecuritySignKey())
                .build()
                .parseClaimsJws(token);
        Claims claimsBody = claims.getBody();
        String username = claimsBody.getSubject();
        User user = userService.getUserByName(username);
        if (user == null) {
            log.error("User '{}' not found", username);
            throw new BadRequestException("Authorization failed");
        }

        return new UsernamePasswordAuthenticationToken(
                user.getName(),
                null,
                user.getRoles());
    }

    /**
     * Returns sign key for signing JWT token
     * @return key
     */
    private Key getSecuritySignKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
