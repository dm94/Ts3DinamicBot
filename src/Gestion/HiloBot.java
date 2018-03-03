package Gestion;

import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

public class HiloBot extends Thread {
	
	private int idCanal=1;
	private String nombreBot="";
	private String usuario="";
	private String contra="";
	private String dire="";
	private int puerto=9987;
	private Inicio inicio;
	private TS3Api api;
	
	public HiloBot(int idCanal, String nombreBot, String usuario, String contra,String dire,int puerto,Inicio inicio) {
		super();
		this.idCanal = idCanal;
		this.nombreBot = nombreBot;
		this.usuario = usuario;
		this.contra = contra;
		this.dire=dire;
		this.puerto=puerto;
		this.inicio=inicio;
	}

	public void run(){
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
	     
	     TS3Query query = new TS3Query(config);
	     query.connect();
	     
	     api = query.getApi();
	     
	     listeners(api);
	     
	     api.registerEvent(TS3EventType.TEXT_PRIVATE);
	     api.registerEvent(TS3EventType.CHANNEL,idCanal);
	     
	}
	
	private void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api) {
		// Logging in, selecting the virtual server, selecting a channel
		// and setting a nickname needs to be done every time we reconnect
		api.login(usuario, contra);
		api.selectVirtualServerByPort(puerto);
		// api.moveQuery(x);
		api.setNickname(nombreBot);
		api.moveQuery(idCanal);
		
		// What events we listen to also resets
		api.registerEvent(TS3EventType.TEXT_PRIVATE);
	    api.registerEvent(TS3EventType.CHANNEL,idCanal);
	}
	
	private void listeners(TS3Api api){
		 api.addTS3Listeners(new TS3EventAdapter() {
	    	 
		    	public void onTextMessage(TextMessageEvent arg0) {
		    		inicio.mensajeRecibido(arg0);
				}
		    	@Override
				public void onClientMoved(ClientMovedEvent arg0) {	
		    		inicio.clienteMovido(arg0);
				}

				@Override
				public void onClientJoin(ClientJoinEvent arg0) {
					try{
						inicio.entradaDeCliente(arg0);
					}catch(Exception e){
						//Salta cuando el cliente ya no esta
					}
				}
				
		    });
	}
}
