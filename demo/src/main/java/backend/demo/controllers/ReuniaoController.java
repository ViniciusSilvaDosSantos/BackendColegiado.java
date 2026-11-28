package backend.demo.controllers;

import backend.demo.dtos.*;
import backend.demo.services.ReuniaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reunioes")
@RequiredArgsConstructor
public class ReuniaoController {

    private final ReuniaoService reuniaoService;

    @PostMapping
    public ResponseEntity<ReuniaoResponseDTO> criarReuniao(@Valid @RequestBody ReuniaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reuniaoService.criarReuniao(dto));
    }

    @PostMapping("/{id}/pautas")
    public ResponseEntity<PautaResponseDTO> adicionarPauta(
            @PathVariable Long id,
            @Valid @RequestBody PautaRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reuniaoService.adicionarPauta(id, dto));
    }

    @PostMapping("/{id}/participacoes")
    public ResponseEntity<ParticipacaoResponseDTO> adicionarParticipacao(
            @PathVariable Long id,
            @Valid @RequestBody ParticipacaoRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reuniaoService.adicionarParticipacao(id, dto));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ReuniaoResponseDTO> finalizarReuniao(@PathVariable Long id) {
        return ResponseEntity.ok(reuniaoService.finalizarReuniao(id));
    }

    @GetMapping
    public ResponseEntity<List<ReuniaoResponseDTO>> listarReunioes() {
        return ResponseEntity.ok(reuniaoService.listarTodasReunioes());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReuniaoResponseDTO> buscarReuniao(@PathVariable Long id) {
        return ResponseEntity.ok(reuniaoService.buscarPorId(id));
    }

    @GetMapping("/{id}/pautas")
    public ResponseEntity<List<PautaResponseDTO>> listarPautas(@PathVariable Long id) {
        return ResponseEntity.ok(reuniaoService.listarPautas(id));
    }

    @GetMapping("/{id}/participacoes")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarParticipacoes(@PathVariable Long id) {
        return ResponseEntity.ok(reuniaoService.listarParticipacoes(id));
    }
}