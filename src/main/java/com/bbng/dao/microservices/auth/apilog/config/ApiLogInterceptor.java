package com.bbng.dao.microservices.auth.apilog.config;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import com.bbng.dao.microservices.auth.apilog.repository.ApiLogRepository;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.vacctgen.impl.AccountManager;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;

@Component
public class ApiLogInterceptor implements HandlerInterceptor {

    private final ApiLogRepository apiLogRepository;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final APIKeyRepository apiKeyRepository;
    private final OrganizationRepository organizationRepository;
    private final HttpServletRequest httpRequest;

    private static final Logger log = LoggerFactory.getLogger(ApiLogInterceptor.class);

    public ApiLogInterceptor(ApiLogRepository apiLogRepository, JWTService jwtService, UserRepository userRepository, APIKeyRepository apiKeyRepository, OrganizationRepository organizationRepository, HttpServletRequest httpRequest) {
        this.apiLogRepository = apiLogRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.organizationRepository = organizationRepository;

        this.httpRequest = httpRequest;
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

            OrganizationEntity org = getOrgForApiKey();

            ApiLogEntity log = new ApiLogEntity();
            log.setMerchantPrefix(org.getProductPrefix());
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


    public OrganizationEntity getOrgForApiKey() {


        String email = GetUserFromToken.extractUserFromApiKey(httpRequest,apiKeyRepository, userRepository);

        log.info("email: {}", email);


        UserEntity user = userRepository.findByUsernameOrEmail(email, email).orElseThrow(() ->
                new UserNotFoundException("Can't find user with the username extracted from token. Is user a paysub user?"));


        return organizationRepository.findOrganizationByMerchantAdminId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Can't find Org with the username extracted from token."));
    }
}
