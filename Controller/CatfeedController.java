package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.CatFeed;
import Model.Hospital;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CatfeedController implements Initializable{
	public Stage catFeedStage;
	
	
	@FXML private ImageView feedImag;
	@FXML private TableView<CatFeed> feedTableView;
	@FXML private TextField feedTxtSearch;
	@FXML private Button feedBtnSearch;
	@FXML private ComboBox<String> feedPetKind;
	
	ObservableList<CatFeed> catFeedOblist	=	FXCollections.observableArrayList();
	ObservableList<String> PetKindCmbList = FXCollections.observableArrayList();
	
	
	CatFeed catFeed=null;
	ArrayList<CatFeed> cfDbArrayList = new ArrayList<>();
	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//00. 콤보박스(동물종류) 를 셋팅한다.
		setComboBoxPetList();
		
		//01. 사료성분분석표 테이블뷰 기본셋팅을 한다.
		setCatFeedTableView();
		
		
		//02. db 에서 사료정보테이블을 가져와서 ,테이블뷰에 담아준다.
		getCatFeedDatafromDB();
		
		//03. 사료명을 검색하면 테이블뷰에서 선택을 해준다.
		feedBtnSearch.setOnAction(e -> searchCatFeedDataByFeedName() );
		
		//04. 테이블뷰에서 선택을 하면 사료이미지가 보인다.
		
		
	}

	private void setComboBoxPetList() {
		PetKindCmbList.addAll("개","고양이","토끼","고슴도치","햄스터","거북이","새","이구아나");
		feedPetKind.setItems(PetKindCmbList);
		feedPetKind.getSelectionModel().select("고양이");
	}

	//01. 사료성분분석표 테이블뷰 기본셋팅을 한다.
	private void setCatFeedTableView() {
		//제품명
		TableColumn tcproductname=feedTableView.getColumns().get(0);
		tcproductname.setCellValueFactory(new PropertyValueFactory<>("productname"));
		tcproductname.setStyle("-fx-alignment: CENTER;" );
		
		//조단백
		TableColumn tccrudeprotein=feedTableView.getColumns().get(1);
		tccrudeprotein.setCellValueFactory(new PropertyValueFactory<>("crudeprotein"));
		tccrudeprotein.setStyle("-fx-alignment: CENTER;" );
		
		//조지방
		TableColumn tccrudefat=feedTableView.getColumns().get(2);
		tccrudefat.setCellValueFactory(new PropertyValueFactory<>("crudefat"));
		tccrudefat.setStyle("-fx-alignment: CENTER;" );
		
		//조섬유
		TableColumn tccrudefiber=feedTableView.getColumns().get(3);
		tccrudefiber.setCellValueFactory(new PropertyValueFactory<>("crudefiber"));
		tccrudefiber.setStyle("-fx-alignment: CENTER;" );
		
		//조회분
		TableColumn tccrudeash=feedTableView.getColumns().get(4);
		tccrudeash.setCellValueFactory(new PropertyValueFactory<>("crudeash"));
		tccrudeash.setStyle("-fx-alignment: CENTER;" );
		
		//탄수화물
		
		TableColumn tccarbohydrate=feedTableView.getColumns().get(5);
		tccarbohydrate.setCellValueFactory(new PropertyValueFactory<>("carbohydrate"));
		tccarbohydrate.setStyle("-fx-alignment: CENTER;" );
		
		//칼로리
		
		TableColumn tccalorie=feedTableView.getColumns().get(6);
		tccalorie.setCellValueFactory(new PropertyValueFactory<>("calorie"));
		tccalorie.setStyle("-fx-alignment: CENTER;" );

		//생산지
		
		TableColumn tcproductionregion=feedTableView.getColumns().get(7);
		tcproductionregion.setCellValueFactory(new PropertyValueFactory<>("productionregion"));
		tcproductionregion.setStyle("-fx-alignment: CENTER;" );
		

	}

	
	//02. db 에서 사료정보테이블을 가져와서 ,테이블뷰에 담아준다.
	private void getCatFeedDatafromDB() {

	String sql="select * from catfood ";
	
	try {
		con=DBUtility.getConection();
		pstmt=con.prepareStatement(sql);
		
		rs=pstmt.executeQuery();
		
		while(rs.next()) {
			catFeed=new CatFeed(
					rs.getString(1),
					rs.getString(2), 
					rs.getString(3), 
					rs.getString(4), 
					rs.getString(5), 
					rs.getString(6), 
					rs.getString(7), 
					rs.getString(8)
					);
			cfDbArrayList.add(catFeed);
		}
		
	} catch (SQLException e) {
		System.out.println("사료성분분석표 불러오기 실패:실패했습니다.");
		e.printStackTrace();
	}finally {
		// 1.6 자원객체를 닫아야한다.
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println("자원닫기 실패 : psmt,con 닫기실패" );
		}
	}	

		feedTableView.setItems(catFeedOblist);

		for (CatFeed feed : cfDbArrayList) {
			catFeedOblist.add(feed);
		}

	}
	
	//03. 사료명을 검색하면 테이블뷰에서 선택을 해준다.
	private void searchCatFeedDataByFeedName() {
		for(CatFeed feed : catFeedOblist) {
			if(feedTxtSearch.getText().trim().equals(feed.getProductname()) ) {    
				feedTableView.getSelectionModel().select(feed);
				feedTableView.scrollTo(feed);
			}
		}
	}
	
	
	
}
