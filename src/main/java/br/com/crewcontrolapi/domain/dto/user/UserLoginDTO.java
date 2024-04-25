package br.com.crewcontrolapi.domain.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginDTO {

    @NotBlank(message = "Login não pode estar vazio!")
    @Email(message = "Login deve ser um e-mail válido!")
    private String login;

    @Size(min = 8, message = "Senha deve ter 8 ou mais caracteres!")
    private String password;
}
