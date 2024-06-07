package curso.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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





	
}
