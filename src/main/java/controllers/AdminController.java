package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDAO;
import dto.AdminDTO;


@WebServlet("*.AdminController")
public class AdminController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getRequestURI();
		AdminDAO dao = AdminDAO.getInstance();
		HttpSession session = request.getSession();

		try {
			if(cmd.equals("/adminLogin.AdminController")) { // 관리자 로그인
				String id = request.getParameter("id");
				String pw = request.getParameter("pw");
				String pw1 = dao.encrypt(pw);
				int result = dao.AdminLogin(new AdminDTO(id,pw1));
				System.out.println(result);
				System.out.println(pw1);
				if(result == 1) {
					session.setAttribute("loginId", id);
					response.getWriter().write(String.valueOf(result));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생 // AdminController");

		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
