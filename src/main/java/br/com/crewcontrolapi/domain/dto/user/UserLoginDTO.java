package br.com.crewcontrolapi.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginDTO {
    @Email
    @NotNull(message = "Login não pode estar vazio")
    private String login;

    @Min(8)
    @NotNull(message = "Senha não pode estar vazia")
    private String password;
}
