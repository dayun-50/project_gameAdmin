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
import dto.GameboardComentDTO;
import dto.GameboardDTO;
import dto.userDTO;

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
	public ArrayList<userDTO> userList() throws Exception {
		String sql = "select * from users";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){

			ArrayList<userDTO> list = new ArrayList<>();
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

					list.add(new userDTO(id, "", nickname, name, phone, email, regdate, agree));
				}
				return list;
			}
		}
	}

	//유저목록...네비로만들라니까 계속 까먹네 아이고야
	public ArrayList<userDTO> selectFromTo(int from, int to) throws Exception{
		String sql = "SELECT * FROM ( SELECT users.*, ROW_NUMBER() OVER (ORDER BY user_join_date DESC) rn FROM users ) sub WHERE rn BETWEEN ? AND ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, from);
			stat.setInt(2, to);

			ArrayList<userDTO> list = new ArrayList<>();
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

					list.add(new userDTO(id, "", nickname, name, phone, email, regdate, agree));
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
	public userDTO getUserPage(String user_id) throws Exception {
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

					return new userDTO(id, "", nickname, name, phone, email, regdate, agree);
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
		String sql = "select * from Game_Coment where game_parent_seq = ?";
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

	//게시판 view카운트
	public void count(int count, String seq) throws Exception {
		String sql = "update game_board set view_count = ? where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setInt(1, count);
			stat.setString(2, seq);
			stat.executeUpdate();
		}
	}


	//댓글 입력
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

	//댓글 삭제
	public int comentDelete(String seq) throws Exception {
		String sql = "delete from Game_Coment where game_coment_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//댓글 수정
	public int comentUpdate(String seq, String text) throws Exception {
		String sql = "update Game_Coment set game_coment = ? where game_coment_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql);){
			stat.setString(1, text);
			stat.setString(2, seq);

			return stat.executeUpdate();
		}
	}	

	//게시물 삭제
	public int deleteGameBoard(String seq) throws Exception{
		String sql = "delete from game_board where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql)){
			stat.setString(1, seq);

			return stat.executeUpdate();
		}
	}

	//게시물 수정
	public int updateGameBoard(String seq, String text) throws Exception  {
		String sql = "update game_board set gamecoment = ? where game_seq = ?";
		try(Connection con = this.getConnection();
				PreparedStatement stat = con.prepareStatement(sql)){
			stat.setString(1, text);
			stat.setString(2, seq);

			return stat.executeUpdate();
		}
	}
}
