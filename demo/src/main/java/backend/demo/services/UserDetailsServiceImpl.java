package backend.demo.services;

import backend.demo.repositories.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MembroRepository membroRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return membroRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Membro não encontrado com o e-mail: " + email));
    }
}