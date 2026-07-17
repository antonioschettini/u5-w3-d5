package antonioschettini.u5_w3_d5.recordDTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewPrenotazionePayload(
        @NotNull(message = "l'id dell'evento è obbligatorio")
        UUID idEvento,

        @NotNull(message = "l'id dell'utente che prenota è obbligatorio")
        UUID idUtente
) {
}
