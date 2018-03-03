package Gestion;

import Data.UsuTwitter;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class sacarAccesoTwitter {
	
	private String CONSUMER_KEY = "";
	private String CONSUMER_KEY_SECRET = "";
	private String datoAccessToken="";
	private String datoAccessTokenSecret="";
	private String mensaje="He verificado mi perfil en el teamspeak de la @ComunidadGzone http://comunidadgzone.es/";
	private Inicio ini;
	private UsuTwitter usuario;
	private TwitterFactory tf;
	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken = null;
	//private GestionBD gbd;
	private Conexion gbd;
	
	public sacarAccesoTwitter(Inicio ini,UsuTwitter usuario){
		//gbd=new GestionBD();
		gbd=new Conexion();
		this.usuario=usuario;
		this.ini=ini;
		conectarAplicacionConUsuario();
	}
	
	public void conectarAplicacionConUsuario(){

		ConfigurationBuilder cb = new ConfigurationBuilder();

		// the following is set without accesstoken- desktop client
		cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY)
				.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);

		try {
			tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken();
				
				ini.mandarMensaje(usuario.getUniqueId(), "Abre el siguiente link y da permisos a la aplicación.");
				ini.mandarMensaje(usuario.getUniqueId(),"[url]"+requestToken.getAuthorizationURL()+"[/url]");
				ini.mandarMensaje(usuario.getUniqueId(),"Responde con el pin que te han dado con el comando !pin. Ejemplo: !pin 1234");

				//System.out.println("Access token: " + accessToken.getToken());
				//System.out.println("Access token secret: " + accessToken.getTokenSecret());

			} catch (IllegalStateException ie) {
				// access token is already available, or consumer key/secret is
				// not set.
				if (!twitter.getAuthorization().isEnabled()) {
					System.out.println("OAuth consumer key/secret is not set.");
					System.exit(-1);
				}
			}


		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
	
	public boolean probarFuncionamiento(String pin){
		boolean verificado=false;
		
		try {
			if (pin.length() > 0) {
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = twitter.getOAuthAccessToken(requestToken);
			}
		} catch (TwitterException te) {
		}
		
		try {
			twitter.updateStatus(mensaje);
			usuario.setAccesstoken(accessToken.getToken());
			usuario.setTokensecret(accessToken.getTokenSecret());
			usuario.setTwitter(accessToken.getScreenName());
			
			if(!gbd.estaTwitter(usuario.getTwitter())){
				gbd.añadirTwitter(usuario);
				ini.mandarMensaje(usuario.getUniqueId(), "Felicidades has sido verificado");
				verificado=true;
			}else{
				ini.mandarMensaje(usuario.getUniqueId(), "Ese twitter ya ha sido verificado");
			}
		} catch (TwitterException e) {
		}
		
		try {
			twitter.createFriendship("ComunidadGzone");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return verificado;
	}

	public String getDatoAccessToken() {
		return datoAccessToken;
	}

	public String getDatoAccessTokenSecret() {
		return datoAccessTokenSecret;
	}

	public String getConsumerKey() {
		return CONSUMER_KEY;
	}

	public String getConsumerKeySecret() {
		return CONSUMER_KEY_SECRET;
	}

	public UsuTwitter getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuTwitter usuario) {
		this.usuario = usuario;
	}

	
}
