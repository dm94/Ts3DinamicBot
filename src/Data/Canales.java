package Data;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

public class Canales {
	private int espaciador=0;
	private ArrayList<Channel> canales;
	private HashMap<ChannelProperty, String> datosCanal=new HashMap<ChannelProperty, String>();
	private String nombreCanales="";
	private String descripcion="";
	private boolean limitar=false;
	private int limiteUsuarios=5;
	private int numCanalesMinimos=3;
	private int minimoParaEntrar=0;
	private int minimoParaHablar=0;

	public Canales(int espaciador,int numCanalesMinimos, String nombreCanales, String descripcion, boolean limitar) {
		super();
		this.espaciador = espaciador;
		this.numCanalesMinimos=numCanalesMinimos;
		this.nombreCanales = nombreCanales;
		this.descripcion=descripcion;
		this.limitar=limitar;
		canales=new ArrayList<>();
		crearPropiedades();
	}
	
	public Canales(int espaciador, String nombreCanales, String descripcion, boolean limitar,int limite) {
		super();
		this.espaciador = espaciador;
		this.nombreCanales = nombreCanales;
		this.descripcion=descripcion;
		this.limitar=limitar;
		this.limiteUsuarios=limite;
		canales=new ArrayList<>();
		crearPropiedades();
	}
	
	public Canales(int espaciador,int numCanalesMinimos, String nombreCanales, String descripcion, boolean limitar,int limite,int minimoParaEntrar,int minimoParaHablar) {
		super();
		this.espaciador = espaciador;
		this.numCanalesMinimos=numCanalesMinimos;
		this.nombreCanales = nombreCanales;
		this.descripcion=descripcion;
		this.limitar=limitar;
		this.limiteUsuarios=limite;
		this.minimoParaEntrar=minimoParaEntrar;
		this.minimoParaHablar=minimoParaHablar;
		canales=new ArrayList<>();
		crearPropiedades();
	}
	
	private void crearPropiedades(){
		
		if(limitar){
			datosCanal.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "0");
			datosCanal.put(ChannelProperty.CHANNEL_MAXCLIENTS, String.valueOf(limiteUsuarios));
		}else{
			datosCanal.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "1");
		}
		
		datosCanal.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "0");
		datosCanal.put(ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT, "1");
		datosCanal.put(ChannelProperty.CHANNEL_DESCRIPTION, descripcion);
		datosCanal.put(ChannelProperty.CPID, String.valueOf(espaciador));
		
		if(minimoParaHablar>0){
			datosCanal.put(ChannelProperty.CHANNEL_NEEDED_TALK_POWER, String.valueOf(minimoParaHablar));
		}
	}

	public int getEspaciador() {
		return espaciador;
	}

	public ArrayList<Channel> getCanales() {
		return canales;
	}

	public void setCanales(ArrayList<Channel> todos) {
		this.canales = todos;
	}

	public String getNombreCanales() {
		return nombreCanales;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public boolean isLimitar() {
		return limitar;
	}

	public int getNumCanalesMinimos() {
		return numCanalesMinimos;
	}

	public HashMap<ChannelProperty, String> getDatosCanal() {
		return datosCanal;
	}

	public int getMinimoParaEntrar() {
		return minimoParaEntrar;
	}

	public void setMinimoParaEntrar(int minimoParaEntrar) {
		this.minimoParaEntrar = minimoParaEntrar;
	}

	public int getMinimoParaHablar() {
		return minimoParaHablar;
	}

	public void setMinimoParaHablar(int minimoParaHablar) {
		this.minimoParaHablar = minimoParaHablar;
	}

	
}
