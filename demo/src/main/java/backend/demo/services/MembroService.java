package backend.demo.services;

import backend.demo.dtos.MembroDTO;
import backend.demo.dtos.MembroResponseDTO;
import backend.demo.entities.Membro;
import backend.demo.repositories.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembroService {

    private final MembroRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MembroResponseDTO cadastrar(MembroDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um membro cadastrado com este e-mail."
            );
        }
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A senha é obrigatória para o cadastro."
            );
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
        return MembroResponseDTO.fromEntity(buscarEntidadePorId(id));
    }

    @Transactional
    public MembroResponseDTO atualizar(Long id, MembroDTO dto) {
        Membro membro = buscarEntidadePorId(id);

        if (!membro.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro membro utilizando este e-mail."
            );
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
        Membro membro = buscarEntidadePorId(id);
        membro.setAtivo(false);
        repository.save(membro);
    }

    private Membro buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Membro não encontrado com ID: " + id
                ));
    }
}