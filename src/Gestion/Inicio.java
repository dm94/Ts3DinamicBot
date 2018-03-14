package Gestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import lenguajes.Idioma;
import Data.Banner;
import Data.CanalBusqueda;
import Data.CanalEspecial;
import Data.Canales;
import Data.PreguntaRespuestas;
import Data.Rango;
import Data.TotalUsuarios;
import Data.UsuTwitter;
import Data.VariosBusqueda;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.DatabaseClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

/**
 * 
 * @author DaniDani
 * @version 3.4
 *
 * Lista de cambios:
 * 2.1 Cambiado el sistema para que lea los datos desde un archivo | Funciona correctamente
 * 2.2 Ahora el limite de usuarios se define en el archivo
 * 2.3 Nuevas mejoras para que sea mas personalizable el bot
 * 2.4 Cambiado el bot para que ahora tambien los canales especiales funcionen por archivos y sean dinamicos
 * 2.5 Nueva opcion para que responda automaticamente en los canales que se le diga y con la respuesta que se le diga
 * 2.6 Ahora tiene un sistema que detecta nuevas actualizaciones
 * 2.7 Nuevo sistema de busqueda de varios grupos en un solo canal
 * 2.8 Mejorado el sistema de busqueda de un solo grupo
 * 2.9 Nuevos comandos para que un admin pueda manejar el bot por chat
 * 3.0 Bot diferenciado en dos versiones y traducido a varios idiomas
 * 3.1 Nueva opcion para poner el numero de usuarios, nueva opcion de configuracion para poner el tiempo de actualizacion y nuevo comando !uniqueid
 * 3.2 Detector de Trolls y VPN aniadidos, nuevo comando !renovar
 * 3.3 Aniadido el AutoRank
 * 3.4 Arreglados multiples fallos, Añadido comando !removetroll, actualizado la api, aniadido sistema de reconexion, nuevo sistema twitch
 */

public class Inicio {
	private int numVeces=0;
	private List<Channel> todosLosCanales=new ArrayList<>();
	private ArrayList<Integer> canalesConContra=new ArrayList<>(); //ID de canales con contraseña
	private ArrayList<Canales> listadoCanales=new ArrayList<Canales>(); //Canales a crear
	private List<DatabaseClient> listaClientes=new ArrayList<DatabaseClient>(); 
	private Timer timer = new Timer(); //Creamos el timer para que repita la misma tarea
	private TS3Api api=null;
	private Utilidades util=new Utilidades();
	private ManejoDeArchivos mda=new ManejoDeArchivos();
	private ArrayList<CanalBusqueda> listadoBusqueda=new ArrayList<CanalBusqueda>(); //Canales de busqueda de equipo
	private ArrayList<CanalEspecial> listadoCanalesEspeciales=new ArrayList<CanalEspecial>(); //Canales con permisos especiales
	private ArrayList<Integer> canalesRegistradosConEventos=new ArrayList<Integer>();//Canales en los que se registran eventos
	private ArrayList<PreguntaRespuestas> listadoRespuestas=new ArrayList<PreguntaRespuestas>();
	private ArrayList<VariosBusqueda> listadoVariosBusqueda=new ArrayList<VariosBusqueda>(); //Canal con todas las busquedas de grupo
	private ArrayList<TotalUsuarios> listadoCanalNumUsu=new ArrayList<TotalUsuarios>(); //Canales donde poner el numero de usuarios
	private ArrayList<Rango> listadoRangos=new ArrayList<Rango>(); //Listado de los rangos
	private ArrayList<Banner> banners=new ArrayList<Banner>();
	private TS3Query query;
	private Idioma idioma;
	private Conexion gbdExterna;
	private GestionBD gbdInterna;
	private boolean bdExterna=false;
	private ArrayList<sacarAccesoTwitter> usuariosVerificandose=new ArrayList<sacarAccesoTwitter>();
	private int bannerActual=0;
	
	public static void main(String[] args) {
		Inicio ini=new Inicio();
		ini.encender();
		
	}
	
