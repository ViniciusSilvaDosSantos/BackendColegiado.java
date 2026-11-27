package backend.demo.controllers;

import backend.demo.dto.DeclaracaoPublicaDTO;
import backend.demo.services.DeclaracaoService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ValidacaoController {

    private final DeclaracaoService service;

    public ValidacaoController(DeclaracaoService service) {
        this.service = service;
    }

    // Público: 200 com dados resumidos ou 404
    @GetMapping("/api/validar/{codigo}")
    public DeclaracaoPublicaDTO validar(@PathVariable String codigo) {
        return service.validar(codigo);
    }
}
