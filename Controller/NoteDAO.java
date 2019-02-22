package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.NoteDiary;

public class NoteDAO {
	public static ArrayList<NoteDiary> dbArrayListDiary=new ArrayList<>();
	public static ArrayList<String> dateArrayList=new ArrayList<>();
	
	//01. 노트의 내용을 모두 등록하는 함수
	public static int insertNoteData(NoteDiary noteDiary) {
		//petinfotb2 에서 petno 를 찾아온다.
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
			}
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		//////// 노트내용 DB 삽입 sql 문 ////////
		StringBuffer insertNote=new StringBuffer();
		insertNote.append("insert into diarytb ");
		insertNote.append("(day,meal,pee,poo,brushing,play,memo,petno) ");
		insertNote.append("values ");
		insertNote.append("(?,?,?,?,?,?,?,?) ");
		
		
		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(insertNote.toString());
		
			pstmt.setString(1, noteDiary.getDay());
			pstmt.setString(2, noteDiary.getMeal());
			pstmt.setString(3, noteDiary.getPee());
			pstmt.setString(4, noteDiary.getPoo());
			pstmt.setString(5, noteDiary.getBrushing());
			pstmt.setString(6, noteDiary.getPlay());
			pstmt.setString(7, noteDiary.getMemo());
			pstmt.setInt(8,  petno);
			
			count=pstmt.executeUpdate();
			
			if(count==0) {
				RootController.callAlert("삽입 쿼리 실패: 노트 삽입 쿼리 실패. 점검바람");
				return count;
			}	
		} catch (SQLException e) {
			RootController.callAlert("삽입 실패: 노트데이터 삽입 실패. 점검바람");
			e.printStackTrace();
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
	
	//02. 다이어리테이블에서 전체 내용을 가져오는 함수.
	public static ArrayList<NoteDiary> getDiaryTotalData(){
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		////먼저 동물번호를 가져온다! 
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
			}
			
			String sql2="select day,meal,pee,poo,brushing,play,memo from diarytb where petno=?  ";
			pstmt=con.prepareStatement(sql2);
			
			pstmt.setInt(1, petno);

			rs=pstmt.executeQuery();
		
			while(rs.next()) {	// 사료,소변,대변,양치,교감,진료,메모
				NoteDiary noteDiary = new NoteDiary(
							rs.getString("day"), 
							rs.getString("meal"), 
							rs.getString("pee"), 
							rs.getString("poo"), 
							rs.getString("brushing"), 
							rs.getString("play"), 
							rs.getString("memo")	);
				dbArrayListDiary.add(noteDiary);
			}
			
		} catch (SQLException e1) {
			RootController.callAlert("노트데이터 불러오기 실패: 노트 데이터 불러오기 실패");
			e1.printStackTrace();
		}
		
		return dbArrayListDiary;
	}

	//03. 다이어리테이블에서 기록일만 불러와서 배열에 넣는 함수//////////////////////쓸지 말지 결정해야함.

	public static ArrayList<String> getDayfromDiarytb(){
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		////먼저 동물번호를 가져온다! 
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
				//System.out.println(petno); 	
			}
		
		String sql2= "select day from diarytb where petno=? ";
		pstmt=con.prepareStatement(sql2);
		
		pstmt.setInt(1, petno);

		rs=pstmt.executeQuery();
		while(rs.next()) {		
			dateArrayList.add(rs.getString(1));		
		}
	
		for(String day: dateArrayList) {
			System.out.println(day);
		}
		

		} catch (SQLException e1) {
			RootController.callAlert("노트데이터 불러오기 실패: 노트 데이터 불러오기 실패");
			e1.printStackTrace();
		}
		
		
	return dateArrayList;
	
	}
	
}
