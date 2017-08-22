package tim9.xml.DTO;

import java.util.List;

import tim9.xml.model.akt.Akt;
import tim9.xml.model.amandman.Amandman;

public class AmandmaniAktaDTO {

	private List<Amandman> amandmani;
	private Akt akt;
	
	public List<Amandman> getAmandmani() {
		return amandmani;
	}
	public void setAmandmani(List<Amandman> amandmani) {
		this.amandmani = amandmani;
	}
	public Akt getAkt() {
		return akt;
	}
	public void setAkt(Akt akt) {
		this.akt = akt;
	}
	@Override
	public String toString() {
		return "AmandmaniAktaDTO [amandmani=" + amandmani + ", akt=" + akt + "]";
	}
	
}
