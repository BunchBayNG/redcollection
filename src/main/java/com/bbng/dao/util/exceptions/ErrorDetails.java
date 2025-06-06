package com.bbng.dao.util.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Schema(
        name = "Error response",
        description = "Response format for unsuccessful operations"
)
public class ErrorDetails {
    private int statusCode;
    private boolean status;
    private String message;
}
