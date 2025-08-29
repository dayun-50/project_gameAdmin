package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dto.AdminDTO;

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
	
	
	
}