	public void encender(){
		if(mda.getTipoBD().equalsIgnoreCase("mysql")) {
			bdExterna=true;
		}else {
			bdExterna=false;
		}
		
		if(bdExterna) {
			gbdExterna=new Conexion();
		}else {
			gbdInterna=new GestionBD();
		}
		
		//--------------------------------------------------------------------------------------
	     
		 banners=mda.getBanners();
	     listadoCanales=mda.getListadoCanales();
	     //listadoCanales.add(new Canales(5216,5,"Grupo","Solo pueden entrar personas que hayan verificado twitter o la web. En este canal esta permitido el bloqueo del mismo, habla por privado a [URL=client://35/OmRFT7kp09SHczLBFtpQYjNfnGA=~TS3DinamicBot]TS3DinamicBot[/URL] y pon !lock lacontraseñaquequieras para que ponga contrasenia al canal",false,20,10,0));
	     listadoBusqueda=mda.getListadoBusqueda();
	     listadoCanalesEspeciales=mda.getListadoCanalesPermisos();
	     listadoRespuestas=mda.getListadoRespuestas();
	     listadoVariosBusqueda=mda.getListadoVariosBusqueda();
	     listadoCanalNumUsu=mda.getTotalUsu();
	     listadoRangos=mda.getListadoRangos();
	     idioma=new Idioma(mda.getIdioma());
	     
	     //--------------------------------------------------------------------------------------
	     
		
		final TS3Config config = new TS3Config(); 
		 config.setQueryPort(10011);
		 config.setHost(mda.getDireccion());
	     config.setDebugLevel(Level.WARNING);
	     
	     // Use default exponential backoff reconnect strategy
		 config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
		
		
		// Make stuff run every time the query (re)connects
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
	     
	     api=query.getApi();
	     
	     listeners(api);
	     
	     query.getAsyncApi();
	     
	     if(1<mda.getTiempo()){
	    	 timer.scheduleAtFixedRate(timerTask, 0, ((1000*60)*mda.getTiempo())); //1000 es un segundo, por defecto 600000
	     }else{
	    	 timer.scheduleAtFixedRate(timerTask, 0, (1000*60)); //1000 es un segundo, por defecto 600000
	     }
	     
	     aniadirCanalesParaEventos();
	     
	     //Hacemos esto para crear un hilo por canal con evento que necesitemos
	     if(mda.isPro()){
	    	 for(int i=0;i<canalesRegistradosConEventos.size();i++){
		    	 new HiloBot(canalesRegistradosConEventos.get(i),mda.getNombreBot()+i,mda.getUsuario(),mda.getContra(),mda.getDireccion(),mda.getPuerto(),this).start();
		     }
	     }else{
	    	 if(canalesRegistradosConEventos.size()>0){
	    		 new HiloBot(canalesRegistradosConEventos.get(0),mda.getNombreBot()+0,mda.getUsuario(),mda.getContra(),mda.getDireccion(),mda.getPuerto(),this).start();
	    	 }
	     }
	     
	     if(mda.isDetectorTrolls() && mda.isPro()){
	    	 sacarUsuariosTrolls(api);
	    	 detectarTrolls();
	     }
	     
	     System.out.println(idioma.getProperty("botlisto"));
	      
	}
	
	
	private void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api) {
		// Logging in, selecting the virtual server, selecting a channel
		// and setting a nickname needs to be done every time we reconnect
		api.login(mda.getUsuario(), mda.getContra());
		api.selectVirtualServerByPort(mda.getPuerto());
		// api.moveQuery(x);
		api.setNickname(mda.getNombreBot());

		// What events we listen to also resets
		api.registerEvent(TS3EventType.TEXT_PRIVATE);
		
		 if(mda.getCanalPrincipal()>0){
	    	 api.registerEvent(TS3EventType.CHANNEL,mda.getCanalPrincipal()); 
	     }
	     if(mda.getCanalAtendido()>0){
    		 new HiloBot(mda.getCanalAtendido(),mda.getNombreBot()+(canalesRegistradosConEventos.size()+1),mda.getUsuario(),mda.getContra(),mda.getDireccion(),mda.getPuerto(),this).start();
	     }
		
	     api.moveQuery(mda.getCanal());

	}
	
	private void listeners(TS3Api api){
		 api.addTS3Listeners(new TS3EventAdapter() {
	    	 
		    	public void onTextMessage(TextMessageEvent e) {
		    		mensajeRecibido(e);
				}
		    	@Override
				public void onClientMoved(ClientMovedEvent arg0) {	
		    		clienteMovido(arg0);
				}

				@Override
				public void onClientJoin(ClientJoinEvent arg0) {
					entradaDeCliente(arg0);
				}
				
		    });
	}
	
	public synchronized void entradaDeCliente(ClientJoinEvent arg0){
		try{
			if((mda.isDetectorTrolls() || mda.isDetectorVPNs()) && mda.isPro()){
				String uniqueid="";
	
				if(arg0.getClientType()==0){
					if(arg0.getClientId()>0){
						uniqueid=sacarUsuarioPorId(arg0.getClientId());
						String ip=api.getClientInfo(arg0.getClientId()).getIp();
						int dbId=arg0.getClientDatabaseId();
					
						if(bdExterna) {
							if(gbdExterna.estaIP(ip) && mda.isDetectorTrolls()){
								String idprini=gbdExterna.sacarPrimeraID(ip);
								
								if(!(gbdExterna.estaTroll(uniqueid))){
									aniadirDescripcion(dbId,"Troll - DetectorTrolls - 1 ID "+idprini);
									aniadirTroll(dbId);
									gbdExterna.aniadirTroll(ip, uniqueid);
									api.sendChannelMessage("Troll | Usuario: "+String.format("[URL=client://%s/%s~%s]%s[/URL]",String.valueOf(arg0.getClientId()),uniqueid,arg0.getClientNickname(),arg0.getClientNickname())+" | ID Unica: "+uniqueid);
									mandarMensaje(mda.getAdminBot(),"Troll | Usuario: "+String.format("[URL=client://%s/%s~%s]%s[/URL]",String.valueOf(arg0.getClientId()),uniqueid,arg0.getClientNickname(),arg0.getClientNickname())+" | ID Unica: "+uniqueid);
								}
							}
						}else {
							if(gbdInterna.estaIP(ip) && mda.isDetectorTrolls()){
								String idprini=gbdInterna.sacarPrimeraID(ip);
								
								if(!(gbdInterna.estaTroll(uniqueid))){
									aniadirDescripcion(dbId,"Troll - DetectorTrolls - 1 ID "+idprini);
									aniadirTroll(dbId);
									gbdInterna.aniadirTroll(ip, uniqueid);
									api.sendChannelMessage("Troll | Usuario: "+String.format("[URL=client://%s/%s~%s]%s[/URL]",String.valueOf(arg0.getClientId()),uniqueid,arg0.getClientNickname(),arg0.getClientNickname())+" | ID Unica: "+uniqueid);
									mandarMensaje(mda.getAdminBot(),"Troll | Usuario: "+String.format("[URL=client://%s/%s~%s]%s[/URL]",String.valueOf(arg0.getClientId()),uniqueid,arg0.getClientNickname(),arg0.getClientNickname())+" | ID Unica: "+uniqueid);
								}
							}
						}
						
					}
				}
			}
		}catch(Exception npe){
			//Salta cuando el que ha entrado ya no esta
		}
	}
	
	public synchronized void clienteMovido(ClientMovedEvent arg0){
		try{
			int idCanal=arg0.getTargetChannelId();
			int idUsuario=arg0.getClientId();
			
			if(idCanal==mda.getCanalAtendido()){
				api.sendPrivateMessage(arg0.getClientId(), idioma.getProperty("botpregunta"));
			}
			
			for(int i=0;i<listadoBusqueda.size();i++){
				
				if(listadoBusqueda.get(i).getCanal()==idCanal){//Comprobar id del canal si es la de busca grupo
					
					ArrayList<String> usuariosBusqueda=listadoBusqueda.get(i).getUsuariosBusqueda();
					HashMap<ChannelProperty, String> datosCanal=new HashMap<ChannelProperty, String>(); //Creamos el ChannelProperty
					
					usuariosBusqueda=comprobarSiEstaGrupo(usuariosBusqueda);
					
					if(!usuarioEsta(sacarUsuarioPorId(idUsuario),usuariosBusqueda)){ //Comprobar si ese usuario esta
						usuariosBusqueda.add(sacarUsuarioPorId(idUsuario));
						
						api.sendPrivateMessage(arg0.getClientId(), idioma.getProperty("apuntadobusquedagrupo")+" | "+idioma.getProperty("faltan")+" "+(listadoBusqueda.get(i).getNumeroBusqueda()-usuariosBusqueda.size()));
						api.sendPrivateMessage(arg0.getClientId(), idioma.getProperty("puedessalirbusqueda")+" !exit");
						
						if(usuariosBusqueda.size()==listadoBusqueda.get(i).getNumeroBusqueda()){
							listadoBusqueda.get(i).setUsuariosBusqueda(usuariosBusqueda);
							cambiarUBuscaGrupo(listadoBusqueda.get(i));
							usuariosBusqueda.clear();
						}
						
						datosCanal.put(ChannelProperty.CHANNEL_NAME,(String.format("%s | %s %d", listadoBusqueda.get(i).getNombre(),idioma.getProperty("faltan"),(listadoBusqueda.get(i).getNumeroBusqueda()-usuariosBusqueda.size()))));
						
						api.editChannel(listadoBusqueda.get(i).getCanal(), datosCanal);
					}
					listadoBusqueda.get(i).setUsuariosBusqueda(usuariosBusqueda);
				}
				
			}
			
			for(int i=0;i<listadoVariosBusqueda.size();i++){
				if(listadoVariosBusqueda.get(i).getIdCanal()==idCanal){
					ArrayList<CanalBusqueda> juegosBusqueda=listadoVariosBusqueda.get(i).getCanalesBusqueda();
					api.sendPrivateMessage(arg0.getClientId(), idioma.getProperty("preguntabusquedagrupo"));
					
					for(int s=0;s<juegosBusqueda.size();s++){
						api.sendPrivateMessage(arg0.getClientId(), idioma.getProperty("pon")+" [B]!"+juegosBusqueda.get(s).getNombre()+ "[/B] "+idioma.getProperty("unirtebusqueda")+" "+juegosBusqueda.get(s).getDescripcion()+". "+idioma.getProperty("senecesitan")+" "+(juegosBusqueda.get(s).getNumeroBusqueda()-juegosBusqueda.get(s).getUsuariosBuscando())+" "+idioma.getProperty("personas")+".");
					}
					
				}
			}
		}catch(Exception npe){
			//Salta cuando el que ha entrado ya no esta
		}
		
	}
	
	public synchronized void mensajeRecibido(TextMessageEvent e){
		try{
			HashMap<ChannelProperty, String> datosCanal=new HashMap<ChannelProperty, String>();
			int idCanal=0;
			if (e.getInvokerId() != api.whoAmI().getId()) {
				idCanal=api.getClientInfo(e.getInvokerId()).getChannelId();
				if(e.getTargetMode()==TextMessageTargetMode.CLIENT){
					if(e.getMessage().contains("!veri")){
						verificacionTwitter(e.getMessage(),e.getInvokerUniqueId());
					}
					if(e.getMessage().contains("!pin")){
						verificacionTwitter(e.getMessage(),e.getInvokerUniqueId());
					}
					if(e.getMessage().contains("!lock")){
						String contra=util.sacarContrasenia(e.getMessage());
						
						for(int i=0;i<listadoCanalesEspeciales.size();i++){
							if(esUsuarioConPermisos(e.getInvokerUniqueId(),listadoCanalesEspeciales.get(i).getIdServerGroup()) && esCanalConPermisos(idCanal,listadoCanalesEspeciales.get(i).getNombreCanales(),listadoCanalesEspeciales.get(i).getEspaciador()) ){
	    						datosCanal.put(ChannelProperty.CHANNEL_PASSWORD, contra);
		    					api.editChannel(idCanal, datosCanal);
			    				api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("contracambiada")+" "+contra);
			    				canalesConContra.add(api.getClientInfo(e.getInvokerId()).getChannelId());
	    					}
						}
						if(canalValido(idCanal)){
	    					if(!estaCanalParaBorrar(idCanal)){
	    						datosCanal.put(ChannelProperty.CHANNEL_PASSWORD, contra);
		    					api.editChannel(idCanal, datosCanal);
			    				api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("contracambiada")+" "+contra);
			    				canalesConContra.add(api.getClientInfo(e.getInvokerId()).getChannelId());
	    					}else{
	    						api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("errorcontrayacambiada"));
	    					}
						}else{
		    				api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("errorcanalnopermitido"));
		    			}
						
					}else if(e.getMessage().contains("!exit")){
						boolean estaEnVarios=false;
						
						for(int i=0;i<listadoVariosBusqueda.size();i++){					
							ArrayList<CanalBusqueda> juegosBusqueda=listadoVariosBusqueda.get(i).getCanalesBusqueda();
						
							for(int s=0;s<juegosBusqueda.size();s++){
								if(e.getMessage().toLowerCase().contains(juegosBusqueda.get(s).getNombre().toLowerCase())){
									ArrayList<String> usuariosBuscando=new ArrayList<String>();
									usuariosBuscando=juegosBusqueda.get(s).getUsuariosBusqueda();
										
									for(int r=0;r<usuariosBuscando.size();r++){
		    							if(usuariosBuscando.get(r).equals(e.getInvokerUniqueId())){
		    								usuariosBuscando.remove(r);
			    							api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("borradobusquedagrupo")+" "+juegosBusqueda.get(s).getNombre());
			    						}
		    						}
										
									juegosBusqueda.get(s).setUsuariosBusqueda(usuariosBuscando);
									estaEnVarios=true;
								}
							}
								
							listadoVariosBusqueda.get(i).setCanalesBusqueda(juegosBusqueda);
							actualizarDescripcionBusquedaGrupos(idCanal);
						}
						
						if(!estaEnVarios){
							for(int l=0;l<listadoBusqueda.size();l++){
	    						ArrayList<String> usuariosBusqueda=listadoBusqueda.get(l).getUsuariosBusqueda();
	    						
	    						for(int i=0;i<usuariosBusqueda.size();i++){
	    							if(usuariosBusqueda.get(i).equals(e.getInvokerUniqueId())){
	    								usuariosBusqueda.remove(i);
		    							api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("borradobusquedagrupo"));
		    							
		    							datosCanal.put(ChannelProperty.CHANNEL_NAME,(String.format("%s | %s %d", listadoBusqueda.get(l).getNombre(),idioma.getProperty("faltan"),(listadoBusqueda.get(l).getNumeroBusqueda()-listadoBusqueda.get(l).getUsuariosBuscando()))));
		    							api.editChannel(listadoBusqueda.get(l).getCanal(), datosCanal);
		    						}
	    						}
	    						
	    						listadoBusqueda.get(l).setUsuariosBusqueda(usuariosBusqueda);
	    					}
						}
			
					}else if(e.getMessage().contains("!name")){
						for(int i=0;i<listadoCanalesEspeciales.size();i++){
							if(esUsuarioConPermisos(e.getInvokerUniqueId(),listadoCanalesEspeciales.get(i).getIdServerGroup()) && esCanalConPermisos(idCanal,listadoCanalesEspeciales.get(i).getNombreCanales(),listadoCanalesEspeciales.get(i).getEspaciador()) ){
								String nombre=util.sacarNombre(e.getMessage());
	    						datosCanal.put(ChannelProperty.CHANNEL_NAME, nombre);
	    						api.editChannel(idCanal, datosCanal);
	    						api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("nombrecambiado")+" "+nombre);
	    						if(!estaCanalParaBorrar(idCanal)){
	    							canalesConContra.add(api.getClientInfo(e.getInvokerId()).getChannelId());
	    						}
							}else{
			    				api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("errornopermisos"));
			    			}
						}
					}else if(e.getMessage().equalsIgnoreCase("!recargar")){
						if(e.getInvokerUniqueId().equalsIgnoreCase(mda.getAdminBot())){
							ManejoDeArchivos mda2=new ManejoDeArchivos();
							listadoCanales=mda2.getListadoCanales();
						     listadoBusqueda=mda2.getListadoBusqueda();
						     listadoCanalesEspeciales=mda2.getListadoCanalesPermisos();
						     listadoRespuestas=mda2.getListadoRespuestas();
						     
						     mandarMensaje(mda.getAdminBot(),"BD Recargada");
						}
					}else if(e.getMessage().equalsIgnoreCase("!stop")){
						if(e.getInvokerUniqueId().equalsIgnoreCase(mda.getAdminBot())){
							query.exit();
							System.exit(1);
						}
					}else if(e.getMessage().equalsIgnoreCase("!help")){
						api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("comandohelp"));
					}else if(e.getMessage().equalsIgnoreCase("!uniqueid")){
						api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("tuunique")+" "+e.getInvokerUniqueId());
					}else if(e.getMessage().equalsIgnoreCase("!renovar")){
						if(e.getInvokerUniqueId().equalsIgnoreCase(mda.getAdminBot())){
						    sacarUsuariosTrolls(api);
						    detectarTrolls();
						    api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("renovacionterminada"));
						}
					}else if(e.getMessage().contains("!removetroll")){
						if(e.getInvokerUniqueId().equalsIgnoreCase(mda.getAdminBot())){
							api.sendPrivateMessage(e.getInvokerId(), "Borrando el Troll");
							quitarTrolls(e.getMessage());
						}
					}else if(e.getMessage().equalsIgnoreCase("!rank")){
						int puntos=0;
						if(bdExterna) {
							puntos=gbdExterna.getPuntosUsuario(e.getInvokerUniqueId());
						}else {
							puntos=gbdInterna.getPuntosUsuario(e.getInvokerUniqueId());
						}
						
						int puntosFaltantes=0;
						
						Rango rank=sacarProxRango(puntos);
						
						if(rank!=null){
							puntosFaltantes=rank.getPuntosNecesarios()-puntos;
							api.sendPrivateMessage(e.getInvokerId(), "Tendras el proximo rango cuando reunas "+puntosFaltantes+" puntos mas.");
						}
						
					}
					
					if(idCanal==mda.getCanalAtendido()){
						for(int i=0;i<listadoRespuestas.size();i++){
							if(listadoRespuestas.get(i).estanPalabras(e.getMessage())){
								api.sendPrivateMessage(e.getInvokerId(), listadoRespuestas.get(i).getRespuesta());
								api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("querias"));
							}
						}
					}
					
					for(int i=0;i<listadoVariosBusqueda.size();i++){
						int idUsuario=e.getInvokerId();
					    					
						if(listadoVariosBusqueda.get(i).getIdCanal()==idCanal){
							
							ArrayList<CanalBusqueda> juegosBusqueda=listadoVariosBusqueda.get(i).getCanalesBusqueda();
					
							for(int s=0;s<juegosBusqueda.size();s++){
								
								ArrayList<String> usuariosBuscando=new ArrayList<String>();
								usuariosBuscando=juegosBusqueda.get(s).getUsuariosBusqueda();
								
								if(e.getMessage().toLowerCase().contains("!"+juegosBusqueda.get(s).getNombre().toLowerCase())){
									
									if(!usuarioEsta(sacarUsuarioPorId(idUsuario),usuariosBuscando)){ //Comprobar si ese usuario esta
										usuariosBuscando.add(sacarUsuarioPorId(idUsuario));
										
										api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("apuntadobusquedagrupo")+" | "+idioma.getProperty("faltan")+" "+(juegosBusqueda.get(s).getNumeroBusqueda()-usuariosBuscando.size()));
										api.sendPrivateMessage(e.getInvokerId(), idioma.getProperty("puedessalirbusqueda")+" !exit "+juegosBusqueda.get(s).getNombre());
										
										if(usuariosBuscando.size()==juegosBusqueda.get(s).getNumeroBusqueda()){
											juegosBusqueda.get(s).setUsuariosBusqueda(usuariosBuscando);
											cambiarUBuscaGrupo(juegosBusqueda.get(s));
											usuariosBuscando.clear();
										}
										
									}
									juegosBusqueda.get(s).setUsuariosBusqueda(usuariosBuscando);
								}

							}
							
							listadoVariosBusqueda.get(i).setCanalesBusqueda(juegosBusqueda);
							
							actualizarDescripcionBusquedaGrupos(idCanal);
						}
					}
				}
	        }
		}catch(Exception npe){
			//Salta cuando el que ha entrado ya no esta
		}
		
	}
	
	private void actualizarDescripcionBusquedaGrupos(int idCanal){
		try{
			for(int i=0;i<listadoVariosBusqueda.size();i++){
				
				if(listadoVariosBusqueda.get(i).getIdCanal()==idCanal){
					String descripcion="[center][B]"+idioma.getProperty("listadobusqueda")+"[/B][/center]\n";
					HashMap<ChannelProperty, String> datosCanal2=new HashMap<ChannelProperty, String>(); //Creamos el ChannelProperty
					descripcion=descripcion+" "+idioma.getProperty("descripcionbusqueda")+" \n";
					ArrayList<CanalBusqueda> juegosBusqueda=listadoVariosBusqueda.get(i).getCanalesBusqueda();
			
					for(int s=0;s<juegosBusqueda.size();s++){
						descripcion=descripcion+"\n --> "+juegosBusqueda.get(s).getDescripcion()+" | [B]!"+juegosBusqueda.get(s).getNombre()+ "[/B] \n";
						
						ArrayList<String> usuariosBuscando=new ArrayList<String>();
						usuariosBuscando=juegosBusqueda.get(s).getUsuariosBusqueda();
						for(int l=0;l<usuariosBuscando.size();l++){
							descripcion=descripcion+"\n"+api.getClientByUId(usuariosBuscando.get(l)).getNickname()+" \n";
						}
					}
					
					datosCanal2.put(ChannelProperty.CHANNEL_DESCRIPTION,descripcion);
					api.editChannel(idCanal, datosCanal2);
					
				}
			}
		}catch(Exception eo){
			//Si el usuario ya no esta salta la excepcion nullpointerexception
		}
	}
	
	private void aniadirCanalesParaEventos(){
		 for(int i=0;i<listadoBusqueda.size();i++){
			 if(!estaCanalEvento(listadoBusqueda.get(i).getCanal())){
				 canalesRegistradosConEventos.add(listadoBusqueda.get(i).getCanal());
			 }
	     }
		 
		 for(int i=0;i<listadoVariosBusqueda.size();i++){
			 if(!estaCanalEvento(listadoVariosBusqueda.get(i).getIdCanal())){
				 canalesRegistradosConEventos.add(listadoVariosBusqueda.get(i).getIdCanal());
			 }
		 }
		 
	}
	
	private boolean estaCanalEvento(int canal){
		boolean esta=false;
		
		for(int i=0;i<canalesRegistradosConEventos.size();i++){
			if(canalesRegistradosConEventos.get(i)==canal){
				esta=true;
				return esta;
			}
		}
		
		return esta;
	}
	
	private boolean esUsuarioConPermisos(String uniqueid,int serverGroup){
		boolean esStreamer=false;
		
		int[] grupos=api.getClientByUId(uniqueid).getServerGroups();
		
		for(int i=0;i<grupos.length;i++){
			if(grupos[i]==serverGroup){
				esStreamer=true;
			}
		}
		
		return esStreamer;
	}
	
	public boolean estaCanalParaBorrar(int id){
		boolean esta=false;
		
		for(int i=0;i<canalesConContra.size();i++){
			if(canalesConContra.get(i)==id){
				esta=true;
			}
		}
		
		
		return esta;
	}
	
	private ArrayList<String> comprobarSiEstaGrupo(ArrayList<String> usuariosBusqueda){
		/*
		 * Comprueba si todos los miembros de la lista estan conectados y si hay alguien desconectado lo quita de la lista
		 */
		
		for(int i=0;i<usuariosBusqueda.size();i++){
			if(!estaConectado(usuariosBusqueda.get(i))){
				usuariosBusqueda.remove(i);
			}
		}
		
		return usuariosBusqueda;
	}
	
	private boolean estaConectado(String id){
		boolean estaConectado=false;
		
		List<Client> clientes=new ArrayList<Client>();
		clientes=api.getClients();
		
		for(int i=0;i<clientes.size();i++){
			if(clientes.get(i).getUniqueIdentifier().equalsIgnoreCase(id)){
				estaConectado=true;
			}
		}
		
		return estaConectado;
	}
	
	public void cambiarUBuscaGrupo(CanalBusqueda canalDeBuscar) {
		int idCanal=0;
		
		ArrayList<String> usuariosBusqueda=new ArrayList<String>();
		usuariosBusqueda=canalDeBuscar.getUsuariosBusqueda();
		
		idCanal=buscarCanalVacio(canalDeBuscar.getEspaciador());
		List<Client> clientes=new ArrayList<Client>();
		clientes=api.getClients();
		
		for(int i=0;i<usuariosBusqueda.size();i++){
			for(int j=0;j<clientes.size();j++){
				if(clientes.get(j).getUniqueIdentifier().equalsIgnoreCase(usuariosBusqueda.get(i))){
					api.moveClient(clientes.get(j).getId(), idCanal,"");
					api.pokeClient(clientes.get(j).getId(), idioma.getProperty("busquedaterminada"));
				}
			}
			
		}
		
		sacarDeOtrasListas(usuariosBusqueda);
		
		usuariosBusqueda.clear();
		
	}
	
	private void sacarDeOtrasListas(ArrayList<String> usuariosBusqueda){ //Para que al moverlos de canal los saque de las demas listas
		for(int i=0;i<listadoVariosBusqueda.size();i++){ //Recorremos el listado de busqueda de grupos
			ArrayList<CanalBusqueda> canalesBusqueda=listadoVariosBusqueda.get(i).getCanalesBusqueda(); //Sacamos los grupos de busqueda
			for(int f=0;f<canalesBusqueda.size();f++){ //Recorremos cada busqueda
				ArrayList<String> usuariosBuscando=canalesBusqueda.get(f).getUsuariosBusqueda(); //Sacamos los usuarios de cada busqueda
				for(int g=0;g<usuariosBuscando.size();g++){ //Recorremos los usuarios de la busqueda
					String usu="";
					usu=usuariosBuscando.get(g);
					
					for(int l=0;l<usuariosBusqueda.size();l++){ //Recorremos los usuarios que ya han encontrado partida
						if(usuariosBusqueda.get(l).equals(usu)){ //Si ambos usuarios coinciden
							usuariosBuscando.remove(g); //Quitamos al usuario que coincida
						}
					}
				}
				canalesBusqueda.get(f).setUsuariosBusqueda(usuariosBuscando);
			}
			listadoVariosBusqueda.get(i).setCanalesBusqueda(canalesBusqueda);
		}
	}
	
	private boolean usuarioEsta(String id,ArrayList<String> usuariosBusqueda){
		boolean esValido=false;
		
		for(int i=0;i<usuariosBusqueda.size();i++){
			if(usuariosBusqueda.get(i).equalsIgnoreCase(id)){
				esValido=true;
			}
		}
		
		return esValido;
	}

	private void sacarTodosLosCanales(){
		todosLosCanales.clear();
		todosLosCanales=api.getChannels();

		filtrarCanalesPorJuego();
		
	}
	
	private Integer buscarCanalVacio(int espaciador){
		int idCanal=0;
				
		for(int j=0;j<todosLosCanales.size();j++){
			if(todosLosCanales.get(j).getTotalClients()<=0){
				if(todosLosCanales.get(j).getParentChannelId()==espaciador){
					idCanal=todosLosCanales.get(j).getId();
				}
			}
		}
		
		return idCanal;
	}
	
	
	private void filtrarCanalesPorJuego(){
		ArrayList<Channel> canales=new ArrayList<>();
		int espaciador=0;
		String nombre="";
		
		for(int i=0;i<listadoCanales.size();i++){
			espaciador=listadoCanales.get(i).getEspaciador();
			nombre=listadoCanales.get(i).getNombreCanales();
			
			canales.clear();
			
			for(int j=0;j<todosLosCanales.size();j++){
				if(todosLosCanales.get(j).getParentChannelId()==espaciador){
					if(todosLosCanales.get(j).getName().contains(nombre)){
						Channel canal=todosLosCanales.get(j);
						canales.add(canal);
					}
				}
			}
			
			listadoCanales.get(i).setCanales(canales);
		}
	}
	
	private ArrayList<Channel> filtrarCanales(Canales claseCanal){
		ArrayList<Channel> canales=new ArrayList<>();
		int espaciador=0;
		String nombre="";
		
		espaciador=claseCanal.getEspaciador();
		nombre=claseCanal.getNombreCanales();
			
		canales.clear();
			
		for(int j=0;j<todosLosCanales.size();j++){
			if(todosLosCanales.get(j).getParentChannelId()==espaciador){
				if(todosLosCanales.get(j).getName().contains(nombre)){
					canales.add(todosLosCanales.get(j));
				}
			}
		}
			
		
		return canales;
	}
	
	
	private void borrarCanales(){
		List<Channel> canalesParaBorrar=new ArrayList<>();
		List<Channel> canalesParaBorrarConContra=new ArrayList<>();
		List<Channel> canalesAMirar=new ArrayList<>();
		int numCanalesTotal=0;
		
		for(int i=0;i<listadoCanales.size();i++){
			canalesParaBorrarConContra.clear();
			canalesParaBorrar.clear();
			canalesAMirar.clear();
			
			numCanalesTotal=0;
			canalesAMirar=filtrarCanales(listadoCanales.get(i));
			
			for(int j=0;j<canalesAMirar.size();j++){
				if(canalesAMirar.get(j).isSemiPermanent()){
					numCanalesTotal++;
					if(canalesAMirar.get(j).getTotalClients()==0){
						if(canalesAMirar.get(j).hasPassword()){
							canalesParaBorrarConContra.add(canalesAMirar.get(j));
						}else{
							canalesParaBorrar.add(canalesAMirar.get(j));
						}
					}
				}
			}
			
			if(canalesParaBorrar.size()>2 && numCanalesTotal>listadoCanales.get(i).getNumCanalesMinimos()){
				for(int j=2;j<canalesParaBorrar.size();j++){
					if(canalesParaBorrar.get(j).getTotalClients()<=0){
						api.deleteChannel(canalesParaBorrar.get(j).getId());
					}
				}
			}
			
			for(int j=0;j<canalesParaBorrarConContra.size();j++){
				if(canalesParaBorrarConContra.get(j).getTotalClients()<=0){
					api.deleteChannel(canalesParaBorrarConContra.get(j).getId());
				}
			}
			
		}
		
		sacarTodosLosCanales();
	}
	
	
	private void crearCanales(){
		int numCanalesVacios=0;
		int numCanalesTotal=0;
		int numCanal=1;
		String nombreCanal="";
		HashMap<ChannelProperty, String> propiedadesCanales;
		ArrayList<Channel> canales=new ArrayList<Channel>();
		
		for(int j=0;j<listadoCanales.size();j++){
			int espaciador=0;
			int minimoParaEntrar=0;
			canales.clear();
			numCanalesVacios=0;
			numCanalesTotal=0;
			numCanal=1;
			espaciador=listadoCanales.get(j).getEspaciador();
			
			nombreCanal=listadoCanales.get(j).getNombreCanales();
			propiedadesCanales=listadoCanales.get(j).getDatosCanal();
			minimoParaEntrar=listadoCanales.get(j).getMinimoParaEntrar();
			canales=filtrarCanales(listadoCanales.get(j));
			numCanalesTotal=canales.size();
			
			for(int i=0;i<canales.size();i++){
				if(canales.get(i).isSemiPermanent()){
					if(canales.get(i).getTotalClients()==0){
						numCanalesVacios++;
					}
					
				}
			}
			
			//Para que cree el primer grupo de canales
			
			if(numCanalesTotal<(listadoCanales.get(j).getNumCanalesMinimos())){
				String nombre="";
				
				do{
					nombre=nombreCanal+" #"+String.valueOf(numCanal++);
				}while(canalExiste(nombre));
				
				api.createChannel(nombre,propiedadesCanales);
				
				int idCanal=0;
				int idIcono=0;
				
				for(int i=0;i<todosLosCanales.size();i++){
					if(todosLosCanales.get(i).getId()==espaciador){
						idIcono=(int) todosLosCanales.get(i).getIconId();
					}
				}
				
				idCanal=api.getChannelByNameExact(nombre,false).getId();
				
				if(minimoParaEntrar>0){
					api.addChannelPermission(idCanal, "i_channel_needed_join_power", minimoParaEntrar);
				}
				api.addChannelPermission(idCanal, "i_icon_id", idIcono);
			}
			
			//De esta forma si no hay canales vacios crea nuevos canales. Si sobre pasa el numero maximo no crea más canales
			
			if(numCanalesVacios==0 && numCanalesTotal<mda.getNumMaximoCanales()){
				String nombre="";
				do{
					nombre=nombreCanal+" #"+String.valueOf(numCanal++);
				}while(canalExiste(nombre));
				
				api.createChannel(nombre,propiedadesCanales);
				
				int idCanal=0;
				int idIcono=0;
				
				idIcono=(int) api.getChannelByNameExact(nombreCanal+" #1",false).getIconId();
				idCanal=api.getChannelByNameExact(nombre,false).getId();
				
				if(minimoParaEntrar>0){
					api.addChannelPermission(idCanal, "i_channel_needed_join_power", minimoParaEntrar);
				}
				api.addChannelPermission(idCanal, "i_icon_id", idIcono);
			}
		}
	}
	
	private boolean canalExiste(String canal){
		sacarTodosLosCanales();
		
		boolean existe=false;
		
		for(int i=0;i<todosLosCanales.size();i++){
			if(todosLosCanales.get(i).getName().equalsIgnoreCase(canal)){
				existe=true;
			}
		}
		
		return existe;
	}
	
	private void quitarContraCanales(){
		try{
			String nombreCanal="";
			
			for(int i=0;i<canalesConContra.size();i++){
				nombreCanal=sacarNombreCanal(canalesConContra.get(i));
				if(nombreCanal.length()>0){
					if(api.getChannelByNameExact(api.getChannelInfo(canalesConContra.get(i)).getName(),false).getTotalClients()<=0){	
						if(api.getChannelInfo(canalesConContra.get(i)).isSemiPermanent()){
							api.deleteChannel(canalesConContra.get(i));
							canalesConContra.remove(i);
						}	
					}
				}
			}
		}catch(Exception e){
			
		}
	}
	
	private boolean canalValido(int idCanal){
		boolean esValido=false;
		List<Channel> canales=new ArrayList<>();
		
		for(int i=0;i<listadoCanales.size();i++){
			canales=filtrarCanales(listadoCanales.get(i));
			
			for(int j=0;j<canales.size();j++){
				if(canales.get(j).isSemiPermanent()){
					if(canales.get(j).getId()==idCanal){
						esValido=true;
					}
				}
			}
			
		}
		
		return esValido;
	}
	
	private void actualizarNumUsuarios(){
		for(int i=0;i<listadoCanalNumUsu.size();i++){
			HashMap<ChannelProperty, String> propiedadesCanales = new HashMap<ChannelProperty, String>();
			
			String nombre=listadoCanalNumUsu.get(i).getMensaje()+" "+api.getServerInfo().getClientsOnline();
			
			if(!canalExiste(nombre)){
				propiedadesCanales.put(ChannelProperty.CHANNEL_NAME, nombre);
				api.editChannel(listadoCanalNumUsu.get(i).getIdCanal(), propiedadesCanales);
			}
			
		}
	}
	
	private boolean esCanalConPermisos(int idCanal,String nombre,int espaciador){
		boolean esValido=false;
		
		List<Channel> canales=new ArrayList<>();
		
		for(int i=0;i<listadoCanales.size();i++){
			if(listadoCanales.get(i).getNombreCanales().contains(nombre)){
				canales=filtrarCanales(listadoCanales.get(i));
				
				for(int j=0;j<canales.size();j++){
					if(canales.get(j).isSemiPermanent()){
						if(canales.get(j).getId()==idCanal){
							if(canales.get(j).getParentChannelId()==espaciador){
								esValido=true;
								return esValido;
							}
						}
					}
				}
			}
		}
		
		return esValido;
	}
	
	public TimerTask timerTask = new TimerTask(){ 
		ActualizarTwitch twitch;
		
		@SuppressWarnings("deprecation")
		public void run(){
			
			sacarTodosLosCanales();
			quitarContraCanales();
	    	borrarCanales();
	    	sacarTodosLosCanales();
	    	crearCanales();
	    	actualizarNumUsuarios();
	    	
	    	if(mda.isAutoRank() && mda.isPro()){
	    		aniadirPuntos();
	    		asignarRangos();
	    	}
	    	if(mda.isAutobanner() && banners.size()>=2){
    			cambiarBanner();
    		}
	    	
	    	if(numVeces>=5){
	    		numVeces=0;
	    		
	    		if(mda.getCanalTwitch()>0){
	    			if(twitch!=null){
	    				if(twitch.isAlive()){
	    					twitch.stop();
	    				}
	    			}
	    			twitch=new ActualizarTwitch(mda.getCanalTwitch(),mda.getNombreCanalTwitch(),mda.getGrupoTwitch(),mda.getUsuario(),mda.getContra(),mda.getDireccion(),mda.getPuerto());
    				twitch.run();
	    		}
	    		usuariosVerificandose.clear();
	    	}
	    	
	    	numVeces++;
	    	
		}
	};
	
	/**
	 * Apartado Banner
	 */
	
	private void cambiarBanner(){
		Map<VirtualServerProperty,String> propiedades = new HashMap<VirtualServerProperty, String>();
		
		propiedades.put(VirtualServerProperty.VIRTUALSERVER_HOSTBANNER_GFX_URL, banners.get(bannerActual).getImagen());
		propiedades.put(VirtualServerProperty.VIRTUALSERVER_HOSTBANNER_URL, banners.get(bannerActual).getUrl());
		
		if(propiedades.size()>0){
			api.editServer(propiedades);
		}
		
		bannerActual++;
		
		if(bannerActual==banners.size()){
			bannerActual=0;
		}
	}
	
	/**
	 * Apartado Funciones Generales
	 */
	private String sacarNombreCanal(int id){
		String nombreC="";
			
		for(int i=0;i<todosLosCanales.size();i++){
			if(todosLosCanales.get(i).getId()==id){
				nombreC=todosLosCanales.get(i).getName();

				return nombreC;
			}
		}
		
		return nombreC;
	}
	
	public synchronized void mandarMensaje(String uniqueId, String message){
		int clientId=sacarCLientID(uniqueId);
		
		if(clientId!=0){
			api.sendPrivateMessage(clientId, message);
		}
		
	}
	
	private int sacarCLientID(String uniqueId){
		/*
		 * Busca un cliente y saca su clientID, si no esta conectado devuelve el valor 0
		 */
		
		int clientID=0;
		
		List<Client> clientes=api.getClients();
		
		for(int i=0;i<clientes.size();i++){
			if(clientes.get(i).getUniqueIdentifier().equalsIgnoreCase(uniqueId)){
				clientID=clientes.get(i).getId();
				return clientID;
			}
		}
		
		return clientID;
	}
	
	public String sacarUsuarioPorId(int id){
		//Le pasas una DB id y te saca su Unique ID
		
		String uniqueid="";
		
		List<Client> clientes=api.getClients();
		
		for(int i=0;i<clientes.size();i++){
			if(clientes.get(i).getId()==id){
				uniqueid=clientes.get(i).getUniqueIdentifier();
				return uniqueid;
			}
		}
		
		return uniqueid;
	}
	
	/**
	 * Apartado AutoRank
	 */
	
	private void aniadirPuntos(){
		//Aqui se sacaran los usuarios online y activos y se les añadira sus puntos
		ArrayList<String> usuarios=new ArrayList<String>();
		
		List<Client> clientes=api.getClients();
		
		for(int i=0;i<clientes.size();i++){
			usuarios.add(clientes.get(i).getUniqueIdentifier());
		}
		
		if(bdExterna) {
			new aniadirPuntos(usuarios,gbdExterna).start();
		}else {
			new aniadirPuntos(usuarios,gbdInterna).start();
		}
		
	}
	
	private void asignarRangos(){
		//Aqui se sacaran los usuarios online y se asignara el rango que le corresponda
		
		List<Client> clientes=api.getClients();
		for(int i=0;i<clientes.size();i++){
			int puntos=0;

			if(bdExterna) {
				puntos=gbdExterna.getPuntosUsuario(clientes.get(i).getUniqueIdentifier());
			}else {
				puntos=gbdInterna.getPuntosUsuario(clientes.get(i).getUniqueIdentifier());
			}
					
			int idGrupo=sacarRangoAdecuado(puntos);
			
			if(idGrupo>0){
				if(!tieneEseRango(clientes.get(i).getId(),idGrupo)){
					quitarTodosLosRangos(clientes.get(i).getId(),clientes.get(i).getDatabaseId());
					api.addClientToServerGroup(idGrupo, clientes.get(i).getDatabaseId());
				}				
			}
			
		}
		
	}
	
	private int sacarRangoAdecuado(int puntos){
		//Buscamos entre todos los rangos cual le corresponde
		int rango=0;
		int puntosMaximos=puntos;
		
		for(int i=0;i<listadoRangos.size();i++){
			if(puntos>=listadoRangos.get(i).getPuntosNecesarios() && puntosMaximos<=listadoRangos.get(i).getPuntosNecesarios()){
				rango=listadoRangos.get(i).getIdGrupo();
				puntosMaximos=listadoRangos.get(i).getPuntosNecesarios();
			}
		}
		
		return rango;
	}
	
	private Rango sacarProxRango(int puntos){
		//Buscamos entre todos los rangos cual le corresponde
		Rango rank = null;
		int puntosMaximos=0;
		
		for(int i=0;i<listadoRangos.size();i++){
			if(puntos>listadoRangos.get(i).getPuntosNecesarios() && puntosMaximos>listadoRangos.get(i).getPuntosNecesarios()){
				rank=listadoRangos.get(i);
				puntosMaximos=listadoRangos.get(i).getPuntosNecesarios();
			}
		}
		
		return rank;
	}
	
	private boolean tieneEseRango(int clientID,int idGrupo){
		//Miramos si tiene ese rango
		boolean loTiene=false;
		
		List<ServerGroup> grupos=api.getServerGroupsByClientId(clientID);
		
		if(grupos!=null){
			for(int i=0;i<grupos.size();i++){
				if(grupos.get(i).getId()==idGrupo){
					loTiene=true;
					return loTiene;
				}
			}
		}
		
		return loTiene;
	}
	
	private void quitarTodosLosRangos(int clientID,int databaseID){
		//Le quitamos todos los rangos que pueda tener
		for(int i=0;i<listadoRangos.size();i++){
			if(tieneEseRango(clientID,listadoRangos.get(i).getIdGrupo())){
				api.removeClientFromServerGroup(listadoRangos.get(i).getIdGrupo(), databaseID);
			}
		}
	}
	
	/**
	 * Apartado Detector Trolls y VPN
	 */

	private void sacarUsuariosTrolls(TS3Api api){
		
		if(mda.getGrupoTroll()>0){
			try{
				List<ServerGroupClient> groupServer=api.getServerGroupClients(mda.getGrupoTroll());
			     for(int i=0;i<groupServer.size();i++){
			    	 if(bdExterna) {
			    		 if(!gbdExterna.estaTroll(groupServer.get(i).getUniqueIdentifier())){
				    		 String ip="";
				    		 ip=api.getDatabaseClientByUId(groupServer.get(i).getUniqueIdentifier()).getLastIp();
				    		 
				    		 gbdExterna.aniadirTroll(ip, groupServer.get(i).getUniqueIdentifier());
				    	 }
			    	 }else {
			    		 if(!gbdInterna.estaTroll(groupServer.get(i).getUniqueIdentifier())){
				    		 String ip="";
				    		 ip=api.getDatabaseClientByUId(groupServer.get(i).getUniqueIdentifier()).getLastIp();
				    		 
				    		 gbdInterna.aniadirTroll(ip, groupServer.get(i).getUniqueIdentifier());
				    	 }
			    	 }
			     }
			}catch(Exception e){
				mandarMensaje(mda.getAdminBot(),idioma.getProperty("errornoservergroup"));
			}
		}
	}
	
	private void aniadirDescripcion(int databaseId,String descripcion){
		
		HashMap<ClientProperty,String> propiedades=new HashMap<ClientProperty,String>();
		propiedades.put(ClientProperty.CLIENT_DESCRIPTION, descripcion);
		api.editDatabaseClient(databaseId, propiedades);
		
	}
	
	private void aniadirTroll(int databaseId){
		api.addClientToServerGroup(mda.getGrupoTroll(), databaseId);
	}
	
	private void detectarTrolls(){
		for(int i=0;i<listaClientes.size();i++){
			if(bdExterna) {
				if(gbdExterna.estaIP(listaClientes.get(i).getLastIp())){
					if(!(gbdExterna.estaTroll(listaClientes.get(i).getUniqueIdentifier()))){
						int dbId=0;
						dbId=listaClientes.get(i).getDatabaseId();
						aniadirDescripcion(dbId,"Troll - DetectorTrolls - 1 ID: "+gbdExterna.sacarPrimeraID(listaClientes.get(i).getLastIp()));
						aniadirTroll(dbId);
						gbdExterna.aniadirTroll(listaClientes.get(i).getLastIp(), listaClientes.get(i).getUniqueIdentifier());
					}
				}
			}else {
				if(gbdInterna.estaIP(listaClientes.get(i).getLastIp())){
					if(!(gbdInterna.estaTroll(listaClientes.get(i).getUniqueIdentifier()))){
						int dbId=0;
						dbId=listaClientes.get(i).getDatabaseId();
						aniadirDescripcion(dbId,"Troll - DetectorTrolls - 1 ID: "+gbdInterna.sacarPrimeraID(listaClientes.get(i).getLastIp()));
						aniadirTroll(dbId);
						gbdInterna.aniadirTroll(listaClientes.get(i).getLastIp(), listaClientes.get(i).getUniqueIdentifier());
					}
				}
			}
		}
	}
	
	private void quitarTrolls(String mensaje){
		String uniqueid=util.sacarUniqueId(mensaje);
		
		ArrayList<String> trolls=null;
		
		if(bdExterna) {
			trolls=gbdExterna.sacarTodosLosTrollsIp(uniqueid);
		}else {
			trolls=gbdInterna.sacarTodosLosTrollsIp(uniqueid);
		}
		
		int dbId=0;
		
		for(int i=0;i<trolls.size();i++){
			try{
				if(trolls.get(i).length()>0 && trolls.get(i)!=null){
					mandarMensaje(mda.getAdminBot(),"Eliminando troll a la Unique Id: "+trolls.get(i));
					dbId=0;
					if(bdExterna) {
						gbdExterna.eliminarTrollUniqueId(trolls.get(i));
					}else {
						gbdInterna.eliminarTrollUniqueId(trolls.get(i));
					}
					dbId=api.getDatabaseClientByUId(trolls.get(i)).getDatabaseId();
					
					if(dbId>0){
						api.removeClientFromServerGroup(mda.getGrupoTroll(), dbId);
					}
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Apartado Verificador Twitter
	 */
	
	private void verificacionTwitter(String mensaje,String uniqueid){
		if(estaUsuarioVerificando(uniqueid)){
			if(mensaje.toLowerCase().contains("!pin")){
				for(int i=0;i<usuariosVerificandose.size();i++){
					if(usuariosVerificandose.get(i).getUsuario().getUniqueId().equalsIgnoreCase(uniqueid)){
						if(usuariosVerificandose.get(i).probarFuncionamiento(mensaje.substring(5).trim())){
							aniadirRangoVerificado(uniqueid);
							usuariosVerificandose.remove(i);
						}else{
							mandarMensaje(uniqueid,"No has podido ser verficado prueba mas tarde");
							usuariosVerificandose.remove(i);
						}
					}
				}
			}
		}else{
			usuariosVerificandose.add(new sacarAccesoTwitter(this,new UsuTwitter(uniqueid)));
		}
	}
	
	private boolean estaUsuarioVerificando(String uniqueId){
		boolean esta=false;
		
		for(int i=0;i<usuariosVerificandose.size();i++){
			if(usuariosVerificandose.get(i).getUsuario().getUniqueId().equalsIgnoreCase(uniqueId)){
				esta=true;
			}
		}
		
		return esta;
	}
	
	private void aniadirRangoVerificado(String uniqueId){
		api.addClientToServerGroup(441, api.getDatabaseClientByUId(uniqueId).getDatabaseId());
	}
	
}
