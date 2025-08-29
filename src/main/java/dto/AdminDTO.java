package dto;

public class AdminDTO {
	private String admin_id;
	private String damin_pw;
	
	public AdminDTO() {}
	public AdminDTO(String admin_id, String damin_pw) {
		super();
		this.admin_id = admin_id;
		this.damin_pw = damin_pw;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getDamin_pw() {
		return damin_pw;
	}
	public void setDamin_pw(String damin_pw) {
		this.damin_pw = damin_pw;
	}
	
	
}
