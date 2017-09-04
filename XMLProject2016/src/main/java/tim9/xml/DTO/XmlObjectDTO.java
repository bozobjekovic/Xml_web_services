package tim9.xml.DTO;

import tim9.xml.model.korisnik.Korisnik;

public class XmlObjectDTO {
	
	private String xml;
	private Korisnik user;
	private String aktId;
	
	
	@Override
	public String toString() {
		return "XmlObjectDTO [xml=" + xml + ", user=" + user + ", aktId=" + aktId + "]";
	}

	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public Korisnik getUser() {
		return user;
	}
	
	public void setUser(Korisnik user) {
		this.user = user;
	}
	
	public String getAktId() {
		return aktId;
	}
	
	public void setAktId(String aktId) {
		this.aktId = aktId;
	}
	
}
