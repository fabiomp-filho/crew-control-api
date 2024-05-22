package br.com.crewcontrolapi.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UserTokenDTO {

    private String token;
    private UserDTO user;
}
