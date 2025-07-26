package cloud.devjunior.mapper;

import cloud.devjunior.dto.request.CadastroImportacaoRequest;
import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.Importacao;
import cloud.devjunior.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ImportacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", source = "usuario")
    Importacao fromCadastroImportacaoRequest(CadastroImportacaoRequest request,
                                             Usuario usuario);
}
