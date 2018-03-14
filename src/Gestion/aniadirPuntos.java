package Gestion;

import java.util.ArrayList;

public class aniadirPuntos extends Thread{
	private Conexion gbdExterna;
	private GestionBD gbdInterna;
	private ArrayList<String> usuarios;
	

	public aniadirPuntos(ArrayList<String> usuarios, Conexion gbdExterna) {
		this.usuarios=usuarios;
		this.gbdExterna=gbdExterna;
	}

	public aniadirPuntos(ArrayList<String> usuarios, GestionBD gbdInterna) {
		this.usuarios=usuarios;
		this.gbdInterna=gbdInterna;
	}

	public void run(){
		if(gbdInterna!=null) {
			for(int i=0;i<usuarios.size();i++) {
				gbdInterna.aniadirPunto(usuarios.get(i));
			}
		}else {
			for(int i=0;i<usuarios.size();i++) {
				gbdInterna.aniadirPunto(usuarios.get(i));
			}
		}
	}
}
