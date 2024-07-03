package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
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
import curso.api.rest.model.Categoria;
import curso.api.rest.model.Usuario;
import curso.api.rest.model.DTO.CategoriaDTO;
import curso.api.rest.repository.CategoriaRepository;

@RestController
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	

	
	@GetMapping(value = "**/lsitaCategoria", produces = "application/json")
	public ResponseEntity<List<Categoria>> Categorias(){
		
		List<Categoria> lista = categoriaRepository.findAll();

		return new ResponseEntity<List<Categoria>>(lista,HttpStatus.OK );
	}
	
	
	
	//BUSCA CATEGORIA POR ID
	@GetMapping(value = "/buscaId/{id}", produces = "application/json")
	public ResponseEntity<Categoria> initV1(@PathVariable(value = "id") Long id) {

		Categoria categoria = categoriaRepository.findById(id).get();

		return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
	}
	
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteCategoria") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteAcesso(@RequestBody Categoria categoria) throws ExecptionSpring { /*Recebe o JSON e converte pra Objeto*/
		
		if (categoriaRepository.findById(categoria.getId()).isPresent() == false) {
			
			
			throw new ExecptionSpring("Categoria já foi removida");
		}
		
		categoriaRepository.deleteById(categoria.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Categoria Removida"),HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarCategoria")
	public ResponseEntity<Categoria> salvarCategoria(@RequestBody Categoria categoria) throws ExecptionSpring {
		
		if (categoria.getUsuario() == null ) {
			throw new ExecptionSpring("A empresa deve ser informada.");
		}
		
		if (categoria.getId() == null && categoriaRepository.existeCatehoria(categoria.getDescricao())) {
			throw new ExecptionSpring("Não pode cadastar categoria com mesmo nome.");
		}
		
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		
		return new ResponseEntity<Categoria>(categoriaSalva, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "**/deletarCategoria")
	public ResponseEntity<String> deletarCategoria(@RequestBody Categoria categoria) throws ExecptionSpring {
		
		if (categoriaRepository.findById(categoria.getId()).isPresent() == false){
			throw new ExecptionSpring ("Categoria ja foi removida");
		}

	 categoriaRepository.deleteById(categoria.getId());
		
		
		return new ResponseEntity<String>(new Gson().toJson("Categoria removida com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscaPorDesCategoria/{descricao}/{usuario}")
	public ResponseEntity<List<Categoria>>buscaPorDesCategoria2(@PathVariable("descricao")String descricao,
			@PathVariable("usuario")Long usuario) throws ExecptionSpring {
		
	
             List<Categoria> acesso = categoriaRepository.buscarCategoria(descricao.toUpperCase(),usuario);
		
		
		return new ResponseEntity<List<Categoria>>(acesso, HttpStatus.OK);
	}
	
	//LISTAR POR PAGINA
	@GetMapping(value = "/listaPorPageCategoria/{idUser}/{pagina}", produces = "application/json")
	public ResponseEntity<List<Categoria>> pageCategoria(@PathVariable("idUser") Long idUser, @PathVariable("pagina") Integer pagina) {

		 Pageable pageable = PageRequest.of(pagina, 5, Sort.by("descricao"));
		
List<Categoria> lista = categoriaRepository.findPorPagina(idUser, pageable);

		return new ResponseEntity<List<Categoria>>(lista, HttpStatus.OK);
	}
	

	//QUANTIDADES PAGINA
	@GetMapping(value = "/qtdPagina/{idUser}", produces = "application/json")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idUser") Long idUser) {

		Integer qtdpagina = categoriaRepository.qtdpagina(idUser);

		return new ResponseEntity<Integer>(qtdpagina, HttpStatus.OK);
	}
	
	
	//Listar todos categoria sem paginação
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/listaPage/{idUser}", produces = "application/json")
	public ResponseEntity<List<Categoria>> listPage(@PathVariable("idUser") Long idUser) throws InterruptedException {

	
		List<Categoria> list = categoriaRepository.findAll();

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<List<Categoria>>(list, HttpStatus.OK);
	}

	
}
