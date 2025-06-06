package com.bbng.dao.microservices.auth.auditlog.impl;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       return Optional.ofNullable(authentication).map(Authentication::getName);
    }
}
