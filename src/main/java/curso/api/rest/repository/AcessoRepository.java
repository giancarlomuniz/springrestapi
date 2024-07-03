package curso.api.rest.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Role;


@Repository
@Transactional
public interface AcessoRepository extends JpaRepository<Role, Long> {
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from role where upper(trim(nome_role)) = upper(trim(?1))")
	public boolean existeAcesso(String nomeRole);
	
	
	@Query("select a from Role a where upper(trim(a.nomeRole)) like %?1%")
	List<Role> buscarAcessoDesc(String desc);
	
	
	@Query("select a from Role a where upper(trim(a.nomeRole)) like %?1% and a.usuario = ?2")
	public List<Role> buscarAcessoDes(String nomeRole, Long usario);
	
	@Query(nativeQuery = true, value = "select cast((count(1) / 5) as integer) + 1 as qtdpagina  from  role where usuario_id = ?1")
	public Integer qtdPagina(Long idUsario);
	
	@Query(value =  "select a from Role a where a.usuario = ?1 ")
	public List<Role> findPorPage(Long idUsario, Pageable pageable);
	
	@Query(value = "select a from Role a where a.usuario.id = ?1 ")
	public List<Role> findAcessos(Long idUsario);

}
