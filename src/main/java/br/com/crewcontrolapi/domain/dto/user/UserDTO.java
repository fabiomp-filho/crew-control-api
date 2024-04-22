package br.com.crewcontrolapi.domain.dto.user;

import br.com.crewcontrolapi.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private RoleEnum role;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

}
