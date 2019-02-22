package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Join;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable {
	public Stage primaryStage;
	public static String EMAIL;
	@FXML private TextField txtId;
	
	@FXML private TextField txtPassWord;
	@FXML private Button btnLogin;
	@FXML private Button btnJoin;
	@FXML private Button btnFindId;
	@FXML private Button btnFindPassWord;
	@FXML private ImageView imgViewLogo;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//00. 이미지 버튼을 누르면 아이디와 비밀번호가 자동으로 세팅되는 기능(테스트를 빨리 하려고)
		imgViewLogo.setOnMouseClicked(e -> clickImgLoginSetting() );
	
		// 00. 회원가입 버튼을 누르면 회원가입 창이 뜬다.
		btnJoin.setOnAction(e-> handleBtnJoinAction() );
		
		// 01. 로그인 버튼을 누르면 메인화면으로 창이 바뀐다.
		btnLogin.setOnAction(e -> handleBtnLoginAction() );
			
		//02. 아이디찾기 버튼을 누르면 아이디찾기 창이 뜬다. __ 다이얼로그창
		btnFindId.setOnAction(e -> handleBtnFindIdAction() );

		//03. 비밀번호찾기 버튼을 누르면 비밀번호찾기 창이 뜬다.__ 다이얼로그창
		btnFindPassWord.setOnAction(e->handleBtnFindPassWordAction()  );
		
	}

	//00. 이미지 버튼을 누르면 아이디와 비밀번호가 자동으로 세팅되는 기능(테스트를 빨리 하려고)
	private void clickImgLoginSetting() {
		txtId.setText("aaa@naver.com");
		txtPassWord.setText("12345678");
		
	}


	// 00. 회원가입 버튼을 누르면 회원가입 창이 뜬다.
	private void handleBtnJoinAction() {
		try {
			Stage joinStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/join.fxml"));
			Parent root = loader.load();
			
			JoinController joinController=loader.getController();
			joinController.joinStage=joinStage;
			
			Scene scene=new Scene(root);
			joinStage.setScene(scene);
			primaryStage.close();
			joinStage.setTitle("회원가입");
			joinStage.show();
		
		} catch (Exception e) {
			e.printStackTrace();
			callAlert("화면 전환 오류: 화면 전환에 문제 발생");
		}

		
	}

	
	// 01. 로그인 버튼을 누르면 메인화면으로 창이 바뀐다.
	private void handleBtnLoginAction() {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		String email=txtId.getText().trim();
		String pw= txtPassWord.getText().trim();	
		
		String sql = "select email, password " + "from joinmembershiptb where email=? and password=? ";
		
		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				email=rs.getString("email");
				callAlert("로그인 성공: 로그인 성공하였습니다.");

				EMAIL = email;
				try {
					Stage mainStage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main.fxml"));
					Parent root = loader.load();
								
					MainController mainController=loader.getController();
					mainController.mainStage=mainStage;
					
					Scene scene=new Scene(root);
					mainStage.setScene(scene);
					primaryStage.close();
					mainStage.setTitle("마이페이지");
					mainStage.show();
					
				} catch (IOException e) {
					callAlert("화면 전환 오류: 메인화면 전환에 문제 발생");
				}

				}else {
					callAlert("로그인 실패:로그인실패하였습니다." );
					
				}
			
				rs.close();
				
		
		} catch (Exception e1) {
			System.out.println("쿼리문 실패 : email, password 비교에 실패했습니다. 점검바람");
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
			} catch (SQLException e) {
				RootController.callAlert("자원닫기 실패 : psmt,con 닫기실패");
			}
		}
	}


	//02. 아이디찾기 버튼을 누르면 아이디찾기 창이 뜬다. __ 다이얼로그창
	private void handleBtnFindIdAction() {
		String id=null;
		//ArrayList <Join> dbArrayListJoin;
		try {
			Stage idSearchStage = new Stage(StageStyle.UTILITY);
			idSearchStage.initModality(Modality.WINDOW_MODAL);
			idSearchStage.initOwner(primaryStage);
			idSearchStage.setTitle("아이디 찾기");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/idsearch.fxml"));
			Parent root = loader.load();
			
			
			// id 찾아오기
			TextField idStxtName=(TextField)root.lookup("#idStxtName");
			TextField idStxtPhone=(TextField)root.lookup("#idStxtPhone");
			Button idSbtnOk=(Button)root.lookup("#idSbtnOk");
			
			
			//02_01.휴대폰번호 11자리 제한 
			inputDecimalFormatElevenDigit(idStxtPhone); 
			
			Scene scene=new Scene(root);
			idSearchStage.setScene(scene);
			idSearchStage.show();
			
			idSbtnOk.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					
					Connection con=null;
					PreparedStatement pstmt=null;
					ResultSet rs=null;
	
					String idSname=idStxtName.getText().trim();
					String idSphone= idStxtPhone.getText().trim();	
					
					String sql = "select email " + "from joinmembershiptb where name=? and phone=? ";
					
					try {				
						con = DBUtility.getConection();
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, idSname);
						pstmt.setString(2, idSphone);
						
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							rs.getString("email");
					
							RootController.callAlert("아이디 찾기 완료:"+rs.getString("email"));				
							txtId.setText(rs.getString("email"));
							idSearchStage.close();
						}else {
							callAlert("아이디 찾기 결과: 찾으시는 아이디가 없습니다.");
						}
						
					
					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
										
					} //end of  try-catch (DBUtility.getConnecton() 의 try-catch)

				}
			});
			
		} catch (IOException e) { 
			System.out.println("아이디찾기 창이 제대로 안떴습니다.");
		}

	}



	//03. 비밀번호찾기 버튼을 누르면 비밀번호찾기 창이 뜬다.__ 다이얼로그창
	private void handleBtnFindPassWordAction() {
		try {
			Stage pwSearchStage = new Stage(StageStyle.UTILITY);
			pwSearchStage.initModality(Modality.WINDOW_MODAL);
			pwSearchStage.initOwner(primaryStage);
			pwSearchStage.setTitle("비밀번호 찾기");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/passwordsearch.fxml"));
			Parent root = loader.load();
			
			
			// id 찾아오기
			TextField pwStxtName=(TextField)root.lookup("#pwStxtName");
			TextField pwStxtEmail=(TextField)root.lookup("#pwStxtEmail");
			TextField pwStxtBirth=(TextField)root.lookup("#pwStxtBirth");
			TextField pwStxtPhone=(TextField)root.lookup("#pwStxtPhone");
			Button pwSbtnOk=(Button)root.lookup("#pwSbtnOk");
			Button pwSbtnIdSearch=(Button)root.lookup("#pwSbtnIdSearch");
			
			Scene scene=new Scene(root);
			pwSearchStage.setScene(scene);
			pwSearchStage.show();
				

			
			//03_02. 생년월일 8자리
			inputDecimalFormatEightDigit(pwStxtBirth);
			
			//03_03. 휴대폰번호 11자리
			inputDecimalFormatElevenDigit(pwStxtPhone);
			
			pwSbtnOk.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Connection con=null;
					PreparedStatement pstmt=null;
					ResultSet rs= null;
					
					String sql= "select password "+"from joinmembershiptb where name=? and email=? and birth=? and phone=? " ;
					
					try {
						con=DBUtility.getConection();
						pstmt=con.prepareStatement(sql);
					
						pstmt.setString(1, pwStxtName.getText().trim());
						pstmt.setString(2, pwStxtEmail.getText().trim());
						pstmt.setString(3, pwStxtBirth.getText().trim());
						pstmt.setString(4, pwStxtPhone.getText().trim());
					
						rs=pstmt.executeQuery();
						
						if(rs.next()) {
							rs.getString("password");
							
							callAlert("비밀번호 찾기 완료: "+rs.getString("password") );
							pwStxtName.clear();
							pwStxtEmail.clear();
							pwStxtBirth.clear();
							pwStxtPhone.clear();
							
						}else {
							callAlert("비밀번호 찾기 결과: 찾으시는 비밀번호가 없습니다.");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				} 
			}); // end of pwSbtnOk.setOnAction(event ~~)
			
			
			// 아이디가 생각나지 않는다면 -> 아이디찾기 창으로 전환
			pwSbtnIdSearch.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					pwSearchStage.close();
					 handleBtnFindIdAction();
					
				}
			});
				
		} catch (Exception e) { 	
			System.out.println("패스워드 찾기 창이 제대로 안떴습니다.");
		}
	}

	
	//기타: 알림창(중간에 꼭 콜론을 적어줄 것)  예시-- "오류정보 : 값을 제대로 입력해주세요."
	public static void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
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
					|| event.getControlNewText().length() == 12 ||  event.getControlNewText()=="-") {
				return null;
			} else {
				return event;
			}
		}));
		
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
	
	
	
}
