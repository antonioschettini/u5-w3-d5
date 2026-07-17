package antonioschettini.u5_w3_d5.recordDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPayload(
        @NotBlank(message = "l'email è obbligatoria")
        @Email(message = "inserisci un formato di email corretto")
        String email,

        @NotBlank(message = "la password è obbligatoria")
        String password
) {
}
