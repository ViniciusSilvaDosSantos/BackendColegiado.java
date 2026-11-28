package backend.demo.services;

import backend.demo.dtos.*;
import backend.demo.entities.Membro;
import backend.demo.entities.Participacao;
import backend.demo.entities.Pauta;
import backend.demo.entities.Reuniao;
import backend.demo.repositories.MembroRepository;
import backend.demo.repositories.ParticipacaoRepository;
import backend.demo.repositories.PautaRepository;
import backend.demo.repositories.ReuniaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReuniaoService {

    private final ReuniaoRepository reuniaoRepository;
    private final PautaRepository pautaRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final MembroRepository membroRepository;

    @Transactional
    public ReuniaoResponseDTO criarReuniao(ReuniaoRequestDTO dto) {
        Reuniao reuniao = new Reuniao();
        reuniao.setDataHora(dto.dataHora());
        reuniao.setLocal(dto.local().trim());
        reuniao.setStatus("AGENDADA");

        Reuniao salva = reuniaoRepository.save(reuniao);
        return ReuniaoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public PautaResponseDTO adicionarPauta(Long reuniaoId, PautaRequestDTO dto) {
        Reuniao reuniao = buscarReuniaoOuFalhar(reuniaoId);
        validarReuniaoAberta(reuniao);

        Pauta pauta = new Pauta();
        pauta.setReuniao(reuniao);
        pauta.setTitulo(dto.titulo().trim());
        pauta.setDescricao(dto.descricao());
        pauta.setOrdemApresentacao(dto.ordemApresentacao());
        pauta.setDeliberacao(dto.deliberacao());

        Pauta salva = pautaRepository.save(pauta);
        return PautaResponseDTO.fromEntity(salva);
    }

    @Transactional
    public ParticipacaoResponseDTO adicionarParticipacao(Long reuniaoId, ParticipacaoRequestDTO dto) {
        Reuniao reuniao = buscarReuniaoOuFalhar(reuniaoId);
        validarReuniaoAberta(reuniao);

        if (participacaoRepository.findByReuniaoIdAndMembroId(reuniaoId, dto.membroId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este membro já está registrado nesta reunião!");
        }

        Membro membro = membroRepository.findById(dto.membroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado!"));

        Participacao participacao = new Participacao();
        participacao.setReuniao(reuniao);
        participacao.setMembro(membro);
        participacao.setStatusPresenca(dto.statusPresenca().trim().toUpperCase());

        Participacao salva = participacaoRepository.save(participacao);
        return ParticipacaoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public ReuniaoResponseDTO finalizarReuniao(Long reuniaoId) {
        Reuniao reuniao = buscarReuniaoOuFalhar(reuniaoId);
        validarReuniaoAberta(reuniao);

        reuniao.setStatus("FINALIZADA");

        List<Pauta> pautas = pautaRepository.findByReuniaoIdOrderByOrdemApresentacaoAsc(reuniaoId);
        List<Participacao> participacoes = participacaoRepository.findByReuniaoId(reuniaoId);

        String ataTexto = gerarTextoDaAta(reuniao, pautas, participacoes);

        reuniao.setAtaConteudo(ataTexto);
        reuniao.setDataGeracaoAta(LocalDateTime.now());

        Reuniao finalizada = reuniaoRepository.save(reuniao);
        return ReuniaoResponseDTO.fromEntity(finalizada);
    }

    @Transactional(readOnly = true)
    public List<ReuniaoResponseDTO> listarTodasReunioes() {
        return reuniaoRepository.findAll()
                .stream()
                .map(ReuniaoResponseDTO::fromEntity)
                .toList();
    }
    @Transactional(readOnly = true)
    public ReuniaoResponseDTO buscarPorId(Long reuniaoId) {
        return ReuniaoResponseDTO.fromEntity(buscarReuniaoOuFalhar(reuniaoId));
    }

    @Transactional(readOnly = true)
    public List<PautaResponseDTO> listarPautas(Long reuniaoId) {
        buscarReuniaoOuFalhar(reuniaoId);
        return pautaRepository.findByReuniaoIdOrderByOrdemApresentacaoAsc(reuniaoId)
                .stream()
                .map(PautaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarParticipacoes(Long reuniaoId) {
        buscarReuniaoOuFalhar(reuniaoId);
        return participacaoRepository.findByReuniaoId(reuniaoId)
                .stream()
                .map(ParticipacaoResponseDTO::fromEntity)
                .toList();
    }

    private Reuniao buscarReuniaoOuFalhar(Long reuniaoId) {
        return reuniaoRepository.findById(reuniaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunião não encontrada!"));
    }

    private void validarReuniaoAberta(Reuniao reuniao) {
        if ("FINALIZADA".equalsIgnoreCase(reuniao.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta reunião já foi finalizada e não pode mais ser alterada."
            );
        }
    }

    private String gerarTextoDaAta(Reuniao reuniao, List<Pauta> pautas, List<Participacao> participacoes) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        sb.append("ATA DE REUNIÃO DE COLEGIADO\n");
        sb.append("Data e Hora: ").append(reuniao.getDataHora().format(formatter)).append("\n");
        sb.append("Local: ").append(reuniao.getLocal()).append("\n\n");

        sb.append("--- PARTICIPANTES ---\n");
        for (Participacao p : participacoes) {
            sb.append("- ").append(p.getMembro().getNome())
                    .append(" (").append(p.getStatusPresenca()).append(")\n");
        }

        sb.append("\n--- PAUTAS E DELIBERAÇÕES ---\n");
        for (Pauta pauta : pautas) {
            sb.append(pauta.getOrdemApresentacao()).append(". ")
                    .append(pauta.getTitulo()).append("\n");

            String deliberacao = pauta.getDeliberacao();
            sb.append("Deliberação: ").append(deliberacao != null ? deliberacao : "Sem deliberação registrada.").append("\n\n");
        }

        sb.append("Reunião encerrada e ata gerada automaticamente pelo sistema.");

        return sb.toString();
    }
}