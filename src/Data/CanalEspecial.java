package Data;

public class CanalEspecial{
	
	private int idServerGroup=0;
	private int espaciador=0;
	private String nombreCanales="";
	
	public CanalEspecial(int espaciador, String nombreCanales,int idServerGroup) {
		this.espaciador=espaciador;
		this.idServerGroup=idServerGroup;
		this.nombreCanales=nombreCanales;
	}

	public int getIdServerGroup() {
		return idServerGroup;
	}

	public int getEspaciador() {
		return espaciador;
	}

	public String getNombreCanales() {
		return nombreCanales;
	}

}
