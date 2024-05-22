package br.com.crewcontrolapi.controllers;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.dto.user.UserUpdateDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.domain.pagination.CustomPage;
import br.com.crewcontrolapi.exception.AccessDeniedException;
import br.com.crewcontrolapi.exception.BusinessException;
import br.com.crewcontrolapi.infra.security.TokenService;
import br.com.crewcontrolapi.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
@Tag(name = "auth-controller")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, HttpServletRequest request) {
        String token = tokenService.recoverToken(request);
        String role = tokenService.getUserRole(token);

        userService.saveUser(userRegistrationDTO, role);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {

        String token = tokenService.recoverToken(request);
        String username = tokenService.getUserLogin(token);
        User requestingUser = (User) userService.loadUserByUsername(username);

        userService.updateUser(id, userUpdateDTO, requestingUser);

        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = tokenService.recoverToken(request);
            String username = tokenService.getUserLogin(token);
            User requestingUser = (User) userService.loadUserByUsername(username);

            UserDTO userDTO = userService.getUserById(id, requestingUser);
            return ResponseEntity.ok(userDTO);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<CustomPage<UserDTO>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        HttpServletRequest request) {
        try {
            String token = tokenService.recoverToken(request);
            String username = tokenService.getUserLogin(token);
            User requestingUser = (User) userService.loadUserByUsername(username);

            Pageable pageable = PageRequest.of(page, size);
            CustomPage<UserDTO> users = userService.getUsers(pageable, requestingUser);

            return ResponseEntity.ok(users);

        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
