package br.com.crewcontrolapi.controllers;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserInfoDTO;
import br.com.crewcontrolapi.domain.dto.user.UserLoginDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.infra.security.TokenService;
import br.com.crewcontrolapi.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {

        Optional<UserDTO> userOptional = userService.getUser(userLoginDTO.getLogin());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: Credenciais inválidas");
        }

        UserDTO userLogged = userOptional.get();
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.getLogin(), userLoginDTO.getPassword());

        try {
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) auth.getPrincipal());
            UserInfoDTO userInfo = new UserInfoDTO(token, userLogged);
            return ResponseEntity.ok(userInfo);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        }
    }
}
