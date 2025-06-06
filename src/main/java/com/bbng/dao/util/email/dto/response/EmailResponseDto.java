package com.bbng.dao.util.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDto {
    private String email;
    private String status;
    private String userId;
    private String rejectReason;
    private String queuedReason;
}
