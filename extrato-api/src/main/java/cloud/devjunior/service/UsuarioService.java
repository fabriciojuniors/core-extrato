package cloud.devjunior.service;

import cloud.devjunior.entity.Usuario;
import cloud.devjunior.repository.UsuarioRepository;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@RequestScoped
public class UsuarioService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    UsuarioRepository usuarioRepository;

    public Usuario findCurrent() {
        return usuarioRepository.findByUsername(securityIdentity.getPrincipal().getName())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }
}
