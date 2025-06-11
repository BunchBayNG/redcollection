package com.bbng.dao.microservices.auth.passport.impl.setupImpl;


import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.PermissionRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final List<Object> entityClasses = List.of(new UserEntity());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        log.info("has permission checked");
        log.info(targetDomainObject.getClass().getName());
        log.info(String.valueOf(entityClasses.stream().map(entityClass -> entityClass.getClass().getName()).findAny()));

        return entityClasses.stream().filter(entityClass -> entityClass.getClass().getName().equals(targetDomainObject.getClass().getName())).findFirst().map(matchingEntity -> checkUserHasPermissionForEntity(authentication, targetDomainObject, permission, matchingEntity)).orElse(false);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private boolean checkUserHasPermissionForEntity(Authentication authentication, Object targetDomainObject, Object permission, Object entityToBeAccessed) {

        UserEntity user = checkThatPermissionExistsAndUserExists(authentication, String.valueOf(permission));

        if (targetDomainObject.getClass().getName().equals(entityToBeAccessed.getClass().getName())) {

            return user.getRoleEntities().stream()
                    .flatMap(roleEntity -> roleEntity.getPermissions().stream())
                    .anyMatch(permissionEntity -> permissionEntity.getName().equals(String.valueOf(permission)));
        }
        return false;
    }

    private UserEntity checkThatPermissionExistsAndUserExists(Authentication authentication, String permissionName) {
        if (!permissionRepository.existsByNameOrDescription(permissionName, permissionName)) {
            throw new ResourceNotFoundException("No such permission exists");
        }

        String username = authentication.getName();

        return userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found in Permission Evaluator checking"));
    }

}