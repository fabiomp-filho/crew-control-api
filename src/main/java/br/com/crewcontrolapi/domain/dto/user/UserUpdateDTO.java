package br.com.crewcontrolapi.domain.dto.user;

import br.com.crewcontrolapi.annotations.cpf.Cpf;
import br.com.crewcontrolapi.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserUpdateDTO {

    @NotBlank(message = "O primeiro nome não deve estar vazio!")
    private String firstName;

    @NotBlank(message = "Sobrenome não deve estar vazio!")
    private String lastName;

    @Email(message = "Deve ser um e-mail válido!")
    private String email;

    @Cpf
    private String cpf;

    private RoleEnum role;

    @NotNull(message = "A data de nascimento não pode estar vazia!")
    @Past(message = "A data de nascimento deve ser uma data válida!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
}
