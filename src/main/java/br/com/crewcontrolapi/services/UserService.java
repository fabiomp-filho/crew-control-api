package br.com.crewcontrolapi.services;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.enums.RoleEnum;
import br.com.crewcontrolapi.exception.BusinessException;
import br.com.crewcontrolapi.mapper.UserMapper;
import br.com.crewcontrolapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public UserDTO getUser(String username) throws UsernameNotFoundException {
        Optional<User> userToGet = userRepository.findByEmail(username);

        User user = userToGet.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + username));

        return UserMapper.toDTO(user);
    }

    @Transactional
    public void saveUser(UserRegistrationDTO userRegistrationDTO, String role) {

        if (role.equals(String.valueOf(RoleEnum.LEADER)) && userRegistrationDTO.getRole() == RoleEnum.LEADER) {
            throw new BusinessException("Líderes não podem cadastrar outros líderes!");
        }

        if (userRegistrationDTO.getRole() == RoleEnum.ADMINISTRATOR) {
            throw new BusinessException("Nenhum usuário pode cadastrar um administrador!");
        }

        User entityToSave = UserMapper.toEntity(userRegistrationDTO, passwordEncoder);
        userRepository.save(entityToSave);
    }
}
