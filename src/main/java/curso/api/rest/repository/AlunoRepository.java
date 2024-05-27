package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Aluno;
import curso.api.rest.model.Professor;
import curso.api.rest.model.Usuario;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	@Query("select u from Usuario u where u.login= ?1")
	Usuario findUserByLogin(String login);
	
	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUserByNome(String nome);

	@Query("select p from Aluno p where p.codigo= ?1")
	Professor findUserByCodigo(Long codigo);

	@Query(nativeQuery = true, value = "select  * from Aluno where codigo_aluno = ?1")
	List<Aluno> findProfByCodigo(Long id);

	@Query(nativeQuery = true, value = "select * from Aluno  join Usuario  on usuario_id = id  where nome like %?1%")
	Page<Aluno> findUserByNamePage(String nome, Pageable pageable);

	


}
