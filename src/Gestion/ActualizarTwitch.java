package Gestion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.CanalTwitch;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ActualizarTwitch extends Thread {

	private int canalTwitch=0;
	private String nombreCanalTwitch="";
	private String grupoTwitch="";
	private String nombreBot="BotAutoTwitch";
	private String usuario="";
	private String contra="";
	private String dire="";
	private int puerto=9987;
	private TS3Api api;
	private String clientId="";
	private int idEspaciadorStreamers=957;
	private TS3Query query;
	
	public ActualizarTwitch(int canalTwitch,String nombreCanalTwitch,String grupoTwitch, String usuario, String contra,String dire,int puerto){
		super();
		this.canalTwitch=canalTwitch;
		this.nombreCanalTwitch = nombreCanalTwitch;
		this.grupoTwitch=grupoTwitch;
		this.usuario = usuario;
		this.contra = contra;
		this.dire=dire;
		this.puerto=puerto;
	}
	
	public void run(){
		try{
			final TS3Config config = new TS3Config(); 
			config.setQueryPort(10011);
			 config.setHost(dire);
		     config.setDebugLevel(Level.WARNING);
		     
		     config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
		     config.setConnectionHandler(new ConnectionHandler() {

					@Override
					public void onConnect(TS3Query ts3Query) {
						stuffThatNeedsToRunEveryTimeTheQueryConnects(ts3Query.getApi());
					}

					@Override
					public void onDisconnect(TS3Query ts3Query) {
						
					}
				});
		     
		     query = new TS3Query(config);
		     query.connect();
		     
		     api = query.getApi();
 
		     actualizarCanales();
		     //crearCanalTwitch();
		     //borrarCanalTwitch();
		     
		     
		     
		     query.exit();
		}catch(Exception e){
			if(query!=null){
				query.exit();
			}
		}
	}
	
	private void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api) {
		// Logging in, selecting the virtual server, selecting a channel
		// and setting a nickname needs to be done every time we reconnect
		api.login(usuario, contra);
		api.selectVirtualServerByPort(puerto);
		// api.moveQuery(x);
		api.setNickname(nombreBot);
	}

	private void actualizarCanales(){
		ArrayList<CanalTwitch> canalesTwitch=actualizarOnline(sacarTodosLosCanales());
		int canalesOnline=0;
		String descripcion="[center][B]Canales Twitch[/B]\n";
		String descripcion2="";
		HashMap<ChannelProperty, String> datosCanal=new HashMap<ChannelProperty, String>();
		
		for(int i=0;i<canalesTwitch.size();i++){
			if(canalesTwitch.get(i).isOnline()){
				canalesOnline++;
				descripcion=descripcion+("[B]"+canalesTwitch.get(i).getNick()+"[/B] - [url]https://www.twitch.tv/"+canalesTwitch.get(i).getNick()+"[/url] -[COLOR=#00aa00] Online[/COLOR]\n");
			}else{
				descripcion2=descripcion2+("[B]"+canalesTwitch.get(i).getNick()+"[/B] - [url]https://www.twitch.tv/"+canalesTwitch.get(i).getNick()+"[/url] -[COLOR=#cf0000] Offline[/COLOR]\n");
			}
		}
		descripcion=descripcion+descripcion2+"[/center]";
		
		datosCanal.put(ChannelProperty.CHANNEL_DESCRIPTION,descripcion);
		
		if(!canalExiste(nombreCanalTwitch+"- Online: "+canalesOnline)){
			datosCanal.put(ChannelProperty.CHANNEL_NAME,nombreCanalTwitch+"- Online: "+canalesOnline);
		}
		
		if(canalTwitch>0){
			api.editChannel(canalTwitch, datosCanal);
		}
	}
	
	private ArrayList<CanalTwitch> sacarTodosLosCanales(){
		ArrayList<CanalTwitch> canalesTwitch=new ArrayList<CanalTwitch>();
		
		String url="";
		
		for(int i=1;i<10;i++){
			url = "https://www.twitch.tv/team/"+grupoTwitch+"/live_member_list?page=" + i; 
			if (getStatusConnectionCode(url) == 200) {
				
		            Document document = getHtmlDocument(url);
		            Elements entradas = document.select("span[class=member_name]");
					
		            for (Element elem : entradas) {
		                String nick = elem.text();
						
		                canalesTwitch.add(new CanalTwitch(nick,false));
		            }
						
		        }
		}
		
		return canalesTwitch;
	}
	
	private ArrayList<CanalTwitch> actualizarOnline(ArrayList<CanalTwitch> canalesTwitch){
		
		for(int i=0;i<canalesTwitch.size();i++){
			canalesTwitch.get(i).setOnline(estaOnlineFalse(canalesTwitch.get(i).getNick()));
		}
		
		return canalesTwitch;
	}
	
	public int getStatusConnectionCode(String url) {
		
	    Response response = null;
		
	    try {
		response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    } catch (Exception ex) {
	    	System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
	    	if(query!=null){
				query.exit();
			}
	    }
	    return response.statusCode();
	}
	
	public Document getHtmlDocument(String url) {

	    Document doc = null;
		try {
		    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		    } catch (Exception ex) {
		    	System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		    	if(query!=null){
					query.exit();
				}
		    }
	    return doc;
	}
	
	private CanalTwitch actualizarDatos(CanalTwitch usuarioTwitch){
		String imagen="";
		String titulo="";
		String juego="";
		
		 try {
			 URL url = new URL("https://api.twitch.tv/kraken/streams/"+usuarioTwitch.getNick()+"?client_id="+clientId);
		     JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(url.openStream())).getAsJsonObject();

		     imagen=jsonObject.getAsJsonObject("stream").getAsJsonObject("preview").get("medium").getAsString();
		     
		     titulo = jsonObject.getAsJsonObject("stream").getAsJsonObject("channel").get("status").getAsString();
		     juego=jsonObject.getAsJsonObject("stream").getAsJsonObject("channel").get("game").getAsString();
		        
		     usuarioTwitch.setGame(juego);
		     usuarioTwitch.setImagen(imagen);
		     usuarioTwitch.setStatus(titulo);
		     
		 }catch (Exception e){
			 if(query!=null){
					query.exit();
				}
	     }
		
		return usuarioTwitch;
	}
	
	private synchronized boolean estaOnlineFalse(String channel){
		URL url;
		BufferedReader reader;
		boolean estaOnline=false;
		
		 try {
	            url = new URL("https://api.twitch.tv/kraken/streams/" + channel.toLowerCase()+"?client_id="+clientId); //Bukkit automatically adds the URL tags, remove them when you copy the class
	            reader = new BufferedReader(new InputStreamReader(url.openStream()));
	 
	            String cad=reader.readLine();
	            if(cad.toLowerCase().contains("viewers")){
	            	estaOnline = true;
	            } else {
	            	estaOnline = false;
	            }
	        } catch (Exception e) {
	        	if(query!=null){
					query.exit();
				}
	        }
		
		return estaOnline;
	}

	
	private void borrarCanalTwitch(){
		List<Channel> todosLosCanales=api.getChannels();
		
		for(int i=0;i<todosLosCanales.size();i++){
			if(todosLosCanales.get(i).getParentChannelId()==idEspaciadorStreamers){
				if(todosLosCanales.get(i).isSemiPermanent()){
					if(todosLosCanales.get(i).isEmpty()){
						if(!estaOnlineFalse(todosLosCanales.get(i).getTopic())){
							api.deleteChannel(todosLosCanales.get(i).getId());
						}
					}
				}
			}
		}
	}
	
	private void crearCanalTwitch(){
		//Crea un canal de twitch si no existe ya
		ArrayList<CanalTwitch> canalesTwitch=actualizarOnline(sacarTodosLosCanales());
		ArrayList<CanalTwitch> canalesOnline=new ArrayList<CanalTwitch>();
		for(int i=0;i<canalesTwitch.size();i++){
			if(canalesTwitch.get(i).isOnline()){
				canalesOnline.add(actualizarDatos(canalesTwitch.get(i)));
			}
		}
		
		for(int i=0;i<canalesOnline.size();i++){
			String nombreCanal="Ver a "+canalesOnline.get(i).getNick();
			String descripcion="[center][b]\n";
			if(!canalExiste(nombreCanal)){
				
				descripcion=descripcion+"[url=https://www.twitch.tv/"+canalesOnline.get(i).getNick()+"]"+"Teneis a "+canalesOnline.get(i).getNick()+" online \n";
				if(canalesOnline.get(i).getStatus().length()>0){
					descripcion=descripcion+"\n "+canalesOnline.get(i).getStatus()+" \n";
				}
				if(canalesOnline.get(i).getGame().length()>0){
					descripcion=descripcion+"Juego: "+canalesOnline.get(i).getGame()+" \n";
				}
				if(canalesOnline.get(i).getImagen().length()>0){
					descripcion=descripcion+"\n [img]"+canalesOnline.get(i).getImagen()+"[/img] \n";
				}
				
				descripcion=descripcion+"[/url][/b][/center]";
				
				HashMap<ChannelProperty, String> propiedadesCanales=new HashMap<ChannelProperty, String>();
				propiedadesCanales.put(ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT, "1");
				propiedadesCanales.put(ChannelProperty.CHANNEL_DESCRIPTION, descripcion);
				propiedadesCanales.put(ChannelProperty.CPID, String.valueOf(idEspaciadorStreamers));
				propiedadesCanales.put(ChannelProperty.CHANNEL_TOPIC, canalesOnline.get(i).getNick());
				
				api.createChannel(nombreCanal,propiedadesCanales);
				
				int idCanal=api.getChannelByNameExact(nombreCanal,false).getId();
				int idIcono=(int) api.getChannelInfo(idEspaciadorStreamers).getIconId();
				
				api.addChannelPermission(idCanal, "i_icon_id", idIcono);
			}
		}
		
	}
	
	private boolean canalExiste(String canal){
		List<Channel> todosLosCanales=api.getChannels();
		
		boolean existe=false;
		
		for(int i=0;i<todosLosCanales.size();i++){
			if(todosLosCanales.get(i).getName().equalsIgnoreCase(canal)){
				existe=true;
			}
		}
		
		return existe;
	}
}
