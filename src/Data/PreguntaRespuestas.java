package Data;

import java.util.ArrayList;

public class PreguntaRespuestas {

	private String respuesta="";
	private ArrayList<String> palabras;
	
	public PreguntaRespuestas(String respuesta, ArrayList<String> palabras) {
		super();
		this.respuesta = respuesta;
		this.palabras = palabras;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	public ArrayList<String> getPalabras() {
		return palabras;
	}
	
	public boolean estanPalabras(String pregunta){
		boolean esta=false;
		int numPalabras=0;
		
		for(int i=0;i<palabras.size();i++){
			if(pregunta.toLowerCase().contains(palabras.get(i))){
				numPalabras++;
			}
		}
		
		if(numPalabras>=2){
			esta=true;
		}
		
		return esta;
	}
}
