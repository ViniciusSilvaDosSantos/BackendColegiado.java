package backend.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReuniaoRequestDTO(
        @NotNull(message = "A data e hora da reunião são obrigatórias.")
        LocalDateTime dataHora,

        @NotBlank(message = "O local da reunião é obrigatório.")
        String local
) {
}