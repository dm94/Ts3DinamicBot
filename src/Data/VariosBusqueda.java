package Data;

import java.util.ArrayList;

public class VariosBusqueda {
	
	private int idCanal=0;
	private ArrayList<CanalBusqueda> canalesBusqueda=new ArrayList<CanalBusqueda>();
	
	public VariosBusqueda(int idCanal, ArrayList<CanalBusqueda> canalesBusqueda) {
		super();
		this.idCanal = idCanal;
		this.canalesBusqueda = canalesBusqueda;
	}

	public int getIdCanal() {
		return idCanal;
	}
	
	
	public ArrayList<CanalBusqueda> getCanalesBusqueda() {
		return canalesBusqueda;
	}

	public void setCanalesBusqueda(ArrayList<CanalBusqueda> canalesBusqueda) {
		this.canalesBusqueda = canalesBusqueda;
	}
	
}
