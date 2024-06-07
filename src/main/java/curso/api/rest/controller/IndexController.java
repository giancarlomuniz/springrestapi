package curso.api.rest.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import curso.api.rest.model.UserChart;
import curso.api.rest.model.UserReport;
import curso.api.rest.model.Usuario;
import curso.api.rest.model.DTO.ObjetoMsgGeral;
import curso.api.rest.repository.TelefoneRepository;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.ImplementacaoUserDatailsService;
import curso.api.rest.service.ServiceEnviarEmail;
import curso.api.rest.service.ServiceRelatorio;
import curso.api.rest.service.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
	@Autowired
	private ServiceRelatorio serviceRelatorio;
	@Autowired
	private ServiceEnviarEmail serviceEnviarEmail;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	private ImplementacaoUserDatailsService implementacaoUserDatailsService;

	// Consulta por id usuario usando api restfull

	@CacheEvict(value = "cacheusuarioId", allEntries = true)
	@CachePut(value = "cacheusuarioId")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>((usuario.get()), HttpStatus.OK);
	}

	@GetMapping(value = "/listaUse", produces = "application/json")
	public ResponseEntity<List<Usuario>> listaUsuario() throws InterruptedException {

				List<Usuario> list = usuarioRepository.findAll();


		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		
		return new ResponseEntity<List<Usuario>>( list, HttpStatus.OK);
	}
	
	
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/listaUser", produces = "application/json")
	public ResponseEntity<Page<Usuario>> usuario() throws InterruptedException {

		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("nome"));

		Page<Usuario> list = usuarioRepository.findAll(pageRequest);

	
		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> usuarioPagina(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));

		Page<Usuario> list = usuarioRepository.findAll(page);

		// Thread.sleep(6000);//Interromper o sistema em 6 segundo

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	/* END-POINT consulta por nome de usaurio */
	@CacheEvict(value = "cacheusuarios", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacheusuarios") // atualiza caches modificados
	@GetMapping(value = "/consultanome/{nome}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> consultaNome(@PathVariable("nome") String nome, Pageable pageable)
			throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> list = null;
		if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(page);
		} else {

			list = usuarioRepository.findUserByNamePage(nome, pageable);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	
	

	// Salvando usuario usando microserviço restfull @PostMapping
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {

		System.out.println(usuario.getCargo());
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {

			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		// Declaração de metodos consumindo Vicep publico
	

		String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());

		usuario.setSenha(senhaCriptogrfado);

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		implementacaoUserDatailsService.inserirUserPadrao(usuarioSalvo.getId());

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	// Atualizando usuario usando mapeamento @Putmapping
	@CacheEvict(value = "cacheuserAtua", allEntries = true)
	@CachePut(value = "cacheuserAtua")
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) throws Exception {

		/* outras rotinas antes de atualizar */

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		// cosumirCep(usuario);

		Usuario userCompara = usuarioRepository.findById(usuario.getId()).get();

		if (!userCompara.getSenha().equals(usuario.getSenha())) {

			String senhaCriptogrfado = new BCryptPasswordEncoder().encode(usuario.getSenha());

			usuario.setSenha(senhaCriptogrfado);
		}
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	// teste de atualização de 2 parametros de usuario e vendas usando mapeamento
	// @Putmapping
	@PutMapping(value = "/{idUser}/venda/{idVenda}", produces = "application/json")
	public ResponseEntity atualizarVenda(@PathVariable Long idUser, @PathVariable long idVenda) {

		// Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity("Usuario :" + idUser + ", e Venda :" + idVenda + " atualizados !", HttpStatus.OK);
	}

	// Metodo para delete usando api restfull

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable(value = "id") Long id) {

		usuarioRepository.deleteById(id);

		return "Ok";

	}

	// End Point Deletando Telefone de usuario
	@DeleteMapping(value = "deleteTelefone/{id}", produces = "application/text")
	public String deleteTelefone(@PathVariable(value = "id") Long id) {

		telefoneRepository.deleteById(id);

		return "Telefone deletado com sucesso";

	}

	@GetMapping("/cep/{cep}")
	public Object cosumirCep(@PathVariable String cep) throws Exception {
		/* Consumindo API externo Via Cep */

		URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		String line ="";
		StringBuilder jsonCep = new StringBuilder();

		while ((line = br.readLine()) != null) {
			jsonCep.append(line);

		}
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
	
if(userAux != null) {
	Usuario usuario = new Usuario();
	usuario.setCep(userAux.getCep());
	usuario.setLogradouro(userAux.getLogradouro());
	usuario.setComplemento(userAux.getComplemento());
	usuario.setBairro(userAux.getBairro());
	usuario.setLocalidade(userAux.getLocalidade());
	usuario.setUf(userAux.getUf());
	/* Consumindo API externo Via Cep */
	return usuario;
		
}else {
	 return null;
}
		
	}
	

	@CacheEvict(value = "cacherelatorio", allEntries = true) // Exclui todos cache não mais utilizados
	@CachePut(value = "cacherelatorio") // atualiza caches modificados
	@GetMapping(value = "/relatorio", produces = "application/text")
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request)throws Exception{
		byte[] pdf = serviceRelatorio.gerarRelatorio("RelatorioUsuario",new HashMap(), request.getServletContext());
		
		String base64pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64pdf, HttpStatus.OK);
		
	}
	

	@PostMapping(value="/relatorio/", produces = "application/text")
	public ResponseEntity<String> downloadRelatorioParam(HttpServletRequest request, 
			@RequestBody UserReport userReport) throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		SimpleDateFormat dateFormatParam = new SimpleDateFormat("yyyy-MM-dd");
		
		String dataInicio =  dateFormatParam.format(dateFormat.parse(userReport.getDataNascInicio()));
		
		String dataFim =  dateFormatParam.format(dateFormat.parse(userReport.getDataNascFim()));
		
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFim);
		
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario-param", params,
				request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
		
	}
	
	@PostMapping(value="/relatorionome/", produces = "application/text")
	public ResponseEntity<String> downloadRelatorioParamNome(HttpServletRequest request, 
			@RequestBody UserReport userReport) throws Exception {
		
		
		String nome =  userReport.getNome();
		
		
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("NOME_USER", nome);
	
		
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-nomeusuario-param", params,
				request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
		
	}
	
	
	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<UserChart> graficoUsuario(HttpServletRequest request) {
		  
		UserChart userChart = new UserChart();
		
		List<String> resultado = jdbcTemplate.queryForList("select array_agg (nome) from usuario where salario > 0 and nome <> '' "
				+ "union all select cast(array_agg (salario)as character varying[]) from usuario where salario > 0 and nome <> '' ", String.class);
		
		userChart.setNome(resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", ""));
		userChart.setSalario(resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", ""));
		
		return new ResponseEntity<UserChart>(userChart, HttpStatus.OK);
	}
	
	
	
	

@ResponseBody
	@GetMapping(value = "/possuiAcesso/{username}/{role}")
	public ResponseEntity<Boolean> possuiAcesso(@PathVariable("username") String username, 
			@PathVariable("role") String role) {
		
		String sqlRole = "'" + role.replaceAll(",", "', '") + "'"; 
		
		Boolean possuiAcesso = usuarioService.possuiAcesso(username, sqlRole);
		

	
		
		return new ResponseEntity<Boolean>(possuiAcesso, HttpStatus.OK);
	}

	
	

	@PostMapping(value = "/recuperarSenha",  produces = "application/json")
	public ResponseEntity<ObjetoMsgGeral> recuperarAcesso(@RequestBody String login) throws Exception {
				
		
		Usuario user = usuarioRepository.findUserByLogin(login);
		if (user == null) {
		  return new ResponseEntity<ObjetoMsgGeral>(new ObjetoMsgGeral("Usuario não encontrado"),HttpStatus.OK);
		}
			
		String senha = UUID.randomUUID().toString();
		
		senha = senha.substring(0, 6);
			String senhaCriptografada = new  BCryptPasswordEncoder().encode(senha);;
			usuarioRepository.updateSenhaUser(senhaCriptografada, login);
			StringBuilder msgEmail = new StringBuilder();
			msgEmail.append("<b> Senha nova é :<b/>").append(senha);
			
			serviceEnviarEmail.enviarEmail("Sua nova senha", user.getLogin(), msgEmail.toString());
			
             
		
		
		return new ResponseEntity<ObjetoMsgGeral>(new ObjetoMsgGeral("Senha enviada para o email"), HttpStatus.OK);
	}



}
