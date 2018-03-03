package Data;

import java.util.ArrayList;

public class Respuesta {

	private int canal=0;
	private String respuesta="";
	private ArrayList<String> palabras;
	
	public Respuesta(int canal,ArrayList<String> palabras,String respuesta){
		palabras=new ArrayList<String>();
		this.canal=canal;
		this.palabras=palabras;
		this.respuesta=respuesta;
	}

	public String getRespuesta() {
		return respuesta;
	}
	
	public int getCanal() {
		return canal;
	}

	public boolean estanPalabras(String pregunta){
		boolean esta=false;
		int numPalabras=0;
		
		for(int i=0;i<palabras.size();i++){
			if(pregunta.toLowerCase().contains(palabras.get(i))){
				numPalabras++;
			}
		}
		
		if(numPalabras>=(palabras.size()/2)){
			esta=true;
		}
		
		return esta;
	}
}
