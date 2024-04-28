package br.com.crewcontrolapi.controllers;

import br.com.crewcontrolapi.domain.dto.user.UserRegistrationDTO;
import br.com.crewcontrolapi.domain.dto.user.UserUpdateDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.infra.security.TokenService;
import br.com.crewcontrolapi.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
@Tag(name = "auth-controller")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, HttpServletRequest request) {
        String token = tokenService.recoverToken(request);
        String role = tokenService.getUserRole(token);

        userService.saveUser(userRegistrationDTO, role);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {

        String token = tokenService.recoverToken(request);
        String username = tokenService.getUserLogin(token);
        User user = (User) userService.loadUserByUsername(username);

        userService.updateUser(id, userUpdateDTO, user);

        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
    }
}
