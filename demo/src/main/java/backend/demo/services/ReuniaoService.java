package backend.demo.services;

import backend.demo.entities.Participacao;
import backend.demo.entities.Pauta;
import backend.demo.entities.Reuniao;
import backend.demo.repositories.ParticipacaoRepository;
import backend.demo.repositories.PautaRepository;
import backend.demo.repositories.ReuniaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    // 1. Cria uma nova reunião
    public Reuniao criarReuniao(Reuniao reuniao) {
        return reuniaoRepository.save(reuniao);
    }

    // 2. Adiciona uma pauta a uma reunião existente
    public Pauta adicionarPauta(Long reuniaoId, Pauta pauta) {
        Reuniao reuniao = reuniaoRepository.findById(reuniaoId)
                .orElseThrow(() -> new RuntimeException("Reunião não encontrada!"));

        pauta.setReuniao(reuniao);
        return pautaRepository.save(pauta);
    }

    // 3. Adiciona uma participação (presença) a uma reunião
    public Participacao adicionarParticipacao(Long reuniaoId, Participacao participacao) {
        Reuniao reuniao = reuniaoRepository.findById(reuniaoId)
                .orElseThrow(() -> new RuntimeException("Reunião não encontrada!"));

        participacao.setReuniao(reuniao);
        return participacaoRepository.save(participacao);
    }

    // 4. Método principal que finaliza a reunião e constrói a ata
    public Reuniao finalizarReuniao(Long reuniaoId) {
        Reuniao reuniao = reuniaoRepository.findById(reuniaoId)
                .orElseThrow(() -> new RuntimeException("Reunião não encontrada!"));

        reuniao.setStatus("FINALIZADA");

        List<Pauta> pautas = pautaRepository.findByReuniaoIdOrderByOrdemApresentacaoAsc(reuniaoId);
        List<Participacao> participacoes = participacaoRepository.findByReuniaoId(reuniaoId);

        String ataTexto = gerarTextoDaAta(reuniao, pautas, participacoes);

        reuniao.setAtaConteudo(ataTexto);
        reuniao.setDataGeracaoAta(LocalDateTime.now());

        return reuniaoRepository.save(reuniao);
    }

    // Método auxiliar (privado) de formatação de texto
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
    // 5. Lista todas as reuniões cadastradas
    public List<Reuniao> listarTodasReunioes() {
        return reuniaoRepository.findAll();
    }
}