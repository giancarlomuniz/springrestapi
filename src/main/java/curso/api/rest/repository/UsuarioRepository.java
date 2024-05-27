package curso.api.rest.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.login= ?1")
	Usuario findUserByLogin(String login);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where login =?2")
	void atualizarTokenUser(String token, String login);
	
	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUserByNome(String nome);
	
	 
	@Query("select u from Usuario u where u.id = ?1")
	Usuario findUseById(Usuario usuario) ;
	
	
	@Query(nativeQuery = true, value = "SELECT constraint_name FROM information_schema.constraint_column_usage where table_name = 'usuarios_role' and column_name = 'role_id' "
			+ "and constraint_name <> 'unique_role_user'")
	String consultaConstraintRoleUser();
    
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_role(usuario_id, role_id) values(?1,(select id from role where nome_role = 'ROLE_USER'));")
	void inserirConstraintRole(Long idUser);
	
	@Transactional
	@Modifying
	@Query(value = "update Usuario set login =?1 where senha =?2" , nativeQuery = true)
	void updateSenha(String login, Long senha) ;
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "update usuario set senha =?1 where  login = ?2", nativeQuery = true)
	void updateSenhaUser(String senha, String login) ;
	

	

	
		
	
/*METODO PARA BUSCA POR NOME PAGINADO. FEITO PELO PROFESSOR
 * 
 * 
	default Page<Usuario> findUserByNomePage(String nome, Pageable pageable ){
	
	Usuario usuario = new Usuario();
	usuario.setNome(nome);
	
	ExampleMatcher exemplaMatcher = ExampleMatcher.matchingAny()
			.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers
					.contains().ignoreCase());
		
	Example<Usuario> example = Example.of(usuario, exemplaMatcher);
	Page<Usuario> retorno =  (Page<Usuario>) findAll(example, pageable);
		
	return retorno;
	}
*/
	default Page<Usuario> findUserByNamePage(String nome, Pageable pageable) {
	    Usuario usuario = new Usuario();
	    usuario.setNome(nome);

	    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
	            .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

	    Example<Usuario> example = Example.of(usuario, exampleMatcher);

	    return findAll(example, pageable);
	}

	

	
	
}
