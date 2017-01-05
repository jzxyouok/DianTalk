package application;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Control.TopTitleControl;
import __class.ApiURL;

import __class.DB;
import __class.FriendList;
import __class.Group;
import __class.GroupList;
import __class.Message;
import __class.Person;
import __class.config;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
 
public class MainApplication extends Application {
	private static TrayIcon trayIcon;
	int FriendCount = 0;
	public static List<GridPane> MessageListItem = new ArrayList<GridPane>();
	public static List<Label> close = new ArrayList<Label>();
	public static List<Label> name = new ArrayList<Label>();
	public static List<Label> msg = new ArrayList<Label>();
	public static List<Image> ico = new ArrayList<Image>();
	public static List<ImagePattern> pattern = new ArrayList<ImagePattern>();
	private List<Circle> circle = new ArrayList<Circle>();
	protected double xOffset;
	protected double yOffset;
	public static WebView TalkVeiw;
	public static WebEngine Talk;
	public static BorderPane Main;
	public static String fromSid;
	public static String toSid;
	public static String SHOWHTML;
	public static String SHOWHTML_start;
	public static  String SHOWHTML_end;
	public static  String SHOWHTML_main = "";
	public static List <String>msgList;
	public static Map<String,List> MsgMap;
	public static String msgString = null;
	public static int LoginStatus = 0;
	public static Stage primaryStage;
	private long Strat_time;
	private int time_status = 0;
	private String Stage_size_status = "Min";
	private String ChatDay = "欢迎来到·DianTalk";
	public static Label newMsg;
	private ImageView ioc;
	public static Slider Volume;
	public static Stage MusicStage;
	public static Label CloseMusicLable;
	public static Label PalyMusciLable;
	public static Label MusicLable;
	public static Node MainTip;
	public static Label Mainname;
	public static Label Mainimg;
	public static String fromName;
	public static String toName;
	public static ScrollPane sp;
	public static int flag = 0;
	public static ClientThread clientThread;
	public static List<Message> nearlyMessageList;
	public static HTMLEditor htmlEditor;
	public static Alert alert;
	public static String type;
	public static int FriendStatus;
	public static String password;
	public static List<? extends org.dom4j.Node> friendList;
	protected static Media media;
	protected static MediaPlayer mediaPlayer;
	public static Stage InfoStage;
	public static int enableTrayStatus;
	public static String from;
	
	
	//右小角,最小化.
		public static void enableTray(final Stage stage) {
			if(enableTrayStatus == 1){
				Platform.setImplicitExit(false); 
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();
					}
				});
			}else{
				
			}
			enableTrayStatus = 1;
			PopupMenu popupMenu = new PopupMenu();		
			java.awt.MenuItem openItem = new java.awt.MenuItem("Show");
			java.awt.MenuItem hideItem = new java.awt.MenuItem("Min");
			java.awt.MenuItem quitItem = new java.awt.MenuItem("Exit");
			
			
			ActionListener acl = new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();
					Platform.setImplicitExit(false); //多次使用显示和隐藏设置false
					 JSONObject JsonServer = new JSONObject();
					if (item.getLabel().equals("Exit")) {
						SystemTray.getSystemTray().remove(trayIcon);
						try {
							JsonServer.put("code", "0002");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    				try {
							MainApplication.clientThread.sendMessageToServer(JsonServer);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Platform.exit();
						System.exit(0);
						return;
					}
					if (item.getLabel().equals("Show")) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.show();
							}
						});
					}
					if (item.getLabel().equals("Min")) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.hide();
							}
						});
					}
	 
				}
	 
			};
	 
	                //双击事件方法
			MouseListener sj = new MouseListener() {
			
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					// TODO Auto-generated method stub
					Platform.setImplicitExit(false); //多次使用显示和隐藏设置false
					if (e.getClickCount() == 2) {
						if (stage.isShowing()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									stage.hide();
								}
							});
						}else{
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									stage.show();
								}
							});
						}					
					}
				}
				@Override
				public void mouseEntered(java.awt.event.MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseExited(java.awt.event.MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mousePressed(java.awt.event.MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseReleased(java.awt.event.MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			};
	 
	 
	 
			openItem.addActionListener(acl);
			quitItem.addActionListener(acl);
			hideItem.addActionListener(acl);
	 
			popupMenu.add(openItem);
			popupMenu.add(hideItem);
			popupMenu.add(quitItem);
	 
			try {
				
				SystemTray tray = SystemTray.getSystemTray();
				BufferedImage image = ImageIO.read(MainApplication.class.getResourceAsStream("/images/ico15_15.png"));
				trayIcon = new TrayIcon(image, "DianTalk", popupMenu);
				trayIcon.setToolTip("DianTalk");
				tray.add(trayIcon);
				trayIcon.addMouseListener(sj);
	 
	 
	 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
	
	
    @Override
    public void start(Stage primaryStage) throws IOException, JSONException {
    	enableTrayStatus = 0;
    	enableTray(primaryStage);
    	MainApplication.htmlEditor =  new HTMLEditor();
    	MusicStage = new Stage();
    	nearlyMessageList = new ArrayList<Message>();
    	msgList =new ArrayList<String>();
    	FriendStatus = 0;
    	MsgMap =  new HashMap<String, List>(); 
    	TalkVeiw = new WebView();
    	TalkVeiw.setStyle("-fx-background-color:#fff;");
    	Talk = TalkVeiw.getEngine();
    	this.primaryStage  = primaryStage;
    	this.SHOWHTML_start = "<!doctype html><html lang='zh'><head><meta charset='UTF-8'><meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'><meta name='viewport' content='width=device-width, initial-scale=1.0'><link rel='stylesheet' type='text/css' href='http://api.kilingzhang.com/diantalk/css/diantalk/normalize.css' /><link rel='stylesheet' type='text/css' href='http://api.kilingzhang.com/diantalk/css/diantalk/default.css'><link rel='stylesheet' type='text/css' href='http://api.kilingzhang.com/diantalk/css/diantalk/styles.css'><!--[if IE]><script src='http://cdn.bootcss.com/html5shiv/r29/html5.min.js'></script><![endif]--></head><body onload='scroll(0,document.body.scrollHeight)'><div id='chatbox' style='position:static;'><div id='chat-messages' class='animate'  ><label>"+ChatDay +"</label>";
		
    	Main = new BorderPane();
    	
    	Main.setId("Main");
    	Main.setOnMousePressed(new EventHandler<MouseEvent>() {  
			@Override  
            public void handle(MouseEvent event) {  
                xOffset = event.getSceneX();  
                yOffset = event.getSceneY();  
            }  
        });  
    	Main.setOnMouseDragged(new EventHandler<MouseEvent>() {  
            @Override  
            public void handle(MouseEvent event) {  
                primaryStage.setX(event.getScreenX() - xOffset);  
                primaryStage.setY(event.getScreenY() - yOffset);  
            }  
        }); 
    	
    	//---------消息列表开始
    	
//    	InitMessageList();
    	//---------消息列表结束

        //好友列表开始
       
    	InitFriendList();
    	
        //好友列表结束
        
        
        
        //聊天窗口开始
        
//    	InitTalkVeiw(this.toSid);
        
        //聊天窗口结束
         
    	
    	//APP
//    	InitApp();
    	
        
        //头部标题加载开始
        Parent toptitle = FXMLLoader.load(getClass().getResource("/View/topTitle.fxml")); 
        newMsg  = (Label) toptitle.lookup("#msg");
        Mainimg  = (Label) toptitle.lookup("#ico");
        Mainname  = (Label) toptitle.lookup("#name");
        MainTip  =  toptitle.lookup("#msgTip");
        MusicLable = (Label)toptitle.lookup("#MusicLable");
        MusicLable.setText("");
        CloseMusicLable = (Label) toptitle.lookup("#CloseMusicLable");
        PalyMusciLable = (Label) toptitle.lookup("#PalyMusciLable");
        Volume = (Slider) toptitle.lookup("#Volume");
        CloseMusicLable.setText("");
        PalyMusciLable.setText("");
        
        
        newMsg.setText("");
        Mainname.setText("");
        Volume.setOpacity(0);
        
        Mainimg.setStyle("-fx-background-image:url('') ");
        
        
        
        Mainimg.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				
				MainApplication.InfoStage = new Stage();
				InfoStage.setTitle(MainApplication.fromSid+"的详细信息");
				// 设置窗口显示模式
				MainApplication.InfoStage.initModality(Modality.WINDOW_MODAL);
	            // 设置窗口的所有者
				InfoStage.initOwner(primaryStage);
				WebView w  = new WebView();
				WebEngine g = w.getEngine();
				g.load("http://api.kilingzhang.com/diantalk/userInfo.php?sid="+MainApplication.fromSid+"&searchSid="+MainApplication.fromSid);
				//System.out.println("http://api.kilingzhang.com/diantalk/userInfo.php?sid="+MainApplication.fromSid+"&searchSid="+MainApplication.fromSid);
	            Scene MusicStageScene = new Scene(w);
	           
	            InfoStage.setScene(MusicStageScene);
	            
	            // Show the dialog and wait until the user closes it
	           if(MainApplication.LoginStatus == 1){
	        	   InfoStage.showAndWait();
	           }
			}
			
		});
      
       
        
       
        Main.setTop(toptitle);
       //头部标题加载结束
      
		//登陆
        InitLogin();
        //登陆结束

		
        
		Scene scene = new Scene(Main, 1000, 600);
		primaryStage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题
		scene.getStylesheets().add(getClass().getResource("MyUI.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Dian·talk(点聊)");
		primaryStage.show();
    }
    
    
    public void InitApp() throws IOException {
		// TODO Auto-generated method stub
    	Parent App = FXMLLoader.load(getClass().getResource("/View/APP.fxml")); 
        MainApplication.Main.setCenter(App);
	}
   

	//
	public void InitLogin() {
        GridPane login_Grid = new GridPane();
		login_Grid.setAlignment(Pos.CENTER);
		login_Grid.setVgap(10);
		login_Grid.setHgap(10);
		login_Grid.setPadding(new Insets(25,25,25,25));
		
		
		Image logo = new Image(getClass().getResourceAsStream("/images/avatar.png"));
		ImageView logo_IV = new ImageView(logo);
		logo_IV.setFitWidth(50);
		logo_IV.setFitHeight(50);
		Label scenetitle = new Label("Dian·talk",logo_IV);
		login_Grid.add(scenetitle, 0, 0, 2, 1);
		//创建Label对象，放到第0列，第1行
       Label userName = new Label("UserSid:");
       login_Grid.add(userName, 0, 1);
 
       //创建文本输入框，放到第1列，第1行
       TextField userTextField = new TextField();
       login_Grid.add(userTextField, 1, 1);
 
       Label pw = new Label("Password:");
       login_Grid.add(pw, 0, 2);
 
       PasswordField pwBox = new PasswordField();
       login_Grid.add(pwBox, 1, 2);
		
       
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        Button btn_re = new Button("注  册");
        HBox hbBtn_re = new HBox(10);
        
        
        Button btn_sel = new Button("切换账号登陆");
        HBox hbBtn_sel = new HBox(10);
        hbBtn_sel.setAlignment(Pos.BOTTOM_RIGHT);
        
        
        hbBtn_sel.getChildren().add(btn_sel);//将按钮控件作为子节点
        login_Grid.add(hbBtn_sel, 0, 4);//将HBox pane放到grid中的第1列，第4行
        
        
        hbBtn_re.getChildren().add(btn_re);//将按钮控件作为子节点
        login_Grid.add(hbBtn_re, 1, 4);//将HBox pane放到grid中的第1列，第4行
        
        hbBtn.getChildren().add(btn);//将按钮控件作为子节点
        login_Grid.add(hbBtn, 2, 4);//将HBox pane放到grid中的第1列，第4行
       
        //  login
        btn.addEventFilter(MouseEvent.MOUSE_CLICKED , (MouseEvent e)->{
        	String sid =  userTextField.getText();
			String password = pwBox.getText();
			if(sid.trim() == null || sid.trim().length() <= 0 || password.trim() == null || password.trim().length() <= 0){
				if(MainApplication.LoginStatus == 0){
				
			        Alert alert=new Alert(Alert.AlertType.WARNING);
			        alert.setHeaderText("提示");
			        alert.setContentText("输入框禁止为空~~~");
			        alert.showAndWait();
			        return ;
				
				}
			}
		
			this.fromSid = sid;
			
	
			try {
				String AccountType = userName.getText();
				// login 
				clientThread = new  ClientThread(sid,password,AccountType);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// start ClientThread
			clientThread.start();

        });
        
        // select Account
        btn_sel.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				// TODO Auto-generated method stub
				if(btn_sel.getText().equals("切换账号登陆")){
//					//System.out.println(btn_sel.getText().equals("切换账号登陆"));
					userName.setText("Account:");
					btn_sel.setText("切换学号登陆");
//					//System.out.println(btn_sel.getText());
					
				}else if(btn_sel.getText().equals("切换学号登陆")){
//					//System.out.println(btn_sel.getText().equals("切换学号登陆"));
					userName.setText("UserSid:");
					btn_sel.setText("切换账号登陆");					
//					//System.out.println(btn_sel.getText());
					
				}else{
//					//System.out.println(btn_sel.getText());
					
				}
			}
		});
		
        // register
        btn_re.addEventFilter(MouseEvent.MOUSE_CLICKED , new EventHandler<MouseEvent>() {
			

			@Override
			public void handle(MouseEvent e) {
			
		        WebView w = new WebView();
		        WebEngine e1 = w.getEngine();
		        e1.load("http://diantalk.kilingzhang.com/register/");
		        Stage RegisterStage = new Stage();
	            RegisterStage.setTitle("注册");
	            // 设置窗口显示模式
	            RegisterStage.initModality(Modality.WINDOW_MODAL);
	            // 设置窗口的所有者
	            RegisterStage.initOwner(primaryStage);
	            BorderPane p = new BorderPane();
	           	p.setCenter(w);
	            Scene RegisterStageScene = new Scene(p,940,650);
	            RegisterStage.setScene(RegisterStageScene);
	            
	            // Show the dialog and wait until the user closes it
	            RegisterStage.showAndWait();
	  
				
			}
		});
        
        System.out.println(this.toSid+"Pane is done");
        Main.setCenter(login_Grid);
	}
    
	

    /**
     * 	消息列表
     * @param MessageList
     */
    public void InitMessageList() {
    	
    	
		int i = 0;
		flag = 0;
		//Remove all MessageListItem
		MessageListItem.removeAll(MessageListItem);
		close.removeAll(close);
		name.removeAll(name);
		msg.removeAll(msg);
    	for (Iterator<Message> messageItem = nearlyMessageList.iterator(); messageItem.hasNext();) {
    		Message MItem = (Message) messageItem.next();
    		if(MItem == null ){
    		
    			continue;
    		}
			
			GridPane gp = new GridPane();
			gp.setMinWidth(341);
			gp.setUserData(MItem.getPersion().getSid());
//    		gp.setGridLinesVisible(true);
    		gp.setAlignment(Pos.CENTER);

    		Label cl = new Label(" ");
    		
			if(i==0){
    			cl.setText("X");
    			i++;
    		}else{
    			gp.setId("MessageListItem");
    		}
    		
    		cl.setFont(Font.font(15));
    		cl.setPadding(new Insets(7));
    		close.add(cl);
    		Label na = new Label(MItem.getPersion().getSid());
    		na.setFont(Font.font(23));
    		na.setPadding(new Insets(20,0,0,10));
    		name.add(na);
    		Label ms = new Label(MItem.getMessage());
    		ms.setPadding(new Insets(0,0,0,15));
    		msg.add(ms);   		
    		Image  ic =null;
    	   try {
    			ic = new Image("http://api.kilingzhang.com/diantalk/diantalk/"+MItem.getPersion().getSid()+".jpg");
		} catch (Exception e) {
			    ic = new Image(getClass().getResourceAsStream("/images/avatar.png"));
			// TODO: handle exception
		}
    		ImagePattern patt = new ImagePattern(ic);
    		Circle cir = new Circle(35);
    		cir.setFill(patt);		
    		gp.add(cl, 0, 0, 1, 2);
    		gp.getRowConstraints().add(new RowConstraints(50));
    		gp.getRowConstraints().add(new RowConstraints(50));
    		gp.add(na, 2, 0, 3, 1);
    		gp.add(ms, 2, 1, 3, 1);
    		gp.add(cir, 1, 0, 1, 2);
    		MessageListItem.add(gp);
    		
    		//切换消息列表用户
    		gp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
    			int n = 0;
    			for (Iterator iterator = close.iterator(); iterator.hasNext();) {
    				if(flag == 1 && n == 0){
    					Label label = (Label) iterator.next();
	    				label.setText("X");
    				}else{
    					Label label = (Label) iterator.next();
	    				label.setText(" ");
    				}
    				n ++;
    			}
    			n = 0;
    			for (Iterator iterator = MessageListItem.iterator(); iterator.hasNext();) {
    				if(flag == 1 && n == 0){
    					GridPane item = (GridPane) iterator.next();
	    				item.setId("");
    				}else{
    					GridPane item = (GridPane) iterator.next();
	    				item.setId("MessageListItem");
    				}
    				n ++;
    				
    				
    			}
    			try {
    				if(flag == 1){
    					flag = 0;
    				}else{
    					MainApplication.type = gp.getId();
    					InitTalkVeiw(gp.getUserData().toString());
    				}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			gp.setId("");
    			//System.out.println(gp.getUserData().toString());
    			cl.setText("X");
    		});
    		
    		//自定义X事件 
    		cl.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
    			MessageListItem.remove(gp);
    			nearlyMessageList.remove(MItem);
    			VBox Vb = new VBox();
    			Vb.setMaxWidth(340);
    			for (Iterator iterator = MessageListItem.iterator(); iterator.hasNext();) {
    				GridPane gridPane = (GridPane) iterator.next();
//    				VBox.setMargin(gridPane, new Insets(5));
    				Vb.getChildren().add(gridPane);
    			}
    			for (Iterator iterator = MessageListItem.iterator(); iterator.hasNext();) {
    				GridPane item = (GridPane) iterator.next();
    				try {
    					//System.out.println("X:"+item.getUserData().toString());
    					MainApplication.type = item.getId();
						InitTalkVeiw(item.getUserData().toString());
						//System.out.println("X:"+item.getUserData().toString());
						flag =1;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				break;
    				
    			}
    			
    			MainApplication.sp.setMaxWidth(341);
    			MainApplication.sp.setContent(Vb); 	
    			MainApplication.sp.setVbarPolicy(ScrollBarPolicy.NEVER);
    			MainApplication.sp.setHbarPolicy(ScrollBarPolicy.NEVER);
    			
    			MainApplication.Main.setLeft(MainApplication.sp);
    	        
//    			primaryStage.show();
    		});
    		
    		
    		
    		
		}
		
    	
		VBox Vb = new VBox();
		Vb.setMaxWidth(340);
		for (Iterator iterator = MessageListItem.iterator(); iterator.hasNext();) {
			
			GridPane gridPane = (GridPane) iterator.next();
//			VBox.setMargin(gridPane, new Insets(5));
			Vb.getChildren().add(gridPane);
			Vb.setId("VbFriedList");
			
		}
		
		sp = new ScrollPane();
		sp.setId("spFriedList");
		sp.setVbarPolicy(ScrollBarPolicy.NEVER);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setPrefWidth(341);
        sp.setContent(Vb);
        Main.setLeft(sp);
        for (Label  item: this.name) {
        	//System.out.println(item.getText().toString());
		}
        
        //---------消息列表结束
			
}
		
    	
    	
    	
	
    
    
    //好友列表
	private void InitFriendList() {
		// TODO Auto-generated method stub
		
	}
	
	
	public  void InitTalkVeiw(String toSid) throws JSONException, IOException {
		// TODO Auto-generated method stub
		MainApplication.msgList.removeAll(MainApplication.msgList);
		MainApplication.SHOWHTML_main = "";
		VBox VTalk = new VBox();

		
		
    	MainApplication.Main.setCenter(VTalk);
        VTalk.getChildren().add(MainApplication.TalkVeiw);
        WebEngine Talk = MainApplication.Talk;
        String sid = MainApplication.fromSid;
        //System.out.println(toSid);
        String to  = toSid;
        MainApplication.toSid = to ;
        
        try {
			MainApplication.fromName = (String) Person.getPerson(MainApplication.fromSid).get("name");
			MainApplication.toName   = (String) Person.getPerson(toSid).get("name");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MainApplication.SHOWHTML_end = "</div></div><script>window.onload=function(){function loadImg(src,src1){currImg=new Image();currImg.src=src;currImg.onload=function(){var rightDiv=document.getElementsByClassName('right');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src}currImg2=new Image();currImg2.src=src1;currImg2.onload=function(){var rightDiv=document.getElementsByClassName('left');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src1}window.scrollTo(0,document.body.scrollHeight)}}}loadImg('http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg','http://api.kilingzhang.com/diantalk/diantalk/"+to+".jpg')}</script></body></html>";
		
        if(MainApplication.type.equals("friendlist")){
        	VTalk.setUserData("friend_"+MainApplication.toSid);
        }else{
        	VTalk.setUserData("group_"+MainApplication.toSid);
        }
        
        if(MainApplication.MsgMap.containsKey(toSid)){
        	MainApplication.msgList = MainApplication.MsgMap.get(toSid);
        	List TM = new ArrayList<String>();
    		for (Iterator iterator = MainApplication.msgList.iterator(); iterator.hasNext();) {
				String object = (String) iterator.next();
				TM.add(object);
			}
    		MainApplication.MsgMap.put(toSid, TM);
        	//System.out.println(toSid + "--------------");
        	for (Iterator iterator = MainApplication.msgList.iterator(); iterator.hasNext();) {
				String s = (String) iterator.next();
				//System.out.println(s);
			}
        }else{
        	String url = ApiURL.MYSQL_GET_CHAT_RECORD.getUrl()+config.Parme + "&from=" + sid + "&to=" + to;
    		//System.out.println(url);
        	Document doc = null;
        	try {
    			 doc = Jsoup.connect(url).get();
			} catch (Exception e) {
				// TODO: handle exception
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Network anomaly   错误地址:Chat_Record");
				alert.setContentText("网络又炸了，日 ，弄得好像我程序有问题似得，重试下~~~~");
				alert.showAndWait();
			}
    		//System.out.println("db getdoc:"+doc.body().text());
    		JSONObject json = new JSONObject(doc.body().text());
    		
    		JSONArray data = json.getJSONArray(("data"));
    		for (int i = 0; i < data.length(); i++) {
    			JSONObject js = (JSONObject) data.get(i);
    			
    			//装入list中
    			String from = js.get("fromSid").toString();
    			String msg = js.get("msg").toString();
//    			//System.out.println(js.get("msg"));
    			
    			msg = msg.replace("^", "<");
    			//System.out.println(msg);
    			
    			String sendTime = js.get("sendtime").toString();
    			
    			
    			//拼接聊天内容
    			if(from.equals(sid)){
    				String conn = "<div class='message right'><img src='http://api.kilingzhang.com/diantalk/diantalk/"+from+".jpg'><div class='bubble'>"+msg+"<div class='corner'></div><span>"+MainApplication.fromName+"</span></div></div>";
    				MainApplication.msgList.add(conn);
    			}else{
    				String conn = "<div class='message left'><img src='http://api.kilingzhang.com/diantalk/diantalk/"+to+".jpg'><div class='bubble'>"+msg+"<div class='corner'></div><span>"+MainApplication.toName+"</span></div></div>";
    				MainApplication.msgList.add(conn);
    			}
    	      
    		}
    
    		List TM = new ArrayList<String>();
    		for (Iterator iterator = MainApplication.msgList.iterator(); iterator.hasNext();) {
				String object = (String) iterator.next();
				TM.add(object);
			}
    		MainApplication.MsgMap.put(toSid,TM);
    		
        }
        
   
		for (Iterator iterator = MainApplication.msgList.iterator(); iterator.hasNext();) {
			String object = (String) iterator.next();
			MainApplication.SHOWHTML_main += object.toString();
			
 		 }
		MainApplication.msgString = MainApplication.SHOWHTML_main;
		MainApplication.SHOWHTML = MainApplication.SHOWHTML_start+MainApplication.SHOWHTML_main+MainApplication.SHOWHTML_end;
		
       
//        Talk.load("http://www.kilingzhang.com");
        Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Talk.loadContent(MainApplication.SHOWHTML);
		      
			}
		});
        
        BorderPane sendTalk = new BorderPane();	
        MainApplication.htmlEditor = new HTMLEditor();
       
        MainApplication.htmlEditor.setMinHeight(157);
        MainApplication.htmlEditor.setMaxHeight(250);
		
		
		sendTalk.setCenter(MainApplication.htmlEditor);

		VTalk.getChildren().add(sendTalk);

		Button sendBtn = new Button();
		Button shareMusicBtn = new Button("分享音乐");
		Button ChaInfoBtn = new Button("好友信息");
		Button XXXBtn = new Button("我也不知道干啥的按钮凑数");
		sendBtn.setText("发送");
		XXXBtn.setPrefHeight(30);
		XXXBtn.setPrefWidth(80);
		ChaInfoBtn.setPrefHeight(30);
		ChaInfoBtn.setPrefWidth(80);
		shareMusicBtn.setPrefHeight(30);
		shareMusicBtn.setPrefWidth(80);
		sendBtn.setPrefHeight(50);
		sendBtn.setPrefWidth(80);
		VBox v = new VBox();
		v.setPadding(new Insets(10,10,10,10));
		v.getChildren().addAll(XXXBtn,shareMusicBtn,ChaInfoBtn,sendBtn);
		sendTalk.setRight(v);
		
		XXXBtn.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Alert alert=new Alert(Alert.AlertType.WARNING);
				        alert.setHeaderText("2333");
				        alert.setContentText("我也不知道这个按钮干啥用的 纯属凑数");
				        alert.showAndWait();
					}
				});
			}
			
		});
		
		
		ChaInfoBtn.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				MainApplication.InfoStage = new Stage();
				InfoStage.setTitle(MainApplication.toSid+"的详细信息");
				// 设置窗口显示模式
				MainApplication.InfoStage.initModality(Modality.WINDOW_MODAL);
	            // 设置窗口的所有者
				InfoStage.initOwner(primaryStage);
				WebView w  = new WebView();
				WebEngine g = w.getEngine();
				g.load("http://api.kilingzhang.com/diantalk/userInfo.php?sid="+MainApplication.fromSid+"&searchSid="+MainApplication.toSid);
				//System.out.println("http://api.kilingzhang.com/diantalk/userInfo.php?sid="+MainApplication.fromSid+"&searchSid="+MainApplication.toSid);
	            Scene MusicStageScene = new Scene(w);
	            InfoStage.setScene(MusicStageScene);
	            
	            // Show the dialog and wait until the user closes it
	            InfoStage.showAndWait();
			}
			
		});
		
		
		shareMusicBtn.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				
	            try {
	            	FXMLLoader loader = new FXMLLoader();
		            loader.setLocation(getClass().getResource("/View/MusicSearch.fxml"));
					GridPane MusicSearch = (GridPane) loader.load();
					MusicStage = new Stage();
					MusicStage.setTitle("分享音乐");
		            // 设置窗口显示模式
					MainApplication.MusicStage.initModality(Modality.WINDOW_MODAL);
		            // 设置窗口的所有者
					MusicStage.initOwner(primaryStage);
		  
		   
		            Scene MusicStageScene = new Scene(MusicSearch,540,700);
		            MusicStage.setScene(MusicStageScene);
		            
		            // Show the dialog and wait until the user closes it
		            MusicStage.showAndWait();
				} catch (IOException e) {
					// TODO Auto-generated catch block
						// TODO: handle exception
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("File missing  错误地址: MusicSearch.fxml");
					alert.setContentText("文件缺失  请重新安装本软件 去官网下载最新版本");
					alert.showAndWait();
					
					e.printStackTrace();
				}
				
			}
			
		});
		sendBtn.setOnMouseClicked(new EventHandler<Event>() {
			
			private String con;
			public String to;
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				String html = MainApplication.htmlEditor.getHtmlText().toString();
				if(html.length()<= 0 || html == null){
					Alert a = new Alert(Alert.AlertType.WARNING);
					a.setHeaderText("提醒");
					a.setContentText("禁止信息框输入空");
					a.showAndWait();
					return ;
				}
				System.err.println(html);
				MainApplication.htmlEditor.setHtmlText("");
				
				String pattern = "<html dir="+'"'+"ltr"+'"'+"><head></head><body contenteditable="+'"'+"true"+'"'+">(.*?)</body></html>";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(html);
//				//System.out.println(m.toString());
				if (m.find()) {
//			         //System.out.println("Found value: " + m.group(0) );
//			         //System.out.println("Found value: " + m.group(1) );
//					 pattern = "<div .*>(.*?)</div>";
//					 r = Pattern.compile(pattern);
//					 Matcher mm = r.matcher(html);
//					 if(mm.find()){
//						 m = mm;
//					 }
					 
			         con = m.group(1);
			         con = con.replaceAll("&nbsp;", "").trim();
			         if(con == null || con.length() <= 0){
			        	 Alert a = new Alert(Alert.AlertType.WARNING);
						 a.setHeaderText("提醒");
						 a.setContentText("禁止信息框输入空");
						 a.showAndWait();
						 return ;
			         }
			         con = con.replaceAll("\"", "'");
//			         //System.out.println("第一次置换 "+con);
			         to = (String) VTalk.getUserData();
			         int n = to.indexOf("_");
			         String type = to.substring(0, n);
			         to = to.substring(n+1);
			         String conn = "<div class='message right'><img src=''><div class='bubble'>"+con+"<div class='corner'></div><span>"+MainApplication.fromName+"</span></div></div>";
			         MainApplication.msgList.add(conn);
			 		 MainApplication.msgString +=conn;
			 		List TM = new ArrayList<String>();
		    		for (Iterator iterator = MainApplication.msgList.iterator(); iterator.hasNext();) {
						String object = (String) iterator.next();
						TM.add(object);
					}
		    		MainApplication.MsgMap.put(toSid, TM);
		    		
			 		MainApplication.SHOWHTML = MainApplication.SHOWHTML_start+MainApplication.msgString+MainApplication.SHOWHTML_end;
			 	
			 		Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								int i = 0;
								//System.out.println(MainApplication.type);
								Message m = null;
								if(MainApplication.type.equals("friendlist")){
									m = new Message(new Person(to), con);
									for (Iterator<Message> iterator = MainApplication.nearlyMessageList.iterator(); iterator.hasNext();) {
										Message msg = (Message) iterator.next();
										if(msg != null && m != null && msg.getId().equals(m.getId().toString())){
											msg.setMessage(m.getMessage());
											i++;
											break;
										}
									}
									if(i == 0){
										MainApplication.nearlyMessageList.add(m);
									}
								}else if(MainApplication.type.equals("group")){
									m = new Message(new Group(to), con);
									for (Iterator<Message> iterator = MainApplication.nearlyMessageList.iterator(); iterator.hasNext();) {
										Message msg = (Message) iterator.next();
										if(msg != null && m != null && msg.getId().equals(m.getId().toString())){
											msg.setMessage(m.getMessage());
											i++;
											break;
										}
									}
									if(i == 0){
										MainApplication.nearlyMessageList.add(m);
									}
									//System.out.println("MesageNearly"+i);
								}
								
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("File missing  错误地址: Group.xml or Person.xml");
								alert.setContentText("文件缺失  请重新启动本软件");
								alert.showAndWait();
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("JSON WRONG  错误地址: Group  or Person");
								alert.setContentText(" 请重新启动本软件");
								alert.showAndWait();
								e.printStackTrace();
							} catch (DocumentException e) {
								// TODO Auto-generated catch block
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Network anomaly   错误地址:Person or Group");
								alert.setContentText("网络又炸了，日 ，网络不稳定我也没办法，重试下~~~~");
								alert.showAndWait();
								e.printStackTrace();
							}
							
							 Talk.loadContent(MainApplication.SHOWHTML);
						}
					});

			 		 JSONObject JsonServer = new JSONObject();
			 		 //System.out.println(type);
			 		 if(type.equals("friend")){
			 			try {
			 				JsonServer.put("code", "1001");
			 				JsonServer.put("status", "100101");
			 				JsonServer.put("from", sid);
			 				JsonServer.put("to",  to);
			 				JsonServer.put("ip", MainApplication.clientThread.ip);
			 				JsonServer.put("msg", con);
			 				MainApplication.clientThread.sendMessageToServer(JsonServer);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("JSON WRONG    错误地址:100101");
							alert.setContentText("数据出错了 重试下");
							alert.showAndWait();
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("URL_DE_CODE WRONG    错误地址:100101");
							alert.setContentText("转码出错  重试下");
							alert.showAndWait();
							e.printStackTrace();
						}
			 		 }else if(type.equals("group")){
			 			 
			 			try {
			 				JsonServer.put("code", "1002");
			 				JsonServer.put("status", "100201");
			 				JsonServer.put("from", sid);
			 				JsonServer.put("to",  to);
			 				JsonServer.put("ip", MainApplication.clientThread.ip);
			 				JsonServer.put("msg", con);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("JSON WRONG    错误地址:100201");
							alert.setContentText("数据出错了 重试下");
							alert.showAndWait();
							e.printStackTrace();
						}
			 			//System.out.println("Group->"+JsonServer.toString());
			 			try {
							MainApplication.clientThread.sendMessageToServer(JsonServer);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							
							e.printStackTrace();
						}
			 		 }
			 		 
			 		
			     
			      }else {
			    	 
						Alert a = new Alert(Alert.AlertType.WARNING);
						a.setHeaderText("提醒");
						a.setContentText("禁止信息框输入空");
						a.showAndWait();
							
						
			      }

			}
		});
        
	}
    public static void main(String[] args) {
		launch(args);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*-------------------------ClientThread--------------------------------------------------------*/

/**
 * @author Slight
 *
 */
public class ClientThread extends Thread{
	
	
	public PrintStream Write = null;
	public BufferedReader Read = null;
	public Socket clientSocket = null;
	public String sid ;
	public String password ;
	public JSONObject userinfoJson;
	public WebEngine engine;
	public InetAddress ip;
	public String IP;
	private String Group;
	private String to;
	private String con;

	
	/**
	 * @param type 
	 * @params write
	 * @params engine
	 * @params clientSocket
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws DocumentException 
	 * 
	 */
	public ClientThread(String sid,String password, String type) throws JSONException, IOException, DocumentException  {
		// TODO Auto-generated constructor stub
		 MainApplication.alert=new Alert(Alert.AlertType.INFORMATION);
		 MainApplication.alert.setHeaderText("登陆中·······");
		 
		this.sid = sid;
		this.password = password;
		this.ip = InetAddress.getLocalHost();
//		DB db = new DB();
		//System.out.println(DB.IP);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainApplication.alert.setContentText("正在连接服务器~~   ->:"+DB.IP);
				MainApplication.alert.showAndWait();
			}
		});
		
		this.clientSocket = new Socket("139.129.35.103", 8888);
