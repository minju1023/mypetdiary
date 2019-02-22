package Model;

public class Join {

	public static String email;
	private String password;
	private String name;
	private String birth;
	private String gender;
	private String nickname;
	private String phone;
	
	
	
	
	public Join(String email) {
		super();
		this.email = email;
	}

	public Join(String email, String password, String name, String birth, String gender, String nickname,
			String phone) {
		super();

		this.email = email;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.gender = gender;
		this.nickname = nickname;
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
