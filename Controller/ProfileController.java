package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Profile;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ProfileController implements Initializable{
	public Stage profileStage;
	
	@FXML private ImageView pImgView;
	@FXML private Button pBtnImgRegister;
	@FXML private TextField pTxtName;
	@FXML private TextField pTxtBirth;
	@FXML private ComboBox<String> pCmbGender;
	@FXML private ComboBox<String> pCmbKind;
	@FXML private ComboBox<String> pCmbVaccine;
	@FXML private Button pBtnSave;
	@FXML private Button pBtnDelete;
	@FXML private TableView<Profile> pTableView;
	
	
	File file = new File("F:/03.javaProject/MyProject2_0211/src/images/cat_3775226.png");
	ObservableList<String> pListGender=FXCollections.observableArrayList();
	ObservableList<String> pListKind=FXCollections.observableArrayList();
	ObservableList<String> pListVaccine=FXCollections.observableArrayList();
	
	
	
	ObservableList<Profile> profileObList=FXCollections.observableArrayList();
	ObservableList<Profile> profileObList2=FXCollections.observableArrayList(); // 종류,이름,성별,생일 저장
	ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
	
	
	//이미지 등록을 위한 멤버필드 + 저장을 위한 폴더//
	private File selectFile=null; 
	public static File imageDir=new File("C:/myProjectImages");		//저장을 위한 폴더
	private String fileName="";
	/////////////////////////////////
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//00.기존의 동물이 테이블에 보여진다.
		settingPetData();
	
		//00. 입력값제한 프로필 텍스트필드 입력값 제한
		pTxtName.textProperty().addListener(inputDecimalFormatName); 	// 이름은 영어,한글만
		inputDecimalFormatEightDigit(pTxtBirth); 	// 생일 8자리 숫자제한
		
		//01.프로필 등록의 콤보박스 내용을 셋팅한다.
		setComboBoxDate();
	
		//02. 이미지 등록버튼을 클릭했을때 처리하는 함수(파일창이 열린다.)
		pBtnImgRegister.setOnAction(e -> handlePbtnImgRegisterAction() );		
	
		//03.저장 버튼을 누르면 프로필 내용이 DB 에 저장되고, 메인화면으로 다시 전환된다. + 테이블뷰에 내용이 들어간다.
		pBtnSave.setOnAction(e -> handlePbtnSaveAction() );
		
		//04. 테이블에서 동물을 선택하고 삭제하기 버튼을 누르면 화면의 테이블에서 내용이 삭제되고, DB 에서도 해당 동물이 삭제된다
		pBtnDelete.setOnMouseClicked(e-> deletePetData( pTableView.getSelectionModel().getSelectedItem())  );

		
	}


	//기존의 동물이 테이블에 보여진다.
	private void settingPetData() {
		TableColumn tcpetkind = pTableView.getColumns().get(0);
		tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
		tcpetkind.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetname = pTableView.getColumns().get(1);
		tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
		tcpetname.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetgender = pTableView.getColumns().get(2);
		tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
		tcpetgender.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetbirth = pTableView.getColumns().get(3);
		tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
		tcpetbirth.setStyle("-fx-alignment: CENTER;");

		ArrayList<Profile> dbArrayListProfile = ProfileDAO.getPetinfoDataToDB();
		for (Profile pf : dbArrayListProfile) {
			profileObList2.addAll(pf);
		}
		pTableView.setItems(profileObList2);

	}

	
	//01.프로필 등록의 콤보박스 내용을 셋팅한다.
	private void setComboBoxDate() {
		// 성별
		pListGender.addAll("남","남(중성화)","여","여(중성화)","모름");
		pCmbGender.setItems(pListGender);
		
		// 반려동물 종류
		pListKind.addAll("개","고양이","토끼","고슴도치","햄스터","거북이","새","이구아나","기타");
		pCmbKind.setItems(pListKind);
		
		// 기초예방접종여부
		pListVaccine.addAll("모름","접종전","접종중","접종완료");
		pCmbVaccine.setItems(pListVaccine);
		
		
	}

	//02. 이미지 등록버튼을 클릭했을때 처리하는 함수(파일창이 열린다.) 
	//			+   저장은 '저장하기'버튼을 누르면 DB 에 이미지 경로가 저장된다. + 다이어리_'탭1 체중관리'에 저장된 이미지가 뜬다.
		
		private void handlePbtnImgRegisterAction() {
			FileChooser fileChooser= new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File",  "*.png" , "*.jpg" , "*.gif" ));
			selectFile=fileChooser.showOpenDialog(profileStage);
			String localURL=null;
			if(selectFile != null) {
				try {
					localURL=selectFile.toURI().toURL().toString();
				}catch(MalformedURLException e){  }
			}
			
			pImgView.setImage(new Image(localURL,false));
			pImgView.setStyle("-fx-alignment: CENTER;");
			fileName=selectFile.getName();

		}

	



	//03_01. 프로필등록 내용을 입력한다. _ 03번 함수 안에서 쓰이는 함수! + 테이블뷰에 내용이 찍힌다.
	private void insertPetProfileData() {
		
		if(		pTxtName.getText().isEmpty() || 
				pCmbKind.getSelectionModel().isEmpty() ||
				pTxtBirth.getText().isEmpty() ||
				pCmbGender.getSelectionModel().isEmpty() ||
				pCmbVaccine.getSelectionModel().isEmpty() ) 
		{  	
			RootController.callAlert("입력오류: 모든 칸을 입력해주세요.");	
			return;
			}

		
			Profile profile=new Profile(
						pTxtName.getText(),
						pCmbKind.getSelectionModel().getSelectedItem(), 
						pTxtBirth.getText(), 
						pCmbGender.getSelectionModel().getSelectedItem(),
						pCmbVaccine.getSelectionModel().getSelectedItem(), 
						fileName
			);
				
		
			profileObList.add(profile);

			int count = ProfileDAO.insertJoinData(profile);
			if (count != 0) {
				System.out.println("입력성공: 데이터베이스 입력이 성공");
			}
					
			Profile profileForTable = new Profile(
					pCmbKind.getSelectionModel().getSelectedItem(), 
					pTxtName.getText(),
					pCmbGender.getSelectionModel().getSelectedItem(),
					pTxtBirth.getText() );
			
			profileObList2.add(profileForTable);
			

			TableColumn tcpetkind=pTableView.getColumns().get(0);
			tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
			tcpetkind.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetname=pTableView.getColumns().get(1);
			tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
			tcpetname.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetgender=pTableView.getColumns().get(2);
			tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
			tcpetgender.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetbirth=pTableView.getColumns().get(3);
			tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
			tcpetbirth.setStyle("-fx-alignment: CENTER;" );
			
			pTableView.setItems(profileObList2);
			
	}



	//03.저장 버튼을 누르면 프로필 내용이 DB 에 저장되고, 메인화면으로 다시 전환된다. + 이미지가 저장도 된다!!!! (		imageSave();		)
	private void handlePbtnSaveAction() {
		ArrayList <Profile> dbArrayListProfile =  ProfileDAO.getPetinfoDataToDB();
		String petname=null;
		for(Profile pf:dbArrayListProfile) {
			petname=pf.getPetname();
		}
		

		if(pTxtName.getText().equals(petname)) {
			RootController.callAlert("동물이름 오류: 기존에 등록된 동물의 이름과 동일합니다. 다른 이름을 등록해주세요.");
			return;
		}
		
			
		imageSave();		//이미지가 저장___c://images/선택된이미지이름명.jpg 
		
		insertPetProfileData();		//프로필등록 내용을 입력한다. ___DB 에도 저장된다.
		
		
		pTxtName.clear();
		pCmbKind.setValue(null);
		pTxtBirth.clear();
		pCmbGender.setValue(null);
		pCmbVaccine.setValue(null);		
		pImgView.setImage(new Image(getClass().getResource("../images/cat_3775226.png").toString() ));
		
		
		
	}

	
	//04. 테이블에서 동물을 선택하고, 삭제하기 버튼을 누르면 화면의 테이블에서 내용이 삭제되고, DB 에서도 해당 동물이 삭제된다
	private void deletePetData(Profile selectedPet) {	
		int count= ProfileDAO.deletePetinfoData( pTableView.getSelectionModel().getSelectedItem().getPetname() );
		
		if(count != 0) {
	
			RootController.callAlert("삭제완료: 삭제가 완료되었습니다.");
			profileObList2.remove(selectedPet);
			dbArrayListProfile.remove(selectedPet);
			imageDelete(); 
		}
		
	}
	

		// 이미지 저장 ---  
		private void imageSave() {
			if(!imageDir.exists()	) {
				imageDir.mkdirs();
			}
			FileInputStream fis = null;
			BufferedInputStream bis=null;
			FileOutputStream fos=null;
			BufferedOutputStream bos=null;
			// 선택된 이미지를 c:/images/"선택된이미지이름명" 으로 저장한다.
			
			try {
				fis=new FileInputStream(selectFile);
				bis=new BufferedInputStream(fis);
				
			//FileChooser 에서 선택된 파일명이 c:/kkk/pppp/jjjj.송민주.jpg => selectFile
			//selectFile.getName() => 송민주.jpg만 가져온다.
			//새로운 파일명을 규정하는데 => student12345678_송민주.jpg 가 만들어진다.
			//imageDir.getAbsolutePath() + "\\" + fileName => C://images/student12345678_송민주.jpg 이름으로 저장한다.
			//	c:/kkkk/pppp/jjjj/송민주.jpg 파일을 읽어서 c://images/student12345678_송민주.jpg 이렇게 저장된다.
				
				fileName="pet"+System.currentTimeMillis()+""+selectFile.getName();
				fos=new FileOutputStream(imageDir.getAbsolutePath()+"\\"+fileName);
				bos=new BufferedOutputStream(fos);
				
				int data=1;
				while(((data=bis.read())!=-1)) {
				bos.write(data);
				bos.flush();
					
				}
			} catch (Exception e) {
				
				
			}finally {
				try {
				if(fis != null) {fis.close();}
				if(bis != null) {bis.close();}
				if(fos != null) {fos.close();}
				if(bos != null) {bos.close();}
					
				}catch(IOException e){}
			}// end of finally
		}
			
		// 이미지 삭제 --- 
		private void imageDelete() {
			Profile selectedPet=pTableView.getSelectionModel().getSelectedItem();
			File imageFile=new File(imageDir.getAbsolutePath()+"\\"+selectedPet.getImgpath() );
			boolean delFlag = false;
			if(imageFile.exists() && imageFile.isFile()) {
				delFlag=imageFile.delete();
				if(delFlag==false) {
					//System.out.println("이미지 제거 실패: 이미지 제거 실패 점검바람");
				}			
			}
		}
		
		///////////////////////////////////////////////////////////////////////////////////// 
	
		// 입력값 필드 포맷 설정 기능: 숫자 8 자리 제한___ 비밀번호, 생년월일
		private void inputDecimalFormatEightDigit(TextField textField) {

			DecimalFormat format = new DecimalFormat("########");
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
				// 리턴값이 null 이거나,입력한길이와구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 9이면(8자리를 넘었음을 뜻함.)
				// 이면 null 리턴함.
				if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
						|| event.getControlNewText().length() == 9) {
					return null;
				} else {
					return event;
				}
			}));
			
		}

	
		////영어,한글만 쓰이도록하는 함수
		ChangeListener<String> inputDecimalFormatName = (observable, oldValue, newValue) -> {

		      if (newValue != null && !newValue.equals("")) {

		         if (!newValue.matches("\\D*") || newValue.length() > 10) {

		            ((StringProperty) observable).setValue(oldValue);
		         }
		      }
		   };
		

	
}
