package Controller;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Model.NoteDiary;
import Model.Profile;
import Model.Weight;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DiaryController implements Initializable{
	public Stage diaryStage;	
	
	/// 탭1. 체중관리
	@ FXML private TextField wTxtName;
	@ FXML private TextField wTxtBirth;
	@ FXML private TextField wTxtGender;
	@ FXML private TextField wTxtKind;
	@ FXML private ImageView wImageView;

	@ FXML private TableView<Weight> wTableView;
	@ FXML private DatePicker wDatePicker;
	@ FXML private TextField wTxtWeight;
	@ FXML private Button wBtnGetWeightTb;		//db에 저장된 체중 데이터값을 불러오는 버튼
	@ FXML private ImageView wImgViewBack;
	@ FXML private ImageView wImgSave; 
	@ FXML private Button wBtnChartShow; //차트를 보여주는 버튼
	@	FXML private Button wBtnDataDel; // 테이블뷰에서 한 행을 선택해서 해당 데이터를 삭제도록 하는 버튼
	
	 
	//탭2. 다이어리
	@ FXML private TextField dTxtMonth;
	@ FXML private Button dBtnWrite;
	@ FXML private Button dBtnLogout;
	@ FXML private TextField dTxtFeed;  
	@ FXML private TextField dTxtPee;
	@ FXML private TextField dTxtPoo;
	
	@FXML private TextField dTxtBrushing;
	@FXML private TextField dTxtPlay;
	@FXML private TextArea dTxtMemo;
	@FXML private DatePicker dPreDatePicker;
	@FXML private Button dBtnDatePicker;
	@FXML private TableView<NoteDiary> dNoteTableView;

	
	 //차트//
	 @FXML  private LineChart<?, ?> lineChart;
	 @FXML private CategoryAxis x;
	 @FXML  private NumberAxis y;
	

	ObservableList<Weight> weightObList = FXCollections.observableArrayList();  //체중 데이터를 담을 observableArrayList
	
	Weight weight;
	ArrayList<Weight> wDBArrayList=new ArrayList<>();
			
	
	
	public static String petname=null;
	public static String noteDay=null;
	public static String selectedRecordDate=null;
	public static String previousMonthDate=null;
	
	
	@ FXML private Button btn00; 	@ FXML private Button btn01; 	@ FXML private Button btn02;		@ FXML private Button btn03; 
	@ FXML private Button btn04; 	@ FXML private Button btn05;		@ FXML private Button btn06;		@ FXML private Button btn10; 	
	@ FXML private Button btn11; 	@ FXML private Button btn12; 	@ FXML private Button btn13;		@ FXML private Button btn14;	
	@ FXML private Button btn15;  	@ FXML private Button btn16;		@ FXML private Button btn20; 	@ FXML private Button btn21;
	@ FXML private Button btn22;  	@ FXML private Button btn23; 	@ FXML private Button btn24; 	@ FXML private Button btn25;
	@ FXML private Button btn26;		@ FXML private Button btn30;		@ FXML private Button btn31; 	@ FXML private Button btn32;
	@ FXML private Button btn33;		@ FXML private Button btn34;		@ FXML private Button btn35;	 	@ FXML private Button btn36;
	@ FXML private Button btn40; 	@ FXML private Button btn41; 	@ FXML private Button btn42; 	@ FXML private Button btn43;
	@ FXML private Button btn44;		@ FXML private Button btn45;		@ FXML private Button btn46;		@ FXML private Button btn50;
	@ FXML private Button btn51;		@ FXML private Button btn52;		@ FXML private Button btn53;		@ FXML private Button btn54;
	@ FXML private Button btn55;		@ FXML private Button btn56;		
	

	Button[] btnArray = new Button[42];

	LocalDateTime ldt=LocalDateTime.now();		
	Calendar cal=Calendar.getInstance();
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//	blockwBtnGetWeighttb();
		
		//탭1_ 체중 입력값 제한
		inputDecimalFormat(wTxtWeight);
		
		///////////////////////////////// 탭_1.체중관리 //////////////////////////////////////
		//탭1_00. 저장된 동물 리스트를 다이얼로그창에서 보여주고, 선택하도록한다.
		showPetListAndChoice();
		
		//탭1_02. 체중을 입력받고 그 데이터가 테이블 뷰에 세팅된다.	
		wImgSave.setOnMouseClicked(	e-> setWeightDataTableView()  );
		
		//탭1_03. 입력한 체중 데이터를 불러와서 테이블에 보여지게 함.		
		wBtnGetWeightTb.setOnAction(e -> getWeightDataFromDB() );	 // '이전데이터불러오기' 버튼
		
		//탭1_04. '차트보기' 버튼을 클릭하면 라인차트가 화면에 보여진다.	
		wBtnChartShow.setDisable(true);
		wBtnChartShow.setOnAction(e-> showWeightChangingChart() );
			
		
		//탭1_05. '삭제하기' 버튼을 누르면 테이블뷰에서 선택한 해당 행의 데이터가 삭제된다. DB 에서도 삭제된다. //////////////////////	
		wBtnDataDel.setOnAction(e -> deleteSelectedWeightData(wTableView.getSelectionModel().getSelectedItem() ) );
		
		
		//탭1_06. 테이블뷰를 클릭했을때 수정창이 뜨고, 그 입력받은 내용이 수정되어 DB에 저장, 테이블뷰도 수정
		wTableView.setOnMouseClicked(e-> editWeightData(e) );
		
	
		
		///////////////////////////////// 탭_2. 다이어리//////////////////////////////////////
		
		//탭2_00.달력에 메모를 보여주는 칸을 입력을 못하도록 막는다.
		setDiaryTextFieldSetEditableFalse();
		

		// 탭2_01.달력을 보여줌
		showDiary();
			
		
		// 탭2_02. 연필모양 아이콘을 누르면 다이어리 노트로 화면이 전환된다. 뜬다.
		dBtnWrite.setOnAction(e-> openDiaryWritePage() );

		
		//탭2_04. 이전달선택(DatePicker) 에서 날짜를 선택하고 버튼을 선택하면 이전달의 데이터가 화면에 뜬다.
		
		dBtnDatePicker.setOnMouseClicked( e -> showPreviousMonthNoteData( ) );
		
		//탭2_05. 로그아웃 버튼을 누르면 로그인페이지로 돌아간다.
		dBtnLogout.setOnAction(e -> logoutAndShowLoginPage() );
		
		
		//<- 화살표 버튼을 누르면 메인화면으로 전환된다.
		wImgViewBack.setOnMouseClicked(e-> handleWimgViewBackAction() );		
		
		
	}
	
	
	//탭1_00. 저장된 동물 리스트를 다이얼로그창에서 보여주고, 선택하도록한다.
	private void showPetListAndChoice() {
		try {

			Stage petChoiceStage = new Stage(StageStyle.UTILITY);
			petChoiceStage.initModality(Modality.WINDOW_MODAL);
			petChoiceStage.initOwner(diaryStage);
			petChoiceStage.setTitle("반려동물선택");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/petchoice.fxml"));
			Parent root = loader.load();
	
			//아이디//
			TableView<Profile> pChoiceTableView = (TableView<Profile>)root.lookup("#pChoiceTableView");
			Button pChoiceOk =(Button)root.lookup("#pChoiceOk");
			
		
			////테이블뷰 셋팅/////
			TableColumn tcpetkind=pChoiceTableView.getColumns().get(0);
			tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
			tcpetkind.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetname=pChoiceTableView.getColumns().get(1);
			tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
			tcpetname.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetgender=pChoiceTableView.getColumns().get(2);
			tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
			tcpetgender.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetbirth=pChoiceTableView.getColumns().get(3);
			tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
			tcpetbirth.setStyle("-fx-alignment: CENTER;" );
			
			//테이블뷰에 들어갈 내용을 DB 에서 가져온다.__ petinfotb2 에서
			
			ObservableList<Profile> petList=FXCollections.observableArrayList();
			ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
			
			dbArrayListProfile=ProfileDAO.getPetinfoDataToDB();
			for(Profile profile: dbArrayListProfile) {
				petList.addAll(profile);
			}
			
			
			pChoiceTableView.setItems(petList);

			//// 테이블뷰를 선택하면 동물이름을 준다.
			pChoiceTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					petname= pChoiceTableView.getSelectionModel().getSelectedItem().getPetname();
					System.out.println(petname);
					
				}
			});

			Scene scene=new Scene(root);
			petChoiceStage.setScene(scene);
			petChoiceStage.show();
			petChoiceStage.setAlwaysOnTop(true);
			petChoiceStage.setResizable(false);
			
			pChoiceOk.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					if(pChoiceTableView.getSelectionModel().getSelectedItem()==null) {
						
						petChoiceStage.setAlwaysOnTop(false);
						RootController.callAlert("동물 선택: 동물을 선택해주세요");
						petChoiceStage.setAlwaysOnTop(true);
						return;
					}
					settingPetProfile();
					petChoiceStage.close();
					
				}
			});

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	//탭1_ 00번 함수 안에서 쓰인다.
	private void settingPetProfile() {		
		// 테이블뷰에서 한 행을 선택하면 프로필에 등록한 동물의 내용을 DB 에서 불러와서 체중관리 탭의 텍스트필드에 세팅되도록
		//1. petinfotb2 에서 데이터를 가져온다. 뭘? 이름, 성별,생일,종류
			
			ArrayList<Profile> petinfoList = DiaryDAO.getPetDatabyPetname();
		
			for(Profile profile: petinfoList) {
				String petname=profile.getPetname();
				String petkind=profile.getPetkind();
				String petbirth=profile.getPetbirth();
				String petgender=profile.getPetgender();		
				String imgpath=profile.getImgpath();
				Image image=new Image("file:///"+ProfileController.imageDir.getAbsolutePath()+"/"+imgpath);
							
				wTxtName.setText(petname);
				wTxtBirth.setText(petbirth);
				wTxtGender.setText(petgender);
				wTxtKind.setText(petkind);
				wImageView.setImage(image);
		
				wTxtName.setEditable(false);
				wTxtBirth.setEditable(false);
				wTxtGender.setEditable(false);
				wTxtKind.setEditable(false);		
			}			
	}


	//탭1_02. 탭1_체중입력창에서 받은 값으로 테이블 뷰를 세팅한다. + weighttb에 데이터를 저장한다.
	private void setWeightDataTableView() {
		wBtnChartShow.setDisable(false);
		
		//02_02. 테이블뷰
		TableColumn tcRecordate=wTableView.getColumns().get(0);
		tcRecordate.setCellValueFactory(new PropertyValueFactory<>("recordate"));
		tcRecordate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcWeight=wTableView.getColumns().get(1);
		tcWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
		tcWeight.setStyle("-fx-alignment: CENTER;" );
		

		/// 기록날짜, 체중만 테이블뷰에 !//
		Weight weight=new Weight(wDatePicker.getValue().toString(),
														wTxtWeight.getText() );
		
		weightObList.add(weight);
			
		wTableView.setItems(weightObList);

		int count=WeightDAO.insertWeightData(weight);
		if(count !=0 ) {
			//RootController.callAlert("입력성공 : 데이터베이스 입력이 성공되었습니다.");
		}
	
	}
	

	

	//탭1_03. 입력한 체중 데이터를 불러와서 테이블에 보여지게 함. ---- '이전데이터 불러오기'
	private void getWeightDataFromDB() {
		ArrayList<Weight> wDBArrayList=new ArrayList<>();
		
		TableColumn tcRecordate=wTableView.getColumns().get(0);
		tcRecordate.setCellValueFactory(new PropertyValueFactory<>("recordate"));
		tcRecordate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcWeight=wTableView.getColumns().get(1);
		tcWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
		tcWeight.setStyle("-fx-alignment: CENTER;" );
		
		wTableView.setItems(weightObList);
		
		wDBArrayList=WeightDAO.getWeighttbTotalData();
		
		if(wDBArrayList.isEmpty()) {
			RootController.callAlert("이전데이터를 불러올 수 없습니다: 저장된 데이터가 없습니다.");
			wBtnGetWeightTb.setDisable(true);
		}else {		
			for(Weight weight : wDBArrayList ) {
				weightObList.add(weight);
			}
			wBtnGetWeightTb.setDisable(true);
			wBtnChartShow.setDisable(false);
			
		}
	}

	
	
	//탭1_04. '차트보기' 버튼을 클릭하면 라인차트가 화면에 보여진다.
	private void showWeightChangingChart() {
		ArrayList<Weight> wDBArrayList=new ArrayList<>();
		XYChart.Series series = new XYChart.Series<>();
	
		series.getData().clear();
		lineChart.getData().clear();
		
		wDBArrayList=WeightDAO.getWeighttbTotalData();	
		
		for (Weight weight : wDBArrayList) {

			String recordDate = weight.getRecordate();
			double weightData = Double.parseDouble(weight.getWeight());

			series.getData().add(new XYChart.Data(recordDate, weightData));

		}
		lineChart.getData().addAll(series);
		lineChart.setAnimated(false);
	}
	
	
		//탭1_05. '삭제하기' 버튼을 누르면 테이블뷰에서 선택한 해당 행의 데이터가 삭제된다. DB 에서도 삭제된다. //////////////////////
		private void deleteSelectedWeightData(Weight selectedItem) {
			selectedRecordDate=wTableView.getSelectionModel().getSelectedItem().getRecordate();
			Weight selectWeightData=wTableView.getSelectionModel().getSelectedItem();
			int selectWeightDataIndex=wTableView.getSelectionModel().getSelectedIndex();
				
			int count=WeightDAO.deleteWeightData(WeightDAO.petNo); 
		
			if(count != 0) {
				wDBArrayList.clear();
				weightObList.clear();
				
				wDBArrayList=WeightDAO.getWeighttbTotalData();
				for(Weight weight : wDBArrayList) {
					weightObList.add(weight);
				}
	
			}else {
				return;
			}
			
		}

		//탭1_06. 테이블뷰를 클릭했을때 수정창이 뜨고, 그 입력받은 내용이 수정되어 DB에 저장, 테이블뷰도 수정
		private void editWeightData(MouseEvent e) {

			Weight selectWeightData = wTableView.getSelectionModel().getSelectedItem();
			int selectWeightDataIndex = wTableView.getSelectionModel().getSelectedIndex();

			//System.out.println(" 선택한 인덱스는:" + selectWeightDataIndex);

		if (e.getClickCount() == 2) {

			try {

				Stage weightNoteStage = new Stage(StageStyle.UTILITY);
				weightNoteStage.initModality(Modality.WINDOW_MODAL);
				weightNoteStage.initOwner(diaryStage);
				weightNoteStage.setTitle("체중기록 수정");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/weightnote.fxml"));
				Parent root;
				root = loader.load();

				DatePicker wRecordDatePicker = (DatePicker) root.lookup("#wRecordDatePicker");
				TextField wTxtWeight = (TextField) root.lookup("#wTxtWeight");
				Button wBtnRegister = (Button) root.lookup("#wBtnRegister");
				Button wBtnCancel = (Button) root.lookup("#wBtnCancel");

				// 체중 입력값 제한
				inputDecimalFormat(wTxtWeight);

				// 선택된 값을 일단 weightnote 창에 세팅한다.
				wTxtWeight.setText(selectWeightData.getWeight());
				wRecordDatePicker.setValue(LocalDate.parse(selectWeightData.getRecordate()));
				selectedRecordDate = selectWeightData.getRecordate();

				//System.out.println("선택된 날짜는:" + selectedRecordDate);

			wBtnCancel.setOnAction( (event) -> {  weightNoteStage.close(); 	});
			
			wBtnRegister.setOnMouseClicked(new EventHandler<MouseEvent>() {	 // 값을 수정하면 테이블에서도 값이 바뀌고, DB 에서도 값이 수정된다.
				

				@Override
				public void handle(MouseEvent event) {					
					Weight weight=new Weight(wRecordDatePicker.getValue().toString(),  wTxtWeight.getText()  );
					
					int count= WeightDAO.updateWeightData(weight);
					
					if(count != 0) {
						wDBArrayList.clear();
						weightObList.clear();				
						System.out.println("DB수정완료: 수정이 완료되었습니다.");    					
					}else {
						return;
					}
					////////////////////////////////////////////
					wDBArrayList = WeightDAO.getWeighttbTotalData();
					for(Weight updatedWeight : wDBArrayList ) {
						weightObList.add(updatedWeight);
					}
					
					wTableView.setItems(weightObList);
					weightNoteStage.close();
					////////////////////////////////////////////

				}
			});
			
			Scene scene=new Scene(root);
			weightNoteStage.setScene(scene);
			weightNoteStage.show();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
		

	///////////////////////////////// 탭_2. 다이어리//////////////////////////////////////
	
	

	//탭2_00.달력에 메모를 보여주는 칸을 입력을 못하도록 막는다.
	private void setDiaryTextFieldSetEditableFalse() {
		
		dTxtFeed.setEditable(false);
		dTxtPee.setEditable(false);
		dTxtPoo.setEditable(false);
		dTxtBrushing.setEditable(false);
		dTxtPlay.setEditable(false);
		dTxtMemo.setEditable(false);
		
	}


	// 02. 연필모양 아이콘을 누르면 다이어리 노트로 화면이 전환된다. 뜬다.
	private void openDiaryWritePage() {	
		try {

			Stage noteStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/note.fxml"));
			Parent root = loader.load();

			NoteController noteController = loader.getController();
			noteController.noteStage = noteStage;

			Scene scene = new Scene(root);
			noteStage.setScene(scene);
			noteStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/////////////다이어리 달력 세팅////////////////////////////////
	private void showDiary() {
		
		Button[ ]btnArray= 
				{btn00, btn01, btn02, btn03, btn04, btn05, btn06, 
				btn10, btn11, btn12, btn13, btn14, btn15, btn16,
				btn20, btn21, btn22, btn23, btn24, btn25, btn26, 
				btn30, btn31, btn32, btn33, btn34, btn35, btn36,
				btn40, btn41, btn42, btn43, btn44, btn45, btn46,
				btn50, btn51, btn52, btn53, btn54, btn55, btn56};
		
		int year=ldt.getYear();
		int month = ldt.getMonthValue();
		int day=ldt.getDayOfMonth();
		cal.set(year, month-1, 1);		
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);			
		
		int firstday=0;
		int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		switch(dayOfWeek) {
			case 1 : firstday=0; break;
			case 2 : firstday=1; break;
			case 3 : firstday=2; break;
			case 4 : firstday=3; break;
			case 5 : firstday=4; break;
			case 6 : firstday=5; break;
			case 7 : firstday=6; break;
		}	
					
		int j=1;
		for(int i=firstday;  i<lastDay+firstday; i++) {			
			btnArray[i].setText(""+j);
			j++;

			final int ii=i;
		
			btnArray[i].setOnMouseClicked(e->			// 달력버튼을 누르면 실행되는 것들
			{			
				 // 달력을 누르면 String 으로 2019-02-08 이런식으로 뽑히게끔 만들어서 noteDay 에 저장한다.
				String btn= btnArray[ii].getText();
				if(  Integer.valueOf(btn)  <= 9) {
					btn="0"+btn;
					noteDay=ldt.now().toString().substring(0, 7) +"-"+ btn;
				//	System.out.println(noteDay);   ////// 2019-02-01 ~ 2019-02-09
				} else {
					btn=btn;
					noteDay=ldt.now().toString().substring(0, 7) +"-"+ btn;
				//	System.out.println(noteDay);	////////2019-02-10 ~ 2019-02-28
				}
					ArrayList<NoteDiary> dbArrayListDiary=DiaryDAO.getDiaryData();
	
				/////////////////////////////////////////////////////////////////////////////////////////
				// 달력을 눌렀을때 뜨는 날짜와, 기록된 day 가 같으면 그 테이블의 값을 가져와서 텍스트필드에 준다. 
				for (NoteDiary note : dbArrayListDiary) {
					String dbDay = note.getDay();
					if (noteDay.equals(dbDay) ) {
	
						btnArray[ii].setStyle("-fx-background-color: #FFD0AF;");
					
						dTxtFeed.setText(note.getMeal());
						dTxtPee.setText(note.getPee());
						dTxtPoo.setText(note.getPoo());
						dTxtBrushing.setText(note.getBrushing());
						dTxtPlay.setText(note.getPlay());
						dTxtMemo.setText(note.getMemo());

						setDiaryTextFieldSetEditableFalse();
					}
				} // end of for-each
				if (DiaryDAO.count == 0) {
					dTxtFeed.clear();
					dTxtPee.clear();
					dTxtPoo.clear();
					dTxtBrushing.clear();
					dTxtPlay.clear();
					dTxtMemo.clear();
					RootController.callAlert("노트내용없음: 해당날짜에는 입력하신 노트가 없습니다.");
				}
			});
		}
		dTxtMonth.setText(String.valueOf(month + "월"));
		dTxtMonth.setEditable(false);
		dTxtMonth.setStyle("-fx-alignment:CENTER");

	}

	// DatePicker 로 이전달을 누르면 그때의 정보가 써진다. 
	private void showPreviousMonthNoteData(  ) {
		
		previousMonthDate=dPreDatePicker.getValue().toString();
		System.out.println(previousMonthDate);
		
		ArrayList<NoteDiary> dbArrayListDiary=DiaryDAO.getPreviousDiaryData( );
		/////////////////////////////////////////////////////////////////////////////////////////
		// 달력을 눌렀을때 뜨는 날짜와, 기록된 day 가 같으면 그 테이블의 값을 가져와서 텍스트필드에 준다. 
		
		for (NoteDiary note : dbArrayListDiary) {
			//String dbDay = dDatePicker.getValue().toString();
			//System.out.println(dbDay);
			
			if(previousMonthDate.equals(note.getDay())){
				dTxtFeed.setText(note.getMeal());
				dTxtPee.setText(note.getPee());
				dTxtPoo.setText(note.getPoo());
				dTxtBrushing.setText(note.getBrushing());
				dTxtPlay.setText(note.getPlay());
				dTxtMemo.setText(note.getMemo());
				
				setDiaryTextFieldSetEditableFalse(); 
			}
		} // end of for-each
			if(DiaryDAO.count ==0){
				dTxtFeed.clear();
				dTxtPee.clear();
				dTxtPoo.clear();
				dTxtBrushing.clear();
				dTxtPlay.clear();
				dTxtMemo.clear();			
				RootController.callAlert("노트내용없음: 해당날짜에는 입력하신 노트가 없습니다.");				
			}
	}
	//탭2_05. 로그아웃 버튼을 누르면 로그인페이지로 돌아간다.
	private void logoutAndShowLoginPage() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
			Parent root = loader.load();
						
			RootController rootController=loader.getController();
			rootController.primaryStage=primaryStage;
			
			Scene scene=new Scene(root);
			primaryStage.setScene(scene);
			RootController.callAlert("로그아웃: 로그아웃되었습니다.");
			diaryStage.close();
			primaryStage.setTitle("로그인");
			primaryStage.show();
			
		} catch (IOException e) {
			RootController.callAlert("화면 전환 오류: 로그인화면 전환에 문제 발생");
		}
		
	}
		
	//<- 화살표 버튼을 누르면 메인화면으로 전환된다.
	private void handleWimgViewBackAction() {
		try {
			Stage mainStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main.fxml"));
			Parent root = loader.load();
						
			MainController mainController=loader.getController();
			mainController.mainStage=mainStage;
			
			Scene scene=new Scene(root);
			mainStage.setScene(scene);
			System.out.println("화면전환: 메인 화면으로 전환됩니다.");
			diaryStage.close();
			mainStage.show();
			
		} catch (IOException e) {
			System.out.println("화면 전환 오류: 메인화면 전환에 문제 발생");
		}
		
	}

	////////////////체중입력값 제한///////////////////////////////////////
	

	// 입력값 필드 포맷 설정 기능: 소숫점 둘째자리까지만________________ok
	private void inputDecimalFormat(TextField textField) {

		DecimalFormat format = new DecimalFormat("##.##");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 12이면(11자리를 넘었음을 뜻함.)
			// 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 6) {
				return null;
			} else {
				return event;
			}
		}));
		
	}
	
	

	
}
