package br.com.crewcontrolapi.services;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.dto.user.UserUpdateDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.domain.pagination.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDTO getUser(String username) throws UsernameNotFoundException;

    UserDTO getUserById(Long id, User requestingUser);

    void saveUser(UserRegistrationDTO userRegistrationDTO, String role);

    void updateUser(Long id, UserUpdateDTO userUpdateDTO, User requestingUser);

    CustomPage<UserDTO> getUsers(Pageable pageable, User requestingUser);

}