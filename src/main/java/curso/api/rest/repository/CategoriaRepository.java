package curso.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


	@Query(nativeQuery = true, value = "select count(1) > 0 from categoria where upper(trim(descricao)) = upper(trim(?1))")
	public boolean existeCatehoria(String descricao);

	
	
	

}
