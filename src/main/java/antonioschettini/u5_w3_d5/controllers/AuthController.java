package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.recordDTO.LoginPayload;
import antonioschettini.u5_w3_d5.recordDTO.LoginResponse;
import antonioschettini.u5_w3_d5.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Validated LoginPayload body) {
        String token = authService.controllaCredenzialiEGeneraToken(body);
        return new LoginResponse(token);
    }
}