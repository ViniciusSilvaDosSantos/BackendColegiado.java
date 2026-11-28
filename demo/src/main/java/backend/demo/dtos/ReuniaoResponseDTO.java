package backend.demo.dtos;

import backend.demo.entities.Reuniao;

import java.time.LocalDateTime;

public record ReuniaoResponseDTO(
        Long id,
        LocalDateTime dataHora,
        String local,
        String status,
        String ataConteudo,
        LocalDateTime dataGeracaoAta
) {
    public static ReuniaoResponseDTO fromEntity(Reuniao reuniao) {
        return new ReuniaoResponseDTO(
                reuniao.getId(),
                reuniao.getDataHora(),
                reuniao.getLocal(),
                reuniao.getStatus(),
                reuniao.getAtaConteudo(),
                reuniao.getDataGeracaoAta()
        );
    }
}