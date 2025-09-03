package dto;

public class QAboardDTO {
	private int inqu_seq;
	private int inqu_pw; // 문의 비번
	private String inqu_count; //문의 사항
	private String inqu_user_name; //닉네임
	private String inqu_date;
	
	public QAboardDTO() {}
	public QAboardDTO(int inqu_seq, int inqu_pw, String inqu_count, String inqu_user_name, String inqu_date) {
		super();
		this.inqu_seq = inqu_seq;
		this.inqu_pw = inqu_pw;
		this.inqu_count = inqu_count;
		this.inqu_user_name = inqu_user_name;
		this.inqu_date = inqu_date;
	}
	public int getInqu_seq() {
		return inqu_seq;
	}
	public void setInqu_seq(int inqu_seq) {
		this.inqu_seq = inqu_seq;
	}
	public int getInqu_pw() {
		return inqu_pw;
	}
	public void setInqu_pw(int inqu_pw) {
		this.inqu_pw = inqu_pw;
	}
	public String getInqu_count() {
		return inqu_count;
	}
	public void setInqu_count(String inqu_count) {
		this.inqu_count = inqu_count;
	}
	public String getInqu_user_name() {
		return inqu_user_name;
	}
	public void setInqu_user_name(String inqu_user_name) {
		this.inqu_user_name = inqu_user_name;
	}
	public String getInqu_date() {
		return inqu_date;
	}
	public void setInqu_date(String inqu_date) {
		this.inqu_date = inqu_date;
	}
	
	
}
