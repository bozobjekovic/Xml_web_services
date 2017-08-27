package tim9.xml.DTO;

public class RezultatiDTO {

	private int brojGlasovaZa;
	private int brojGlasovaProtiv;
	private int brojSuzdrzanih;
	
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
	public int getBrojSuzdrzanih() {
		return brojSuzdrzanih;
	}
	public void setBrojSuzdrzanih(int brojSuzdrzanih) {
		this.brojSuzdrzanih = brojSuzdrzanih;
	}
	
	@Override
	public String toString() {
		return "RezultatiDTO [brojGlasovaZa=" + brojGlasovaZa + ", brojGlasovaProtiv=" + brojGlasovaProtiv
				+ ", brojSuzdrzanih=" + brojSuzdrzanih + "]";
	}
	
}
