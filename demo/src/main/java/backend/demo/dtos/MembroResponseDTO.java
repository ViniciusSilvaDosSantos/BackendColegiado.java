package backend.demo.dtos;

import backend.demo.entities.Membro;
import backend.demo.entities.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembroResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private Boolean ativo;

    public static MembroResponseDTO fromEntity(Membro membro) {
        return new MembroResponseDTO(
                membro.getId(),
                membro.getNome(),
                membro.getEmail(),
                membro.getPerfil(),
                membro.getAtivo()
        );
    }
}