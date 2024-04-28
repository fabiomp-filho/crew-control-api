package br.com.crewcontrolapi.controllers;

import br.com.crewcontrolapi.domain.dto.user.UserDTO;
import br.com.crewcontrolapi.domain.dto.user.UserInfoDTO;
import br.com.crewcontrolapi.domain.dto.user.UserLoginDTO;
import br.com.crewcontrolapi.domain.entities.user.User;
import br.com.crewcontrolapi.infra.security.TokenService;
import br.com.crewcontrolapi.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
@Tag(name = "auth-controller")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.getLogin(), userLoginDTO.getPassword());

        try {
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) auth.getPrincipal());
            UserDTO userLogged = userService.getUser(userLoginDTO.getLogin());
            UserInfoDTO userInfo = new UserInfoDTO(token, userLogged);
            return ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        }
    }
}
