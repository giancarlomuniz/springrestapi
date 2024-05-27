package curso.api.rest.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

	@PersistenceContext
	private EntityManager entityManager;

	public Boolean possuiAcesso(String username, String roles) {
	    String sql = "SELECT COUNT(1) > 0 FROM usuarios_role AS ur "
	            + "INNER JOIN usuario AS u ON u.id = ur.usuario_id "
	            + "INNER JOIN role AS r ON r.id = ur.role_id "
	            + "WHERE u.login = '" + username + "' "
	            + "AND r.nome_role IN (" + roles + ")";

	    Query query = entityManager.createNativeQuery(sql);

	    return Boolean.valueOf(query.getSingleResult().toString());
	}
}
