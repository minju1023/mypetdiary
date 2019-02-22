package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable{
	public Stage mainStage;
	
	@FXML private Button mBtnProfile;
	@FXML private Button mBtnDiary;
	@FXML private Button mBtnHospital;
	@FXML private Button mBtnFeed;
	@FXML private ImageView mImgViewBack;
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//01. 프로필 등록을 누르면 프로필 등록 화면이 뜬다.
		mBtnProfile.setOnAction( e-> handleMbtnProfileAction() );
		
		
		//02. 다이어리 버튼을 누르면 다이어리(달력) 화면이 뜬다.
		mBtnDiary.setOnAction(e -> handleMbtnDiaryAction() );
				
		
		//03. 병원 버튼을 누르면 병원리스트 화면이 뜬다.
		mBtnHospital.setOnAction(e-> handleMbtnHospitalAction() );
		
			
		//04. 사료 버튼을 누르면 사료성분분석 화면이 뜬다.
		mBtnFeed.setOnAction(e -> handleMbtnFeedAction() );
		
		
		//00. 화살표(←) 를 누르면 다시 로그인 화면으로 돌아간다.
		mImgViewBack.setOnMouseClicked( e-> handleMimgViewBackAction() );
		
	
	}
	

	//01. 프로필 등록을 누르면 프로필 등록 화면이 뜬다.
	private void handleMbtnProfileAction() {

		try {
			Stage profileStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/profile_profiletable.fxml"));
			Parent root = loader.load();
		
			
			ProfileController profileController=loader.getController();
			profileController.profileStage=profileStage;
			
			Scene scene=new Scene(root);
			profileStage.setScene(scene);
			System.out.println("화면전환: 프로필등록 화면으로 전환됩니다.");
			profileStage.setTitle("프로필");
			profileStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	
	//02. 다이어리 버튼을 누르면 다이어리(달력) 화면이 뜬다.
	private void handleMbtnDiaryAction() {
		try {
			Stage diaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/diary_edited.fxml"));
			Parent root = loader.load();
			 
			DiaryController diaryController=loader.getController();
			diaryController.diaryStage=diaryStage;
			
			Scene scene = new Scene(root);
			diaryStage.setScene(scene);
			mainStage.close();
			diaryStage.show();

		} catch (IOException e) {
			System.out.println("다이어리 화면이 제대로 안떴습니다.");
			e.printStackTrace();
		}

	}

	// 03. 병원 버튼을 누르면 병원리스트 화면이 뜬다.
	private void handleMbtnHospitalAction() {
		try {
			Stage hospitalStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/hospital.fxml"));
			Parent root = loader.load();
			 
			HospitalController hospitalController=loader.getController();
			hospitalController.hospitalStage=hospitalStage;
			
			Scene scene = new Scene(root);
			hospitalStage.setScene(scene);
			hospitalStage.setTitle("병원검색");
			hospitalStage.show();

		} catch (IOException e) {
			System.out.println("병원검색 화면이 제대로 안떴습니다.");
			e.printStackTrace();
		}

		
	}


	//04. 사료 버튼을 누르면 사료성분분석 화면이 뜬다.
	private void handleMbtnFeedAction() {
		try {
			Stage catFeedStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/cat_feed.fxml"));
			Parent root = loader.load();
			 
			CatfeedController catFeedController=loader.getController();
			catFeedController.catFeedStage=catFeedStage;
			
			Scene scene = new Scene(root);
			catFeedStage.setScene(scene);
			//mainStage.close();
			catFeedStage.setTitle("사료성분분석표");
			catFeedStage.show();

		} catch (IOException e) {
			System.out.println("사료성분분석표 화면이 제대로 안떴습니다.");
			e.printStackTrace();
		}

		
	}

	
	
	
	

	//00. 화살표(←) 를 누르면 다시 로그인 화면으로 돌아간다.
	private void handleMimgViewBackAction() {

		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
			Parent root = loader.load();
						
			RootController rootController=loader.getController();
			rootController.primaryStage=primaryStage;
			
			Scene scene=new Scene(root);
			primaryStage.setScene(scene);
			System.out.println("화면전환: 로그인 화면으로 전환됩니다.");
			mainStage.close();
			primaryStage.setTitle("로그인");
			primaryStage.show();
			
		} catch (IOException e) {
			System.out.println("화면 전환 오류: 로그인화면 전환에 문제 발생");
		}
		
	}

}
