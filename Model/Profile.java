package Model;

public class Profile {
	//private String petno;
	private String petname;
	private String petkind;
	private String petbirth;
	private String petgender;
	private String petvaccine;
	private String imgpath;
	


	public Profile() {

	}
	
	
	
	
	public Profile(String petkind, String petname,  String petgender , String petbirth ) {
		super();
		this.petkind = petkind;
		this.petname = petname;
		this.petgender = petgender;
		this.petbirth = petbirth;
	}

	
	public Profile(String petname, String petkind, String petbirth, String petgender, String imgpath) {
		super();
		this.petname = petname;
		this.petkind = petkind;
		this.petbirth = petbirth;
		this.petgender = petgender;
		this.imgpath = imgpath;
	}



	public Profile(String petname, String petkind, String petbirth, String petgender, String petvaccine,String imgpath) {
		super();
		this.petname = petname;
		this.petkind = petkind;
		this.petbirth = petbirth;
		this.petgender = petgender;
		this.petvaccine = petvaccine;
		this.imgpath = imgpath;
	}

	
	public String getPetname() {
		return petname;
	}
	public void setPetname(String petname) {
		this.petname = petname;
	}
	public String getPetkind() {
		return petkind;
	}
	public void setPetkind(String petkind) {
		this.petkind = petkind;
	}
	public String getPetbirth() {
		return petbirth;
	}
	public void setPetbirth(String petbirth) {
		this.petbirth = petbirth;
	}
	public String getPetgender() {
		return petgender;
	}
	public void setPetgender(String petgender) {
		this.petgender = petgender;
	}
	public String getPetvaccine() {
		return petvaccine;
	}
	public void setPetvaccine(String petvaccine) {
		this.petvaccine = petvaccine;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	
	
/*	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	*/
	
	
	
	
}
