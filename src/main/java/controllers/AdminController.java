package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import commons.UsersConfig;
import dao.AdminDAO;
import dto.AdminDTO;
import dto.userDTO;


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

				if(result == 1) {
					session.setAttribute("loginId", id);
					response.getWriter().write(String.valueOf(result));
				}
			}else if(cmd.equals("/adminMain.AdminController")) { // 로그인 후 관리페이지 이동
				response.sendRedirect("/admin/main.jsp");
				
			}else if(cmd.equals("/userpage.AdminController")) { // 사용자 관리 페이지 이동
				String cpageStr = request.getParameter("cpage");
				if (cpageStr == null || cpageStr.isEmpty()) {
					response.sendRedirect(request.getRequestURI() + "?cpage=1");
					return;
				}
				int cpage = Integer.parseInt(cpageStr);

			    // 전체 레코드 개수 가져오기
			    int recordTotalCount = dao.getUserCount();

			    ArrayList<userDTO> list =
			            dao.selectFromTo(cpage*UsersConfig.RECORD_COUNT_PER_PAGE-(UsersConfig.RECORD_COUNT_PER_PAGE-1),
			                             cpage*UsersConfig.RECORD_COUNT_PER_PAGE);
			    
				request.setAttribute("list", list);
				request.setAttribute("recordTotalCount", recordTotalCount);
				request.setAttribute("recordCountPerPage",UsersConfig.RECORD_COUNT_PER_PAGE);
				request.setAttribute("naviCountPerPage", UsersConfig.NABI_COUNT_PER_PAGE);
				request.setAttribute("currentPage", cpage);
				request.getRequestDispatcher("/admin/user.jsp").forward(request, response);
			
			}else if(cmd.equals("/userDetail.AdminController")) { //유저 상세페이지
				String user_id = request.getParameter("userId");
				
				userDTO dto = dao.getUserPage(user_id);
				String black = dao.blackUser(user_id);
				
				if(black != "") {
					request.setAttribute("blackUser", "O ) "+black);
				}else {
					request.setAttribute("blackUser", black);
				}
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/users/usersPage.jsp").forward(request, response);
			
			}else if(cmd.equals("/inBlackListe.AdminController")) { // 블랙리스트 등록
				String user_id = request.getParameter("userId");
				String reason = request.getParameter("reason");
				int resutl = dao.blackInsert(user_id, reason);
				
				request.setAttribute("userId", user_id);
				response.getWriter().write(String.valueOf(resutl));
				
			}else if(cmd.equals("/dleBlackListe.AdminController")) { // 블랙리스트 해제
				String user_id = request.getParameter("userId");
				int resutl = dao.blackDelete(user_id);
				
				request.setAttribute("userId", user_id);
				response.getWriter().write(String.valueOf(resutl));
			
			}else if(cmd.equals("/serchUser_Nicname.AdminController")) { // 유저 닉네임 검색
				String user_nickname = request.getParameter("adminsearch");
				
				
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
