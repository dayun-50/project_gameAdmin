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
import dto.FreeboardComentDTO;
import dto.FreeboardDTO;
import dto.GameboardComentDTO;
import dto.GameboardDTO;
import dto.QAboardComentDTO;
import dto.QAboardDTO;
import dto.UserDTO;


@WebServlet("*.AdminController")
public class AdminController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getRequestURI();
		AdminDAO dao = AdminDAO.getInstance();
		HttpSession session = request.getSession();
		response.setContentType("application/json; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

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
			}else if(cmd.equals("/userpage.AdminController")) { // 사용자 관리 페이지 이동
				String cpageStr = request.getParameter("cpage");
				if (cpageStr == null || cpageStr.isEmpty()) {
					response.sendRedirect(request.getRequestURI() + "?cpage=1");
					return;
				}
				int cpage = Integer.parseInt(cpageStr);

				// 전체 레코드 개수 가져오기
				int recordTotalCount = dao.getUserCount();

				ArrayList<UserDTO> list =
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

				UserDTO dto = dao.getUserPage(user_id);
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

			}else if(cmd.equals("/gameboard.AdminController")) { // 게임게시판 이동버튼 
				String cpageStr = request.getParameter("cpage");
				if (cpageStr == null || cpageStr.isEmpty()) {
					response.sendRedirect(request.getRequestURI() + "?cpage=1");
					return;
				}
				int cpage = Integer.parseInt(cpageStr);

				int recordTotalCount = dao.getGameboardCount();

				ArrayList<GameboardDTO> list =
						dao.seletAllGameboard(cpage*UsersConfig.RECORD_COUNT_PER_PAGE-(UsersConfig.RECORD_COUNT_PER_PAGE-1),
								cpage*UsersConfig.RECORD_COUNT_PER_PAGE);

				request.setAttribute("list", list);
				request.setAttribute("recordTotalCount", recordTotalCount);
				request.setAttribute("recordCountPerPage",UsersConfig.RECORD_COUNT_PER_PAGE);
				request.setAttribute("naviCountPerPage", UsersConfig.NABI_COUNT_PER_PAGE);
				request.setAttribute("currentPage", cpage);
				request.getRequestDispatcher("/admin/gameboard.jsp").forward(request, response);

			}else if(cmd.equals("/gameboardNum.AdminController")) { // 게임 게시물 출력
				String gameboardnum = request.getParameter("gameboardnum");
				ArrayList<GameboardDTO> list = dao.seletAllGameboardPrint(gameboardnum);
				ArrayList<GameboardComentDTO> comentList = dao.seletAllGBComent(gameboardnum);
				int comentCount = dao.countComent(gameboardnum);
				int count = list.get(0).getView_count();
				count += 1;
				dao.count(count, gameboardnum);

				request.setAttribute("viewCount", count);
				request.setAttribute("comentCount", comentCount);
				request.setAttribute("comentList", comentList);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/board/gameboardPage.jsp").forward(request, response);

			}else if(cmd.equals("/delGameboard.AdminController")) { // 게임 게시판 글 삭제
				String seq = request.getParameter("seq");
				int result = dao.deleteGameBoard(seq);

				response.getWriter().write(String.valueOf(result));

			}else if(cmd.equals("/comentInsert.AdminController")){ // 게임 게시판 댓글 입력
				String prent_seq = request.getParameter("seq");
				String coment = request.getParameter("coment");
				String nickname = "관리자";

				dao.comentInsert(new GameboardComentDTO(0, Integer.parseInt(prent_seq), 0, nickname, coment, ""));
				response.sendRedirect("/gameboardNum.AdminController?gameboardnum="+prent_seq);

			}else if(cmd.equals("/delGameboardComent.AdminController")) { // 게임 게시판 댓글 삭제
				String seq = request.getParameter("seq");

				int result = dao.comentDelete(seq);
				response.getWriter().write(String.valueOf(result));

			}else if(cmd.equals("/updatGameboardComent.AdminController")) { // 게임 게시판 댓글 수정
				String seq = request.getParameter("seq");
				String text = request.getParameter("text");

				int result = dao.comentUpdate(seq, text);
				response.getWriter().write(String.valueOf(result));

			}else if(cmd.equals("/freeboard.AdminController")) { // 자유게시판 이동버튼
				String cpageStr = request.getParameter("cpage");
				if (cpageStr == null || cpageStr.isEmpty()) {
					response.sendRedirect(request.getRequestURI() + "?cpage=1");
					return;
				}
				int cpage = Integer.parseInt(cpageStr);

				int recordTotalCount = dao.getFreeboardCount();

				ArrayList<FreeboardDTO> list =
						dao.seletAllFreeboard(cpage*UsersConfig.RECORD_COUNT_PER_PAGE-(UsersConfig.RECORD_COUNT_PER_PAGE-1),
								cpage*UsersConfig.RECORD_COUNT_PER_PAGE);

				request.setAttribute("list", list);
				request.setAttribute("recordTotalCount", recordTotalCount);
				request.setAttribute("recordCountPerPage",UsersConfig.RECORD_COUNT_PER_PAGE);
				request.setAttribute("naviCountPerPage", UsersConfig.NABI_COUNT_PER_PAGE);
				request.setAttribute("currentPage", cpage);
				request.getRequestDispatcher("/admin/freeboard.jsp").forward(request, response);

			}else if(cmd.equals("/freeboardNum.AdminController")) { // 자유 게시물 출력
				String freeboardnum = request.getParameter("freeboardnum");
				ArrayList<FreeboardDTO> list = dao.seletAllFreeboardPrint(freeboardnum);
				ArrayList<FreeboardComentDTO> comentList = dao.seletAllFBComent(freeboardnum);
				int comentCount = dao.fCountComent(freeboardnum);
				int count = list.get(0).getView_count();
				count += 1;
				dao.fCount(count, freeboardnum);

				request.setAttribute("viewCount", count);
				request.setAttribute("comentCount", comentCount);
				request.setAttribute("comentList", comentList);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/board/freeboardPage.jsp").forward(request, response);

			}else if(cmd.equals("/delFreeboard.AdminController")) { // 자유 게시판 글 삭제
				String seq = request.getParameter("seq");
				int result = dao.deleteFreeBoard(seq);

				response.getWriter().write(String.valueOf(result));

			}else if(cmd.equals("/delFreeboardComent.AdminController")) { // 자유 게시판 댓글 삭제
				String seq = request.getParameter("seq");

				int result = dao.fComentDelete(seq);
				response.getWriter().write(String.valueOf(result));
				
			}else if(cmd.equals("/updatFreeboardComent.AdminController")) { // 자유 게시판 댓글 수정
				String seq = request.getParameter("seq");
				String text = request.getParameter("text");

				int result = dao.fComentUpdate(seq, text);
				response.getWriter().write(String.valueOf(result));
				
			}else if(cmd.equals("/fComentInsert.AdminController")) { // 자유 게시판 댓글 입력
				String prent_seq = request.getParameter("seq");
				String coment = request.getParameter("coment");
				String nickname = "관리자";

				dao.fComentInsert(new FreeboardComentDTO(0, Integer.parseInt(prent_seq), nickname, coment, ""));
				response.sendRedirect("/freeboardNum.AdminController?freeboardnum="+prent_seq);
				
			}else if(cmd.equals("/QAboard.AdminController")) { // 문의게시판 이동버튼 (로그인 후 메인버튼)
				String cpageStr = request.getParameter("cpage");
				if (cpageStr == null || cpageStr.isEmpty()) {
					response.sendRedirect(request.getRequestURI() + "?cpage=1");
					return;
				}
				int cpage = Integer.parseInt(cpageStr);

				int recordTotalCount = dao.getQAboardCount();

				ArrayList<QAboardDTO> list =
						dao.seletAllQAboard(cpage*UsersConfig.RECORD_COUNT_PER_PAGE-(UsersConfig.RECORD_COUNT_PER_PAGE-1),
								cpage*UsersConfig.RECORD_COUNT_PER_PAGE);
				
				ArrayList<String> comentCount = dao.selectComentWhether();
				
				request.setAttribute("comentCount", comentCount);
				request.setAttribute("list", list);
				request.setAttribute("recordTotalCount", recordTotalCount);
				request.setAttribute("recordCountPerPage",UsersConfig.RECORD_COUNT_PER_PAGE);
				request.setAttribute("naviCountPerPage", UsersConfig.NABI_COUNT_PER_PAGE);
				request.setAttribute("currentPage", cpage);
				request.getRequestDispatcher("/admin/main.jsp").forward(request, response);
				
			}else if(cmd.equals("/QAboardnum.AdminController")) { // 문의 게시물 출력
				String qaboardnum = request.getParameter("qaboardnum");
				ArrayList<QAboardDTO> list = dao.seletAllQAboardPrint(qaboardnum);
				ArrayList<QAboardComentDTO> comentList = dao.seletAllQBComent(qaboardnum);
				int comentCount = dao.qCountComent(qaboardnum);

				request.setAttribute("adminNickname", "관리자");
				request.setAttribute("comentCount", comentCount);
				request.setAttribute("comentList", comentList);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/board/QAboradPage.jsp").forward(request, response);

			}else if(cmd.equals("/delQAboard.AdminController")) { // 문의 게시판 글 삭제
				String seq = request.getParameter("seq");
				int result = dao.deleteQABoard(seq);

				response.getWriter().write(String.valueOf(result));
				
			}else if(cmd.equals("/QAComentInsert.AdminController")) { // 문의 게시판 댓글 입력
				String prent_seq = request.getParameter("seq");
				String coment = request.getParameter("coment");

				dao.qaComentInsert(new QAboardComentDTO(0, Integer.parseInt(prent_seq), coment, ""));
				response.sendRedirect("/QAboardnum.AdminController?qaboardnum="+prent_seq);
				
			}else if(cmd.equals("/delQAboardComent.AdminController")) { // 문의 게시판 댓글 삭제
				String seq = request.getParameter("seq");

				int result = dao.QAComentDelete(seq);
				response.getWriter().write(String.valueOf(result));
			
			}else if(cmd.equals("/updatQAboardComent.AdminController")) { // 문의 게시판 댓글 수정
				String seq = request.getParameter("seq");
				String text = request.getParameter("text");

				int result = dao.QAComentUpdate(seq, text);
				response.getWriter().write(String.valueOf(result));
				
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
