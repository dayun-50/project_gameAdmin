package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.AdminDTO;
import dto.FreeboardComentDTO;
import dto.FreeboardDTO;
import dto.GameboardComentDTO;
import dto.GameboardDTO;
import dto.QAboardComentDTO;
import dto.QAboardDTO;
import dto.UserDTO;

public class AdminDAO {
	private static AdminDAO instance;

	public synchronized static AdminDAO getInstance() {
		if(instance == null) {
			instance = new AdminDAO();
		}
		return instance;
	}

	public Connection getConnection() throws Exception{ //tomcat db연결
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}

	public static String encrypt(String text) { //SHA 암호화
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
			byte[] digest = md.digest(bytes);

			StringBuilder builder = new StringBuilder();
			for (byte b : digest) {
				builder.append(String.format("%02x", b));
			}
			return builder.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-512 암호화 실패", e);
		}
	}


	public int AdminLogin(AdminDTO dto) throws Exception { // 관리자 로그인
		String sql = "select * from admin where admin_id = ? and admin_pw = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, dto.getAdmin_id());
			stat.setString(2, dto.getDamin_pw());

			try(ResultSet rs = stat.executeQuery();){
				if(rs.next()) {
					return 1;
				}else {
					return 0;
				}
			}
		}
	}

	//	public static void main(String[] args) {
	//		master1
	//		System.out.println(encrypt("Master!1"));
	//	}

	//유저 목록출력
	public ArrayList<UserDTO> userList() throws Exception {
		String sql = "select * from users";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){

			ArrayList<UserDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()) {
					String id = rs.getString("user_id");
					String nickname = rs.getString("user_nickname");
					String name = rs.getString("user_name");
					String phone = rs.getString("user_phone");
					String email = rs.getString("user_email");
					Timestamp date = rs.getTimestamp("user_join_date");
					String regdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					String agree = rs.getString("agree");

					list.add(new UserDTO(id, "", nickname, name, phone, email, regdate, agree));
				}
				return list;
			}
		}
	}

	//유저목록...네비로만들라니까 계속 까먹네 아이고야
	public ArrayList<UserDTO> selectFromTo(int from, int to) throws Exception{
		String sql = "SELECT * FROM ( SELECT users.*, ROW_NUMBER() OVER (ORDER BY user_join_date DESC) rn FROM users ) sub WHERE rn BETWEEN ? AND ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, from);
			stat.setInt(2, to);

			ArrayList<UserDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()) {
					String id = rs.getString("user_id");
					String nickname = rs.getString("user_nickname");
					String name = rs.getString("user_name");
					String phone = rs.getString("user_phone");
					String email = rs.getString("user_email");
					Timestamp date = rs.getTimestamp("user_join_date");
					String regdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					String agree = rs.getString("agree");

					list.add(new UserDTO(id, "", nickname, name, phone, email, regdate, agree));
				}
				return list;
			}
		}
	}

	//유저 수
	public int getUserCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM users";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);
				ResultSet rs = stat.executeQuery()) {
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				return 0;
			}
		}
	}

	//유저 상세정보 출력
	public UserDTO getUserPage(String user_id) throws Exception {
		String sql = "select * from users where user_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, user_id);

			try(ResultSet rs = stat.executeQuery()){
				if(rs.next()) {
					String id = rs.getString("user_id");
					String nickname = rs.getString("user_nickname");
					String name = rs.getString("user_name");
					String phone = rs.getString("user_phone");
					String email = rs.getString("user_email");
					Timestamp date = rs.getTimestamp("user_join_date");
					String regdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
					String agree = rs.getString("agree");

					return new UserDTO(id, "", nickname, name, phone, email, regdate, agree);
				}
				return null;
			}
		}
	}

	//유저 블랙리스트 체크
	public String blackUser(String user_id) throws Exception{
		String sql = "select black_user_id, black_comment from BlackList where black_user_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, user_id);

			try(ResultSet rs = stat.executeQuery()){
				if(rs.next()) {
					return rs.getString("black_comment");
				}
				return "";
			}
		}
	}

	//유저 블랙리스트 등록
	public int blackInsert(String user_id, String comment) throws Exception{
		String sql = "insert into BlackList values(BlackList_seq.nextval,?,?)";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, user_id);
			stat.setString(2, comment);

			return stat.executeUpdate();
		}
	}

	//유저 블랙리스트 해제
	public int blackDelete(String user_id) throws Exception{
		String sql = "delete from BlackList where black_user_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, user_id);

			return stat.executeUpdate();
		}
	}

	//게임 게시판 출력
	public ArrayList<GameboardDTO> seletAllGameboard(int from, int to) throws Exception{
		String sql =  "SELECT * FROM ( SELECT game_board.*, ROW_NUMBER() OVER (ORDER BY game_board_date DESC) rn FROM game_board ) sub WHERE rn BETWEEN ? AND ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, from);
			stat.setInt(2, to);

			ArrayList<GameboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()){
					int game_seq = rs.getInt("game_seq");
					int gameid = rs.getInt("gameid");
					String gameboardtitle = rs.getString("gameboardtitle");
					String gamecoment = rs.getString("gamecoment");
					String gamewrtier = rs.getString("gamewrtier");
					Timestamp date = rs.getTimestamp("game_board_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
					int view_count = rs.getInt("view_count");

					list.add(new GameboardDTO(game_seq, gameid, gameboardtitle, gamecoment, gamewrtier, regdate, view_count));
				}
				return list;
			}
		}
	}

	//게임 게시물 출력
	public ArrayList<GameboardDTO> seletAllGameboardPrint(String gameboardNum) throws Exception{
		String sql = "select * from game_board where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, gameboardNum);

			ArrayList<GameboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				if(rs.next()) {
					int game_seq = rs.getInt("game_seq");
					int gameid = rs.getInt("gameid");
					String gameboardtitle = rs.getString("gameboardtitle");
					String gamecoment = rs.getString("gamecoment");
					String gamewrtier = rs.getString("gamewrtier");
					Timestamp date = rs.getTimestamp("game_board_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
					int view_count = rs.getInt("view_count");

					list.add(new GameboardDTO(game_seq, gameid, gameboardtitle, gamecoment, gamewrtier, regdate, view_count));
				}
				return list;
			}
		}
	}

	//게임 게시판 댓글 출력
	public ArrayList<GameboardComentDTO> seletAllGBComent(String prentNum) throws Exception{
		String sql = "select * from Game_Coment where game_parent_seq = ? ORDER BY game_coment_seq DESC";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, prentNum);

			ArrayList<GameboardComentDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()) {
					int seq = rs.getInt("game_coment_seq");
					int parent_seq = rs.getInt("game_parent_seq");
					int gameid = rs.getInt("gameid");
					String writer = rs.getString("game_coment_writer");
					String coment = rs.getString("game_coment");
					Timestamp date = rs.getTimestamp("game_coment_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);

					list.add(new GameboardComentDTO(seq, parent_seq, gameid, writer, coment, regdate));
				}
			}
			return list;
		}
	}

	//댓글 갯수 카운트
	public int countComent(String parent_seq) throws Exception {
		String sql = "SELECT COUNT(*) AS cnt FROM Game_Coment WHERE game_parent_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, parent_seq);

			try(ResultSet rs = stat.executeQuery()){
				if(rs.next()) {
					int result = rs.getInt("cnt");
					return result;
				}else {
					return 0;
				}
			}
		}
	}

	//게임 게시판 view카운트
	public void count(int count, String seq) throws Exception {
		String sql = "update game_board set view_count = ? where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, count);
			stat.setString(2, seq);
			stat.executeUpdate();
		}
	}


	//게임 게시판 댓글 입력
	public int comentInsert(GameboardComentDTO dto) throws Exception {
		String sql = "insert into Game_Coment values(Game_Coment_seq.NEXTVAL,?,?,?,?,?)";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, dto.getGame_parent_seq());
			stat.setInt(2, 0);
			stat.setString(3, dto.getGame_coment_writer());
			stat.setString(4, dto.getGame_coment());
			stat.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

			return stat.executeUpdate();
		}
	}

	//게임 게시판 댓글 삭제
	public int comentDelete(String seq) throws Exception {
		String sql = "delete from Game_Coment where game_coment_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//게임 게시판 댓글 수정
	public int comentUpdate(String seq, String text) throws Exception {
		String sql = "update Game_Coment set game_coment = ? where game_coment_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, text);
			stat.setString(2, seq);

			return stat.executeUpdate();
		}
	}	

	//게임 게시판 게시물 삭제
	public int deleteGameBoard(String seq) throws Exception{
		String sql = "delete from game_board where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql)){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//게임 게시물 갯수 출력
	public int getGameboardCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM game_board";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);
				ResultSet rs = stat.executeQuery()) {
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				return 0;
			}
		}
	}

	//자유 게시판 출력
	public ArrayList<FreeboardDTO> seletAllFreeboard(int from, int to) throws Exception{
		String sql =  "SELECT * FROM ( SELECT freeBoard.*, ROW_NUMBER() OVER (ORDER BY fb_date DESC) rn FROM freeBoard ) sub WHERE rn BETWEEN ? AND ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, from);
			stat.setInt(2, to);

			ArrayList<FreeboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()){
					int fb_id = rs.getInt("fb_id");
					String fb_user_name = rs.getString("fb_user_name");
					String fb_Title = rs.getString("fb_Title");
					String fb_write = rs.getString("fb_write");
					Timestamp fb_date = rs.getTimestamp("fb_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(fb_date);
					int view_count = rs.getInt("view_count");

					list.add(new FreeboardDTO(fb_id, fb_user_name, fb_Title, fb_write, regdate, view_count));
				}
				return list;
			}
		}
	}

	//자유 게시판 갯수 출력
	public int getFreeboardCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM freeBoard";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);
				ResultSet rs = stat.executeQuery()) {
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				return 0;
			}
		}
	}

	//자유 게시물 출력
	public ArrayList<FreeboardDTO> seletAllFreeboardPrint(String freeboardNum) throws Exception{
		String sql = "select * from freeBoard where fb_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, freeboardNum);

			ArrayList<FreeboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				if(rs.next()) {
					int fb_id = rs.getInt("fb_id");
					String fb_user_name = rs.getString("fb_user_name");
					String fb_Title = rs.getString("fb_Title");
					String fb_write = rs.getString("fb_write");
					Timestamp fb_date = rs.getTimestamp("fb_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(fb_date);
					int view_count = rs.getInt("view_count");

					list.add(new FreeboardDTO(fb_id, fb_user_name, fb_Title, fb_write, regdate, view_count));
				}
				return list;
			}
		}
	}

	//자유 게시물 댓글 출력
	public ArrayList<FreeboardComentDTO> seletAllFBComent(String prentNum) throws Exception{
		String sql = "select * from freeComment where fb_id = ? ORDER BY fc_date DESC";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, prentNum);

			ArrayList<FreeboardComentDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()) {
					int seq = rs.getInt("fc_id");
					int fb_id = rs.getInt("fb_id");
					String fc_user_name = rs.getString("fc_user_name");
					String fc_write = rs.getString("fc_write");
					Timestamp fb_date = rs.getTimestamp("fc_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(fb_date);

					list.add(new FreeboardComentDTO(seq, fb_id, fc_user_name, fc_write, regdate));
				}
			}
			return list;
		}
	}

	//댓글 갯수 카운트
	public int fCountComent(String parent_seq) throws Exception {
		String sql = "SELECT COUNT(*) AS cnt FROM freeComment WHERE fb_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, parent_seq);

			try(ResultSet rs = stat.executeQuery()){
				if(rs.next()) {
					int result = rs.getInt("cnt");
					return result;
				}else {
					return 0;
				}
			}
		}
	}

	//프리 게시판 view카운트
	public void fCount(int count, String seq) throws Exception {
		String sql = "update freeBoard set view_count = ? where fb_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, count);
			stat.setString(2, seq);
			stat.executeUpdate();
		}
	}

	//자유 게시판 글삭제
	public int deleteFreeBoard(String seq) throws Exception{
		String sql = "delete from freeBoard where fb_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql)){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//자유 게시판 댓글 삭제
	public int fComentDelete(String seq) throws Exception {
		String sql = "delete from freeComment where fc_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//자유 게시판 댓글 수정
	public int fComentUpdate(String seq, String text) throws Exception {
		String sql = "update freeComment set fc_write = ? where fc_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, text);
			stat.setString(2, seq);

			return stat.executeUpdate();
		}
	}

	//자유 게시판 댓글 입력
	public int fComentInsert(FreeboardComentDTO dto) throws Exception {
		String sql = "insert into freeComment values(freeComment_seq.NEXTVAL,?,?,?,?)";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, dto.getFb_id());
			stat.setString(2, dto.getFc_user_name());
			stat.setString(3, dto.getFc_write());
			stat.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

			return stat.executeUpdate();
		}
	}

	//문의 게시판 출력
	public ArrayList<QAboardDTO> seletAllQAboard(int from, int to) throws Exception{
		String sql =  "SELECT * FROM ( SELECT inquries.*, ROW_NUMBER() OVER (ORDER BY inqu_date DESC) rn FROM inquries ) sub WHERE rn BETWEEN ? AND ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, from);
			stat.setInt(2, to);

			ArrayList<QAboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()){
					int seq = rs.getInt("inqu_id");
					String write = rs.getString("inqu_write");
					String title = rs.getString("inqu_title");
					String nickname = rs.getString("inqu_user_name");
					Timestamp fb_date = rs.getTimestamp("inqu_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(fb_date);

					list.add(new QAboardDTO(0, seq, 0, write, title, nickname, regdate));
				}
				return list;
			}
		}
	}

	//문의 게시판 갯수 출력
	public int getQAboardCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM inquries";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);
				ResultSet rs = stat.executeQuery()) {
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				return 0;
			}
		}
	}

	//문의 게시판 댓글 갯수 카운트
	public int qCountComent(String parent_seq) throws Exception {
		String sql = "SELECT COUNT(*) AS cnt FROM inquries_comment WHERE inqu_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, parent_seq);

			try(ResultSet rs = stat.executeQuery()){
				if(rs.next()) {
					int result = rs.getInt("cnt");
					return result;
				}else {
					return 0;
				}
			}
		}
	}

	//문의 게시물 출력
	public ArrayList<QAboardDTO> seletAllQAboardPrint(String QAboardNum) throws Exception{
		String sql = "select * from inquries where inqu_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, QAboardNum);

			ArrayList<QAboardDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()){
					int seq = rs.getInt("inqu_id");
					String write = rs.getString("inqu_write");
					String title = rs.getString("inqu_title");
					String nickname = rs.getString("inqu_user_name");
					Timestamp fb_date = rs.getTimestamp("inqu_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(fb_date);

					list.add(new QAboardDTO(0, seq, 0, write, title, nickname, regdate));
				}
				return list;
			}
		}
	}

	//문의 게시물 댓글 출력
	public ArrayList<QAboardComentDTO> seletAllQBComent(String prentNum) throws Exception{
		String sql = "select * from inquries_comment where inqu_id = ? ORDER BY inqc_date DESC";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, prentNum);

			ArrayList<QAboardComentDTO> list = new ArrayList<>();
			try(ResultSet rs = stat.executeQuery();){
				while(rs.next()) {
					int seq = rs.getInt("inqc_seq");
					int inqu_id = rs.getInt("inqu_id");
					String inqc_write = rs.getString("inqc_write");
					Timestamp inqc_date = rs.getTimestamp("inqc_date"); 
					String regdate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(inqc_date);

					list.add(new QAboardComentDTO(seq, inqu_id, inqc_write, regdate));
				}
			}
			return list;
		}
	}
	
	//문의 게시판 댓글 여부 출력
	public ArrayList<String> selectComentWhether() throws Exception {
		String sql ="select i.inqu_id, count(c.inqu_id) as comment_count "
		           + "from inquries i left join inquries_comment c on i.inqu_id = c.inqu_id "
		           + "group by i.inqu_id order by i.inqu_id desc";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);
				ResultSet rs = stat.executeQuery();){
			
			ArrayList<String> list = new ArrayList<>();
			while(rs.next()) {
				String result = rs.getString("comment_count");
				
				list.add(result);
			}
			return list;
		}
	}
	
	//문의 게시판 글 삭제
	public int deleteQABoard(String seq) throws Exception{
		String sql = "delete from inquries where inqu_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql)){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}
	
	//문의 게시판 댓글 입력
	public int qaComentInsert(QAboardComentDTO dto) throws Exception {
		String sql = "insert into inquries_comment values(inquries_comment_seq.NEXTVAL,?,?,?)";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, dto.getInqu_id());
			stat.setString(2, dto.getInqc_write());
			stat.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));

			return stat.executeUpdate();
		}
	}
	
	//문의 게시판 댓글 삭제
	public int QAComentDelete(String seq) throws Exception {
		String sql = "delete from inquries_comment where inqu_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}
	
	//문의 게시판 댓글 수정
	public int QAComentUpdate(String seq, String text) throws Exception {
		String sql = "update inquries_comment set inqc_write = ? where inqc_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, text);
			stat.setString(2, seq);

			return stat.executeUpdate();
		}
	}
}
