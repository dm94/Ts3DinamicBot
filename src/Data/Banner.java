package Data;

public class Banner {
	private String url="";
	private String imagen="";
	
	public Banner(String url, String imagen){
		this.url=url;
		this.imagen=imagen;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	

}
