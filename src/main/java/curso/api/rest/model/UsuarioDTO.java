package curso.api.rest.model;

import java.io.Serializable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long Id;
	private String login;
	private String nome;
	private String senha;
	
	
	private String cpf;

	public UsuarioDTO(Usuario usuario) {
		this.Id = usuario.getId();
		this.login = usuario.getLogin();
		this.nome = usuario.getNome();
		this.senha = usuario.getSenha();
		this.cpf = usuario.getCpf();

	}

	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	

}
