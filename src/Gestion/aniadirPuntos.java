package Gestion;

import java.util.ArrayList;

public class aniadirPuntos extends Thread{
	
	private ArrayList<String> usuarios;
	private Conexion gbd;
	
	public aniadirPuntos(ArrayList<String> usuarios,Conexion gbd){
		this.usuarios=usuarios;
		this.gbd=gbd;
	}
	
	public void run(){
		for(int i=0;i<usuarios.size();i++){
			gbd.aniadirPunto(usuarios.get(i));
		}
	}

}
