package dto;

public class QAboardComentDTO {
	private int inqc_seq; 
	private int inqu_id; //부모
	private String inqc_write;
	private String inqc_date;
	
	public QAboardComentDTO() {}
	public QAboardComentDTO(int inqc_seq, int inqu_id, String inqc_write, String inqc_date) {
		super();
		this.inqc_seq = inqc_seq;
		this.inqu_id = inqu_id;
		this.inqc_write = inqc_write;
		this.inqc_date = inqc_date;
	}
	public int getInqc_seq() {
		return inqc_seq;
	}
	public void setInqc_seq(int inqc_seq) {
		this.inqc_seq = inqc_seq;
	}
	public int getInqu_id() {
		return inqu_id;
	}
	public void setInqu_id(int inqu_id) {
		this.inqu_id = inqu_id;
	}
	public String getInqc_write() {
		return inqc_write;
	}
	public void setInqc_write(String inqc_write) {
		this.inqc_write = inqc_write;
	}
	public String getInqc_date() {
		return inqc_date;
	}
	public void setInqc_date(String inqc_date) {
		this.inqc_date = inqc_date;
	}
	
	
	
}
