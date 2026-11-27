package backend.demo.services;

import backend.demo.entities.AssinaturaEletronica;
import backend.demo.entities.Participacao;
import backend.demo.entities.Reuniao;
import backend.demo.repositories.AssinaturaRepository;
import backend.demo.repositories.ParticipacaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final String chaveSecreta;

    public AssinaturaService(AssinaturaRepository assinaturaRepository,
                             ParticipacaoRepository participacaoRepository,
                             @Value("${assinatura.chave-secreta}") String chaveSecreta) {
        this.assinaturaRepository = assinaturaRepository;
        this.participacaoRepository = participacaoRepository;
        this.chaveSecreta = chaveSecreta;
    }

    @Transactional
    public AssinaturaEletronica assinarAta(Long reuniaoId, Long membroId, String ip) {
        Participacao participacao = participacaoRepository
            .findByReuniaoIdAndMembroId(reuniaoId, membroId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Membro não participa desta reunião"));

        if (!"PRESENTE".equals(participacao.getStatusPresenca())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Só quem esteve presente pode assinar a ata");
        }

        // O conteúdo da ata vem SEMPRE do banco, nunca do cliente
        Reuniao reuniao = participacao.getReuniao();
        String ata = reuniao.getAtaConteudo();
        if (ata == null || ata.isBlank()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ata ainda não foi gerada");
        }
        if (assinaturaRepository.existsByParticipacaoId(participacao.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ata já assinada por este membro");
        }

        // Milissegundos: o Postgres não guarda nanos, e o hash precisa ser reproduzível
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        String base = ata + "|" + reuniaoId + "|" + membroId + "|" + ip + "|" + agora + "|" + chaveSecreta;

        AssinaturaEletronica a = new AssinaturaEletronica();
        a.setParticipacao(participacao);
        a.setIpOrigem(ip);
        a.setDataHoraAssinatura(agora);
        a.setHashAssinatura(sha256Hex(base));
        return assinaturaRepository.save(a);
    }

    static String sha256Hex(String texto) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                .digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }
}
