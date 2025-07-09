package cloud.devjunior.mapper;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.InstituicaoFinanceira;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ContaBancariaMapper {

    @Mapping(target = "instituicaoFinanceira", source = "instituicaoFinanceira")
    ContaBancaria fromCadastroContaBancariaRequest(CadastroContaBancariaRequest request,
                                                   InstituicaoFinanceira instituicaoFinanceira);

}
