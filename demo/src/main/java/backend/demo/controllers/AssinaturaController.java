package backend.demo.controllers;

import backend.demo.entities.AssinaturaEletronica;
import backend.demo.entities.Membro;
import backend.demo.services.AssinaturaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/atas")
@RequiredArgsConstructor
public class AssinaturaController {

    private final AssinaturaService service;

    @PostMapping("/{id}/assinar")
    public ResponseEntity<?> assinar(
            @PathVariable Long id,
            @AuthenticationPrincipal Membro membroAutenticado,
            HttpServletRequest request
    ) {
        AssinaturaEletronica a = service.assinarAta(id, membroAutenticado.getId(), extrairIp(request));
        return ResponseEntity.ok(Map.of(
                "mensagem", "Ata assinada com sucesso",
                "hash", a.getHashAssinatura(),
                "dataHora", a.getDataHoraAssinatura().toString()
        ));
    }

    private String extrairIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}