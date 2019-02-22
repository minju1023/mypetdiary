package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtility {
	public static Connection getConection() {
		// 1. MySqp database class 로드한다.
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 2. 주소,아이디,비밀번호를 통해서 접속요청한다.
			con = DriverManager.getConnection("jdbc:mysql://localhost/petdiarydb", "root", "123456");
		//	System.out.println("연결 성공: 데이터베이스 연결에 성공하였습니다.");

		} catch (Exception e) {
			//RootController.callAlert("연결 실패: 데이터베이스 연결에 실패하였습니다.");
			return null;
		}

		return con;

	}
}
