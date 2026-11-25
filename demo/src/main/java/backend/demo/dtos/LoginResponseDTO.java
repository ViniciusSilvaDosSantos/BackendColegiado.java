package backend.demo.dtos;

import backend.demo.entities.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
}