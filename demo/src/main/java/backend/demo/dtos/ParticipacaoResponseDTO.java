package backend.demo.dtos;

import backend.demo.entities.Participacao;

public record ParticipacaoResponseDTO(
        Long id,
        Long reuniaoId,
        Long membroId,
        String nomeMembro,
        String statusPresenca
) {
    public static ParticipacaoResponseDTO fromEntity(Participacao participacao) {
        return new ParticipacaoResponseDTO(
                participacao.getId(),
                participacao.getReuniao().getId(),
                participacao.getMembro().getId(),
                participacao.getMembro().getNome(),
                participacao.getStatusPresenca()
        );
    }
}