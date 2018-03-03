package Data;

public class TotalUsuarios {
	
	private String mensaje="";
	private int idCanal=1;
	
	public TotalUsuarios(String mensaje, int idCanal) {
		super();
		this.mensaje = mensaje;
		this.idCanal = idCanal;
	}
	
	public TotalUsuarios() {
		this.mensaje="";
		this.idCanal=1;
	}

	public String getMensaje() {
		return mensaje;
	}

	public int getIdCanal() {
		return idCanal;
	}

}
