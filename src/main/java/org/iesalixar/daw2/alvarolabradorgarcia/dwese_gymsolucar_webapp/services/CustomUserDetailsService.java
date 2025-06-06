package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.services;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Usuario;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return org.springframework.security.core.userdetails.User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(usuario.getRoles().stream()
                        .map(rol -> rol.getNombre())
                        .collect(Collectors.toList())
                        .toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isEstadoCuenta())
                .build();
    }
}
