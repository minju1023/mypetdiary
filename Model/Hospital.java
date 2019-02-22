package Model;

public class Hospital {
	private String no;
	private String licensedate;
	private String tel;
	private String hospital;
	private String addr;
	private String naddr;
	public Hospital(String no, String licensedate, String tel, String hospital, String addr, String naddr) {
		super();
		this.no = no;
		this.licensedate = licensedate;
		this.tel = tel;
		this.hospital = hospital;
		this.addr = addr;
		this.naddr = naddr;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getLicensedate() {
		return licensedate;
	}
	public void setLicensedate(String licensedate) {
		this.licensedate = licensedate;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getNaddr() {
		return naddr;
	}
	public void setNaddr(String naddr) {
		this.naddr = naddr;
	}
	
	
	
	
}
