package dto;

public class FreeboardComentDTO {
	private int fc_id;
	private int fb_id; //부모
	private String fc_user_name; //작성자
	private String fc_write; //글
	private String fc_date;
	
	public FreeboardComentDTO() {}

	public FreeboardComentDTO(int fc_id, int fb_id, String fc_user_name, String fc_write, String fc_date) {
		super();
		this.fc_id = fc_id;
		this.fb_id = fb_id;
		this.fc_user_name = fc_user_name;
		this.fc_write = fc_write;
		this.fc_date = fc_date;
	}

	public int getFc_id() {
		return fc_id;
	}

	public void setFc_id(int fc_id) {
		this.fc_id = fc_id;
	}

	public int getFb_id() {
		return fb_id;
	}

	public void setFb_id(int fb_id) {
		this.fb_id = fb_id;
	}

	public String getFc_user_name() {
		return fc_user_name;
	}

	public void setFc_user_name(String fc_user_name) {
		this.fc_user_name = fc_user_name;
	}

	public String getFc_write() {
		return fc_write;
	}

	public void setFc_write(String fc_write) {
		this.fc_write = fc_write;
	}

	public String getFc_date() {
		return fc_date;
	}

	public void setFc_date(String fc_date) {
		this.fc_date = fc_date;
	}
	
}
