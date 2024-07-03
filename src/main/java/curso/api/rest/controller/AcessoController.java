package curso.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import curso.api.rest.execption.ExecptionSpring;
import curso.api.rest.model.Role;
import curso.api.rest.repository.AcessoRepository;

@RestController
public class AcessoController {

	@Autowired
	private AcessoRepository acessoRepository;
	

	
	@GetMapping(value = "**/lsitaAcesso", produces = "application/json")
	public ResponseEntity<List<Role>> Acesso(){
		
		List<Role> lista = acessoRepository.findAll();

		return new ResponseEntity<List<Role>>(lista,HttpStatus.OK );
	}
	
	
	
	//BUSCA ACESSO POR ID
	@GetMapping(value = "/buscarId/{id}", produces = "application/json")
	public ResponseEntity<Role> initV2(@PathVariable(value = "id") Long id) {

		Role role = acessoRepository.findById(id).get();

		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteAcesso") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteAcesso(@RequestBody Role role) throws ExecptionSpring { /*Recebe o JSON e converte pra Objeto*/
		
		if (acessoRepository.findById(role.getId()).isPresent() == false) {
			
			
			throw new ExecptionSpring("Acesso já foi removido");
		}
		
		acessoRepository.deleteById(role.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Acesso Removido"),HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarAcesso")
	public ResponseEntity<Role> salvarAcesso(@RequestBody Role role) throws ExecptionSpring {
		
		if (role.getUsuario() == null ) {
			throw new ExecptionSpring("O Usuairio deve ser informado.");
		}
		
		if (role.getId() == null && acessoRepository.existeAcesso(role.getNomeRole())) {
			throw new ExecptionSpring("Não pode cadastar acesso com mesmo nome.");
		}
	
		
		Role salvarAcesso = acessoRepository.save(role);
		
		
		return new ResponseEntity<Role>(salvarAcesso, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "**/deletaAcesso")
	public ResponseEntity<String> deletarAcesso(@RequestBody Role role) throws ExecptionSpring {
		
		if (acessoRepository.findById(role.getId()).isPresent() == false){
			throw new ExecptionSpring ("Acesso ja foi removido");
		}

	 acessoRepository.deleteById(role.getId());
		
		
		return new ResponseEntity<String>(new Gson().toJson("Acesso removido com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscaPorNome/{nomeRole}/{usuario}")
	public ResponseEntity<List<Role>>buscaPorNome(@PathVariable("nomeRole")String nomeRole,
			@PathVariable("usuario")Long usuario) throws ExecptionSpring {
		
	
             List<Role> acesso = acessoRepository.buscarAcessoDes(nomeRole.toUpperCase(),usuario);
		
		
		return new ResponseEntity<List<Role>>(acesso, HttpStatus.OK);
	}
	
	//LISTAR POR PAGINA
	@GetMapping(value = "/listaPorPagina/{idUser}/{pagina}", produces = "application/json")
	public ResponseEntity<List<Role>> pageAcesso(@PathVariable("idUser") Long idUser, @PathVariable("pagina") Integer pagina) {

		 Pageable pageable = PageRequest.of(pagina, 5, Sort.by("nomeRole"));
		
List<Role> lista = acessoRepository.findPorPage(idUser, pageable);

		return new ResponseEntity<List<Role>>(lista, HttpStatus.OK);
	}
	

	//QUANTIDADES PAGINA
	@GetMapping(value = "/qtdPaginas/{idUser}", produces = "application/json")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idUser") Long idUser) {

		Integer qtdpagina = acessoRepository.qtdPagina(idUser);

		return new ResponseEntity<Integer>(qtdpagina, HttpStatus.OK);
	}
	
	
	//Listar todos  sem paginação
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/listaPagina/{idUser}", produces = "application/json")
	public ResponseEntity<List<Role>> listPage(@PathVariable("idUser") Long idUser) throws InterruptedException {

	
		List<Role> list = acessoRepository.findAll();

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<List<Role>>(list, HttpStatus.OK);
	}

	
}
