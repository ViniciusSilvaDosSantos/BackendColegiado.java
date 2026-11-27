package backend.demo.services;

import backend.demo.entities.AssinaturaEletronica;
import backend.demo.entities.Participacao;
import backend.demo.entities.Reuniao;
import backend.demo.repositories.AssinaturaRepository;
import backend.demo.repositories.ParticipacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssinaturaServiceTest {

    @Mock AssinaturaRepository assinaturaRepository;
    @Mock ParticipacaoRepository participacaoRepository;

    AssinaturaService service;

    @BeforeEach
    void setUp() {
        service = new AssinaturaService(assinaturaRepository, participacaoRepository, "segredo-de-teste");
    }

    private Participacao participacao(String status, String ata) {
        Reuniao r = new Reuniao();
        r.setAtaConteudo(ata);
        Participacao p = new Participacao();
        p.setId(10L);
        p.setStatusPresenca(status);
        p.setReuniao(r);
        return p;
    }

    @Test
    void sha256GeraValorConhecido() {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                     AssinaturaService.sha256Hex("abc"));
    }

    @Test
    void assinaComSucessoGerandoHashHexDe64Caracteres() {
        when(participacaoRepository.findByReuniaoIdAndMembroId(1L, 2L))
            .thenReturn(Optional.of(participacao("PRESENTE", "texto da ata")));
        when(assinaturaRepository.existsByParticipacaoId(10L)).thenReturn(false);
        when(assinaturaRepository.save(any(AssinaturaEletronica.class)))
            .thenAnswer(i -> i.getArgument(0));

        AssinaturaEletronica a = service.assinarAta(1L, 2L, "127.0.0.1");

        assertTrue(a.getHashAssinatura().matches("[0-9a-f]{64}"));
        assertEquals("127.0.0.1", a.getIpOrigem());
        assertNotNull(a.getDataHoraAssinatura());
        assertNotNull(a.getParticipacao());
    }

    @Test
    void membroAusenteNaoPodeAssinar() {
        when(participacaoRepository.findByReuniaoIdAndMembroId(1L, 2L))
            .thenReturn(Optional.of(participacao("AUSENTE", "texto da ata")));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.assinarAta(1L, 2L, "127.0.0.1"));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void naoAssinaSeAtaNaoFoiGerada() {
        when(participacaoRepository.findByReuniaoIdAndMembroId(1L, 2L))
            .thenReturn(Optional.of(participacao("PRESENTE", null)));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.assinarAta(1L, 2L, "127.0.0.1"));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void naoAssinaDuasVezes() {
        when(participacaoRepository.findByReuniaoIdAndMembroId(1L, 2L))
            .thenReturn(Optional.of(participacao("PRESENTE", "texto da ata")));
        when(assinaturaRepository.existsByParticipacaoId(10L)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.assinarAta(1L, 2L, "127.0.0.1"));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }
}
