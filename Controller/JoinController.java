package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import Model.Join;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class JoinController implements Initializable{
	public Stage joinStage;
	
	@FXML private TextField jTxtEmail;
	@FXML private TextField jTxtName;
	@FXML private TextField jTxtBirth;
	@FXML private TextField jTxtNickName;
	@FXML private TextField jTxtPhone;
	
	@FXML private PasswordField jTxtPassWord;
	@FXML private PasswordField jTxtPassWordOk;
	@FXML private ComboBox<String> jCmbGender; // 콤보박스_여성,남성
	@FXML private Button jBtnRegister;
	@FXML private Button jBtnCheck;
	@FXML private Button jBtnPwCheck;
	
	
	ObservableList<Join> joinMemberShip=FXCollections.observableArrayList();
	ObservableList<String> jListGender=FXCollections.observableArrayList();

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		
		//01.콤보박스에 성별을 셋팅한다.
		setComboBoxGender();
		
		//02. 텍스트필드 입력값 포멧설정___① 아이디는 @ 포함, ② 비밀번호,생년월일 8자리만 입력받도록
		setTextFieldInputFormat();
			
		//03.보호자 정보 입력값 제한
		jTxtName.textProperty().addListener(inputDecimalFormatName); 	// 영어, 한글만 입력, 5글자 안으로
		jTxtNickName.textProperty().addListener(inputDecimalFormatName); 	
		inputDecimalFormatElevenDigit(jTxtPhone); 		//핸드폰 번호 11자리 제한 
		
				
		//04. 아이디 중복확인___ 아이디를 DB에서 비교해서 중복되는지 확인하고, 이메일 형식에 맞는지도 확인 한 다음에 값을 입력한다.
		jBtnCheck.setOnAction(e -> hadlejBtnIDcheckAction() );

		//05. 비밀번호 확인
		jBtnPwCheck.setOnAction(e->matchPasswordField()  );
		
		//06.완료버튼을 누르면 화면이 로그인 화면으로 다시 전환된다. + membershipDB 에 그 내용이 저장된다.
		jBtnRegister.setOnAction(e -> handlejBtnRegister() );

		
		
	}

	//01.콤보박스에 성별을 셋팅한다.
	private void setComboBoxGender() {
		jListGender.addAll("남성","여성");
		jCmbGender.setItems(jListGender);		
	}

	
	//02. 텍스트필드 입력값 포멧설정___① 이메일은 @ 포함, ② 비밀번호,생년월일 8자리만 입력받도록
	private void setTextFieldInputFormat() {
		inputDecimalFormatEightDigit( jTxtPassWord);
		inputDecimalFormatEightDigit( jTxtPassWordOk);
		inputDecimalFormatEightDigit( jTxtBirth);
	
	}

	
	///03. 보호자 정보 입력값 제한
	ChangeListener<String> inputDecimalFormatName = (observable, oldValue, newValue) -> {

	      if (newValue != null && !newValue.equals("")) {

	         if (!newValue.matches("\\D*") || newValue.length() > 5) {

	            ((StringProperty) observable).setValue(oldValue);
	         }
	      }
	   };
	
	   
	// 04. 아이디(이메일) 중복확인___ 이메일을 DB에서 비교해서 중복되는지 확인하고, 이메일 형식에 맞는지도 확인 한 다음에 값을 입력한다.
	private void hadlejBtnIDcheckAction() {
		
		if(jTxtEmail.getText().isEmpty()) {
			RootController.callAlert("이메일 입력 오류: 빈칸 없이 입력해주시기 바랍니다.");
			return;
		}
		
		// 04_01. 이메일 텍스트필드의 값이 '@'를 포함하고 있는가를 확인하는 작업
		if (!(jTxtEmail.getText().contains("@"))) {
			RootController.callAlert("이메일 입력 오류: 이메일의 형식이 잘못되었습니다.");
			return;
		} 
		
		// 04_02. 입력받은 이메일이 DB에 이미 있는가 확인하는 작업
		if	(emailCheck(jTxtEmail.getText())) {
			RootController.callAlert("아이디 중복: 사용불가능한 아이디입니다. 다시 입력바랍니다.");
			jTxtEmail.clear();
		}else {
			RootController.callAlert("아이디 사용가능: 사용가능한 아이디입니다.");
			jTxtEmail.setDisable(true);
			jBtnCheck.setDisable(true);
		}	
		
	}

	
	//04_02_01.이메일 중복확인에서 쓰이는 함수
	public boolean emailCheck(String email) {
		boolean result=false;
		
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		String sql ="select email from joinmembershiptb where email=? ";
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, email);
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				result=true;
			}else {
				result=false;
			}
			rs.close();	
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {	pstmt.close(); }
				if (con != null) {	con.close(); }
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		return result;
	}
	


	// 05. 비밀번호 확인
	private void matchPasswordField() {
		if(!(jTxtPassWord.getText().equals(jTxtPassWordOk.getText()))) {
			RootController.callAlert("비밀번호 일치 오류: 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
			jTxtPassWord.clear(); 
			jTxtPassWordOk.clear();
		}else if(jTxtPassWord.getText().isEmpty() 	||	jTxtPassWordOk.getText().isEmpty() ){
			RootController.callAlert("비밀번호입력 오류: 빈칸 없이 입력해주세요.");
		}else {
			RootController.callAlert("비밀번호 일치: 비밀번호가 일치합니다.");
			jTxtPassWord.setDisable(true);
			jTxtPassWordOk.setDisable(true);
			jBtnPwCheck.setDisable(true);
		}
	}


	

	
	//06.완료버튼을 누르면 화면이 로그인 화면으로 다시 전환된다. + membershipDB 에 그 내용이 저장된다.
	private void handlejBtnRegister() {	
		
		if( jBtnCheck.isDisable() &&  jBtnPwCheck.isDisable()) {
			
			if(jTxtEmail.getText().isEmpty() ||
					jTxtPassWord.getText().isEmpty() ||
					jTxtName.getText().isEmpty() ||
					jTxtBirth.getText().isEmpty() ||
					jCmbGender.getSelectionModel().isEmpty() ||
					jTxtNickName.getText().isEmpty() ||
					jTxtPhone.getText().isEmpty()) {
				
				RootController.callAlert("회원가입 오류: 빈칸없이 모두 입력바랍니다.");
				return;
			}
			
			try {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
				Parent root = loader.load();
				
				RootController rootController=loader.getController();
				rootController.primaryStage=primaryStage;
				
				Scene scene=new Scene(root);
				primaryStage.setScene(scene);
				joinStage.close();
				primaryStage.show();
				
				
				Join join=new Join(	
						jTxtEmail.getText(),
						jTxtPassWord.getText(),
						jTxtName.getText(),
						jTxtBirth.getText(),
						jCmbGender.getSelectionModel().getSelectedItem(),
						jTxtNickName.getText(),
						jTxtPhone.getText()		
						);				
				joinMemberShip.add(join);
				
				
				int count=JoinDAO.insertJoinData(join);
				if(count!=0) {
					System.out.println("입력성공: 데이터베이스 입력이 성공");
					RootController.callAlert("회원가입완료: 회원가입이 완료되었습니다.");
				}
				
			} catch (IOException e) {
				System.out.println("화면전환 오류: 화면전환 문제발생");						
			}		
		}else{
				RootController.callAlert( "중복확인 및 비밀번호확인 : 아이디 중복확인과 비밀번호확인을 반드시 진행해주세요.");
		}
		
		
	}

	
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
	
	// 입력값 필드 포맷 설정 기능: 숫자 11 자리 제한___ 핸드폰번호
	private void inputDecimalFormatElevenDigit(TextField textField) {

			DecimalFormat format = new DecimalFormat("###########");
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
						|| event.getControlNewText().length() == 12 || event.getControlNewText()=="-") {
					return null;
				} else {
					return event;
				}
			}));
			
		}

	
}
