package curso.api.rest.model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import curso.rest.api.CustomGrantedAuthority;




@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails{

	
	@JsonCreator
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String login;
	
	private String senha;
	
	private String nome;
	
	private String sexo;
	private BigDecimal salario;
	


	@CPF(message = "CPF Invalido.")
	private String cpf;
	
@JsonFormat(pattern = "dd/MM/yyyy")// Data formatada para o front end
@Temporal(TemporalType.DATE)//Tipo: só Data 
@DateTimeFormat(iso = ISO.DATE, pattern = "dd/MM/yyyy")// tipo de data que o back end vai grava no banco
  private  Date dataNascimento;

	

	@OneToMany(mappedBy="usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch =FetchType.LAZY)
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_role", uniqueConstraints = @UniqueConstraint(
			columnNames = {"usuario_id", "role_id"}, name = "unique_role_user"),
	       joinColumns = @JoinColumn(name="usuario_id", referencedColumnName ="id",table = "usuario",
	   unique = false, foreignKey = @ForeignKey(name="usuario_fk", value = ConstraintMode.CONSTRAINT)),
	       
	
	
	inverseJoinColumns = @JoinColumn(name="role_id", 
	referencedColumnName = "id", updatable = false,
	table = "role",unique = false, foreignKey = @ForeignKey(name="role_fk",value = ConstraintMode.CONSTRAINT)))
    private List<Role> roles = new ArrayList<Role>();
	

	
	
	private String token = "";

	 private String cep;
	 private String logradouro;
	 private String complemento;
	 private String bairro;
	 private String localidade;
	 private String uf;
	 

	
	   @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	    private List<Aluno> Alunos;
	 

	      private String cargo;
	 
	
	
	  
	





	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	
	public BigDecimal getSalario() {
		return salario;
	}


	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	

	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getLogradouro() {
		return logradouro;
	}


	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getLocalidade() {
		return localidade;
	}


	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}


	public String getUf() {
		return uf;
	}


	public void setUf(String uf) {
		this.uf = uf;
	}


	public String getToken() {
		return token;
	}
	
	
	public void setToken(String token) {
		this.token = token;
	}
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public Long getId() {
		return id;
	}
	

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public String setNome(String nome) {
		return this.nome = nome;
	}
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	

	
	

	
	


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	
	@Override
	public List<Role> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
