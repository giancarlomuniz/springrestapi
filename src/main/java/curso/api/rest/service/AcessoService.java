package curso.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.api.rest.model.Role;
import curso.api.rest.repository.AcessoRepository;

@Service
public class AcessoService {
	
	@Autowired
	private AcessoRepository acessoRepository;
	
public Role save(Role acesso) {
		
		/*Qualquer tipo de validação*/
		
		return acessoRepository.save(acesso);
	}

}
