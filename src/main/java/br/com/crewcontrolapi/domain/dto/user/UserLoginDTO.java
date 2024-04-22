package br.com.crewcontrolapi.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginDTO {
    @Email
    private String login;

    @Min(8)
    private String password;
}
