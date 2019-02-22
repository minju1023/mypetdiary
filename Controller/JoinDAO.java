package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;


public class JoinDAO {
	public static ArrayList<Join> dbArrayListJoin=new ArrayList<>();
	
	//01. 회원정보를 등록하는 함수
	public static int insertJoinData(Join join) {
		//1.1 데이터베이스에 joinmembershiptb 에 입력하는 쿼리문
		StringBuffer insertJoin=new StringBuffer();
		insertJoin.append("insert into joinmembershiptb ");
		insertJoin.append("(email,password,name,birth,gender,nickname,phone) ");
		insertJoin.append("values ");
		insertJoin.append("(?,?,?,?,?,?,?) ");
		//1.2 데이터베이스 Connection 을 가져와야한다.
		Connection con=null;
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt=null;
		int count=0;
		
		try {
			con=DBUtility.getConection();
			psmt=con.prepareStatement(insertJoin.toString());
			//1.4 쿼리문에 실제 데이터를 연결한다.
			
			psmt.setString(1, join.getEmail());
			psmt.setString(2, join.getPassword() );
			psmt.setString(3, join.getName() );
			psmt.setString(4, join.getBirth());
			psmt.setString(5, join.getGender() );
			psmt.setString(6, join.getNickname() );
			psmt.setString(7, join.getPhone() );

			//1.5 실제 데이터를 연결한 쿼리문을 실행하라
			count=psmt.executeUpdate();			
			if(count==0) {
				System.out.println("삽입 쿼리 실패: 삽입쿼리 실패 점검바람");
				return count;
			}
		
		} catch (SQLException e) {
			System.out.println("삽입 실패: 데이터베이스 삽입 실패. 점검바람");
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) { psmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (SQLException e) {
				System.out.println("자원닫기 실패: psmt,con 닫기 실패.");
			}
			
			}

		return count;
	}

	
	//02. 테이블에서 전체내용을 모두 가져오는 함수
	public static ArrayList<Join> getJoinMembershipTotalData(){

		//2.1 데이터베이스 학생테이블에 있는 레코드를 모두 가져오는 쿼리문
		String selectMember = "select * from joinmembershiptb ";
		// 2.2 데이터베이스 Connection 을 가져와야 한다.
		Connection con = null;
		// 2.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt = null;
		// 2.4. 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기객체 ___ResultSet
		ResultSet rs=null;
		
		
		try {
			con = DBUtility.getConection();
			psmt = con.prepareStatement(selectMember);


			// 1.5 쿼리문 실제데이터를 연결한 쿼리문을 실행하라(번개모양 아이콘을 눌러라)
			//executeQuery();  쿼리문을 실행해서 결과를 가져올때 사용하는 번개문
			//executeUpdate() 는 쿼리문을 실행해서 테이블에 저장을 할 때 사용하는 번개문
			rs= psmt.executeQuery(); 

			if (rs==null) {
				System.out.println("select 실패 : select쿼리문 실패. 점검바람");
				return null;
			}
			
			while(rs.next()) {
				Join join=new Join(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7)		
						);
				dbArrayListJoin.add(join);
			}		
		} catch (SQLException e) {
			System.out.println("삽입 실패 : 데이터베이스 삽입에 실패했습니다. 점검바람");
			e.printStackTrace();
		} finally {
			// 1.6 자원객체를 닫아야한다.
			try {
				
				if (psmt != null) { psmt.close(); }
				if (con != null) { con.close(); }
		
			} catch (SQLException e) {
				System.out.println("자원닫기 실패 : psmt,con 닫기실패");
			}
		}	
		return dbArrayListJoin;
	}	

	
	
	
	
	////////////////03. joinmembershiptb2 에서 이메일 데이터만 가져오는 함수( 로그인때 이메일 비교를 위해서______안씁니다._________)///////////////
	
	public static ArrayList<Join> getJoinMembershipEmailData(){

	//1.1 db 의 joinmembershiptb2 에서 이메일 칼럼의 내용만 다 가져오는 쿼리문
	String selectEmail= "select email, password  from joinmembershiptb ";
	
	//1.2 데이터베이스 Connection을 가져와야한다.
	Connection con=null;
	
	//1.3 쿼리문을 실행해야할 Statememt 를 만들어야한다.
	PreparedStatement psmt =null;
	
	//1.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기객체
	ResultSet rs =null;
	
	
	try {
			con = DBUtility.getConection();
			psmt = con.prepareStatement(selectEmail);
			
			rs=psmt.executeQuery();
			if(rs == null) {
				RootController.callAlert("email select 실패 : select email 쿼리문 실패 점검바람");
				return null;
			}
			while(rs.next()) {
				Join join = new Join(rs.getString(1));
				dbArrayListJoin.add(join);
			}
	} catch (SQLException e) {
			RootController.callAlert("dbArrayListJoin 삽입 실패:  실패 점검바람");
		} finally {
			// 1.6 자원객체를 닫아야한다.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				RootController.callAlert("자원닫기 실패 : psmt,con 닫기실패");
			}
		}
		return dbArrayListJoin;

	}

}