//		this.clientSocket = new Socket("127.0.0.1", 2333);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainApplication.alert.setContentText("成功连接服务器~~   ->:"+DB.IP);
			}
		});
		Write = new PrintStream(clientSocket.getOutputStream());
		Read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		
		
		//创建传输json数据向服务器发送用户登陆指令
		JSONObject JsonServer = new JSONObject();						
		JsonServer.put("code","0001");
		JsonServer.put("status", "00011");
		JsonServer.put("user", sid);
		JsonServer.put("type", type);
		JsonServer.put("password", password);
		MainApplication.password = password;
		//System.out.println(JsonServer.toString());
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainApplication.alert.setContentText("正在验证身份中~~~~~~");
			}
		});
		sendMessageToServer(JsonServer);
		//判断服务器登陆是否成功
		
	}
	
	//向服务器发送消息
	public void sendMessageToServer(JSONObject jsonServer) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String j = URLEncoder.encode(jsonServer.toString(), "UTF-8");
		//System.out.println(j);
		Write.println(j);
		Write.flush();
	}
	public String getEncoding(String str) {

		String encode = "GB2312";

		try {

		if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GB2312

		String s = encode;

		return s; // 是的话，返回“GB2312“，以下代码同理

		}

		} catch (Exception exception) {

		}

		encode = "ISO-8859-1";

		try {

		if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是ISO-8859-1

		String s1 = encode;

		return s1;

		}

		} catch (Exception exception1) {

		}

		encode = "UTF-8";

		try {

		if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是UTF-8

		String s2 = encode;

		return s2;

		}

		} catch (Exception exception2) {

		}

		encode = "GBK";

		try {

		if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GBK

		String s3 = encode;

		return s3;

		}

		} catch (Exception exception3) {

		}

		return ""; // 如果都不是，说明输入的内容不属于常见的编码格式。

		}


	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
					
					String msg =null;
					try {
						msg=Read.readLine();
						//System.out.println("serverReplay:->>"+msg);
						if(msg==null){
							return ;
						}else{
							
							msg = new String(msg.getBytes("UTF-8"),"UTF-8");
							msg = URLDecoder.decode(msg,"UTF-8");
							JSONObject Rjson = new JSONObject(msg);
							String code = Rjson.getString("code");
							switch (code) {
							case "0":
								//获取服务器返回码 登陆成功
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										MainApplication.alert.setContentText("登陆成功 加载数据中~~~");
										
									}
								});
