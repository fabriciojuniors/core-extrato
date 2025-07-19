package cloud.devjunior.mapper;

import cloud.devjunior.dto.response.MovimentacaoResponse;
import cloud.devjunior.entity.Movimentacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta-cdi", uses = {ContaBancariaMapper.class})
public interface MovimentacaoMapper {

    @Mapping(target = "tipo", source = "movimentacao.tipoMovimentacao")
    MovimentacaoResponse toResponse(Movimentacao movimentacao);
}

