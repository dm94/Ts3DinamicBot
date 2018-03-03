package Data;

public class UsuTwitter {
	
	private String uniqueId="";
	private String twitter="";
	private String accesstoken="";
	private String tokensecret="";
	
	public UsuTwitter(String uniqueId, String twitter, String accesstoken, String tokensecret) {
		super();
		this.uniqueId = uniqueId;
		this.twitter = twitter;
		this.accesstoken = accesstoken;
		this.tokensecret = tokensecret;
	}

	public UsuTwitter(String uniqueId) {
		super();
		this.uniqueId = uniqueId;
	}

	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public String getTokensecret() {
		return tokensecret;
	}
	public void setTokensecret(String tokensecret) {
		this.tokensecret = tokensecret;
	}
	
	

}
