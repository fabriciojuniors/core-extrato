package cloud.devjunior.service;

import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.repository.InstituicaoFinanceiraRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class InstituicaoFinanceiraServiceTest {

    @Inject
    InstituicaoFinanceiraService instituicaoFinanceiraService;

    @InjectMock
    InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Test
    void deve_RetornarInstituicaoFinanceira_QuandoExistir() {
        // given
        long id = RandomUtils.secure().randomLong();
        var instituicao = mock(InstituicaoFinanceira.class);

        when(instituicaoFinanceiraRepository.findByIdOptional(id)).thenReturn(Optional.of(instituicao));

        // when
        var resultado = instituicaoFinanceiraService.findById(id);

        // then
        assertNotNull(resultado);
        assertEquals(instituicao, resultado);
        verify(instituicaoFinanceiraRepository).findByIdOptional(id);
    }

    @Test
    void deve_LancarNotFoundException_QuandoInstituicaoFinanceiraNaoExistir() {
        // given
        long id = RandomUtils.secure().randomLong();

        when(instituicaoFinanceiraRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> instituicaoFinanceiraService.findById(id));
        verify(instituicaoFinanceiraRepository).findByIdOptional(id);
    }
}