package backend.demo.dto;

import java.time.LocalDateTime;

// Endpoint público: só dados resumidos, sem e-mail/matrícula/IDs
public record DeclaracaoPublicaDTO(String nomeMembro, String semestre,
                                   Integer quantidadeReunioes, Integer cargaHorariaTotal,
                                   LocalDateTime dataEmissao) {}
