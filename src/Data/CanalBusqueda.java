package Data;

import java.util.ArrayList;

public class CanalBusqueda {

	private String nombre="";
	private int numeroBusqueda=0;
	private int canal=0;
	private ArrayList<String> usuariosBuscando;
	private int espaciador=0;
	private String descripcion="";
	
	public CanalBusqueda(int canal,String nombre,int espaciador, int numeroBusqueda) {
		super();
		this.nombre = nombre;
		this.numeroBusqueda = numeroBusqueda;
		this.canal = canal;
		this.espaciador=espaciador;
		usuariosBuscando=new ArrayList<String>();
	}
	
	public CanalBusqueda(int canal,String nombre,int espaciador, int numeroBusqueda,String descripcion) {
		super();
		this.nombre = nombre;
		this.numeroBusqueda = numeroBusqueda;
		this.canal = canal;
		this.espaciador=espaciador;
		this.descripcion=descripcion;
		usuariosBuscando=new ArrayList<String>();
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public int getNumeroBusqueda() {
		return numeroBusqueda;
	}
	
	public int getCanal() {
		return canal;
	}

	public void setCanal(int canal) {
		this.canal = canal;
	}

	public ArrayList<String> getUsuariosBusqueda() {
		return usuariosBuscando;
	}

	public int getUsuariosBuscando(){
		return usuariosBuscando.size();
	}
	
	public void setUsuariosBusqueda(ArrayList<String> usuariosBuscando) {
		this.usuariosBuscando = usuariosBuscando;
	}

	public int getEspaciador() {
		return espaciador;
	}
	
}
