package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import Model.NoteDiary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NoteController implements Initializable{
	public Stage noteStage;
	
	@FXML private TextField dTxtMeal;
	@FXML private TextField dTxtPee;
	@FXML private ComboBox<String> dCmbPoo;
	@FXML private ComboBox<String> dCmbBrush;
	@FXML private TextField dTxtPlay;
	@FXML private TextArea dTxtMemo;
	@FXML private Button dBtnClear;
	@FXML private Button dBtnSave;
	@FXML private DatePicker dDatePicker;
	
	
	//콤보박스를 사용하기위한 ObservableList 
	ObservableList<String> dListPoo = FXCollections.observableArrayList();
	ObservableList<String> dListBrush = FXCollections.observableArrayList();
	
	//DB 저장을 위한 ObservableList
	ObservableList<NoteDiary> noteObList=FXCollections.observableArrayList();
	ArrayList<String>dateArrayList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//00. 데이터피커 오늘날짜로 설정	
		LocalDateTime ldt=LocalDateTime.now();	
		dDatePicker.setValue((	ldt.toLocalDate()) );	

		//01.콤보박스를 세팅한다.
		setComboBox();
		
		//02. 저장버튼을 누르면 노트 내용이 DB 에 저장된다. 
		dBtnSave.setOnAction(e -> saveNoteDataToDB() );
		
		//03. 입력받은 모든 값을 초기화한다.
		dBtnClear.setOnAction(e -> textFieldClearAction() );
		
	}


	//01.콤보박스를 세팅한다.
	private void setComboBox() {
		//대변
		dListPoo.addAll("없음","보통","무른변","설사","단단함","혈변","변비");
		dCmbPoo.setItems(dListPoo);
		//양치여부
		dListBrush.addAll("양치완료","양치안함");
		dCmbBrush.setItems(dListBrush);
		
	}


	//02. 저장버튼을 누르면 노트 내용이 DB 에 저장된다. 
	private void saveNoteDataToDB() {
		
		String dayCheck=null;
		
		dateArrayList=NoteDAO.getDayfromDiarytb();
		for(String nd : dateArrayList) {
			if(dDatePicker.getValue().toString() .equals(nd)) {
				dayCheck="duplication"; // datepicker에 선택한 날짜와 db의 날짜가 중복되면 "duplication" 을 저장
			}
		}
		
		
		////// 빈칸입력시 알림주기//////
		if ((dTxtMeal.getText().isEmpty()) || (dTxtPee.getText().isEmpty()) || (dCmbPoo.getSelectionModel().isEmpty())
				|| (dCmbBrush.getSelectionModel().isEmpty()) || (dTxtPlay.getText().isEmpty())
				|| (dTxtMemo.getText().isEmpty())) {
			RootController.callAlert("입력오류: 모든 값을 입력해주세요.");

			////// 선택된 날짜에 저장된 노트가 있으면 알림주기, DB에 저장 안됨.//////
		} else if (dayCheck == "duplication") {
			RootController.callAlert("노트 입력 오류: 지정하신 날짜에는 이미 등록된 노트가 존재합니다. 다른 날짜를 선택해주세요.");
			dDatePicker.setValue(null);

			return;

		} else if (dayCheck == null) {
			NoteDiary noteDiary = new NoteDiary(dDatePicker.getValue().toString(), dTxtMeal.getText(),
					dTxtPee.getText(), dCmbPoo.getSelectionModel().getSelectedItem(),
					dCmbBrush.getSelectionModel().getSelectedItem(), dTxtPlay.getText(), dTxtMemo.getText());

			noteObList.add(noteDiary);

			int count = NoteDAO.insertNoteData(noteDiary);

			if (count != 0) {
				RootController.callAlert("노트 등록: 노트의 내용이 등록되었습니다.");
			}
			noteStage.close();
		}
	}

	
	//03. 입력받은 모든 값을 초기화한다.
	private void textFieldClearAction() {
		dDatePicker.setValue(null);
		dTxtMeal.clear();
		dTxtPee.clear();
		dCmbPoo.getSelectionModel().clearSelection();
		dCmbBrush.getSelectionModel().clearSelection();
		dTxtPlay.clear();
		dTxtMemo.clear();
	}

}
