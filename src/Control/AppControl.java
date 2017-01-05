package Control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import __class.ApiURL;
import __class.Group;
import __class.GroupList;
import __class.Person;
import __class.config;
import application.MainApplication;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AppControl {

    private TextField ChaMate;
	private FlowPane flow;
	private String Groupname;
	/**
	 * 添加好友
	 * 添加群
	 * 创建群 
	 * @param event
	 */
	/**
	 * @param event
	 */
	@FXML
    void AppClickAddFriend(MouseEvent event) {
    	System.out.println("AddFriend");
    	 final Label label = new Label("查找");
    	 ChaMate = new TextField();
    	 Button MateBtn = new Button("查好友");
    	 Button GroupBtn = new Button("查  群");
    	 Button CreateGroupBtn = new Button("建  群");
    	 Button BackBtn = new Button("返  回");
    	 HBox Hbox = new HBox();
    	 Hbox.setSpacing(10);
    	 Hbox.getChildren().addAll(ChaMate,MateBtn,GroupBtn,CreateGroupBtn,BackBtn);
         label.setFont(new Font("Arial", 20));
         
        
         flow = new FlowPane();
         flow.setPrefWidth(1000);
         flow.setPadding(new Insets(30));
         
         final VBox vbox = new VBox();
         vbox.setSpacing(30);
         vbox.setPadding(new Insets(10, 10, 10, 10));
         vbox.getChildren().addAll(label,Hbox,flow);
         ScrollPane sp = new ScrollPane();
 		 sp.setId("Mate");
 		 sp.setVbarPolicy(ScrollBarPolicy.NEVER);
 		 sp.setHbarPolicy(ScrollBarPolicy.NEVER);
 		 sp.setPrefWidth(1000);
         sp.setContent(vbox);
    	 MainApplication.Main.setCenter(sp);
    	 MainApplication.Main.setLeft(null);
    	 
    	 
    	 //创建群按钮时间事件
    	 CreateGroupBtn.addEventFilter(MouseEvent.MOUSE_CLICKED , (MouseEvent e)->{
    		 
        	
             Groupname = ChaMate.getText();
             //禁止群名称输入空
             if(ChaMate.getText().trim() == null || ChaMate.getText().length() <= 0){
            	 Alert alert=new Alert(Alert.AlertType.WARNING);
                 alert.setHeaderText("提  醒");
                 alert.setContentText("请在输入框输入群名称，群名称禁止为空");
                 alert.showAndWait();
            	 return ;
             }
             //调取创建群接口
             String url = ApiURL.CREATE_GROUP.getUrl()+config.Parme+"creator="+MainApplication.fromSid+"&name="+Groupname;
             //确认创建群
    		 Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
             alert.setHeaderText("提  醒");
             alert.setContentText("确认创建"+Groupname+"群聊吗？");
             alert.showAndWait().ifPresent(response -> {
                 if (response == ButtonType.OK) {
                	 
                	 Alert alert1=new Alert(Alert.AlertType.INFORMATION);
                     alert.setHeaderText("请求中");
                     alert.setContentText("创建中 点击确认后请稍等~~~");
				     alert.showAndWait();
             		System.out.println(MainApplication.fromSid+"正在创建群名为:"+Groupname+"的群"+url);
             		Document doc1 = null;
					try {
						doc1 = Jsoup.connect(url).get();
						JSONObject json = null;
						try {
							json = new JSONObject(doc1.body().text());
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
	             		
	                     try {
							if(json != null && json.getString("code").equals("0")){
								 try {
									 //更新群列表
									new GroupList(MainApplication.fromSid);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							      alert.setContentText("创建完成快去邀请小伙伴一起聊天吧~！");
							      alert.showAndWait();
							 }else{
							     alert.setContentText("创建失败 请重试 ");
							     alert.showAndWait();
							 }
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
             		System.out.println("Create Group Doc:->" + doc1.body().text());
             		
                     
                 }
             });
    		 
    	 });
    	 
    	 //查找群添加群
    	 GroupBtn.addEventFilter(MouseEvent.MOUSE_CLICKED , (MouseEvent e)->{
    		 //禁止群号为空
    		 if(ChaMate.getText().trim() == null || ChaMate.getText().length() <= 0){
            	 Alert alert=new Alert(Alert.AlertType.WARNING);
                 alert.setHeaderText("提  醒");
                 alert.setContentText("还没有输入群号呢~~");
                 alert.showAndWait();
            	 return ;
             }
    		 String id = ChaMate.getText().trim();
    		 // 重复了 懒得改了 哈哈哈哈哈
    		 if(id == "" || id == null){
    			 Alert alert=new Alert(Alert.AlertType.ERROR);
	             alert.setHeaderText("查询失败");
	             alert.setContentText("id is ban to be empty");
	             alert.showAndWait();
    		 }else{
	    			 try {
	    				 //初始化某群信息
	    				 new Group(id);
	    				System.out.println(MainApplication.fromSid+"正在查找"+id+"群");
	    				 Map p = Group.getGroup(id);
//	    				 查看是否存在此群
	 					if(!p.containsKey("groupId") && p.get("groupId") == null){
	 						 Alert alert=new Alert(Alert.AlertType.ERROR);
	 			             alert.setHeaderText("查询失败");
	 			             alert.setContentText("您申请的群" + id + "并未注册账号。 撒撒，向抢占注册吗！~~\n快邀请小伙伴一起去群聊吧~~");
	 			             alert.showAndWait();
	 					}else{
	 						
	 						//添加信息卡片
	 						 GridPane gp = new GridPane();
	 			    		 gp = AddGP(p,"qun");
	 			    		 flow.getChildren().addAll(gp);
	 					}
	 					
	 				} catch (Exception e1) {
	 					// TODO Auto-generated catch block
	 					e1.printStackTrace();
	 				}
    		 }
    	 });
    	 
    	 //查询好友
    	 MateBtn.addEventFilter(MouseEvent.MOUSE_CLICKED , (MouseEvent e)->{
    		 if(ChaMate.getText().trim() == null || ChaMate.getText().length() <= 0){
            	 Alert alert=new Alert(Alert.AlertType.WARNING);
                 alert.setHeaderText("提  醒");
                 alert.setContentText("还没有输入好友账号哦~~");
                 alert.showAndWait();
            	 return ;
             }
    		 String sid = ChaMate.getText().trim();
    		 if(sid == "" || sid == null){
    			 Alert alert=new Alert(Alert.AlertType.ERROR);
	             alert.setHeaderText("查询失败");
	             alert.setContentText("Sid is ban to be empty");
	             alert.showAndWait();
    		 }else{
    			 try {
    				 	new Person(sid);
    				 	System.out.println(MainApplication.fromSid+"正在查找"+sid+"好友");
    					Map p = Person.getPerson(sid);
    					if(!p.containsKey("sid") && p.get("sid") == null){
    						 Alert alert=new Alert(Alert.AlertType.ERROR);
    			             alert.setHeaderText("查询失败");
    			             alert.setContentText("您申请的好友" + sid + "并未注册账号。 撒撒，快去邀请他和你一同玩耍吧！~~\n也有可能是你输入的账号错误哦~快去查同学查一查吧~~");
    			             alert.showAndWait();
    					}else{
    						
    						 GridPane gp = new GridPane();
    			    		 gp = AddGP(p);
    			    		
    			    		 flow.getChildren().addAll(gp);
    					}
    					
    				} catch (Exception e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    		 }
    		
    		 
    	 });
    	 
    	 //返回上层pane
    	 BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
    		 Parent App = null;
			try {
				App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	         MainApplication.Main.setCenter(App);
    	 });
    }
	
	//添加卡片
	 GridPane AddGP(Map p ,String type){
		GridPane gp = new GridPane();
		gp.setUserData(p.get("groupId"));
	
		gp.setAlignment(Pos.CENTER);
		Image ic = new Image(getClass().getResourceAsStream("/images/avatar.png"));		
		ImagePattern patt = new ImagePattern(ic);
		Circle cir = new Circle(35);
		cir.setFill(patt);		
		
		Label l = new Label(p.get("name")+"("+p.get("groupId")+")");
		Label s ;
	    s = new Label("群主:"+p.get("creator"));
		
		
		l.setPadding(new Insets(0,0,0,20));
		s.setPadding(new Insets(0,0,0,50));
		gp.add(l, 2, 0, 2,1);
		gp.add(s, 2, 1, 2,1);
		gp.add(cir, 0, 0, 2, 2);
		Button AddBtn = new Button("申请加群");
		gp.add(AddBtn, 2, 2,2,2);
		gp.getColumnConstraints().add(new ColumnConstraints(40));
		gp.getColumnConstraints().add(new ColumnConstraints(40));
		gp.getColumnConstraints().add(new ColumnConstraints(150));
		gp.getRowConstraints().add(new RowConstraints(30));
		gp.setMinHeight(65);
		gp.setMinWidth(230);
		gp.setStyle("-fx-background-color:#fff;-fx-background-radius:10;");
		
		//添加好友/群时间
		AddBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
	
			@Override
			public void handle(MouseEvent arg0) {
				 Alert alert=new Alert(Alert.AlertType.INFORMATION);
	             alert.setHeaderText(MainApplication.fromSid);
	             alert.setContentText(MainApplication.fromSid+"请求添加"+ p.get("groupId") +"群");
	             alert.showAndWait();
	             //发送添加请求
	             System.out.println(MainApplication.fromSid+"正在将要添加"+p.get("groupId")+"群");
	             JSONObject JsonServer = new JSONObject();

		 			try {
		 				JsonServer.put("code", "3001");
		 				JsonServer.put("status", "30011");
		 				JsonServer.put("from", MainApplication.fromSid);
		 				JsonServer.put("to",  p.get("creator"));
		 				JsonServer.put("group",  p.get("groupId"));
		 				JsonServer.put("msg",  MainApplication.fromSid+"请求添加你的群号为"+p.get("groupId")+"的群");
		 				JsonServer.put("ip", MainApplication.clientThread.ip);
		 		
		 				MainApplication.clientThread.sendMessageToServer(JsonServer);
						
		 				String url = ApiURL.MYSQL_ADD_GROUP_USER.getUrl()+config.Parme+"fromSid="+MainApplication.fromSid+"&groupId="+p.get("groupId");
		 				System.out.println(url);
		 				Document doc1 = null;
		 				try {
		 					doc1 = Jsoup.connect(url).get();
						} catch (Exception e) {
							alert.setContentText("查询失败请重试");
							alert.showAndWait();
							// TODO: handle exception
						}
		 				System.out.println(MainApplication.fromSid+"正在已调用添加"+p.get("groupId")+"群接口");
		 				JSONObject json = new JSONObject(doc1.body().text());
		 				if(json.getString("code").equals("0")){
		 					 new GroupList(MainApplication.fromSid);
		 					 alert=new Alert(Alert.AlertType.INFORMATION);
		 		             alert.setHeaderText(MainApplication.fromSid);
		 		             alert.setContentText("添加成功 快去和小伙伴聊天吧");
		 		             alert.showAndWait();
		 				}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 		 
	             
			}
			
		});
		return gp;
	}

	GridPane AddGP(Map p){
		GridPane gp = new GridPane();
		gp.setUserData(p.get("sid"));

		gp.setAlignment(Pos.CENTER);
		Image ic = null;
		try {
			 ic = new Image("http://api.kilingzhang.com/diantalk/diantalk/"+p.get("sid")+".jpg");	
		} catch (Exception e) {
			ic = new Image(getClass().getResourceAsStream("/images/1_copy.jpg"));	
			// TODO: handle exception
		}
		ImagePattern patt = new ImagePattern(ic);
		Circle cir = new Circle(35);
		cir.setFill(patt);		
		
		Label l = new Label(p.get("name")+"("+p.get("sid")+")");
		Label s ;
		if(p.get("login_status").equals("0")){
			s = new Label("离线");
		}else{
			s = new Label("在线");
		}
		
		l.setPadding(new Insets(0,0,0,20));
		s.setPadding(new Insets(0,0,0,50));
		gp.add(l, 2, 0, 2,1);
		gp.add(s, 2, 1, 2,1);
		gp.add(cir, 0, 0, 2, 2);
		Button AddBtn = new Button("申请好友");
		gp.add(AddBtn, 2, 2,2,2);
		gp.getColumnConstraints().add(new ColumnConstraints(40));
		gp.getColumnConstraints().add(new ColumnConstraints(40));
		gp.getColumnConstraints().add(new ColumnConstraints(150));
		gp.getRowConstraints().add(new RowConstraints(30));
		gp.setMinHeight(65);
		gp.setMinWidth(230);
		gp.setStyle("-fx-background-color:#fff;-fx-background-radius:10;");
		
		AddBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				 
	             //
	            
	             Alert alert=new Alert(Alert.AlertType.INFORMATION);
	             alert.setHeaderText(MainApplication.fromSid);
	             alert.setContentText(MainApplication.fromSid+"请求添加"+ p.get("sid") +"好友");
	             alert.showAndWait();
	             JSONObject JsonServer = new JSONObject();
//		 		 System.out.println(type);
		 			try {
		 				JsonServer.put("code", "1001");
		 				JsonServer.put("status", "100104");
		 				JsonServer.put("from", MainApplication.fromSid);
		 				JsonServer.put("to",  p.get("sid"));
		 				JsonServer.put("ip", MainApplication.clientThread.ip);
		 		     
		 				MainApplication.clientThread.sendMessageToServer(JsonServer);
		 				System.out.println(MainApplication.fromSid+"正在请求添加"+ p.get("sid")+"好友");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 		 
	             
			}
			
		});
		return gp;
	}

	@FXML public void AppClick(MouseEvent event) {
		
	}

	//查同学 调用查同学页面
	@FXML public void AppClickChatMate(MouseEvent event) {
		Platform.runLater(new Runnable() {

			Alert alert = new Alert(AlertType.ERROR);
		
			private WebView w;
			private WebEngine Cha;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(1 == 1){
					alert.setTitle("公告");
					alert.setContentText("由于教务最近不稳定，此功能暂时关闭  感谢支持");
					alert.show();
					return ;
				}
				w = new WebView();
				Cha = w.getEngine();
				Cha.load(ApiURL.DianCha.getUrl()+"mate.php"+"?sid="+MainApplication.fromSid+"&password="+MainApplication.password);
				Button BackBtn = new Button("返  回");
		    	VBox Hbox = new VBox();
		    	Hbox.setSpacing(10);
		    	Hbox.getChildren().addAll(BackBtn,w);
				MainApplication.Main.setCenter(Hbox);
				BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
		    		 Parent App = null;
					try {
						App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
			         MainApplication.Main.setCenter(App);
		    	 });
			}
		
		});
	}

	@FXML public void AppClickEmpty(MouseEvent event) {
		Platform.runLater(new Runnable() {

			Alert alert = new Alert(AlertType.ERROR);
			
			private WebView w;
			private WebEngine Cha;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(1 == 1){
					alert.setTitle("公告");
					alert.setContentText("由于教务最近不稳定，此功能暂时关闭  感谢支持");
					alert.show();
					return ;
				}
				w = new WebView();
				Cha = w.getEngine();
				Cha.load(ApiURL.DianCha.getUrl()+"empty_room.php"+"?sid="+MainApplication.fromSid+"&password="+MainApplication.password);
				Button BackBtn = new Button("返  回");
		    	VBox Hbox = new VBox();
		    	Hbox.setSpacing(10);
		    	Hbox.getChildren().addAll(BackBtn,w);
				MainApplication.Main.setCenter(Hbox);
				BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
		    		 Parent App = null;
					try {
						App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
			         MainApplication.Main.setCenter(App);
		    	 });
			}
		
		});
	}

	@FXML public void AppClickEmxa(MouseEvent event) {
		Platform.runLater(new Runnable() {

			private WebView w;
			private WebEngine Cha;

			@Override
			public void run() {
				w = new WebView();
				Cha = w.getEngine();
				Cha.load(ApiURL.DianCha.getUrl()+"exam_arrange.php"+"?sid="+MainApplication.fromSid+"&password="+MainApplication.password);
				Button BackBtn = new Button("返  回");
		    	VBox Hbox = new VBox();
		    	Hbox.setSpacing(10);
		    	Hbox.getChildren().addAll(BackBtn,w);
				MainApplication.Main.setCenter(Hbox);
				BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
		    		 Parent App = null;
					try {
						App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
			         MainApplication.Main.setCenter(App);
		    	 });
				
			}
		
		});
	}

	@FXML public void AppClickEcard(MouseEvent event) {
		Platform.runLater(new Runnable() {

			private WebView w;
			private WebEngine Cha;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				w = new WebView();
				Cha = w.getEngine();
				Cha.load(ApiURL.DianCha.getUrl()+"grade_details.php"+"?sid="+MainApplication.fromSid+"&password="+MainApplication.password);
				Button BackBtn = new Button("返  回");
		    	VBox Hbox = new VBox();
		    	Hbox.setSpacing(10);
		    	Hbox.getChildren().addAll(BackBtn,w);
				MainApplication.Main.setCenter(Hbox);
				BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
		    		 Parent App = null;
					try {
						App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
			         MainApplication.Main.setCenter(App);
		    	 });
				
			}
		
		});
	}

	@FXML public void AppClickCourse(MouseEvent event) {
		Platform.runLater(new Runnable() {

			private WebView w;
			private WebEngine Cha;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				w = new WebView();
				Cha = w.getEngine();
				Cha.load(ApiURL.DianCha.getUrl()+"course.php"+"?sid="+MainApplication.fromSid+"&password="+MainApplication.password);
				Button BackBtn = new Button("返  回");
		    	VBox Hbox = new VBox();
		    	Hbox.setSpacing(10);
		    	Hbox.getChildren().addAll(BackBtn,w);
				MainApplication.Main.setCenter(Hbox);
				BackBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
		    		 Parent App = null;
					try {
						App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
			         MainApplication.Main.setCenter(App);
		    	 });
				
			}
		
		});
	}


	public class Mate {

		/**	
		 * 
		 */
		
		
		private final SimpleStringProperty Sid;
	    private final SimpleStringProperty Name;
	    private final SimpleStringProperty _Class;
	    
		


	    private  Mate(String Sid,String Name,String _Class) {
	    	this.Sid = new SimpleStringProperty(Sid);
            this.Name = new SimpleStringProperty(Name);
            this._Class = new SimpleStringProperty(_Class);

			// TODO Auto-generated constructor stub
		}



		/**
		 * @return the sid
		 */
		public SimpleStringProperty getSid() {
			return Sid;
		}



		/**
		 * @return the name
		 */
		public SimpleStringProperty getName() {
			return Name;
		}



		/**
		 * @return the _Class
		 */
		public SimpleStringProperty get_Class() {
			return _Class;
		}
	    
	    
	    
	}

	

}
