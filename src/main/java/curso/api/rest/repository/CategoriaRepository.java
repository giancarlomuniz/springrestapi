package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


	@Query(nativeQuery = true, value = "select count(1) > 0 from categoria where upper(trim(descricao)) = upper(trim(?1))")
	public boolean existeCatehoria(String descricao);

	@Query("select a from Categoria a where upper(trim(a.descricao)) like %?1%")
	public List<Categoria> buscarCategoria(String descricao);

	@Query("select a from Categoria a where upper(trim(a.descricao)) like %?1% and a.usuario =?2")
	public List<Categoria> buscarCategoria(String descricao, Long usuario);

	@Query(nativeQuery = true, value = "select  cast((count(1)/5)  as integer) +1 as qtdCategoria  from categoria where usuario = ?1 ")
	public Integer qtdpagina(long usuario);
	
	@Query(value = "select a from Categoria a where a.usuario = ?1")
	public List<Categoria> findPorPage(Long usuario, Pageable pageable);

	@Query(value = "SELECT c FROM Categoria c WHERE c.usuario = ?1")
	public List<Categoria> findPorPagina(Long idUser, Pageable pageable);
	
	
	

}
