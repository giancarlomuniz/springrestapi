package curso.api.rest.controller;

import java.awt.Window;
import java.util.List;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Professor;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.ProfessorRepository;
import curso.api.rest.repository.UsuarioRepository;


@CrossOrigin
@RestController
@RequestMapping(value = "/usuario/professor")
public class ProfessorController  {
	
	@Autowired

	private ProfessorRepository professorRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;

	



	
	//Consulta por id usuario usando api restfull

		
		@GetMapping(value = "/{id}", produces = "application/json")
		@Cacheable("cacheprof")
		public ResponseEntity<Professor> initV1(@PathVariable(value = "id") Long id) {
			
	Optional<Professor> professor =	professorRepository.findById(id);
		
			return new ResponseEntity<Professor>( professor.get(), HttpStatus.OK);
		}

		

		
		
	

	
	@CacheEvict(value = "cacheprofessor", allEntries = true)//Exclui todos cache não mais utilizados
	@CachePut(value = "cacheprofessores")// atualiza caches modificados
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Page<Professor>> professor(Pageable pageable) throws InterruptedException {
		
		
	    Page<Professor> professores =  professorRepository.findAll(pageable);

	    return new ResponseEntity<Page<Professor>>(professores, HttpStatus.OK);
	}
	
	
	
	/* END-POINT consulta por nome de usaurio */
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/consultanome/{nome}", produces = "application/json")
	public ResponseEntity<Page<Professor>> consultaNome(@PathVariable("nome") String nome, Pageable pageable)
			throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Professor> list = null;
		if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
			list = professorRepository.findAll(page);
		} else {

			list = professorRepository.findUserByNamePage(nome, pageable);
		}

		return new ResponseEntity<Page<Professor>>(list, HttpStatus.OK);
	}
	

	//Salvando usuario usando microserviço restfull @PostMapping
	@PostMapping( value = "/", produces = "application/json")
		public ResponseEntity<Professor> cadastrar(@RequestBody Professor professor) throws Exception{
	     
	if (professor.getUsuario() == null || professor.getUsuario().getLogin() == null) {
		throw  new  Exception("Os dados do Usuarios estão incorretos");
	}
		
		Usuario usuario =usuarioRepository.save(professor.getUsuario());
		 String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuario.setSenha(senhaCriptogrfado);
		
	     professor.setUsuario(usuario);
	   
		
		Professor salvarProf = professorRepository.save(professor);
		
		return new ResponseEntity<Professor>(salvarProf, HttpStatus.OK);
		}
	@CacheEvict(value = "cacheprofessor", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheprofessor") // atualiza caches modificados
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Professor>> professorPagina(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));

		Page<Professor> list = professorRepository.findAll(page);

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<Page<Professor>>(list, HttpStatus.OK);
	}
	
	
	
	//Salvando usuario usando microserviço restfull @PostMapping
	@PutMapping( value = "/", produces = "application/json")
		public ResponseEntity<Professor> atualizar(@RequestBody Professor professor) throws Exception{
	     
	if (professor.getUsuario() == null || professor.getUsuario().getLogin() == null) {
		throw  new  Exception("Os dados do Usuarios estão incorretos");
	}
		
		Usuario usuario =usuarioRepository.save(professor.getUsuario());
		 String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuario.setSenha(senhaCriptogrfado);
		
	     professor.setUsuario(usuario);
	   
		
		Professor salvarProf = professorRepository.save(professor);
		
		return new ResponseEntity<Professor>(salvarProf, HttpStatus.OK);
		}
	
	

	
	
	
	//Metodo delete api restfull, usando o CRUD DO SPRING  
		@DeleteMapping(value = "/{id}", produces = "application/text")
		public String delete (@PathVariable(value = "id") Long id) {
		
	          professorRepository.deleteById(id);
		
		     return "Professor deletado com sucesso";
		
		}
		

}
