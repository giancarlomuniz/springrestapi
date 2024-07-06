package curso.api.rest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Aluno;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.AlunoRepository;
import curso.api.rest.repository.UsuarioRepository;


@CrossOrigin
@RestController
@RequestMapping(value = "/usuario/aluno")
public class AlunoController  {
	
	@Autowired
	private AlunoRepository alunoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;

	



	
	//Consulta por id usuario usando api restfull

		
		@GetMapping(value = "/{id}", produces = "application/json")
		@Cacheable("cacheprof")
		public ResponseEntity<Aluno> consutaPorId(@PathVariable(value = "id") Long id) {
			
	Optional<Aluno> professor =	alunoRepository.findById(id);
		
			return new ResponseEntity<Aluno>( professor.get(), HttpStatus.OK);
		}

		

		
		
	

	
	@CacheEvict(value = "cacheprofessor", allEntries = true)//Exclui todos cache não mais utilizados
	@CachePut(value = "cacheprofessores")// atualiza caches modificados
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Page<Aluno>> professor(Pageable pageable) throws InterruptedException {
		
		
	    Page<Aluno> professores =  alunoRepository.findAll(pageable);

	    return new ResponseEntity<Page<Aluno>>(professores, HttpStatus.OK);
	}
	
	
	
	
	//Salvando usuario usando microserviço restfull @PostMapping
	@PostMapping( value = "/salvar", produces = "application/json")
		public ResponseEntity<Aluno> salvarAluno(@RequestBody Aluno aluno) throws Exception{
	     
	if (aluno.getId() == null) {
		throw  new  Exception("Os dados do Aluno estão incorretos");
	}
	
	usuarioRepository.save(aluno.getUsuario());
		 
		Aluno salvarAluno = alunoRepository.save(aluno);
		
		return new ResponseEntity<>(salvarAluno, HttpStatus.OK);
		}
	

	
	
	//QUANTIDADES PAGINA
		@GetMapping(value = "/qtdPagina", produces = "application/json")
		public ResponseEntity<Integer> qtdPagina( ) {

			Integer qtdpagina = usuarioRepository.qtdpagina();

			return new ResponseEntity<Integer>(qtdpagina, HttpStatus.OK);
		}
	
	@CacheEvict(value = "cacheprofessor", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheprofessor") // atualiza caches modificados
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> professorPagina(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		
		String prof = "Aluno";

		Page<Usuario> list = usuarioRepository.findPorPagina(prof, page);

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
		

}
