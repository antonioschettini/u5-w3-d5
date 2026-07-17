package antonioschettini.u5_w3_d5.recordDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserPayload(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 3, message = "Lo username deve avere almeno 3 caratteri")
        String username,

        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, message = "Il nome deve avere almeno 2 caratteri")
        String nome,

        @NotBlank(message = "il cognome è obbligatorio")
        @Size(min = 2, message = "il cognome deve avere almeno 2 caratteri")
        String cognome,

        @NotBlank(message = "l'email è obbligatoria")
        @Email(message = "l'email non è valida")
        String email,

        @NotBlank(message = "la password è obbligatoria")
        @Size(min = 4, message = "La password deve contenere almeno 4 caratteri")
        String password,

        @NotBlank(message = "il ruolo è obbligatorio (Scegli tra utente_normale o ornanizzatore")
        String ruolo
) {
}
