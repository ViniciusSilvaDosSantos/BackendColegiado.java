package backend.demo.services;

import backend.demo.dtos.MembroDTO;
import backend.demo.dtos.MembroResponseDTO;
import backend.demo.entities.Membro;
import backend.demo.repositories.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembroService {

    private final MembroRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MembroResponseDTO cadastrar(MembroDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um membro cadastrado com este e-mail.");
        }
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória para o cadastro.");
        }

        Membro membro = Membro.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .perfil(dto.getPerfil())
                .ativo(true)
                .build();

        Membro salvo = repository.save(membro);
        return MembroResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<MembroResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(MembroResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembroResponseDTO buscarPorId(Long id) {
        Membro membro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com ID: " + id));
        return MembroResponseDTO.fromEntity(membro);
    }

    @Transactional
    public MembroResponseDTO atualizar(Long id, MembroDTO dto) {
        Membro membro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com ID: " + id));

        if (!membro.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe outro membro utilizando este e-mail.");
        }

        membro.setNome(dto.getNome());
        membro.setEmail(dto.getEmail());
        membro.setPerfil(dto.getPerfil());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            membro.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Membro atualizado = repository.save(membro);
        return MembroResponseDTO.fromEntity(atualizado);
    }

    @Transactional
    public void inativar(Long id) {
        Membro membro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com ID: " + id));
        membro.setAtivo(false);
        repository.save(membro);
    }
}