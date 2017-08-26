package tim9.xml.DTO;

public class MetadataAmdDTO {

	private String status;
	private String datumPredaje;
	private String datumObjave;
	private int brojGlasovaZa;
	private int brojGlasovaProtiv;
	private int brojGlasovaUzdrzano;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDatumPredaje() {
		return datumPredaje;
	}

	public void setDatumPredaje(String datumPredaje) {
		this.datumPredaje = datumPredaje;
	}

	public String getDatumObjave() {
		return datumObjave;
	}

	public void setDatumObjave(String datumObjave) {
		this.datumObjave = datumObjave;
	}

	public int getBrojGlasovaZa() {
		return brojGlasovaZa;
	}

	public void setBrojGlasovaZa(int brojGlasovaZa) {
		this.brojGlasovaZa = brojGlasovaZa;
	}

	public int getBrojGlasovaProtiv() {
		return brojGlasovaProtiv;
	}

	public void setBrojGlasovaProtiv(int brojGlasovaProtiv) {
		this.brojGlasovaProtiv = brojGlasovaProtiv;
	}

	public int getBrojGlasovaUzdrzano() {
		return brojGlasovaUzdrzano;
	}

	public void setBrojGlasovaUzdrzano(int brojGlasovaUzdrzano) {
		this.brojGlasovaUzdrzano = brojGlasovaUzdrzano;
	}

}
