package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.exceptions.UnauthorizedException;
import antonioschettini.u5_w3_d5.recordDTO.LoginPayload;
import antonioschettini.u5_w3_d5.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsersService usersService;
    private final JWTTools jwtTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsersService usersService, JWTTools jwtTools, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String controllaCredenzialiEGeneraToken(LoginPayload body) {
        // cerco l'utente con l'email inserita
        User user = usersService.findByEmail(body.email());

        //confronto se la pssw dell'utente matchi quella brcryptata
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            // se passa genero il token
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
}