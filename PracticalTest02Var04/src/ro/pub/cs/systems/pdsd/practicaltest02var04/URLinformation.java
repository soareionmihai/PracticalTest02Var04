package ro.pub.cs.systems.pdsd.practicaltest02var04;
import ro.pub.cs.systems.pdsd.practicaltest02var04.Constants;

/* clasa pentru formatul datelor */
public class URLinformation {

	private String URL;
	private String pagina;

	// constructor initializator cu null
	public URLinformation() {
		this.URL = null;
		this.pagina   = null;
		
	}

	// constructor cu initializare stringuri 
	public URLinformation(
			String URL,
			String pagina) {
		this.URL = URL;
		this.pagina   = pagina;
		
	}
	
	// get-ere si set-ere pentru toate campurile
	public void setURL(String URL) {
		this.URL = URL;
	}
	
	public String getURL() {
		return URL;
	}
	
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	
	public String getPagina() {
		return pagina;
	}
	
	
	/* metoda to-string overriden pentru a face stringul in formatul dorit*/
	@Override
	public String toString() {
		return Constants.URL + ": " + URL + "\n\r" + 
			   Constants.PAGINA + ": " + pagina;
	}

}