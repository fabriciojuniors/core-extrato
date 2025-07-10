package cloud.devjunior.service;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.entity.QContaBancaria;
import cloud.devjunior.entity.Usuario;
import cloud.devjunior.enums.TipoConta;
import cloud.devjunior.mapper.ContaBancariaMapper;
import cloud.devjunior.repository.ContaBancariaRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class ContaBancariaServiceTest {

    @Inject
    ContaBancariaService contaBancariaService;

    @InjectMock
    InstituicaoFinanceiraService instituicaoFinanceiraService;

    @InjectMock
    UsuarioService usuarioService;

    @InjectMock
    ContaBancariaMapper contaBancariaMapper;

    @InjectMock
    ContaBancariaRepository contaBancariaRepository;

    @Nested
    class CriarContaBancariaTest {

        @Test
        void deve_LancarConstraintViolationException_QuandoRequestForNulo() {
            // given
            CadastroContaBancariaRequest request = null;

            // when & then
            assertThrows(ConstraintViolationException.class, () -> contaBancariaService.criar(request));
            verifyNoInteractions(instituicaoFinanceiraService,
                    usuarioService,
                    contaBancariaMapper,
                    contaBancariaRepository);
        }

        @Test
        void deve_LancarConstraintViolationException_QuandoRequestForInvalido() {
            // given
            CadastroContaBancariaRequest request = new CadastroContaBancariaRequest(-1, -1, null, null);

            // when & then
            assertThrows(ConstraintViolationException.class, () -> contaBancariaService.criar(request));
            verifyNoInteractions(instituicaoFinanceiraService,
                    usuarioService,
                    contaBancariaMapper,
                    contaBancariaRepository);
        }

        @Test
        void deve_LancarIllegalArgumentException_QuandoContaBancariaJaExistir() {
            // given
            var request = new CadastroContaBancariaRequest(12345, 678, 1L, TipoConta.CORRENTE);
            when(contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                    request.numero(), request.agencia(), request.instituicaoFinanceiraId()))
                    .thenReturn(true);

            // when & then
            assertThrows(IllegalArgumentException.class, () -> contaBancariaService.criar(request));
            verifyNoInteractions(instituicaoFinanceiraService,
                    usuarioService,
                    contaBancariaMapper);
            verify(contaBancariaRepository)
                    .existsByNumeroContaAndAgenciaAndInstituicao(request.numero(), request.agencia(), request.instituicaoFinanceiraId());
        }

        @Test
        void deve_LancarNotFoundException_QuandoInstituicaoFinanceiraNaoExistir() {
            // given
            var request = new CadastroContaBancariaRequest(12345, 678, 1L, TipoConta.CORRENTE);

            when(contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                    request.numero(), request.agencia(), request.instituicaoFinanceiraId()))
                    .thenReturn(false);

            when(instituicaoFinanceiraService.findById(request.instituicaoFinanceiraId()))
                    .thenThrow(new NotFoundException("Instituição financeira não encontrada."));

            // when & then
            assertThrows(NotFoundException.class, () -> contaBancariaService.criar(request));
            verify(contaBancariaRepository)
                    .existsByNumeroContaAndAgenciaAndInstituicao(request.numero(), request.agencia(), request.instituicaoFinanceiraId());
            verify(instituicaoFinanceiraService).findById(request.instituicaoFinanceiraId());
            verifyNoInteractions(usuarioService, contaBancariaMapper);
        }

        @Test
        void deve_LancarNotFoundException_QuandoUsuarioNaoExistir() {
            // given
            var request = new CadastroContaBancariaRequest(12345, 678, 1L, TipoConta.CORRENTE);

            when(contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                    request.numero(), request.agencia(), request.instituicaoFinanceiraId()))
                    .thenReturn(false);

            when(instituicaoFinanceiraService.findById(request.instituicaoFinanceiraId()))
                    .thenReturn(mock(InstituicaoFinanceira.class));

            when(usuarioService.findCurrent())
                    .thenThrow(new NotFoundException("Usuário não encontrado."));

            // when & then
            assertThrows(NotFoundException.class, () -> contaBancariaService.criar(request));
            verify(contaBancariaRepository)
                    .existsByNumeroContaAndAgenciaAndInstituicao(request.numero(), request.agencia(), request.instituicaoFinanceiraId());
            verify(instituicaoFinanceiraService).findById(request.instituicaoFinanceiraId());
            verify(usuarioService).findCurrent();
            verifyNoInteractions(contaBancariaMapper);
        }

        @Test
        void deve_CriarContaBancariaComSucesso() {
            // given
            var request = new CadastroContaBancariaRequest(12345, 678, 1L, TipoConta.CORRENTE);
            var instituicaoFinanceira = mock(InstituicaoFinanceira.class);
            var usuario = mock(Usuario.class);

            when(contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                    request.numero(), request.agencia(), request.instituicaoFinanceiraId()))
                    .thenReturn(false);

            when(instituicaoFinanceiraService.findById(request.instituicaoFinanceiraId()))
                    .thenReturn(instituicaoFinanceira);

            when(usuarioService.findCurrent())
                    .thenReturn(usuario);

            var contaBancaria = mock(ContaBancaria.class);
            when(contaBancariaMapper.fromCadastroContaBancariaRequest(request, instituicaoFinanceira, usuario))
                    .thenReturn(contaBancaria);

            // when
            contaBancariaService.criar(request);

            // then
            verify(contaBancariaRepository).persistAndFlush(contaBancaria);
        }
    }

    @Nested
    class FindAllTest {

        @Test
        void deve_LancarNotFoundException_QuandoUsuarioNaoExistir() {
            // given
            when(usuarioService.findCurrent())
                    .thenThrow(new NotFoundException("Usuário não encontrado."));

            // when & then
            assertThrows(NotFoundException.class, () -> contaBancariaService.findAll(0, 10));
            verify(usuarioService).findCurrent();
            verifyNoInteractions(contaBancariaRepository);
        }

        @Test
        void deve_RetornarConsultaPaginadaComSucesso() {
            // given
            var usuario = mock(Usuario.class);
            when(usuarioService.findCurrent()).thenReturn(usuario);

            var consultaPaginada = mock(ConsultaPaginadaResponse.class);
            when(contaBancariaRepository.findAll(0, 10, QContaBancaria.contaBancaria.usuario.eq(usuario)))
                    .thenReturn(consultaPaginada);

            // when
            contaBancariaService.findAll(0, 10);

            // then
            verify(contaBancariaRepository).findAll(0, 10, QContaBancaria.contaBancaria.usuario.eq(usuario));
        }
    }

}