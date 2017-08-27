package tim9.xml.DTO;

public class RezultatiDTO {

	private String id;
	private int brojGlasovaZa;
	private int brojGlasovaProtiv;
	private int brojSuzdrzanih;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public int getBrojSuzdrzanih() {
		return brojSuzdrzanih;
	}
	public void setBrojSuzdrzanih(int brojSuzdrzanih) {
		this.brojSuzdrzanih = brojSuzdrzanih;
	}
	@Override
	public String toString() {
		return "RezultatiDTO [id=" + id + ", brojGlasovaZa=" + brojGlasovaZa + ", brojGlasovaProtiv="
				+ brojGlasovaProtiv + ", brojSuzdrzanih=" + brojSuzdrzanih + "]";
	}
	
}
