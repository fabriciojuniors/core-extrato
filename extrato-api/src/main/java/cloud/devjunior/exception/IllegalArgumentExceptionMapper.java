package cloud.devjunior.exception;

import cloud.devjunior.dto.response.FalhaRequisicao;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {
        var falhaRequisicao = new FalhaRequisicao(LocalDateTime.now(), e.getMessage());
        return Response
                .status(422)
                .entity(falhaRequisicao)
                .build();
    }
}
