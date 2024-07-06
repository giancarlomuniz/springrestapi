package curso.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {


	
	
	

}
