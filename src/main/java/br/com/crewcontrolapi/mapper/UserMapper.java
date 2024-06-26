package br.com.crewcontrolapi.mapper;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.dto.user.UserUpdateDTO;
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
                .active(true)
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .cpf(user.getCpf())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

    }

    public static User toUpdate(User user, UserUpdateDTO userUpdateDTO) {
        return User.builder()
                .id(user.getId())
                .firstName(userUpdateDTO.getFirstName())
                .lastName(userUpdateDTO.getLastName())
                .cpf(userUpdateDTO.getCpf())
                .birthdate(userUpdateDTO.getBirthdate())
                .email(userUpdateDTO.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static User toUpdateAdmin(User user, UserUpdateDTO userUpdateDTO) {
        return User.builder()
                .id(user.getId())
                .firstName(userUpdateDTO.getFirstName())
                .lastName(userUpdateDTO.getLastName())
                .cpf(userUpdateDTO.getCpf())
                .birthdate(userUpdateDTO.getBirthdate())
                .email(userUpdateDTO.getEmail())
                .password(user.getPassword())
                .role(userUpdateDTO.getRole())
                .active(user.getActive())
                .build();
    }
}
