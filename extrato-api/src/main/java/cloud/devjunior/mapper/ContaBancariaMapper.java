package cloud.devjunior.mapper;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.dto.response.ContaBancariaResponse;
import cloud.devjunior.dto.response.InstituicaoFinanceiraResponse;
import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "cdi")
public interface ContaBancariaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicaoFinanceira", source = "instituicaoFinanceira")
    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "saldo", constant = "0.00")
    @Mapping(target = "tipo", source = "request.tipo")
    ContaBancaria fromCadastroContaBancariaRequest(CadastroContaBancariaRequest request,
                                                   InstituicaoFinanceira instituicaoFinanceira,
                                                   Usuario usuario);

    @Mapping(target = "instituicaoFinanceira", expression = "java(fromInstituicaoFinanceira(contaBancaria.getInstituicaoFinanceira()))")
    ContaBancariaResponse fromContaBancaria(ContaBancaria contaBancaria);

    @Named("fromInstituicaoFinanceira")
    default InstituicaoFinanceiraResponse fromInstituicaoFinanceira(InstituicaoFinanceira instituicaoFinanceira) {
        if (instituicaoFinanceira == null) {
            return null;
        }
        return new InstituicaoFinanceiraResponse(instituicaoFinanceira.getId(), instituicaoFinanceira.getNome());
    }

}
