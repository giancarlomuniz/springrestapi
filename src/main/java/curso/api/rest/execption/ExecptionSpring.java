package curso.api.rest.execption;

public class ExecptionSpring extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	
	public ExecptionSpring(String msgErro) {
		super(msgErro);
	}


	public ExecptionSpring() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ExecptionSpring(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}


	public ExecptionSpring(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


	public ExecptionSpring(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
