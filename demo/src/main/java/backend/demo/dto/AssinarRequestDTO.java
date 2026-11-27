package backend.demo.dto;

// TEMPORÁRIO: quando o Spring Security (Integrante 2) estiver pronto,
// o membro deve vir do usuário autenticado e este DTO deixa de existir.
import jakarta.validation.constraints.NotNull;

public record AssinarRequestDTO(
        @NotNull(message = "O ID do membro é obrigatório") Long membroId
) {}
