package backend.demo.controllers;

import backend.demo.dtos.EmitirDeclaracaoDTO;
import backend.demo.entities.DeclaracaoSemestral;
import backend.demo.services.DeclaracaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/declaracoes")
public class DeclaracaoController {

    private final DeclaracaoService service;

    public DeclaracaoController(DeclaracaoService service) {
        this.service = service;
    }

    @PostMapping
    public Map<String, Object> emitir(@Valid @RequestBody EmitirDeclaracaoDTO dto) {
        DeclaracaoSemestral d = service.emitir(dto.membroId(), dto.semestre(), dto.horasPorReuniao());
        return Map.of("codigoAutenticacao", d.getCodigoAutenticacao(),
                      "quantidadeReunioes", d.getQuantidadeReunioesParticipadas(),
                      "cargaHorariaTotal", d.getCargaHorariaTotal());
    }
}
