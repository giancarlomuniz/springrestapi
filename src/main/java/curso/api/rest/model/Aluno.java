package curso.api.rest.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;



@Entity
public class Aluno  implements Serializable  {



  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "codigo_aluno")
	private Long codigo;
	
	private String nomePai;

	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
	private Date dataIngresso;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario = new Usuario();


	public Long getCodigo() {
		return codigo;
	}


	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}



	public String getNomePai() {
		return nomePai;
	}


	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}


	public Date getDataIngresso() {
		return dataIngresso;
	}


	public void setDataIngresso(Date dataIngresso) {
		this.dataIngresso = dataIngresso;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		return Objects.equals(codigo, other.codigo);
	}
	
	
}

	

