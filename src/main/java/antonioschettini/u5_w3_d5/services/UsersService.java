package antonioschettini.u5_w3_d5.services;

import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.enums.Ruolo;
import antonioschettini.u5_w3_d5.exceptions.BadRequestException;
import antonioschettini.u5_w3_d5.exceptions.NotFoundException;
import antonioschettini.u5_w3_d5.recordDTO.NewUserPayload;
import antonioschettini.u5_w3_d5.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {
    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // salvo uno user e controllo se email e username sono già in uso
    public User save(NewUserPayload body) {
        if (userRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");
        }
        if (userRepository.existsByUsername(body.username())) {
            throw new BadRequestException("Lo username " + body.username() + " è già in uso!");
        }

        User nuovoUtente = new User();
        nuovoUtente.setNome(body.nome());
        nuovoUtente.setCognome(body.cognome());
        nuovoUtente.setEmail(body.email());
        nuovoUtente.setUsername(body.username());

        // todo: bcrypt per la protezione password
        nuovoUtente.setPassword(body.password());

        try {
            nuovoUtente.setRuolo(Ruolo.valueOf(body.ruolo().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Ruolo non valido! Scegli tra UTENTE_NORMALE o ORGANIZZATORE");
        }

        return userRepository.save(nuovoUtente);
    }

    // cerco tutti gli utenti
    public Page<User> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    // trovo utente by id
    public User findById(UUID idUtente) {
        return userRepository.findById(idUtente)
                .orElseThrow(() -> new NotFoundException("Utente con ID " + idUtente + " non trovato!"));
    }

    // trovo utente dall'email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }
}
