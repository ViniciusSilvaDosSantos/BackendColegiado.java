package backend.demo.services;

import backend.demo.dto.DeclaracaoPublicaDTO;
import backend.demo.entities.DeclaracaoSemestral;
import backend.demo.entities.Membro;
import backend.demo.repositories.DeclaracaoSemestralRepository;
import backend.demo.repositories.MembroRepository;
import backend.demo.repositories.ParticipacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeclaracaoService {

    private final DeclaracaoSemestralRepository declaracaoRepository;
    private final MembroRepository membroRepository;
    private final ParticipacaoRepository participacaoRepository;

    public DeclaracaoService(DeclaracaoSemestralRepository declaracaoRepository,
                             MembroRepository membroRepository,
                             ParticipacaoRepository participacaoRepository) {
        this.declaracaoRepository = declaracaoRepository;
        this.membroRepository = membroRepository;
        this.participacaoRepository = participacaoRepository;
    }

    /** semestre no formato "2026.1" (jan-jun) ou "2026.2" (jul-dez) */
    @Transactional
    public DeclaracaoSemestral emitir(Long membroId, String semestre, int horasPorReuniao) {
        LocalDateTime[] periodo = periodoDoSemestre(semestre);

        Membro membro = membroRepository.findById(membroId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));

        if (declaracaoRepository.existsByMembroIdAndSemestre(membroId, semestre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Declaração deste semestre já foi emitida");
        }

        long presencas = participacaoRepository.contarPresencas(membroId, periodo[0], periodo[1]);
        if (presencas == 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Nenhuma presença registrada no semestre");
        }

        DeclaracaoSemestral d = new DeclaracaoSemestral();
        d.setMembro(membro);
        d.setSemestre(semestre);
        d.setQuantidadeReunioesParticipadas((int) presencas);
        d.setCargaHorariaTotal((int) presencas * horasPorReuniao);
        d.setCodigoAutenticacao(UUID.randomUUID().toString());
        return declaracaoRepository.save(d);
    }

    @Transactional(readOnly = true)
    public DeclaracaoPublicaDTO validar(String codigo) {
        DeclaracaoSemestral d = declaracaoRepository.findByCodigoAutenticacao(codigo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));
        return new DeclaracaoPublicaDTO(d.getMembro().getNome(), d.getSemestre(),
            d.getQuantidadeReunioesParticipadas(), d.getCargaHorariaTotal(), d.getDataEmissao());
    }

    private LocalDateTime[] periodoDoSemestre(String semestre) {
        if (semestre == null || !semestre.matches("\\d{4}\\.[12]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Semestre inválido. Use ex.: 2026.1");
        }
        int ano = Integer.parseInt(semestre.substring(0, 4));
        boolean primeiro = semestre.endsWith(".1");
        LocalDateTime inicio = LocalDateTime.of(ano, primeiro ? 1 : 7, 1, 0, 0);
        LocalDateTime fim = primeiro ? LocalDateTime.of(ano, 7, 1, 0, 0)
                                     : LocalDateTime.of(ano + 1, 1, 1, 0, 0);
        return new LocalDateTime[]{inicio, fim};
    }
}
