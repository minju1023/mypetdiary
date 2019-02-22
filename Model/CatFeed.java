package Model;

public class CatFeed {
	private String productname;
	private String crudeprotein;
	private String crudefat;
	private String crudefiber;
	private String crudeash;
	private String carbohydrate;
	private String calorie;
	private String productionregion;
	private String catfoodimg;
	
	
	
	
	
	public CatFeed(String productname, String crudeprotein, String crudefat, String crudefiber, String crudeash,
			String carbohydrate, String calorie, String productionregion) {
		super();
		this.productname = productname;
		this.crudeprotein = crudeprotein;
		this.crudefat = crudefat;
		this.crudefiber = crudefiber;
		this.crudeash = crudeash;
		this.carbohydrate = carbohydrate;
		this.calorie = calorie;
		this.productionregion = productionregion;
	}

	public CatFeed(String productname, String crudeprotein, String crudefat, String crudefiber, String crudeash,
			String carbohydrate, String calorie, String productionregion, String catfoodimg) {
		super();
		this.productname = productname;
		this.crudeprotein = crudeprotein;
		this.crudefat = crudefat;
		this.crudefiber = crudefiber;
		this.crudeash = crudeash;
		this.carbohydrate = carbohydrate;
		this.calorie = calorie;
		this.productionregion = productionregion;
		this.catfoodimg = catfoodimg;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getCrudeprotein() {
		return crudeprotein;
	}

	public void setCrudeprotein(String crudeprotein) {
		this.crudeprotein = crudeprotein;
	}

	public String getCrudefat() {
		return crudefat;
	}

	public void setCrudefat(String crudefat) {
		this.crudefat = crudefat;
	}

	public String getCrudefiber() {
		return crudefiber;
	}

	public void setCrudefiber(String crudefiber) {
		this.crudefiber = crudefiber;
	}

	public String getCrudeash() {
		return crudeash;
	}

	public void setCrudeash(String crudeash) {
		this.crudeash = crudeash;
	}

	public String getCarbohydrate() {
		return carbohydrate;
	}

	public void setCarbohydrate(String carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public String getCalorie() {
		return calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}

	public String getProductionregion() {
		return productionregion;
	}

	public void setProductionregion(String productionregion) {
		this.productionregion = productionregion;
	}

	public String getCatfoodimg() {
		return catfoodimg;
	}

	public void setCatfoodimg(String catfoodimg) {
		this.catfoodimg = catfoodimg;
	}
	

}
