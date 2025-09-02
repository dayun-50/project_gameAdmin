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
	
}
