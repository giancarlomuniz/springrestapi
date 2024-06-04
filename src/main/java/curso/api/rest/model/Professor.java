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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
public class Professor implements Serializable  {
	

	private static final long serialVersionUID = 1L;

	
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_prof")
	private Long id;


	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
	private Date dataAdmProf;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario = new Usuario();
   
public Professor() {
	// TODO Auto-generated constructor stub
}
	

	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		Professor other = (Professor) obj;
		return Objects.equals(id, other.id);
	}

	public Date getDataAdmProf() {
		return dataAdmProf;
	}

	public void setDataAdmProf(Date dataAdmProf) {
		this.dataAdmProf = dataAdmProf;
	}






	

}
