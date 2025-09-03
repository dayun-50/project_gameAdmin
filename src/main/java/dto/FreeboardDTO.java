package dto;

public class FreeboardDTO {
	private int fb_id; //시퀀스
	private String fb_user_name; //작성자
	private String fb_Title; //제목
	private String fb_write; //글
	private String fb_date;
	private int view_count;
	
	public FreeboardDTO() {}
	public FreeboardDTO(int fb_id, String fb_user_name, String fb_Title, String fb_write, String fb_date,
			int view_count) {
		super();
		this.fb_id = fb_id;
		this.fb_user_name = fb_user_name;
		this.fb_Title = fb_Title;
		this.fb_write = fb_write;
		this.fb_date = fb_date;
		this.view_count = view_count;
	}
	public int getFb_id() {
		return fb_id;
	}
	public void setFb_id(int fb_id) {
		this.fb_id = fb_id;
	}
	public String getFb_user_name() {
		return fb_user_name;
	}
	public void setFb_user_name(String fb_user_name) {
		this.fb_user_name = fb_user_name;
	}
	public String getFb_Title() {
		return fb_Title;
	}
	public void setFb_Title(String fb_Title) {
		this.fb_Title = fb_Title;
	}
	public String getFb_write() {
		return fb_write;
	}
	public void setFb_write(String fb_write) {
		this.fb_write = fb_write;
	}
	public String getFb_date() {
		return fb_date;
	}
	public void setFb_date(String fb_date) {
		this.fb_date = fb_date;
	}
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	
	
	
}
