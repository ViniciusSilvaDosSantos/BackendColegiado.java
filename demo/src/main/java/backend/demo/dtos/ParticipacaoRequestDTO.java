package backend.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipacaoRequestDTO(
        @NotNull(message = "O ID do membro é obrigatório.")
        Long membroId,

        @NotBlank(message = "O status de presença é obrigatório.")
        String statusPresenca
) {
}