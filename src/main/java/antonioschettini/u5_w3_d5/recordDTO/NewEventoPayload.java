package antonioschettini.u5_w3_d5.recordDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewEventoPayload(
        @NotBlank(message = "il titolo dell'evento è obbligatorio")
        String titolo,

        @NotBlank(message = "la descrizione è obbligatoria")
        String descrizione,

        @NotNull(message = "la data è obbligatoria")
        LocalDate data,

        @NotBlank(message = "il luogo è obbligatorio")
        String luogo,

        @Min(value = 1, message = "l'evento deve avere almeno un posto disponibile")
        int postiTotali
) {
}
