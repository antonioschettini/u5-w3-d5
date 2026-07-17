package antonioschettini.u5_w3_d5.controllers;

import antonioschettini.u5_w3_d5.entities.User;
import antonioschettini.u5_w3_d5.recordDTO.NewUserPayload;
import antonioschettini.u5_w3_d5.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // post per registrazione
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public User saveUser(@RequestBody @Validated NewUserPayload body) {
        return usersService.save(body);
    }

    // get all
    @GetMapping
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idUser") String sortBy
    ) {
        return usersService.findAll(page, size, sortBy);
    }

    @GetMapping("/me")
    public User getMyProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    // get by id
    @GetMapping("/{idUtente}")
    public User getUserById(@PathVariable UUID idUtente) {
        return usersService.findById(idUtente);
    }
}