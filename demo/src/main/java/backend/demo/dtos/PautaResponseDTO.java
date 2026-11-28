package backend.demo.dtos;

import backend.demo.entities.Pauta;

public record PautaResponseDTO(
        Long id,
        Long reuniaoId,
        String titulo,
        String descricao,
        Integer ordemApresentacao,
        String deliberacao
) {
    public static PautaResponseDTO fromEntity(Pauta pauta) {
        return new PautaResponseDTO(
                pauta.getId(),
                pauta.getReuniao().getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getOrdemApresentacao(),
                pauta.getDeliberacao()
        );
    }
}