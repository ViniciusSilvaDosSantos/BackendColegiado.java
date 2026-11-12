package backend.demo.controllers;

import backend.demo.entities.Participacao;
import backend.demo.entities.Pauta;
import backend.demo.entities.Reuniao;
import backend.demo.services.ReuniaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reunioes")
public class ReuniaoController {

    @Autowired
    private ReuniaoService reuniaoService;

    // 1. Endpoint para criar uma Reunião (Usado no Passo 1 do Postman)
    @PostMapping
    public ResponseEntity<Reuniao> criarReuniao(@RequestBody Reuniao reuniao) {
        Reuniao novaReuniao = reuniaoService.criarReuniao(reuniao);
        return ResponseEntity.ok(novaReuniao);
    }

    // 2. Endpoint para adicionar Pauta na Reunião (Usado no Passo 2 do Postman)
    @PostMapping("/{id}/pautas")
    public ResponseEntity<Pauta> adicionarPauta(@PathVariable Long id, @RequestBody Pauta pauta) {
        Pauta novaPauta = reuniaoService.adicionarPauta(id, pauta);
        return ResponseEntity.ok(novaPauta);
    }

    // 3. Endpoint para adicionar Participação na Reunião (Usado no Passo 3 do Postman)
    @PostMapping("/{id}/participacoes")
    public ResponseEntity<Participacao> adicionarParticipacao(@PathVariable Long id, @RequestBody Participacao participacao) {
        Participacao novaParticipacao = reuniaoService.adicionarParticipacao(id, participacao);
        return ResponseEntity.ok(novaParticipacao);
    }

    // 4. Endpoint que o frontend vai chamar para fechar a reunião e gerar a ata (O Grande Final!)
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Reuniao> finalizarReuniao(@PathVariable Long id) {
        try {
            Reuniao reuniaoFinalizada = reuniaoService.finalizarReuniao(id);
            return ResponseEntity.ok(reuniaoFinalizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // 5. Endpoint para o frontend consultar a lista de reuniões
    @GetMapping
    public ResponseEntity<List<Reuniao>> listarReunioes() {
        List<Reuniao> lista = reuniaoService.listarTodasReunioes();
        return ResponseEntity.ok(lista);
    }
}