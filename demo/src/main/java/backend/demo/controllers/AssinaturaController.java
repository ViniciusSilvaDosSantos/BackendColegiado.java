package backend.demo.controllers;

import backend.demo.dto.AssinarRequestDTO;
import backend.demo.entities.AssinaturaEletronica;
import backend.demo.services.AssinaturaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/atas")
public class AssinaturaController {

    private final AssinaturaService service;

    public AssinaturaController(AssinaturaService service) {
        this.service = service;
    }

    // {id} = id da reunião. Rota combinada no plano (o Integrante 5 chama esta).
    @PostMapping("/{id}/assinar")
    public Map<String, Object> assinar(@PathVariable Long id,
                                       @Valid @RequestBody AssinarRequestDTO body,
                                       HttpServletRequest request) {
        AssinaturaEletronica a = service.assinarAta(id, body.membroId(), extrairIp(request));
        return Map.of("mensagem", "Ata assinada com sucesso",
                      "hash", a.getHashAssinatura(),
                      "dataHora", a.getDataHoraAssinatura().toString());
    }

    private String extrairIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
