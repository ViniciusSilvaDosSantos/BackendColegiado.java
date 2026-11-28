package backend.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PautaRequestDTO(
        @NotBlank(message = "O título da pauta é obrigatório.")
        String titulo,

        String descricao,

        @NotNull(message = "A ordem de apresentação é obrigatória.")
        @Positive(message = "A ordem de apresentação deve ser maior que zero.")
        Integer ordemApresentacao,

        String deliberacao
) {
}