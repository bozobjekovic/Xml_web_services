package tim9.xml.DTO;

public class SearchDTO {

	private String status;
	private String oblast;
	private String minDatumPredaje;
	private String maxDatumPredaje;
	private String minDatumObjave;
	private String maxDatumObjave;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOblast() {
		return oblast;
	}

	public void setOblast(String oblast) {
		this.oblast = oblast;
	}

	public String getMinDatumPredaje() {
		return minDatumPredaje;
	}

	public void setMinDatumPredaje(String minDatumPredaje) {
		this.minDatumPredaje = minDatumPredaje;
	}

	public String getMaxDatumPredaje() {
		return maxDatumPredaje;
	}

	public void setMaxDatumPredaje(String maxDatumPredaje) {
		this.maxDatumPredaje = maxDatumPredaje;
	}

	public String getMinDatumObjave() {
		return minDatumObjave;
	}

	public void setMinDatumObjave(String minDatumObjave) {
		this.minDatumObjave = minDatumObjave;
	}

	public String getMaxDatumObjave() {
		return maxDatumObjave;
	}

	public void setMaxDatumObjave(String maxDatumObjave) {
		this.maxDatumObjave = maxDatumObjave;
	}

}
