package backend.demo.services;

import backend.demo.entities.DeclaracaoSemestral;
import backend.demo.entities.Membro;
import backend.demo.repositories.DeclaracaoSemestralRepository;
import backend.demo.repositories.MembroRepository;
import backend.demo.repositories.ParticipacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeclaracaoServiceTest {

    @Mock DeclaracaoSemestralRepository declaracaoRepository;
    @Mock MembroRepository membroRepository;
    @Mock ParticipacaoRepository participacaoRepository;

    DeclaracaoService service;

    @BeforeEach
    void setUp() {
        service = new DeclaracaoService(declaracaoRepository, membroRepository, participacaoRepository);
    }

    @Test
    void rejeitaSemestreInvalido() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.emitir(1L, "2026-3", 2));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void emiteComUuidECalculaCargaHoraria() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(new Membro()));
        when(declaracaoRepository.existsByMembroIdAndSemestre(1L, "2026.2")).thenReturn(false);
        when(participacaoRepository.contarPresencas(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(3L);
        when(declaracaoRepository.save(any(DeclaracaoSemestral.class))).thenAnswer(i -> i.getArgument(0));

        DeclaracaoSemestral d = service.emitir(1L, "2026.2", 2);

        assertEquals(3, d.getQuantidadeReunioesParticipadas());
        assertEquals(6, d.getCargaHorariaTotal());
        assertDoesNotThrow(() -> UUID.fromString(d.getCodigoAutenticacao()));
    }

    @Test
    void naoEmiteSemPresencas() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(new Membro()));
        when(declaracaoRepository.existsByMembroIdAndSemestre(1L, "2026.1")).thenReturn(false);
        when(participacaoRepository.contarPresencas(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(0L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.emitir(1L, "2026.1", 2));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
    }

    @Test
    void naoEmiteDuasVezesNoMesmoSemestre() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(new Membro()));
        when(declaracaoRepository.existsByMembroIdAndSemestre(1L, "2026.1")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.emitir(1L, "2026.1", 2));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void validarCodigoInexistenteRetorna404() {
        when(declaracaoRepository.findByCodigoAutenticacao("xxx")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.validar("xxx"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
