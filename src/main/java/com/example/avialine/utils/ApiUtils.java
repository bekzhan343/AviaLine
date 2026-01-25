package com.example.avialine.utils;

import jakarta.servlet.http.Cookie;

public class ApiUtils {

    public static final String REFRESH_TOKEN = "refresh_token";
    public static Cookie createCookie(String value){
        Cookie cookie = new Cookie(REFRESH_TOKEN, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30);

        return cookie;
    }
}
