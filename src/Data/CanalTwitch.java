package Data;

public class CanalTwitch {
	
	private String nick="";
	private boolean online=false;
	private String status="";
	private String game="";
	private String imagen="";
	
	public CanalTwitch(String nick, boolean online) {
		super();
		this.nick = nick;
		this.online = online;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getNick() {
		return nick;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
}
