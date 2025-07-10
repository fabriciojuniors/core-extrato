package cloud.devjunior.service;

import cloud.devjunior.entity.Usuario;
import cloud.devjunior.repository.UsuarioRepository;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class UsuarioServiceTest {

    @Inject
    UsuarioService usuarioService;

    @InjectMock
    SecurityIdentity securityIdentity;

    @InjectMock
    UsuarioRepository usuarioRepository;

    @Test
    void deve_RetornarUsuario_QuandoExistir() {
        // given
        var username = "testUser";
        var usuario = mock(Usuario.class);
        var principal = mock(Principal.class);

        when(principal.getName()).thenReturn(username);
        when(securityIdentity.getPrincipal()).thenReturn(principal);
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // when
        var resultado = usuarioService.findCurrent();

        // then
        assertNotNull(resultado);
        assertEquals(usuario, resultado);
    }

    @Test
    void deve_LancarNotFoundException_QuandoUsuarioNaoExistir() {
        // given
        var username = "nonExistentUser";
        var principal = mock(Principal.class);

        when(principal.getName()).thenReturn(username);
        when(securityIdentity.getPrincipal()).thenReturn(principal);
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> usuarioService.findCurrent());
    }
}