//								//System.out.println(Rjson.getString("msg").toString());
								MainApplication.LoginStatus = 1;
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										
										
										        
										// TODO Auto-generated method stub
//										Main.setCenter(null);
										//---------消息列表开始
										
//								    	InitMessageList();
								    	//---------消息列表结束
										try {
											MainApplication.fromSid = Rjson.getString("sid");
										} catch (JSONException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
								    	MainApplication.toSid= MainApplication.fromSid;
								    	try {
								    		new Person(MainApplication.toSid);
								    		MainApplication.type = "friendlist";
											InitTalkVeiw(MainApplication.toSid);
											 
										    Mainname.setText(MainApplication.fromName+"，您好");
										        
										    Mainimg.setStyle("-fx-background-image:url(' http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg '); -fx-background-size:60;");
										    System.out.println("http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg ");
											
										    new FriendList(MainApplication.fromSid);
											new GroupList(MainApplication.fromSid);
										} catch (JSONException | IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (DocumentException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										MainApplication.alert.setContentText("登陆成功 快去找小伙伴玩耍吧~~~");
										
									}
								});
								break;
							case "1":
								//获取服务器返回码 已在线
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										alert.setContentText("您已经登陆在其余地点登陆，已将其强制下线，请重新登录");	
									}
									
								});
								JSONObject JsonServer = new JSONObject();
			    				try {
									JsonServer.put("code", "0002");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			    				MainApplication.clientThread.sendMessageToServer(JsonServer);
								break;
							case "2":
								//获取服务器返回码 登陆失败
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											MainApplication.alert.setContentText(Rjson.get("msg").toString());
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});

								break;
							case "2004":
								String status = Rjson.getString("status");
								//System.out.println(status);
								String st;
								switch(status){
								case "20041":
									
									//获取服务器返回码  好友消息通知

									
									con = Rjson.getString("msg");
									sid = Rjson.getString("from");
									st = Person.getPerson(sid).get("name")+"给你发了最新消息，快去看看吧\n\r"+con;
									//System.out.println(st);
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											MainApplication.newMsg.setText("你有新的消息");
											newMsg.getTooltip().setText(st);
										}
									});
									String sendtime = Rjson.getString("sendtime");
									MainApplication.SHOWHTML_end = "</div></div><script>window.onload=function(){function loadImg(src,src1){currImg=new Image();currImg.src=src;currImg.onload=function(){var rightDiv=document.getElementsByClassName('right');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src}currImg2=new Image();currImg2.src=src1;currImg2.onload=function(){var rightDiv=document.getElementsByClassName('left');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src1}window.scrollTo(0,document.body.scrollHeight)}}}loadImg('http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg','http://api.kilingzhang.com/diantalk/diantalk/"+sid+".jpg')}</script></body></html>";
									
									String conn = "<div class='message left'><img src=''><div class='bubble'>"+con+"<div class='corner'></div><span>"+Person.getPerson(sid).get("name")+"</span></div></div>";
									MsgMap.get(sid).add(conn);
									
									//System.out.println("Sid="+sid+"MainSid="+MainApplication.toSid);
									if(sid.equals(MainApplication.toSid)){
										MainApplication.msgList.add(conn);
										MainApplication.msgString +=conn;
										MainApplication.SHOWHTML = MainApplication.SHOWHTML_start+MainApplication.msgString+MainApplication.SHOWHTML_end;
									
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												MainApplication.Talk.loadContent(MainApplication.SHOWHTML);
											
//												JOptionPane.showMessageDialog(null, sid+"对你说："+MainApplication.msgString ,"con" , JOptionPane.DEFAULT_OPTION);
											}
										});
									}
									
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											try {
												int i = 0;
												Message m = new Message(new Person(sid), con);
												for (Iterator iterator = nearlyMessageList.iterator(); iterator.hasNext();) {
													Message msg = (Message) iterator.next();
													if(msg.getId().equals(m.getId().toString())){
														msg.setMessage(m.getMessage());
														i++;
														break;
													}
												}
												if(i == 0){
													nearlyMessageList.add(m);
												}
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (DocumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}
									});
									break;
								
								case "20042":
									//获取服务器返回码 群消息通知
	
									con = Rjson.getString("msg");
									to = Rjson.getString("to");
									sid = Rjson.getString("from");
									
									st = Person.getPerson(sid).get("name")+"给你发了最新消息，快去看看吧\n\r"+con;
									//System.out.println(st);
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											MainApplication.newMsg.setText("你有新的消息");
											newMsg.getTooltip().setText(st);
										}
									});
									MainApplication.SHOWHTML_end = "</div></div><script>window.onload=function(){function loadImg(src,src1){currImg=new Image();currImg.src=src;currImg.onload=function(){var rightDiv=document.getElementsByClassName('right');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src}currImg2=new Image();currImg2.src=src1;currImg2.onload=function(){var rightDiv=document.getElementsByClassName('left');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src1}window.scrollTo(0,document.body.scrollHeight)}}}loadImg('http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg','http://api.kilingzhang.com/diantalk/diantalk/"+sid+".jpg')}</script></body></html>";
								
									sendtime = Rjson.getString("sendtime");
									conn = "<div class='message left group'><img src=''><div class='bubble'>"+con+"<div class='corner'></div><span>"+Person.getPerson(sid).get("name")+"</span></div></div>";
								
									//System.out.println("Sid="+sid+"MainSid="+MainApplication.toSid);
									if(to.equals(MainApplication.toSid)){
										MainApplication.msgList.add(conn);
										MainApplication.msgString +=conn;
										MainApplication.SHOWHTML = MainApplication.SHOWHTML_start+MainApplication.msgString+MainApplication.SHOWHTML_end;
									Platform.runLater(new Runnable() {
											@Override
											public void run() {
												MainApplication.Talk.loadContent(MainApplication.SHOWHTML);
											}
										});
									}
									
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											try {
												int i = 0;
												Message m = new Message(new __class.Group(to), con);
												for (Iterator iterator = nearlyMessageList.iterator(); iterator.hasNext();) {
													Message msg = (Message) iterator.next();
													if(msg.getId().equals(m.getId().toString())){
														msg.setMessage(m.getMessage());
														i++;
														break;
													}
												}
												System.out.println(i);
												if(i == 0){
													nearlyMessageList.add(m);
												}
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (DocumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}
									});
									break;
									
									case "20043":
										con = Rjson.getString("msg");
										to = Rjson.getString("to");
										sid = Rjson.getString("from");
										JSONObject d = new JSONObject(Rjson.getString("data"));
										//System.out.println(Rjson.toString());
										
										//System.out.println("Music---->"+Rjson.toString());		
										
										Platform.runLater(new Runnable() {
											
											
											private String MusicName;
											private String MusicUrl;

											@Override
											public void run() {
												// TODO Auto-generated method stub
												Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
										        alert.setHeaderText("音乐分享");
										        alert.setContentText(con+"\n\r播放确定，取消播放点击取消即可");
										        
												try {
													MusicName = d.getString("name");
													MusicUrl  = d.getString("url");
												} catch (JSONException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
										        alert.showAndWait().ifPresent(response -> {
										            if (response == ButtonType.OK) {
										            	MainApplication.MusicLable.setText(MusicName + " 正在播放中---");
										            	if(MainApplication.mediaPlayer != null){
										            		MainApplication.mediaPlayer.	stop();
										            		MainApplication.mediaPlayer.dispose();
										            	}
														MainApplication.media = new Media(MusicUrl);
										                // Create the player and set to play automatically.
														MainApplication.mediaPlayer = new MediaPlayer(MainApplication.media);
														MainApplication.mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
														MainApplication.mediaPlayer.setAutoPlay(true);
														MainApplication.mediaPlayer.setVolume(0.1);
										                PalyMusciLable.setText("暂停");
										                CloseMusicLable.setText("关闭音乐");
										                MainApplication.Volume.setValue(0.1);
										                MainApplication.Volume.setShowTickLabels(true);
										                MainApplication.Volume.setOpacity(1);
										                MainApplication.Volume.valueProperty().addListener(new ChangeListener<Number>() {
										                    public void changed(ObservableValue<? extends Number> ov,
										                            Number old_val, Number new_val) {
										                    		mediaPlayer.setVolume((double) new_val);
										                        }
										                    });
										                MainApplication.PalyMusciLable.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
										                	Platform.runLater(new Runnable() {
																
																@Override
																public void run() {
																	if(PalyMusciLable.getText().equals("暂停")){
												                		PalyMusciLable.setText("播放");
												                		MainApplication.MusicLable.setText(MusicName + " 已暂停---");
												                		MainApplication.mediaPlayer.pause();
												                	}else if(PalyMusciLable.getText().equals("播放")){
												                		PalyMusciLable.setText("暂停");
												                		MainApplication.MusicLable.setText(MusicName + " 正在播放中---");
												                		MainApplication.mediaPlayer.play();
												                	}
																}
															});
										                });
										                
										                MainApplication.CloseMusicLable.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
										                	mediaPlayer.stop();
										                	PalyMusciLable.setText("");
										                	CloseMusicLable.setText("");
										                	MusicLable.setText("");
										                	MainApplication.Volume.setOpacity(0);
										                	 
										                	
										                });

										            }else{
										            	
										            }
										        });
											}
										});
									break;
								}
								
