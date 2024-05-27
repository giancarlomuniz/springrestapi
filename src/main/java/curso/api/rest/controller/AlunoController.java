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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Aluno;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.AlunoRepository;
import curso.api.rest.repository.UsuarioRepository;


@CrossOrigin
@RestController
@RequestMapping(value = "/aluno")
public class AlunoController  {
	
	@Autowired

	private AlunoRepository alunoRepository;
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	



	
	//Consulta por id usuario usando api restfull

		
		@GetMapping(value = "/{codigo}", produces = "application/json")
		@Cacheable("cachealuno")
		public ResponseEntity<Aluno> initV1(@PathVariable(value = "codigo") Long codigo) {
			
	Optional<Aluno> aluno =	alunoRepository.findById(codigo);
		
			return new ResponseEntity<Aluno>( aluno.get(), HttpStatus.OK);
		}

		

		
		
	

	
	@CacheEvict(value = "cacheAluno", allEntries = true)//Exclui todos cache não mais utilizados
	@CachePut(value = "cacheAluno")// atualiza caches modificados
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Page<Aluno>> Aluno(Pageable pageable) throws InterruptedException {
		
		
	    Page<Aluno> Alunoes =  alunoRepository.findAll(pageable);

	    return new ResponseEntity<Page<Aluno>>(Alunoes, HttpStatus.OK);
	}
	
	
	
	/* END-POINT consulta por nome de usaurio */
	@CacheEvict(value = "cachealuno", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cachealuno") // atualiza caches modificados
	@GetMapping(value = "/consultanome/{nome}", produces = "application/json")
	public ResponseEntity<Page<Aluno>> consultaNome(@PathVariable("nome") String nome, Pageable pageable)
			throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Aluno> list = null;
		if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
			list = alunoRepository.findAll(page);
		} else {

			list = alunoRepository.findUserByNamePage(nome, pageable);
		}

		return new ResponseEntity<Page<Aluno>>(list, HttpStatus.OK);
	}
	

	//Salvando usuario usando microserviço restfull @PostMapping
	@PostMapping( value = "/", produces = "application/json")
		public ResponseEntity<Aluno> cadastrar(@RequestBody Aluno Aluno) throws Exception{
	     
	if (Aluno.getUsuario() == null || Aluno.getUsuario().getLogin() == null) {
		throw  new  Exception("Os dados do Aluno estão incorretos");
	}
		
		Usuario usuario =usuarioRepository.save(Aluno.getUsuario());
		 String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuario.setSenha(senhaCriptogrfado);
		
	     Aluno.setUsuario(usuario);
	   
		
		Aluno salvarProf = alunoRepository.save(Aluno);
		
		return new ResponseEntity<Aluno>(salvarProf, HttpStatus.OK);
		}
	@CacheEvict(value = "cacheAluno", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheAluno") // atualiza caches modificados
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Aluno>> AlunoPagina(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));

		Page<Aluno> list = alunoRepository.findAll(page);

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<Page<Aluno>>(list, HttpStatus.OK);
	}
	
	
	
	//Salvando usuario usando microserviço restfull @PostMapping
	@PutMapping( value = "/", produces = "application/json")
		public ResponseEntity<Aluno> atualizar(@RequestBody Aluno Aluno) throws Exception{
	     
	if (Aluno.getUsuario() == null || Aluno.getUsuario().getLogin() == null) {
		throw  new  Exception("Os dados do Usuarios estão incorretos");
	}
		
		Usuario usuario =usuarioRepository.save(Aluno.getUsuario());
		 String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuario.setSenha(senhaCriptogrfado);
		
	     Aluno.setUsuario(usuario);
	   
		
		Aluno salvarAluno = alunoRepository.save(Aluno);
		
		return new ResponseEntity<Aluno>(salvarAluno, HttpStatus.OK);
		}
	
	

	
	
	
	//Metodo delete api restfull, usando o CRUD DO SPRING  
		@DeleteMapping(value = "/{id}", produces = "application/text")
		public String delete (@PathVariable(value = "id") Long id) {
		
	          alunoRepository.deleteById(id);
		
		     return "Aluno deletado com sucesso";
		
		}
		

}
