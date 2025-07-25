package com.bbng.dao.microservices.auth.apilog.config;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import com.bbng.dao.microservices.auth.apilog.repository.ApiLogRepository;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;

@Component
public class ApiLogInterceptor implements HandlerInterceptor {

    private final ApiLogRepository apiLogRepository;
    private final JWTService jwtService;

    public ApiLogInterceptor(ApiLogRepository apiLogRepository, JWTService jwtService ) {
        this.apiLogRepository = apiLogRepository;
        this.jwtService = jwtService;
    }

    private static final ThreadLocal<LocalDateTime> requestStartTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestStartTime.set(LocalDateTime.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            String authHeader = request.getHeader("Authorization");
            String merchantPrefix = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                merchantPrefix = jwtService.getMerchantPrefix(authHeader);
            }

            ApiLogEntity log = new ApiLogEntity();
            log.setMerchantPrefix(merchantPrefix);
            log.setService(request.getRequestURI());
            log.setRequestTimestamp(requestStartTime.get());
            log.setResponseTimestamp(LocalDateTime.now());
            log.setResponseStatus(response.getStatus());

            apiLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to log API call: " + e.getMessage());
        } finally {
            requestStartTime.remove();
        }
    }
}
