package curso.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Categoria;
import curso.api.rest.repository.CategoriaRepository;

@RestController
@RequestMapping(value = "/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Categoria>> Categorias(){
		
		List<Categoria> lista = categoriaRepository.findAll();
		
		return new ResponseEntity<List<Categoria>>(lista,HttpStatus.OK );
	}
}
