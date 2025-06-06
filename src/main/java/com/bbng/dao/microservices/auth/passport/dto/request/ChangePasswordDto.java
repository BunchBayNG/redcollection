package com.bbng.dao.microservices.auth.passport.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordDto {
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!-_])(?=\\S+$).{8,20}$", message = """
            At least one digit.
            At least one lowercase letter.
            At least one uppercase letter.
            At least one special character from the specified set (@, #, $, %, ^, &, +, =, !, -, _)
            No whitespace characters.
            The overall length of the password must be between 8 and 20 characters.
            """)
    private String oldPassword;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!-_])(?=\\S+$).{8,20}$", message = """
            At least one digit.
            At least one lowercase letter.
            At least one uppercase letter.
            At least one special character from the specified set (@, #, $, %, ^, &, +, =, !, -, _)
            No whitespace characters.
            The overall length of the password must be between 8 and 20 characters.
            """)
    private String newPassword;
}
