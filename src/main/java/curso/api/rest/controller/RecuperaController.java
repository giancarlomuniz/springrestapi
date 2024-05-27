package curso.api.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.ServiceEnviarEmail;
import curso.rest.api.ObjetoErro;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/recuperarSenha")
public class RecuperaController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ServiceEnviarEmail enviarEmail;
	
	@ResponseBody
	@PostMapping(value = "/",  produces = "application/json")
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario login) throws Exception {
				
		ObjetoErro objetoErro = new ObjetoErro();
		
		Usuario user = usuarioRepository.findUserByLogin(login.getLogin());
		if (user == null) {
			objetoErro.setCode("404"); // nao encontrado
			objetoErro.setError("Usuario não encontrado");
		}else {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
            
			String senhaCriptografada = new  BCryptPasswordEncoder().encode(senhaNova);;
			usuarioRepository.updateSenha(senhaCriptografada, user.getId());
			
			enviarEmail.enviarEmail("Recuperação de senha", user.getLogin(), "Sua nova senha é : " + senhaNova);
			
/* Rotina de envio de email*/
			objetoErro.setCode("200"); // nao encontrado
			objetoErro.setError("Acesso enviado para seu e-mail;");
		}
		
		return new ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
	}
	
}
