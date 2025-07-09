package cloud.devjunior.exception;

import cloud.devjunior.dto.response.FalhaRequisicao;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        var falhaRequisicao = new FalhaRequisicao(LocalDateTime.now(), e.getMessage());
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(falhaRequisicao)
                .build();
    }
}
