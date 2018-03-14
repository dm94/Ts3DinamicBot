package Gestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Data.Banner;
import Data.CanalBusqueda;
import Data.CanalEspecial;
import Data.Canales;
import Data.PreguntaRespuestas;
import Data.Rango;
import Data.TotalUsuarios;
import Data.VariosBusqueda;

public class ManejoDeArchivos {
	private int canal = 1; //Canal al que se va a mover el bot
	private String usuario = ""; //Usuario del bot
	private String contra = ""; //Contra del bot
	private String direccion = ""; //Direccion del servidor
	private int tiempo=1; //Tiempo en minutos de la frecuencia de comprobacion
	private int puerto = 9987; //Puerto del servidor
	private String nombreBot="Ts3DinamicBot"; //Nombre que tendra el bot
	private String adminBot="WBO8y9P3AWu+ociKtAqFg1wafF0="; //Usuario que podra administrar el bot
	private int canalAtendido=0; //Canal el cual va a mirar el bot
	private String idioma="Spanish"; //Idioma del bot
	private String fConfig="config.txt";
	private String fCanales="canales.txt";
	private String fEspeciales="especiales.txt";
	private String fAutoRank="rangos.txt";
	private final boolean pro=true;
	private ArrayList<Canales> listadoCanales=new ArrayList<Canales>(); //Canales a crear
	private ArrayList<CanalBusqueda> listadoBusqueda=new ArrayList<CanalBusqueda>(); //Canales de busqueda de equipo
	private ArrayList<CanalEspecial> listadoCanalesEspeciales=new ArrayList<CanalEspecial>(); //Canales con permisos especiales
	private ArrayList<PreguntaRespuestas> listadoRespuestas=new ArrayList<PreguntaRespuestas>(); //Respuestas para los diferentes canales
	private ArrayList<VariosBusqueda> listadoVariosBusqueda=new ArrayList<VariosBusqueda>(); //Canal con todas las busquedas de grupo
	private ArrayList<TotalUsuarios> listatoCanalNumUsu=new ArrayList<TotalUsuarios>(); //Canales donde poner el numero de usuarios
	private ArrayList<Rango> listadoRangos=new ArrayList<Rango>(); //Listado de los rangos
	private boolean detectorTrolls=false;
	private boolean detectorVPNs=false;
	private int canalPrincipal=0; //Canal de entrada del teamspeak
	private int grupoTroll=0; //Grupo del que se quiere comprobar
	private int numMaximoCanales=10; //Numero maximo de canales que se van a crear de un mismo tipo
	private boolean autoRank=false;
	private boolean detectorDDos=true;
	private boolean autobanner=false;
	private int canalTwitch=0;
	private String nombreCanalTwitch="";
	private String grupoTwitch="";
	private ArrayList<Banner> banners=new ArrayList<Banner>();
	private String tipoBD="sqlite";
	private String ipBD="";
	private String usuarioBD="";
	private String passBD="";
	private String nombreBD="";
	
