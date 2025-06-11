package com.bbng.dao.microservices.auth.passport.utils;

public class SecurityConstants {

    public static final long JWT_EXPIRATION = 1000 * 30 * 60 * 1000;

    public static final String JWT_SECRET = "uPWl+Tgc++99AGX9YLz6xGbbKnuAZdQVT/GemEyudnM=";

    //    this refresh token expiration is written in milliseconds i.e 7days was converted to 604800000
    public static final long REFRESH_TOKEN_EXPIRATION = 604800000;
    public static final int ALGORITHM_SHIFT_KEY = 25;
//    app.jwt-refresh-token-expiration-milliseconds=

    public static final String APPLICANT_EXISTED_CODE = "001";
    public static final String APPLICANT_EXISTED_MESSAGE = "Applicant existed";
    public static final String APPLICANT_REGISTRATION_CODE = "001";
    public static final String APPLICANT_REGISTRATION_MESSAGE = "Applicant created successfully";
}
