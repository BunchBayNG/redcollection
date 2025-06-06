package com.bbng.dao.microservices.auth.passport.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileUpdateRequestDto {

    private String userId;
    private MultipartFile logoUrl;
}
