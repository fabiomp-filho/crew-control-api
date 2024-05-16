package br.com.crewcontrolapi.services;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.dto.user.UserUpdateDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.enums.RoleEnum;
import br.com.crewcontrolapi.exception.AccessDeniedException;
import br.com.crewcontrolapi.exception.BusinessException;
import br.com.crewcontrolapi.mapper.UserMapper;
import br.com.crewcontrolapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id, User requestingUser) {
        Optional<User> userToGet = userRepository.findById(id);
        UserDTO userDTO = userToGet.map(UserMapper::toDTO)
                .orElseThrow(() -> new BusinessException("Usuário com o id: " + id + " Não encontrado"));

        if (!canAccessUserDetails(requestingUser, userDTO)) {
            throw new AccessDeniedException("Você não tem permissão para acessar as informações deste usuário.");
        }

        return userDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(Long id, UserUpdateDTO userUpdateDTO, User requestingUser) {

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));

        boolean isRoleChangeAttempt = !userToUpdate.getRole().equals(userUpdateDTO.getRole());

        if (userUpdateDTO.getRole().equals(RoleEnum.ADMINISTRATOR) && !userToUpdate.getRole().equals(RoleEnum.ADMINISTRATOR)) {
            throw new IllegalStateException("Não é possível cadastrar administradores!");
        }

        if (requestingUser.getRole().equals(RoleEnum.ADMINISTRATOR)) {
            if (userToUpdate.getRole().equals(RoleEnum.ADMINISTRATOR) && !userToUpdate.getEmail().equals(requestingUser.getEmail())) {
                throw new IllegalStateException("Administradores não podem mudar a informação de outros administradores.");
            }
            User updatedUser = UserMapper.toUpdateAdmin(userToUpdate, userUpdateDTO);
            userRepository.save(updatedUser);
            return;
        }

        if (requestingUser.getRole().equals(RoleEnum.LEADER)) {
            if (isRoleChangeAttempt) {
                throw new IllegalStateException("Líderes não têm permissão para mudar roles.");
            }
            if (userToUpdate.getRole().equals(RoleEnum.COLLABORATOR) && isInSameGroup(requestingUser, userToUpdate)) {
                User updatedUser = UserMapper.toUpdate(userToUpdate, userUpdateDTO);
                userRepository.save(updatedUser);
                return;
            }
        }

        if (requestingUser.getId().equals(userToUpdate.getId())) {
            if (isRoleChangeAttempt) {
                throw new IllegalStateException("Você não tem permissão para mudar roles.");
            }
            User updatedUser = UserMapper.toUpdate(userToUpdate, userUpdateDTO);
            userRepository.save(updatedUser);
            return;
        }

        throw new IllegalStateException("Operação não permitida");
    }

    private boolean isInSameGroup(User requestingUser, User userToUpdate) {
        return requestingUser.getTeam().getId().equals(userToUpdate.getTeam().getId());
    }

    private boolean canAccessUserDetails(User requestingUser, UserDTO userDTO) {
        RoleEnum requesterRole = requestingUser.getRole();
        RoleEnum targetRole = userDTO.getRole();

        if (requesterRole.equals(RoleEnum.ADMINISTRATOR)) {
            return !targetRole.equals(RoleEnum.ADMINISTRATOR) || requestingUser.getId().equals(userDTO.getId());
        }

        if (requesterRole.equals(RoleEnum.LEADER)) {
            return !targetRole.equals(RoleEnum.LEADER) && !targetRole.equals(RoleEnum.ADMINISTRATOR) || requestingUser.getId().equals(userDTO.getId());
        }

        if (requesterRole.equals(RoleEnum.COLLABORATOR)) {
            return requestingUser.getId().equals(userDTO.getId());
        }

        return false;
    }
}
