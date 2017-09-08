package tim9.xml.util;

public class PrimeniAmandman {

	private String docIDAkt;
	private String odredba;
	private String predlozenoResenje;
	private String predlog;
	private String patch;
	
	

	@Override
	public String toString() {
		return "PrimeniAmandman [docIDAkt=" + docIDAkt + ", odredba=" + odredba + ", predlozenoResenje="
				+ predlozenoResenje + ", predlog=" + predlog + ", patch=" + patch + "]";
	}

	public String getDocIDAkt() {
		return docIDAkt;
	}

	public void setDocIDAkt(String docIDAkt) {
		this.docIDAkt = docIDAkt;
	}

	public String getOdredba() {
		return odredba;
	}

	public void setOdredba(String odredba) {
		this.odredba = odredba;
	}

	public String getPredlozenoResenje() {
		return predlozenoResenje;
	}

	public void setPredlozenoResenje(String predlozenoResenje) {
		this.predlozenoResenje = predlozenoResenje;
	}

	public String getPredlog() {
		return predlog;
	}

	public void setPredlog(String predlog) {
		this.predlog = predlog;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

}
