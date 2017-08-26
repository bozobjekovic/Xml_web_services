package tim9.xml.DTO;

import tim9.xml.model.akt.Akt;

public class AktDTO {

	private Akt akt;
	private String text;
	
	public AktDTO() {
		super();
	}

	public AktDTO(Akt akt, String text) {
		super();
		this.akt = akt;
		this.text = text;
	}

	public Akt getAkt() {
		return akt;
	}

	public void setAkt(Akt akt) {
		this.akt = akt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
