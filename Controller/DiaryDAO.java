package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.NoteDiary;
import Model.Profile;

public class DiaryDAO {
	public static ArrayList<Profile> petinfoList = new ArrayList<>();
	public static ArrayList<NoteDiary> dbArrayListDiary = new ArrayList<>();
	public static int count = 0;
	public static int petno=0;
	// 1.동물의 정보에서 이름,성별,생일,종류만 가져와서 체중관리 탭에 주기위한 함수.
	public static ArrayList<Profile> getPetDatabyPetname(){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		
		//먼저 선택된 동물의 이름을 가져온다.
		//String petname=
		
		
		
		String sql = "select * from petinfotb2 where petname=? "; 
		
		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, DiaryController.petname);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Profile profile=new Profile(
						rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(8)
						);
				petinfoList.add(profile);	
			}
				
			
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return petinfoList;
	}
	
	//2. 다이어리테이블에서 내용을 가져오는 함수
	public static ArrayList<NoteDiary> getDiaryData() {
	/////////////////////////////////////petno 와 day 로 DB 에서 노트내용을 가져오자.////////////////////////////////////////////////
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		////먼저 동물번호를 가져온다!  __ 로그인시 입력한 email 과 해당 동물이름으로 petno 를 가져온다.
		String sql = "select petno " + "from petinfotb2 where email=? and petname=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				petno = rs.getInt("petno");
				System.out.println(petno); ///////// 콘솔/////////
			}
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sql2 = "select day,meal,pee,poo,brushing,play,memo,count(memo) from diarytb where petno=? and day=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql2);

			pstmt.setInt(1, petno);
			pstmt.setString(2, DiaryController.noteDay);

			rs = pstmt.executeQuery();

			System.out.println(DiaryController.noteDay + "잘 될것인가!"); ////////// 콘솔확인

			while (rs.next()) {
				NoteDiary noteDiary = new NoteDiary(rs.getString("day"), rs.getString("meal"), rs.getString("pee"),
						rs.getString("poo"), rs.getString("brushing"), rs.getString("play"), rs.getString("memo"));

				count = rs.getInt(8);
				dbArrayListDiary.add(noteDiary);
			}

		} catch (SQLException e1) {
			RootController.callAlert("노트데이터 불러오기 실패: 노트 데이터 불러오기 실패");
			e1.printStackTrace();
		} finally {
				// 자원객체를 닫아야한다.
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				RootController.callAlert("자원닫기 실패 : psmt,con 닫기실패");
			}
		}

		return dbArrayListDiary;
	}
	
	
	
	//3. 다이어리테이블에서 이전달의 노트내용을 가져오기 위한 함수
	public static ArrayList<NoteDiary> getPreviousDiaryData( ) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		////먼저 동물번호를 가져온다!  __ 로그인시 입력한 email 과 해당 동물이름으로 petno 를 가져온다.
		String sql = "select petno " + "from petinfotb2 where email=? and petname=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				petno = rs.getInt("petno");
				System.out.println(petno); ///////// 콘솔/////////
			}
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sql2 = "select day,meal,pee,poo,brushing,play,memo,count(memo) from diarytb where petno=? and day=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql2);

			pstmt.setInt(1, petno);
			pstmt.setString(2, DiaryController.previousMonthDate);

			rs = pstmt.executeQuery();
			//System.out.println(petno);
		
			while (rs.next()) {
				NoteDiary noteDiary = new NoteDiary(rs.getString("day"), rs.getString("meal"), rs.getString("pee"),
						rs.getString("poo"), rs.getString("brushing"), rs.getString("play"), rs.getString("memo"));

				count = rs.getInt(8);
				dbArrayListDiary.add(noteDiary);
			}

		} catch (SQLException e1) {
			System.out.println("노트데이터 불러오기 실패: 노트 데이터 불러오기 실패");
			e1.printStackTrace();
		} finally {
// 자원객체를 닫아야한다.
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("자원닫기 실패 : psmt,con 닫기실패");
			}
		}

		return dbArrayListDiary;
	}

}
