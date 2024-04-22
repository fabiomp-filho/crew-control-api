package br.com.crewcontrolapi.mapper;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User toEntity(UserRegistrationDTO dto, PasswordEncoder passwordEncoder) {

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .cpf(dto.getCpf())
                .birthdate(dto.getBirthdate())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .cpf(user.getCpf())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

    }
}
