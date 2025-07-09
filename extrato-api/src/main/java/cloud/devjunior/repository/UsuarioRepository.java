package cloud.devjunior.repository;

import cloud.devjunior.entity.QUsuario;
import cloud.devjunior.entity.Usuario;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UsuarioRepository extends QuerydslBaseRepository<Usuario, String> implements PanacheRepository<Usuario> {

    @Override
    protected EntityPath<Usuario> getEntityPath() {
        return QUsuario.usuario;
    }

    @Override
    protected SimpleExpression<String> getIdPath() {
        return QUsuario.usuario.id;
    }

    public Usuario findByUsername(String username) {
        return queryFactory.selectFrom(getEntityPath())
                .where(QUsuario.usuario.username.equalsIgnoreCase(username))
                .fetchFirst();
    }
}
