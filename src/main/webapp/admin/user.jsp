<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>   
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: black;
            /* body 전체 배경 */
            flex-direction: column;
            /* 세로 배치 */
        }

        .container {
            flex: 1;
            /* 남은 공간을 채워 footer를 아래로 밀기 */
            display: flex;
            flex-direction: column;
            justify-content: center;
            /* 기존 중앙 정렬 유지 */
            align-items: center;
            max-width: 600px;
            min-height: 600px;
            background-color: black;
            position: relative;
            z-index: 1;
            /* container 위로 내용 표시 */
        }

        .top {
    width: 600px; /* 화면 전체 너비 */
    height: 100px;
    border: 1px solid black;
    background: linear-gradient(135deg, rgba(163, 163, 163, 0.7), rgba(54, 74, 146, 0.5));
    margin-bottom: 5px;
    display: flex;           /* 글씨 중앙 정렬을 위해 flex 사용 */
    justify-content: center; /* 가로 중앙 */
    align-items: center;     /* 세로 중앙 */
}
.top h1 {
    margin: 0;               /* 불필요한 margin 제거 */
    color: white;
    font-family: 'Montserrat', sans-serif;
}

        .control {
            height: 80px;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 5px;
        }

        .box {
            width: 600px;
            height: 40px;
            display: flex;
            background: linear-gradient(135deg, #b01cbe, #18a393);
        }

        .box button {
            flex: 1;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background: transparent;
            color: white;
            font-weight: bold;
            font-size: 15px;
            font-family: 'Lato', sans-serif;
            cursor: pointer;
        }

        .control2 {
            height: 80px;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 5px;
        }

        .box2 {
            width: 600px;
            height: 40px;
            display: flex;
            background: linear-gradient(135deg, #18a393, #b01cbe);
        }

        .box2 button {
            flex: 1;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background: transparent;
            color: white;
            font-weight: bold;
            font-size: 15px;
            font-family: 'Lato', sans-serif;
            cursor: pointer;
        }

        .search {
            height: 50px;
            display: flex;
            align-items: center;
            padding-left: 1px;
            margin-bottom: 5px;
            
        }

        .adminsearch input {
            border: none;
            border-bottom: 1px solid #00ffff;
            background: transparent;
            color: white;
            font-size: 16px;
            width: 315px;
            margin-left: 10px;
            padding: 5px 0;
        }

        .search label {
            font-weight: bold;
            color: white;
            margin: 15px 0 0 20px;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            color: white;
            font-family: 'Montserrat', sans-serif;
            text-align: center;
            margin-bottom: 10px;
        }

        .table th {
            font-weight: bold;
            padding: 12px;
        }

        .table td {
            padding: 10px;
            border-bottom: 1px solid #00ffff;
        }

        .table input {
            background: transparent;
            border: none;
            border-bottom: 1px solid #00ffff;
            color: white;
            font-size: 14px;
            width: 90%;
            text-align: center;
        }

        .table2 {
            width: 100%;
            border-collapse: collapse;
            color: white;
            font-family: 'Montserrat', sans-serif;
            text-align: center;
            margin-bottom: 10px;
        }

        .table2 th {
            font-weight: bold;
            padding: 12px;
        }

        .table2 td {
            padding: 10px;
            border-bottom: 1px solid #00ffff;
        }

        .table2 input {
            background: transparent;
            border: none;
            border-bottom: 1px solid #00ffff;
            color: white;
            font-size: 14px;
            width: 90%;
            text-align: center;
        }

        .star {
            position: fixed;
            width: 2px;
            height: 2px;
            background: white;
            border-radius: 50%;
            animation: twinkle 3s infinite ease-in-out;
            z-index: 0;
            /* container 뒤로 별 위치 */
        }

        @keyframes twinkle {

            0%,
            100% {
                opacity: 0.2;
            }

            50% {
                opacity: 1;
            }
        }

        .searchbutton {
            background: linear-gradient(135deg, #b01cbe, #18a393);
            color: white;
            border: none;
            padding: 8px 15px;
            font-weight: bold;
            font-size: 14px;
            border-radius: 5px;
            /* 오른쪽 둥글게 */
            cursor: pointer;
            transition: all 0.3s ease;
            margin-left: 15px;
        }

        .searchbutton:hover {
            filter: brightness(1.2);
            /* 살짝 밝아지게 */
            transform: translateY(-1px);
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin: 20px 0 80px 0;
            /* 표와 footer 사이 간격 */
            font-family: 'Montserrat', sans-serif;
        }

        .pagination a {
            color: white;
            background: #222;
            padding: 8px 12px;
            margin: 0 4px;
            text-decoration: none;
            border-radius: 5px;
            border: 1px solid #00ffff;
            transition: all 0.3s ease;
            line-height: 20px;
            /* 링크의 내부 텍스트 세로 기준 */
        }

        .pagination a:hover {
            background: #00ffff;
            color: black;
        }

        .pagination a.active {
            background: #b01cbe;
            border-color: #b01cbe;
            font-weight: bold;
        }

        .pagination-wrapper {
            display: flex;
            justify-content: center;
            /* 페이지네이션은 왼쪽, 글쓰기 버튼은 오른쪽 */
            align-items: flex-start;
            margin: 20px 0 80px 0;
            gap: 20px;
        }

        .write-btn {
            background: linear-gradient(135deg, #b01cbe, #18a393);
            color: white;
            border: none;
            padding: 8px 12px;
            font-weight: bold;
            line-height: 20px;
            /* 링크의 내부 텍스트 세로 기준 */
            font-size: 15px;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 20px;
        }

        .write-btn:hover {
            filter: brightness(1.2);
            transform: translateY(-1px);
        }

        .footer {
            width: 100%;
            background-color: #111;
            /* 어두운 배경 */
            color: #ccc;
            /* 글자 색상 */
            font-family: 'Montserrat', sans-serif;
            /* border-top: 1px solid orangered;    위쪽 경계선 */

            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            /* 글자를 아래쪽으로 정렬 */
            align-items: center;
            /* 왼쪽 정렬 */
            padding: 10px 20px;
            /* 위/아래 여백 조절, 글자와 경계선 간격 */
        }

        .footer p {
            margin: 2px 0;
            /* 문단 간 간격 최소화 */
            font-size: 12px;
        }
    </style>
</head>
<body>
 <div class="container">

        <div class="control">
            <div class="box">
                <button id="userpage">사용자 관리</button>
                <button id="boardpage">게시물 관리</button>
            </div>
        </div>

        <div class="top">
            <h1>사용자 관리</h1>
        </div>

      
        <div class="search">
            <label for="adminsearch"><i class="fas fa-search"></i></label>
            
            <div class="adminsearch">
                <input type="text" id="adminsearch" name="adminsearch" placeholder="검색할 유저 닉네임 입력">
            </div>
            <button class="searchbutton">검색</button>
            
        </div>

        <table class="table2" id="table2">
            <tr>
            	<th></th>
                <th>ID</th>
                <th>닉네임</th>
                <th>날짜</th>
            </tr>
           <c:forEach var="user" items="${list}" varStatus="status">
           	<tr class="user-row" data-userid="${user.user_id}">
           		<td>${status.index + 1 }</td>
           		<td>${user.user_id }</td>
           		<td>${user.user_nickname }</td>
           		<td>${user.user_date }</td>
           	</tr>
           </c:forEach>
            
        </table>

        <div class="pagination-wrapper">
            <div  class="pagination" id="pageNavi"></div>
        </div>


    </div>

    <script>
        // 별 생성
        const colors = ['#ffffff', '#aabbff', '#ffb3ff', '#b3ffea', '#ffd9b3']; // 은하수 느낌 색상
        for (let i = 0; i < 300; i++) {
            const s = document.createElement('div');
            s.className = 'star';
            s.style.top = Math.random() * 100 + 'vh';
            s.style.left = Math.random() * 100 + 'vw';
            s.style.width = s.style.height = (Math.random() * 2 + 1) + 'px'; // 크기 랜덤
            s.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
            s.style.animationDuration = (2 + Math.random() * 3) + 's';
            document.body.appendChild(s);
        }
        
        let recordTotalCount = parseInt("${recordTotalCount}");
		let recordCountPerPage = parseInt("${recordCountPerPage}");
		let naviCountPerPage = parseInt("${naviCountPerPage}");
		let currentPage = parseInt("${currentPage}");

		let pageTotalCount = Math.ceil(recordTotalCount / recordCountPerPage);
		if(currentPage < 1) {
			currentPage=1;
		}else if(currentPage > pageTotalCount) {
			currentPage = pageTotalCount;
		}
		
		let startNavi = Math.floor((currentPage - 1) / naviCountPerPage)
				* naviCountPerPage + 1;
		
		let endNavi = startNavi + (naviCountPerPage - 1);
		if (endNavi > pageTotalCount)
			endNavi = pageTotalCount;

		let html = "";
		let needPrev = true;
		let needNext = true;
		
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}

		if (needPrev) {
			html += "<a href='/userpage.AdminController?cpage=" + (startNavi - 1) + "'>< </a>";
	      }

	      for (let i = startNavi; i <= endNavi; i++) {
	    	  html += "<a href='/userpage.AdminController?cpage=" + i + "'>" + i + "</a> ";
	      }

	      if (needNext) {
	    	  html += "<a href='/userpage.AdminController?cpage=" + (endNavi + 1) + "'>> </a>";
	      }
	    
		document.getElementById("pageNavi").innerHTML = html;
		
		$(document).on("click", ".user-row", function() { //유저 상세 페이지 이동
		    let userId = $(this).data("userid");
		    window.location.href = "/userDetail.AdminController?userId=" + userId;
		});
		
		// ✅ 실시간 검색 함수
	    function filterTable(keyword) {
	        $("#table2 tr").each(function (index) {
	            if (index === 0) return; // 첫 번째 행(헤더)은 건너뜀

	            let rowText = $(this).text(); // 행 전체 텍스트 (ID, 닉네임, 날짜 모두 포함)
	            if (rowText.includes(keyword)) {
	                $(this).show();
	            } else {
	                $(this).hide();
	            }
	        });
	    }

	    // input 입력 시 실시간 검색
	    $("#adminsearch").on("input", function () {
	        let keyword = $(this).val().trim();
	        filterTable(keyword);
	    });
	    // 누르면 game_project_game/game/game.Main.jsp로 이동!!!
	    $(".box button:contains('사이트 바로가기')").on("click", function() {
	        window.location.href = "/project_game/game/gameMain.jsp";
	    });
	    // 누르면 main.jsp로 이동!!
	    $(".box button:contains('게시물 관리')").on("click", function() {
	    	window.location.href = "<%= request.getContextPath() %>/admin/main.jsp";
	    });
	    
	    $("#boardpage").on("click", function(){ //게시판관리 이동페이지
	    	window.location.href = "/QAboard.AdminController";
	    });
	    </script>

    <footer class="footer">
        <p>(주)자바 스프링 리액트로 완성하는 클라우드 활용 풀스택 개발 | 대표이사 조성태 | 주소 : 서울 관악구 봉천로 227 보라매샤르망 5층 한국정보교육원 | 전화 : 010-9006-2139 |
            Fax:02-856-9742</p>
        <p>E-mail : bedivere219@gmail.com | 사업자 등록번호 : 202-506-09 | 개인정보보호책임자 : 김선경 (kyoungwon199@naver.com)</p>
        <p>&copy; 2025 Your Company. 한국정보교육원은 항상 여러분과 함께합니다.</p>
        <p>문이 : 010-9006-2139</p>
    </footer>
</body>
</html>