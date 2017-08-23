package tim9.xml.DTO;

import java.util.Date;

public class SednicaDTO {

	Date datum;
	private int brojPrisutnih;
	private String status;
	
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public int getBrojPrisutnih() {
		return brojPrisutnih;
	}
	public void setBrojPrisutnih(int brojPrisutnih) {
		this.brojPrisutnih = brojPrisutnih;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
