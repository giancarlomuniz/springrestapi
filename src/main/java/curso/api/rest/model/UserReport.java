package curso.api.rest.model;



public class UserReport {

	
	private String dataNascInicio;
	
	private String dataNascFim;
	
	private String nome;

	public String getDataNascInicio() {
		return dataNascInicio;
	}

	public void setDataNascInicio(String dataNascInicio) {
		this.dataNascInicio = dataNascInicio;
	}

	public String getDataNascFim() {
		return dataNascFim;
	}

	public void setDataNascFim(String dataNascFim) {
		this.dataNascFim = dataNascFim;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	
}
