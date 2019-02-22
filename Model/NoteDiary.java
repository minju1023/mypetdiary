package Model;

public class NoteDiary {
	public static String day; /////////// 
	private String meal;
	private String pee;
	private String poo;
	private String brushing;
	private String play;
	private String memo;
	
	
	public NoteDiary(String day) {

		this.day=day;
	}
	
	public NoteDiary(String day, String meal, String pee, String poo, String brushing, String play, String memo) {
		super();
		this.day = day;
		this.meal = meal;
		this.pee = pee;
		this.poo = poo;
		this.brushing = brushing;
		this.play = play;
		this.memo = memo;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMeal() {
		return meal;
	}

	public void setMeal(String meal) {
		this.meal = meal;
	}

	public String getPee() {
		return pee;
	}

	public void setPee(String pee) {
		this.pee = pee;
	}

	public String getPoo() {
		return poo;
	}

	public void setPoo(String poo) {
		this.poo = poo;
	}

	public String getBrushing() {
		return brushing;
	}

	public void setBrushing(String brushing) {
		this.brushing = brushing;
	}

	public String getPlay() {
		return play;
	}

	public void setPlay(String play) {
		this.play = play;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
	
}
