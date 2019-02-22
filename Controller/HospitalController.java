package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Hospital;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HospitalController implements Initializable{
	public Stage hospitalStage;
	
	@FXML private TableView<Hospital> hTableView;
	@FXML private Button hBtnSearch;
	@FXML private Button hBtnSearchByRegion;
	@FXML private ComboBox<String> hCmbRegion;
	@FXML private TextField hTxtHospitalName;
    
	ObservableList<String> regionList = FXCollections.observableArrayList();
	
	
	ObservableList<Hospital> hospitalObList=FXCollections.observableArrayList();
	ObservableList<Hospital> hospitalObList2=FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//01. 테이블뷰 기본 세팅을 한다.
		setTableView();
		
		//02. 콤보박스에 지역을 세팅한다.
				
		setComboBoxLocalList();
		
		//03. 지역별로 검색을 했을때 리스트를 보여준다.

		hBtnSearchByRegion.setOnAction(e ->  showListOrderByRegion( hCmbRegion.getSelectionModel().getSelectedItem()) );
		
		
		//04. 병원이름을 검색하면 테이블뷰에서 선택을 해준다.

		hBtnSearch.setOnAction(e ->	searchHospitalDataByHospitalName() 		);
		
		
	}



	//01. 테이블뷰 기본 세팅을 한다.
	private void setTableView() {	
		TableColumn tcNo=hTableView.getColumns().get(0);
		tcNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		tcNo.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcLicensedate=hTableView.getColumns().get(1);
		tcLicensedate.setCellValueFactory(new PropertyValueFactory<>("licensedate"));
		tcLicensedate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcTel=hTableView.getColumns().get(2);
		tcTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
		tcTel.setStyle("-fx-alignment: CENTER;" );
		
		
		TableColumn tcHospital=hTableView.getColumns().get(3);
		tcHospital.setCellValueFactory(new PropertyValueFactory<>("hospital"));
		tcHospital.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcAddr=hTableView.getColumns().get(4);
		tcAddr.setCellValueFactory(new PropertyValueFactory<>("addr"));
		tcAddr.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcNaddr=hTableView.getColumns().get(5);
		tcNaddr.setCellValueFactory(new PropertyValueFactory<>("naddr"));
		tcNaddr.setStyle("-fx-alignment: CENTER;" );
	
		/// db연결해서 병원리스트를 담은 arraylist 를 리턴하는 함수를 불러온 다음에  그 값들을 하나씩 불러내서 객체로 선언하고
		/// 테이블뷰에 넣어준다.
		
		ArrayList<Hospital> dbArrayListHospital=HospitalDAO.getHospitaltbTotalData();
		
		for( Hospital hospital     :   dbArrayListHospital     ) {		
			Hospital hospital1=new Hospital(
											hospital.getNo(),
											hospital.getLicensedate(), 
											hospital.getTel(), 
											hospital.getHospital(), 
											hospital.getAddr(),
											hospital.getNaddr());		
			hospitalObList.add(hospital1);
		}
		
		hTableView.setItems(hospitalObList);
	}

	
	//02. 콤보박스에 지역을 세팅한다.
	private void setComboBoxLocalList() {
		regionList.addAll("강남구","강동구","강북구","강서구","송파구","종로구","관악구",
							"광진구","구로구","금천구","양천구","중구","노원구","도봉구","동대문구",
							"동작구","영등포구","중랑구","마포구","서대문구","서초구","용산구","성동구","성북구","은평구");
		
		hCmbRegion.setItems(regionList);	
	}


	
	//03. 지역별로 검색을 했을때 리스트를 보여준다.
	private void  showListOrderByRegion(String region) {
		hospitalObList2.clear();	
		 ArrayList<Hospital> dbArrayListHospital = HospitalDAO.getHospitaltbDataByRegion( hCmbRegion.getSelectionModel().getSelectedItem() );
		
		 System.out.println( hCmbRegion.getSelectionModel().getSelectedItem()    );
		
			for( Hospital hospital     :   dbArrayListHospital   ) {			
				Hospital hospital1=new Hospital(
												hospital.getNo(),
												hospital.getLicensedate(), 
												hospital.getTel(), 
												hospital.getHospital(), 
												hospital.getAddr(),
												hospital.getNaddr());			
			
				hospitalObList2.add(hospital1);
			}
		
			hTableView.setItems(hospitalObList2);
		
			
	}
		
	//04. 병원이름을 검색하면 테이블뷰에서 선택을 해준다.
	private void searchHospitalDataByHospitalName() {
		
		if(!(hTxtHospitalName.getText().isEmpty())) {
			setTableView();
			
			for(Hospital hospital : hospitalObList) {
				if(hTxtHospitalName.getText().trim().equals(hospital.getHospital())) {
					hTableView.getSelectionModel().select(hospital);
					hTableView.scrollTo(hospital);
				}
			}
		
		}
		
		
	}

} //end of controller



