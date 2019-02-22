package Model;

public class Weight {

	private String recordate;
	private String weight;

	
	public Weight( String recordate, String weight) {
		super();
		this.recordate = recordate;
		this.weight = weight;
		
	}
	
	public Weight(String weight) {
		super();
		this.weight = weight;
	}


	public String getRecordate() {
		return recordate;
	}

	public void setRecordate(String recordate) {
		this.recordate = recordate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	
}
