package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Hospital;
import Model.Join;

public class HospitalDAO {

	//01. 테이블에서 전체내용을 모두 가져오는 함수
		public static ArrayList<Hospital> getHospitaltbTotalData(){
			ArrayList<Hospital> dbArrayListHospital=new ArrayList<>();
			//1.1 데이터베이스 hospitaltb에 있는 레코드를 모두 가져오는 쿼리문
			String selectHospital = "select * from hospitaltb";
			// 1.2 데이터베이스 Connection 을 가져와야 한다.
			Connection con = null;
			// 1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
			PreparedStatement psmt = null;
			// 1.4. 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기객체 ___ResultSet
			ResultSet rs=null;			
			try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectHospital);
				// 1.5 쿼리문 실제데이터를 연결한 쿼리문을 실행하라(번개모양 아이콘을 눌러라)
				//executeQuery();  쿼리문을 실행해서 결과를 가져올때 사용하는 번개문
				//executeUpdate() 는 쿼리문을 실행해서 테이블에 저장을 할 때 사용하는 번개문
				rs= psmt.executeQuery(); 
				if (rs==null) {
					System.out.println("select 실패 : select쿼리문 실패. 점검바람");
					return null;
				}				
				while(rs.next()) {
					Hospital hospital=new Hospital(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6)
							);
					dbArrayListHospital.add(hospital);
				}		
			} catch (SQLException e) {
				System.out.println("삽입 실패 : 데이터베이스 삽입에 실패했습니다. 점검바람");
				e.printStackTrace();
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
					System.out.println("자원닫기 실패 : psmt,con 닫기실패");
				}
			}	
			return dbArrayListHospital;
		}	

		//02. 병원리스트에서 검색을 하는 기능 
		public static ArrayList<Hospital> getHospitaltbDataByRegion(String Region){
			ArrayList<Hospital> dbArrayListHospital=new ArrayList<>();
			String selectHospitalByRegion="select * from hospitaltb where addr like ? ";     	 	//'서울특별시 광진구%'								
																							
			Connection con = null;		
			PreparedStatement psmt = null;			
			ResultSet rs=null;			
			try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectHospitalByRegion);
				psmt.setString(1, "서울특별시 "+ Region+"%");
				
			//	System.out.println( "서울특별시 "+ Region+"%"  );

				rs= psmt.executeQuery(); 
				
				dbArrayListHospital.clear();//////////////??????????????
				
				while(rs.next()) {
					Hospital hospital=new Hospital(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6)	
							);
					
					dbArrayListHospital.add(hospital);
				}		
			} catch (SQLException e) {
				System.out.println("삽입 실패 : 데이터베이스 삽입에 실패했습니다. 점검바람");
				e.printStackTrace();
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
			return dbArrayListHospital;
		}

}
