package curso.rest.api;

import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority{

	private static final long serialVersionUID = 1L;
	private String authority;
	  public CustomGrantedAuthority(String authority) {
	        this.authority = authority;
	    }
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}

}
