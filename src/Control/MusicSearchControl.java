package Control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import __class.ApiURL;
import application.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MusicSearchControl {

	  @FXML
	    private TextField MusicName;

	    @FXML
	    private Button SearchMusic;

	    @FXML
	    private Label MusicTipLable;

	    @FXML
	    private ScrollPane MusicScrolPane;
	    
	    @FXML
	    private AnchorPane MusicListAPane;
	    
	    @FXML
	    private Stage MusicStage;
	    
	    @FXML
	    void Initializable(){
	    	MusicStage = new Stage();
	    }
	    

    @FXML
    void SearchMusic(MouseEvent event) throws IOException, JSONException {
    	String name = MusicName.getText().trim();
    	if(name.length() <= 0){
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("小贴士");
    		alert.setContentText("不输入歌名怎么分享呢~~~");
    		alert.showAndWait();
    		return;
    	}
    
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("小贴士");
		alert.setContentText("加载中 ····  好酒不怕巷子深~~~");
		alert.show();
		name = URLEncoder.encode(name,"UTF-8");
		 Document doc1 = null; 
		try {
	    	doc1  = Jsoup.connect(ApiURL.MUSICURL.getUrl()+name+"&limit=5").timeout(10000).get();
		} catch (Exception e) {
			// TODO: handle exception
		    alert = new Alert(AlertType.ERROR);
			alert.setTitle("Network anomaly   错误地址:Search Music");
			alert.setContentText("接口服务器又炸了，日 ，网络不稳定我也没办法，重试下~~~~");
			alert.showAndWait();
		}
    	System.out.println("searching music "+ name);
		JSONObject json = new JSONObject(doc1.body().text());
		JSONArray data  = null;
		try {
				 data  = json.getJSONArray("data");
		} catch (Exception e) {
			alert.setContentText("没查到哎  换一首歌曲怎么样~~~");
			alert.showAndWait();
			return ;
			// TODO: handle exception
		}
		System.out.println("Music resluts ->"+data.toString());
		VBox v = new VBox();
		//add Music infomation card
		for (int i = 0; i < data.length(); i++) {
			JSONObject d  = (JSONObject) data.get(i);
	    	GridPane gp = new GridPane();
			gp.setMinWidth(340);
			System.out.println(d.getString("url").toString());
			gp.setUserData(d.getString("url").toString());
//			gp.setGridLinesVisible(true);
			gp.setPadding(new Insets(20));
			gp.setAlignment(Pos.BASELINE_LEFT);
			Circle ShareBtn = new Circle(15);
			ShareBtn.setFill(Color.PINK);
			
			Label na = new Label(d.getString("name").toString());
			na.setFont(Font.font(23));
			na.setPadding(new Insets(20,0,0,10));
			MainApplication.name.add(na);
			Label ms = new Label(d.getString("singer").toString()+"\n\r"+d.getString("singer_briefDesc").toString());
			ms.setPadding(new Insets(0,0,0,15));
			MainApplication.msg.add(ms);   		
//			System.out.println(d.getString("singer_pic").toString());
			if(d.getString("singer_pic").toString() == null || d.getString("singer_pic").toString().length() <= 0){
				continue;
			}
			Image ic = new Image(d.getString("singer_pic").toString());
			ImagePattern patt = new ImagePattern(ic);
			Circle cir = new Circle(35);
			cir.setFill(patt);		
			gp.add(ShareBtn, 0, 0, 1, 2);
			gp.getColumnConstraints().add(new ColumnConstraints(100));
			gp.getRowConstraints().add(new RowConstraints(50));
			gp.getRowConstraints().add(new RowConstraints(50));
			gp.add(na, 2, 0, 3, 1);
			gp.add(ms, 2, 1, 3, 1);
			gp.add(cir, 1, 0, 1, 2);
			v.getChildren().add(gp);
			
			//share Music to friend
			ShareBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
				JSONObject JsonServer = new JSONObject();
				String con = null;
				if(MainApplication.type.equals("friendlist")){
					try {
						con = "您的好友"+MainApplication.fromSid+"为您分享了音乐"+ms.getText().toString()+"的"+na.getText().toString();
		 				JsonServer.put("code", "1004");
		 				JsonServer.put("status", "100401");
		 				JsonServer.put("from", MainApplication.fromSid);
		 				JsonServer.put("to",  MainApplication.toSid);
		 				JsonServer.put("ip", MainApplication.clientThread.ip);
		 				JsonServer.put("data", d.toString());
		 				JsonServer.put("msg", con);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						
						e1.printStackTrace();
					}
				}else if (MainApplication.type.equals("grouplist")){
					try {
						con = "您的好友"+MainApplication.fromSid+"为您分享了音乐"+ms.getText().toString()+"的"+na.getText().toString();
		 				JsonServer.put("code", "1004");
		 				JsonServer.put("status", "100402");
		 				JsonServer.put("from", MainApplication.fromSid);
		 				JsonServer.put("to",  MainApplication.toSid);
		 				JsonServer.put("ip", MainApplication.clientThread.ip);
		 				JsonServer.put("data", d.toString());
		 				JsonServer.put("msg", con);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						
						e1.printStackTrace();
					}
				}
			
	
	 			try {
	 				//send share request
					MainApplication.clientThread.sendMessageToServer(JsonServer);
		 			System.out.println(con );
					MainApplication.MusicStage.close();
					Alert al=new Alert(Alert.AlertType.INFORMATION);
					al.setHeaderText("音乐分享");
					al.setContentText("分享成功~~~~相信好友看到分享一定会很开心的~~");
					al.showAndWait();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	 			
	 			
			});
	    
		}
		MusicScrolPane.setContent(v);
		alert.close();
    	
    }
    
    

}
