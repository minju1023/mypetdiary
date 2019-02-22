package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;
import Model.Weight;

public class WeightDAO {	
	
	public static int petNo=0;
	
	//01. 체중정보를 등록하는 함수
	public static int insertWeightData(Weight weight) {
		//select 문으로 petno를 가져온다
		int petno=0;
	
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				
				petno= rs.getInt("petno");
				System.out.println(petno);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer insertWeight=new StringBuffer();
		insertWeight.append("insert into weighttb ");
		insertWeight.append("(petno,weight,recorddate) ");
		insertWeight.append("values ");
		insertWeight.append("(?,?,?) ");
		//1.2 데이터베이스 Connection 을 가져온다.

		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(insertWeight.toString());
	
			//1.4 쿼리문에 실제 데이터를 연결
			pstmt.setInt(1, petno);
			pstmt.setString(2, weight.getWeight());
			pstmt.setString(3, weight.getRecordate());
			
			count=pstmt.executeUpdate();
			
			if(count==0) {
			//	RootController.callAlert("삽입 쿼리 실패: 삽입쿼리 실패 점검바람");
				return count;
			}
	
		} catch (SQLException e) {
			//RootController.callAlert("삽입 실패: 데이터베이스 삽입 실패. 점검바람");
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {  pstmt.close(); }
				if (con != null) { 	con.close();	}
			} catch (SQLException e) {
				RootController.callAlert("자원닫기 실패: psmt,con 닫기 실패.");
			}
		}
		return count;
	}
	
	//02.체중정보 테이블에서 전체 내용을 모두 가져오는 함수
	public static ArrayList<Weight> getWeighttbTotalData(){
		ArrayList<Weight> wDBArrayList = new ArrayList<>();
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		try {
			String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {			
				petno= rs.getInt("petno");		
				//System.out.println("이름과 이메일로 가져온 동물의 번호는: "+petno);	//////////////// 콘솔에서 확인
				
			}	
			
			// 가져온 petno 로  weighttb 데이터를 또 가져온다.
			String sql2 ="select weight,recorddate from weighttb where petno=? " ;
			pstmt=con.prepareStatement(sql2);
			
			pstmt.setInt(1, petno);
			rs=pstmt.executeQuery();
		
			
			wDBArrayList.clear();
			while(rs.next()) {			
				Weight weight=new Weight( 					// 기록일자, 체중
						rs.getString("recorddate"), 
						rs.getString("weight") );			
				wDBArrayList.add(weight);	
			}		
		} catch (SQLException e) {	
			//RootController.callAlert("데이터 불러오기 실패: 데이터 불러오기가 실패했습니다.");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) { pstmt.close(); }
				if(con != null) {con.close(); }
			} catch (SQLException e) {
			//RootController.callAlert("자원닫기 실패: pstmt,con 닫기 실패");
			}
		}

		return wDBArrayList;
	}
	
	
	 public static int deleteWeightData(int petno) { // petno 에 DiaryDAO.petno 를 넣어줘야한다. 
		 	Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int count=0;			
		 try {
				String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
				con=DBUtility.getConection();
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, RootController.EMAIL);
				pstmt.setString(2, DiaryController.petname);
		
				rs=pstmt.executeQuery();
				while(rs.next()) {			
					petNo= rs.getInt("petno");
					System.out.println(petNo);
				}		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
		 String delWeightData= "delete from weighttb where petno= ? and recorddate=? ";
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(delWeightData);
			pstmt.setInt(1, petNo);	
			pstmt.setString(2, DiaryController.selectedRecordDate); 	// 	wTableView.getSelectionModel().getSelectedItem().getRecordDate()  
		
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
	
	 //03. 체중정보를 수정하고 그 값을 DB 에서도 수정하도록 하는 함수
	 public static int updateWeightData(Weight weight ) {
		 int count =0;
			 Connection con=null;
			 PreparedStatement pstmt=null;
			 ResultSet rs=null;
			 
			 String updateSql="update weighttb set weight=? , recorddate=? where recorddate=? " ;
			 
			
		 try {	
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(updateSql);
			
			pstmt.setDouble(1, Double.parseDouble(weight.getWeight()) );
			pstmt.setString(2, weight.getRecordate());
			pstmt.setString(3, DiaryController.selectedRecordDate);
	
			count=pstmt.executeUpdate();
			
		if(count==0) {
				System.out.println("수정 쿼리 실패: 수정 쿼리문실패 점검바람");
				return count;
		}
		 
		 } catch (SQLException e) {
			 System.out.println("수정 실패: 데이터베이스 수정 실패했어요. 점검바랍니다.");
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

