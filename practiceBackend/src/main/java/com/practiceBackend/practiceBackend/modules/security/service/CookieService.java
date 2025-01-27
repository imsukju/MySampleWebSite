package com.practiceBackend.practiceBackend.modules.security.service;

import com.practiceBackend.practiceBackend.modules.security.service.util.CookieIUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;


@Service
public class CookieService {
    CookieIUtil cookieIUtil = new CookieIUtil();

    public ResponseCookie createCookie(String token){
        return cookieIUtil.createAccessToken(token);

    }
    public void deleteCookie(HttpServletResponse response, String token){
        cookieIUtil.deleteCookie(response, token);
    }

    public String AccessTokkenFromHeader(HttpServletRequest request) {
        return cookieIUtil.AccessTokenFromHeader(request);
    }

    public String RefreshTokkenFromHeader(HttpServletRequest request) {
        return cookieIUtil.AccessTokenFromHeader(request);
    }
}
