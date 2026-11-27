package backend.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record EmitirDeclaracaoDTO(
        @NotNull(message = "O ID do membro é obrigatório") Long membroId,
        @NotBlank(message = "O semestre é obrigatório")
        @Pattern(regexp = "\\d{4}\\.[12]", message = "Semestre inválido. Use ex.: 2026.1") String semestre,
        @Positive(message = "As horas por reunião devem ser maior que zero") Integer horasPorReuniao
) {}
