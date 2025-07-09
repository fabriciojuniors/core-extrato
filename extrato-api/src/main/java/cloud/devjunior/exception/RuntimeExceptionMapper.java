package cloud.devjunior.exception;

import cloud.devjunior.dto.response.FalhaRequisicao;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException e) {
        var falhaRequisicao = new FalhaRequisicao(
                LocalDateTime.now(),
                e.getMessage()
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(falhaRequisicao)
                .build();
    }
}
