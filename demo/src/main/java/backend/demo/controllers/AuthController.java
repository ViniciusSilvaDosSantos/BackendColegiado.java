package backend.demo.controllers;

import backend.demo.dtos.LoginRequestDTO;
import backend.demo.dtos.LoginResponseDTO;
import backend.demo.entities.Membro;
import backend.demo.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Membro membro = (Membro) authentication.getPrincipal();
        String token = jwtService.gerarToken(membro);

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                membro.getId(),
                membro.getNome(),
                membro.getEmail(),
                membro.getPerfil()
        ));
    }
}