package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;
import Model.Profile;

public class ProfileDAO {
	public static ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
	
	//01. 동물정보를 등록하는 함수
	public static int insertJoinData(Profile profile) {
		//1.1 데이터베이스에 petinfotb 에 입력하는 쿼리문
		StringBuffer insertJoin=new StringBuffer();
		insertJoin.append("insert into petinfotb2 ");
		insertJoin.append("(petno,petname,petkind,petbirth,petgender,petvaccine,email,imgpath) ");
		insertJoin.append("values ");
		insertJoin.append("(null,?,?,?,?,?,?,?) ");
		//1.2 데이터베이스 Connection 을 가져와야한다.
		Connection con=null;
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt=null;
		int count=0;
		
		ArrayList<Join> join =JoinDAO.getJoinMembershipEmailData();
		for( Join j: join) {
	
			System.out.println(j.getEmail());
		}
		
		try {
			con=DBUtility.getConection();
			psmt=con.prepareStatement(insertJoin.toString());
			//1.4 쿼리문에 실제 데이터를 연결한다.
			//pr=join.email;
			psmt.setString(1, profile.getPetname() );
			psmt.setString(2, profile.getPetkind() );
			psmt.setString(3, profile.getPetbirth() );
			psmt.setString(4, profile.getPetgender() );
			psmt.setString(5, profile.getPetvaccine() );		
			psmt.setString(6, RootController.EMAIL);//////////
			psmt.setString(7, profile.getImgpath() );
			

			//1.5 실제 데이터를 연결한 쿼리문을 실행하라
			count=psmt.executeUpdate();			
			if(count==0) {
				RootController.callAlert("삽입 쿼리 실패: 삽입쿼리 실패 점검바람");
				return count;
			}
		
		} catch (SQLException e) {
			RootController.callAlert("삽입 실패: 데이터베이스 삽입 실패. 점검바람");
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) { psmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (Exception e) {
				RootController.callAlert("자원닫기 실패: psmt,con 닫기 실패.");
			}
			
			}

		return count;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//02. 동물정보를 가져오는 함수 ____ 종류, 이름,성별,생일
	

	public static ArrayList<Profile> getPetinfoDataToDB(){

		//2.1 db 의 joinmembershiptb2 에서 이메일 칼럼의 내용만 다 가져오는 쿼리문
		String selectPetInfo= "select petkind, petname, petgender, petbirth "+  "from petinfotb2 where email=? ";
		//2.2 데이터베이스 Connection을 가져와야한다.
		Connection con=null;
		//2.3 쿼리문을 실행해야할 Statememt 를 만들어야한다.
		PreparedStatement psmt =null;
		//2.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기객체
		ResultSet rs =null;
		
		
		try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectPetInfo);
				psmt.setString(1, RootController.EMAIL);
				
				rs=psmt.executeQuery();
				
				
				if(rs == null) {
					RootController.callAlert("동물프로필 가져오기 실패 : 쿼리문 실패 점검바람");
					return null;
				}
				dbArrayListProfile.clear(); ///////////// 
				while(rs.next()) {
					Profile petProfile=new Profile(
							rs.getString("petkind"),
							rs.getString("petname"),
							rs.getString("petgender"),
							rs.getString("petbirth")
						);
					
						System.out.println("aaa@naver.com 이용자의 동물은"+rs.getString("petname"));/////////////
						System.out.println(rs.getString("petgender"));////////////////
						
						dbArrayListProfile.add(petProfile);
				}
		} catch (SQLException e) {
				RootController.callAlert("dbArrayListJoin 삽입 실패:  실패 점검바람");
			} finally {
				// 2.6 자원객체를 닫아야한다.
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
			return dbArrayListProfile;
		}
	
	
	
	
	
	
	//03. 테이블에서 선택한 동물 프로필을 삭제하는 함수 ('삭제하기' 버튼을 누를때 쓴다. -- 이름으로 검색해서 지우자! )
	public static int deletePetinfoData(String petname) {
		String deletePet = "delete from petinfotb2 where petname= ? ";
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(deletePet);
			pstmt.setString(1, petname);
			
			count=pstmt.executeUpdate();
			if(count ==0) {
				System.out.println("delete실패: delete쿼리문 실패 점검바람");
				return count;
			}
			

		} catch (SQLException e) {
			System.out.println("delete실패: 데이터베이스 delete 실패했음. 점검바람");
		} finally {
			try {
				if(pstmt != null) { pstmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (Exception e) {
				RootController.callAlert("자원닫기 실패: psmt,con 닫기 실패.");
			}
			
			}

		return count;	
		
	}
	
}