//								Platform.runLater(new Runnable() {	
//									@Override
//									public void run() {
//										// TODO Auto-generated method stub
//										InitMessageList();
//									}
//								});
								
								break;
							case "2003":
								String notice;
								notice = Rjson.getString("msg");
								String from = Rjson.getString("from");
								String to   = Rjson.getString("to");
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
							             alert.setHeaderText(MainApplication.fromSid);
							             alert.setContentText(notice);
							             alert.showAndWait().ifPresent(response -> {
							                 if (response == ButtonType.OK) {
							                     
							                     JSONObject JsonServer = new JSONObject();
//							    		 		 //System.out.println(type);
							    		 			try {
							    		 				JsonServer.put("code", "1001");
							    		 				JsonServer.put("status", "100105");
							    		 				JsonServer.put("from", from );
							    		 				JsonServer.put("to", to);
							    		 			
							    		 		
							    		 				try {
															MainApplication.clientThread.sendMessageToServer(JsonServer);
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
							    		 				
													    MainApplication.FriendStatus = 1;
														
							    					} catch (JSONException e) {
							    						// TODO Auto-generated catch block
							    						e.printStackTrace();
							    					}
							                 }
							             });
									}
								});
								 
								
								
								
								break;

							case "1001051":
								
								notice = Rjson.getString("msg");
								String Sid = Rjson.getString("from");
								toSid	= Rjson.getString("to");
								//System.out.println(Sid+"请求添加"+toSid+"好友");
								try {
									new FriendList(MainApplication.fromSid);
									//System.out.println("更新好友列表");
								} catch (IOException | JSONException | DocumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										//System.out.println("同意添加 哈哈哈哈哈哈哈");
										Alert alert=new Alert(Alert.AlertType.INFORMATION);
							             alert.setHeaderText("好友消息");
							             alert.setContentText(notice);
							             alert.showAndWait();
							             
									}
								});
								 
								
								
								
								break;
							
							case "3001":
								notice = Rjson.getString("msg");
								from = Rjson.getString("from");
								toSid	= Rjson.getString("to");
								Group	= Rjson.getString("group");
								//System.out.println("申请添加群"+Rjson.toString());
								Platform.runLater(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										
										 Alert alert=new Alert(Alert.AlertType.INFORMATION);
							             alert.setHeaderText(MainApplication.fromSid);
							             alert.setContentText(notice);
							             alert.showAndWait();
									}
								});
								
								break;
							
							case "2007":
							status = Rjson.getString("status");
								switch(status){
								case "20071":
									from = Rjson.getString("from");
									st = from +"已上线快去找他聊天吧";
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											MainApplication.newMsg.setText("好友上线");
											newMsg.getTooltip().setText(st);
										}
									});
									break;
								case "20072":
									from = Rjson.getString("from");
									st = from +"已下线";
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											MainApplication.newMsg.setText("好友下线");
											newMsg.getTooltip().setText(st);
										}
									});
									
								}
							
								
							break;
							
							default:
								//获取服务器返回码  未知状态
								JOptionPane.showMessageDialog(null, Rjson.toString());
								break;
							}
							
						
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
		}
	}
	

}
    
    
    
}