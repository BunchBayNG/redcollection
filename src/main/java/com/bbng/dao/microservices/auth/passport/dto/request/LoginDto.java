package com.bbng.dao.microservices.auth.passport.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    @Email(message = "Enter valid email")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!-_])(?=\\S+$).{8,20}$", message = """
            At least one digit.
            At least one lowercase letter.
            At least one uppercase letter.
            At least one special character from the specified set (@, #, $, %, ^, &, +, =, !, -, _])
            No whitespace characters.
            The overall length of the password must be between 8 and 20 characters.
            """)
    private String password;
}
