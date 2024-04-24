package br.com.crewcontrolapi.domain.dto.user;

import br.com.crewcontrolapi.annotations.cpf.Cpf;
import br.com.crewcontrolapi.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "O primeiro nome não deve estar vazio!")
    private String firstName;

    @NotBlank(message = "Sobrenome não deve estar vazio!")
    private String lastName;

    @Email(message = "Deve ser um e-mail válido!")
    private String email;

    @Cpf
    private String cpf;

    @Min(value = 8, message = "A senha deve possuir pelo menos 8 digitos!")
    @NotBlank(message = "Senha não pode estar vazia!")
    private String password;

    @NotNull(message = "A função do usuário deve estar entre os valores válidos!")
    private RoleEnum role;

    @NotNull(message = "A data de nascimento não pode estar vazia!")
    @Past(message = "A data de nascimento deve ser uma data válida!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
}
