package Gestion;

import java.util.ArrayList;

import Data.Rango;

public class Utilidades {

	public String sacarContrasenia(String mensaje){
		String contra="";
		
		try{
			if(mensaje.length()<20){
				contra=mensaje.substring(6, mensaje.length());
			}else{
				contra="gzone";
			}
		}catch(Exception e){
			//El error se produce cuando se usa el comando sin contra
		}
		
		
		return contra.trim();
	}

	public String sacarNombre(String message) {
				
		if(message.contains("!name")){
			message=message.substring(6, message.length());
		}
				
		if(message.contains("[URL]")){
			message=message.substring(message.indexOf("[URL]")+5, message.length());
		}
				
		if(message.contains("http")){
			message=message.substring(message.indexOf("http")+6, message.length());
		}
				
		if(message.contains("www")){
			message=message.substring(message.lastIndexOf("www.")+4, message.length());
		}
				
		if(message.contains("[/URL]")){
			message=message.substring(0,message.indexOf("[/URL]"));
		}
				
		return message.trim();
	}
	
	public ArrayList<Rango> ordenarRangos(ArrayList<Rango> desordenados){
		ArrayList<Rango> rangosOrdenados=new ArrayList<Rango>();
		
		//Aqui se colocaran los rangos de menos a mas minutos
		
		return rangosOrdenados;
	}
	
	public String sacarUniqueId(String mensaje){
		String uniqueid="";

		try{
			if(mensaje.length()>13){
				uniqueid=mensaje.substring(13).trim();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return uniqueid;
	}
	
	
}
