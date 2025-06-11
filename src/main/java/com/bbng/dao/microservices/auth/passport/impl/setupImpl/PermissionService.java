package com.bbng.dao.microservices.auth.passport.impl.setupImpl;


import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.PermissionRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.exceptions.customExceptions.ForbiddenException;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public Optional<PermissionEntity> createPermissionIfNotFound(String name, String description) {
        Optional<PermissionEntity> permissionOptional = permissionRepository.findByNameOrDescription(name, description);

        if (permissionOptional.isEmpty()) {
            PermissionEntity permission = PermissionEntity.builder()
                    .name(name)
                    .description(description)
                    .build();
            permissionRepository.save(permission);
            return Optional.of(permission);
        } else {
            return permissionOptional;
        }
    }

    public Set<PermissionEntity> createListOfPermissionIfNotFound(List<String> names, List<String> descriptions) {
        if (names.size() != descriptions.size()) {
            throw new IllegalArgumentException("Names and descriptions lists must have the same size.");
        }

        Set<PermissionEntity> permissionEntities = new HashSet<>();
        Map<String, PermissionEntity> existingPermissions =
                permissionRepository.findAllByNameInOrDescriptionIn(names, descriptions).stream()
                        .collect(Collectors.toMap(PermissionEntity::getName, permission -> permission));

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String description = descriptions.get(i);

            PermissionEntity permission = existingPermissions.getOrDefault(name,
                    PermissionEntity.builder()
                            .name(name)
                            .description(description)
                            .build());

            permissionEntities.add(permission);
        }

        permissionRepository.saveAll(permissionEntities);
        return permissionEntities;
    }

    public void checkPermission(HttpServletRequest request, String permissionName, JWTService jwtService) {
        PermissionEntity permissionEntityOptional =
                permissionRepository.findByNameOrDescription(permissionName, null).orElseThrow(() ->
                        new ResourceNotFoundException("No permission found for the given name: " + permissionName));
        String username = GetUserFromToken.extractTokenFromHeader(request, jwtService);

        UserEntity user = userRepository.findByUsernameOrEmail(username, null).orElseThrow(() ->
                new UserNotFoundException("Can't find user with the username extracted from token. Is user a redtech user?"));
        boolean hasPermission = user.getRoleEntities().stream().map(RoleEntity::getPermissions).anyMatch(permissionEntities ->
                permissionEntities.contains(permissionEntityOptional));

        if (!hasPermission) {
            throw new ForbiddenException("You do not have the necessary permission for this operation");
        }
    }

}