	public ManejoDeArchivos(){
		try {
			leerConfig();
			aniadirCanales();
			sacarCanalesEspeciales();
			if(pro){
				aniadirRangos();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void leerConfig() throws IOException{
		
		/*
		 * Formato del archivo:
		 canal:4
		 direccion:127.0.0.1
		 puerto:9987
		 usuario:dm94dani
		 contra:435345
		 */
		String cad = null;
		int inicio=0;
		File f=new File(fConfig);
		int linea=1;
		
		if(!(f.canRead())){
			f.createNewFile();
			System.out.println("El archivo de configuración no existia se ha creado uno");
		}
		
		FileReader pr=new FileReader(f);
		BufferedReader br=new BufferedReader(pr);
		
		while((cad=br.readLine())!=null){
			try{
				if(cad.contains("canalentrada")){
					inicio=cad.indexOf(":");
		            canal=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("direccion")){
					inicio=cad.indexOf(":");
		            direccion=cad.substring(inicio+1).trim();
				}else if(cad.contains("puerto")){
					inicio=cad.indexOf(":");
		            puerto=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("usuario")){
					inicio=cad.indexOf(":");
		            usuario=cad.substring(inicio+1).trim();
				}else if(cad.contains("contra")){
					inicio=cad.indexOf(":");
		            contra=cad.substring(inicio+1).trim();
				}else if(cad.contains("nombrebot")){
					inicio=cad.indexOf(":");
					nombreBot=cad.substring(inicio+1).trim();
				}else if(cad.contains("canalatendido")){
					inicio=cad.indexOf(":");
					canalAtendido=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("admin")){
					inicio=cad.indexOf(":");
					adminBot=cad.substring(inicio+1).trim();
				}else if(cad.contains("idioma")){
					inicio=cad.indexOf(":");
					idioma=cad.substring(inicio+1).trim();
				}else if(cad.contains("tiempo")){
					inicio=cad.indexOf(":");
					tiempo=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("detectorTrolls")){
					if(cad.toLowerCase().contains("true")){
						detectorTrolls=true;
					}else{
						detectorTrolls=false;
					}
				}else if(cad.contains("detectorvpn")){
					if(cad.toLowerCase().contains("true")){
						detectorVPNs=true;
					}else{
						detectorVPNs=false;
					}
				}else if(cad.contains("canalprincipal")){
					inicio=cad.indexOf(":");
					canalPrincipal=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("grupotroll")){
					inicio=cad.indexOf(":");
					grupoTroll=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("autorank")){
					if(cad.toLowerCase().contains("true")){
						autoRank=true;
					}else{
						autoRank=false;
					}
				}else if(cad.contains("nummaximocanales")){
					inicio=cad.indexOf(":");
					numMaximoCanales=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("autobanner")){
					if(cad.toLowerCase().contains("true")){
						autobanner=true;
					}else{
						autobanner=false;
					}
				}else if(cad.contains("grupotwitch")){
					inicio=cad.indexOf(":");
					grupoTwitch=cad.substring(inicio+1).trim();
				}else if(cad.contains("nombrecanaltwitch")){
					inicio=cad.indexOf(":");
					nombreCanalTwitch=cad.substring(inicio+1).trim();
				}else if(cad.contains("canaltwitch")){
					inicio=cad.indexOf(":");
					canalTwitch=Integer.parseInt(cad.substring(inicio+1).trim());
				}else if(cad.contains("tipobd")){
					inicio=cad.indexOf(":");
		            String tip=cad.substring(inicio+1).trim();
		            if(tip.equalsIgnoreCase("mysql")) {
		            	tipoBD="mysql";
		            }else {
		            	tipoBD="sqlite";
		            }
				}else if(cad.contains("ipbd")) {
					inicio=cad.indexOf(":");
		            ipBD=cad.substring(inicio+1).trim();
				}else if(cad.contains("usuariobd")) {
					inicio=cad.indexOf(":");
					usuarioBD=cad.substring(inicio+1).trim();
				}else if(cad.contains("passbd")) {
					inicio=cad.indexOf(":");
					passBD=cad.substring(inicio+1).trim();
				}else if(cad.contains("nombrebd")) {
					inicio=cad.indexOf(":");
					nombreBD=cad.substring(inicio+1).trim();
				}
			}catch(Exception e){
				System.out.println("Error en el archivo config.txt en la linea "+linea);
			}
			linea++;
		}
		br.close();
	}
	
	private void aniadirCanales() throws IOException{
		/*
		 * Formato de los canales
		 * 
		 * 5#CSGO#Esta es la descripcion del csgo#true#5
		 * IDEspaciador#MinCanales#Nombre Canal#Descripcion#limite#Int limite usuarios#int min entrar#int min poder para hablar
		 * 
		 */
		String cad = null;
		File f=new File(fCanales);
		
		if(!(f.canRead())){
			f.createNewFile();
			System.out.println("El archivo de configuración no existia se ha creado uno");
		}
		
		FileReader pr=new FileReader(f);
		BufferedReader br=new BufferedReader(pr);
		StringTokenizer token=null;
		int linea=1;
		
		while((cad=br.readLine())!=null){
			try{
				int idEspaciador=0;
				int numCanalesMinimos=2;
				String nombreCanales="";
				String descripcion="En este canal esta permitido el bloqueo del mismo, habla por privado a [URL=client://35/OmRFT7kp09SHczLBFtpQYjNfnGA=~TS3DinamicBot]TS3DinamicBot[/URL] y pon !lock lacontraseñaquequieras para que ponga contraseña al canal";
				boolean limitar=false;
				@SuppressWarnings("unused")
				int limiteUsuarios=0;
				int minimoParaEntrar=0;
				int minimoParaHablar=0;
				token=new StringTokenizer(cad,"#");
				try{
					while(token.hasMoreTokens()) {
						linea++;
						idEspaciador=Integer.parseInt(token.nextToken().trim());
						numCanalesMinimos=Integer.parseInt(token.nextToken().trim());
						nombreCanales=token.nextToken().trim();
						descripcion=token.nextToken().trim();
						if(token.nextToken().contains("true")){
							limitar=true;
						}else{
							limitar=false;
						}
						limiteUsuarios=Integer.parseInt(token.nextToken().trim());
						minimoParaEntrar=Integer.parseInt(token.nextToken().trim());
						minimoParaHablar=Integer.parseInt(token.nextToken().trim());
					}
					
					listadoCanales.add(new Canales(idEspaciador,numCanalesMinimos,nombreCanales,descripcion,limitar,limiteUsuarios,minimoParaEntrar,minimoParaHablar));
					
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				linea++;
			}catch(Exception e){
				System.out.println("Error en el archivo canales.txt en la linea "+linea);
			}
			
		}
		br.close();
	}
	
	private void sacarCanalesEspeciales() throws IOException{
		
		/*
		 * Formato de los canales
		 * 
		 * Busqueda de grupo
		 * 1#445#Buscando grupo#2#5
		 * 
		 * Permisos especiales
		 * 2#idEspaciador#Nombre#ServerGroup
		 * 
		 * Respuesta de preguntas
		 * 3#palabras,necesarias#Respuesta
		 * 
		 * Varios busqueda de grupo
		 * 4#IDcanal#Comando,IdEspaciador,NumPersonasEnBusca,Nombre#Comando,IdEspaciador,NumPersonasEnBusca,Nombre#....
		 * 
		 * Numero de usuarios online
		 * 5#IDCanal#Nombre Canal
		 * 
		 * Auto Cambiar Banners
		 * 
		 * 6#URL#Imagen
		 * 
		 */
		String cad = null;
		File f=new File(fEspeciales);
		
		if(!(f.canRead())){
			f.createNewFile();
			System.out.println("El archivo de configuración no existia se ha creado uno");
		}
		
		FileReader pr=new FileReader(f);
		BufferedReader br=new BufferedReader(pr);
		StringTokenizer token=null;
		int linea=1;
		
		while((cad=br.readLine())!=null){
			
			try{
				int idEspaciador=0;
				String nombreCanales="";
				int limiteUsuarios=0;
				int idCanal=0;
				int tipo=0;
				int serverGroup=0;
				
				token=new StringTokenizer(cad,"#");
				try{
					while(token.hasMoreTokens()) {
						tipo=Integer.parseInt(token.nextToken().trim());
						
						if(tipo==1){
							idCanal=Integer.parseInt(token.nextToken().trim());
							nombreCanales=token.nextToken().trim();
							idEspaciador=Integer.parseInt(token.nextToken().trim());
							limiteUsuarios=Integer.parseInt(token.nextToken().trim());
							
							if(pro){
								listadoBusqueda.add(new CanalBusqueda(idCanal,nombreCanales,idEspaciador,limiteUsuarios));
							}else{
								if(listadoBusqueda.size()<1){
									listadoBusqueda.add(new CanalBusqueda(idCanal,nombreCanales,idEspaciador,limiteUsuarios));
								}
							}
						}else if(tipo==2){
							idEspaciador=Integer.parseInt(token.nextToken().trim());
							nombreCanales=token.nextToken().trim();
							serverGroup=Integer.parseInt(token.nextToken().trim());
							
							if(pro){
								listadoCanalesEspeciales.add(new CanalEspecial(idEspaciador,nombreCanales,serverGroup));
							}else{
								if(listadoCanalesEspeciales.size()<1){
									listadoCanalesEspeciales.add(new CanalEspecial(idEspaciador,nombreCanales,serverGroup));
								}
							}

						}else if(tipo==3){
							String palabras=token.nextToken();
							String respuesta=token.nextToken().trim();
							ArrayList<String> listadoPalabras=new ArrayList<String>();
							
							StringTokenizer tokenPalabras=new StringTokenizer(palabras,",");
							
							while(tokenPalabras.hasMoreTokens()){
								listadoPalabras.add(tokenPalabras.nextToken());
							}
							
							if(pro){
								listadoRespuestas.add(new PreguntaRespuestas(respuesta,listadoPalabras));
							}else{
								if(listadoRespuestas.size()<1){
									listadoRespuestas.add(new PreguntaRespuestas(respuesta,listadoPalabras));
								}
							}
						}else if(tipo==4){
							ArrayList<CanalBusqueda> canalesBusqueda=new ArrayList<CanalBusqueda>();
							
							int canal=Integer.parseInt(token.nextToken());
							String canales="";
							StringTokenizer tokenCanales;
							String descripcion="";
							while(token.hasMoreTokens()){
								canales=token.nextToken();
								tokenCanales=new StringTokenizer(canales,",");
								String juego="";
								int idEspaciadores=0;
								int numPersonas=0;
								juego=tokenCanales.nextToken();
								idEspaciadores=Integer.parseInt(tokenCanales.nextToken());
								numPersonas=Integer.parseInt(tokenCanales.nextToken());
								descripcion=tokenCanales.nextToken();
								
								canalesBusqueda.add(new CanalBusqueda(canal,juego,idEspaciadores,numPersonas,descripcion));	
							}
							
							if(pro){
								listadoVariosBusqueda.add(new VariosBusqueda(canal,canalesBusqueda));
							}else{
								if(listadoVariosBusqueda.size()<1){
									listadoVariosBusqueda.add(new VariosBusqueda(canal,canalesBusqueda));
								}
							}
							
							
						}else if(tipo==5){
							int canal=Integer.parseInt(token.nextToken());
							String nombreCanal=token.nextToken().trim();
							
							listatoCanalNumUsu.add(new TotalUsuarios(nombreCanal,canal));
						}else if(tipo==6){
							String url=token.nextToken().trim();
							String imagen=token.nextToken().trim();
							
							banners.add(new Banner(url,imagen));
						}
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				linea++;
			}catch(Exception e){
				System.out.println("Error en el archivo especiales.txt en la linea "+linea);
			}
		}
		br.close();
	}
	
	private void aniadirRangos() throws IOException{
			String cad = null;
			File f=new File(fAutoRank);
			
			if(!(f.canRead())){
				f.createNewFile();
				System.out.println("El archivo "+fAutoRank+" no existia se ha creado uno");
			}
			
			FileReader pr=new FileReader(f);
			BufferedReader br=new BufferedReader(pr);
			StringTokenizer token=null;
			int linea=1;
			
			while((cad=br.readLine())!=null){
				try{
					int puntos=0;
					int rango=0;
					token=new StringTokenizer(cad,"#");
					try{
						while(token.hasMoreTokens()) {
							linea++;
							puntos=Integer.parseInt(token.nextToken().trim());
							rango=Integer.parseInt(token.nextToken().trim());
						}
						
						listadoRangos.add(new Rango(puntos,rango));
						
					}catch(Exception e){
						System.out.println(e.getMessage());
					}
					linea++;
				}catch(Exception e){
					System.out.println("Error en el archivo "+fAutoRank+" en la linea "+linea);
				}
				
			}
			br.close();
	}

	public int getCanal() {
		return canal;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getContra() {
		return contra;
	}

	public String getDireccion() {
		return direccion;
	}

	public int getPuerto() {
		return puerto;
	}

	public ArrayList<Canales> getListadoCanales() {
		return listadoCanales;
	}
	
	public ArrayList<CanalBusqueda> getListadoBusqueda() {
		return listadoBusqueda;
	}
	
	public ArrayList<CanalEspecial> getListadoCanalesPermisos(){
		return listadoCanalesEspeciales;
	}

	public ArrayList<PreguntaRespuestas> getListadoRespuestas() {
		return listadoRespuestas;
	}
	
	public ArrayList<VariosBusqueda> getListadoVariosBusqueda() {
		return listadoVariosBusqueda;
	}

	public String getNombreBot() {
		return nombreBot;
	}

	public String getAdminBot() {
		return adminBot;
	}

	public String getIdioma() {
		return idioma;
	}

	public int getCanalAtendido() {
		return canalAtendido;
	}

	public ArrayList<TotalUsuarios> getTotalUsu() {
		return listatoCanalNumUsu;
	}

	public int getTiempo() {
		return tiempo;
	}

	public boolean isPro() {
		return pro;
	}

	public ArrayList<TotalUsuarios> getListatoCanalNumUsu() {
		return listatoCanalNumUsu;
	}

	public boolean isDetectorTrolls() {
		return detectorTrolls;
	}

	public boolean isDetectorVPNs() {
		return detectorVPNs;
	}

	public int getCanalPrincipal() {
		return canalPrincipal;
	}

	public int getGrupoTroll() {
		return grupoTroll;
	}

	public int getNumMaximoCanales() {
		return numMaximoCanales;
	}

	public ArrayList<Rango> getListadoRangos() {
		return listadoRangos;
	}

	public boolean isAutoRank() {
		return autoRank;
	}

	public boolean isDetectorDDos() {
		return detectorDDos;
	}

	public int getCanalTwitch() {
		return canalTwitch;
	}

	public String getNombreCanalTwitch() {
		return nombreCanalTwitch;
	}

	public String getGrupoTwitch() {
		return grupoTwitch;
	}

	public ArrayList<Banner> getBanners() {
		return banners;
	}

	public void setBanners(ArrayList<Banner> banners) {
		this.banners = banners;
	}

	public boolean isAutobanner() {
		return autobanner;
	}

	public String getfConfig() {
		return fConfig;
	}

	public String getfCanales() {
		return fCanales;
	}

	public String getfEspeciales() {
		return fEspeciales;
	}

	public String getfAutoRank() {
		return fAutoRank;
	}

	public ArrayList<CanalEspecial> getListadoCanalesEspeciales() {
		return listadoCanalesEspeciales;
	}

	public String getTipoBD() {
		return tipoBD;
	}

	public String getIpBD() {
		return ipBD;
	}

	public String getUsuarioBD() {
		return usuarioBD;
	}

	public String getPassBD() {
		return passBD;
	}

	public String getNombreBD() {
		return nombreBD;
	}

}
