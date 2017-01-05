/**
 * 
 */
package Control;

import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import __class.ApiURL;
import __class.FriendList;
import __class.Group;
import __class.GroupList;
import __class.Message;
import __class.Person;
import __class.config;
import application.MainApplication;
import application.MainApplication;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Administrator
 *
 */
public class TopTitleControl extends VBox{

	/**
	 * 
	 */
	@FXML private Circle closeStage;
	@FXML private Circle MinStage;
	@FXML private Circle MaxStage;
	@FXML private TextField Search;
	@FXML private Tooltip SearchTooltip;
	@FXML private ImageView ico1;
	@FXML private ImageView ico2;
	@FXML private ImageView ico3;
	@FXML
    private Label MusicLable;
    @FXML
    private Label CloseMusicLable;
    @FXML
    private Label PalyMusciLable;
    
    @FXML
    private Slider Volume;
    
    
    
	private MainApplication Main;
	private Container msgList;
	@FXML VBox TopTitle;
	
	/**
	 * 	Exit  Diantalk
	 * @param event
	 * @throws JSONException
	 */
	@FXML
	public void onMouseClickedCloseStage(MouseEvent event) throws JSONException {  
		
		
		Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("退出");
        alert.setContentText("确定退出DianTalk?确定退出程序，取消则桌面右下角运行");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
            	// is on online
            	if(MainApplication.LoginStatus == 1){
    				JSONObject JsonServer = new JSONObject();
    				try {
    					//logout request
						JsonServer.put("code", "0002");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				try {
						MainApplication.clientThread.sendMessageToServer(JsonServer);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			System.exit(0); 
            }else{
            	//min 
            	if(MainApplication.enableTrayStatus == 1){
            		Platform.setImplicitExit(false);
            		Platform.runLater(new Runnable() {
						@Override
						public void run() {
							MainApplication.primaryStage.hide();
						}
					});
            		
            	}else{
            		MainApplication.enableTray(MainApplication.primaryStage);
            	}
            	
            }
        });

		 
    }  
	
	/**
	 * Min
	 * @param event
	 */
	@FXML 
	public void onMouseClickedMinStage(MouseEvent event) {  
        
        MainApplication.primaryStage.setIconified(true);
    }  
	
	/**
	 *  About
	 * @param event
	 */
	@FXML
	public void onMouseClickedMaxStage(MouseEvent event) {  
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("关于");
        alert.setContentText("DianTalk  by Slight\n\r 如果遇到任何bug 请联系slight@kilingzhang.com \r\n 个人主页 : http://www.kilingzhang.com");
        alert.showAndWait();
		
    }  

	@FXML
	public void onMouseEnteredCloseStage(MouseEvent event){
		
		
		
	}
	
	@FXML
	public void onMouseExitedCloseStage(MouseEvent event){
		
		
		
	}
	
	/**
	 * 
	 * Message Nearly
	 * @param event
	 */
	@FXML
	public void onMouseClickedMessage(MouseEvent event) {  
		
		// is on online
		if(MainApplication.LoginStatus == 0){
	        Alert alert=new Alert(Alert.AlertType.WARNING);
	        alert.setHeaderText("提示");
	        alert.setContentText("您未登录，请先登录后在执行此操作~~~");
	        alert.showAndWait();
	        return ;
		}
		
		// update UI nearly  message  List
		Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						
						MainApplication.newMsg.setText("");
						int i = 0;
						MainApplication.flag = 0;
						
						//remove All message
						MainApplication.MessageListItem.removeAll(MainApplication.MessageListItem);
						MainApplication.close.removeAll(MainApplication.close);
						MainApplication.name.removeAll(MainApplication.name);
						MainApplication.msg.removeAll(MainApplication.msg);
						//System.out.println(MainApplication.nearlyMessageList.size());
						
						// add the last nearly message  cards
				    	for (Iterator messageItem = MainApplication.nearlyMessageList.iterator(); messageItem.hasNext();) {
				    		Message MItem = (Message) messageItem.next();
				    		
							
							GridPane gp = new GridPane();
							gp.setMinWidth(340);
							gp.setUserData(MItem.getId());
//				    		gp.setGridLinesVisible(true);
				    		gp.setAlignment(Pos.CENTER);

				    		Label cl = new Label(" ");
				    		
							if(i==0){
				    			cl.setText("X");
				    			i++;
				    		}else{
				    			gp.setId("friendlist");
				    			gp.setStyle("-fx-background-color:#fff;");
				    		}
				    		
				    		cl.setFont(Font.font(15));
				    		cl.setPadding(new Insets(7));
				    		MainApplication.close.add(cl);
				    		Label na = new Label(MItem.getId());
				    		na.setFont(Font.font(23));
				    		na.setPadding(new Insets(20,0,0,10));
				    		MainApplication.name.add(na);
				    		Label ms = new Label(MItem.getMessage()+"                                                                            ......");
				    		ms.setPadding(new Insets(0,0,0,15));
				    		MainApplication.msg.add(ms);   		
				    		Image ic  = null;
				    		
				    		
				    		try {
				    			 ic = new Image("http://api.kilingzhang.com/diantalk/diantalk/"+MItem.getId()+".jpg");	
							} catch (Exception e) {
								 ic = new Image(getClass().getResourceAsStream("/images/avatar.png"));	
								// TODO: handle exception
							}
//				    		Image ic = new Image("http://api.kilingzhang.com/diantalk/diantalk/2015551439.jpg");
				    		ImagePattern patt = new ImagePattern(ic);
				    		Circle cir = new Circle(35);
				    		cir.setFill(patt);		
				    		gp.add(cl, 0, 0, 1, 2);
				    		gp.getRowConstraints().add(new RowConstraints(50));
				    		gp.getRowConstraints().add(new RowConstraints(50));
				    		gp.add(na, 2, 0, 3, 1);
				    		gp.add(ms, 2, 1, 3, 1);
				    		gp.add(cir, 1, 0, 1, 2);
				    		MainApplication.MessageListItem.add(gp);
				    		
				    		//切换消息列表用户
				    		gp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
				    			int n = 0;
				    			for (Iterator iterator = MainApplication.close.iterator(); iterator.hasNext();) {
				    				if(MainApplication.flag == 1 && n == 0){
				    					Label label = (Label) iterator.next();
					    				label.setText("X");
				    				}else{
				    					Label label = (Label) iterator.next();
					    				label.setText(" ");
				    				}
				    				n ++;
				    			}
				    			n = 0;
				    			for (Iterator iterator = MainApplication.MessageListItem.iterator(); iterator.hasNext();) {
				    				if(MainApplication.flag == 1 && n == 0){
				    					GridPane item = (GridPane) iterator.next();
					    				item.setId("");
				    				}else{
				    					GridPane item = (GridPane) iterator.next();
				    					if(MItem.getId().substring(0).equals("3") || MItem.getId().substring(0).equals("6") ){
				    						item.setId("grouplist");
				    					}else{
				    						item.setId("friendlist");
				    					}
					    				
				    				}
				    				n ++;

				    			}
				    			try {
				    				if(MainApplication.flag == 1){
				    					MainApplication.flag = 0;
				    				}else{
				    					MainApplication.type = gp.getId();
				    					if(n!=0){
				    						InitTalkVeiw(gp.getUserData().toString());
				    					}
				    				}
									
								} catch (Exception e1) {
									e1.printStackTrace();
								}
				    			gp.setId("");
	
				    			cl.setText("X");
				    		});
				    		
				    		//自定义X事件 
				    		cl.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
				    			MainApplication.MessageListItem.remove(gp);
				    			MainApplication.nearlyMessageList.remove(MItem);
				    			VBox Vb = new VBox();
				    			Vb.setMaxWidth(340);
				    			for (Iterator iterator = MainApplication.MessageListItem.iterator(); iterator.hasNext();) {
				    				GridPane gridPane = (GridPane) iterator.next();
//				    				VBox.setMargin(gridPane, new Insets(5));
				    				Vb.getChildren().add(gridPane);
				    			}
				    			for (Iterator iterator = MainApplication.MessageListItem.iterator(); iterator.hasNext();) {
				    				GridPane item = (GridPane) iterator.next();
				    				try {
				    					//System.out.println("X:"+item.getUserData().toString());
				    					MainApplication.type = item.getId();
										InitTalkVeiw(item.getUserData().toString());
										//重置颜色
										item.setStyle("-fx-background-color:;");
										
										//System.out.println("X:"+item.getUserData().toString());
										MainApplication.flag =1;
									} catch (Exception e1) {
										 Alert  alert = new Alert(AlertType.ERROR);
										 alert.setTitle("未知错误");
										 alert.setContentText("初始化组件失败 点击重新生成下  偶尔我也是会犯错滴~~~");
										 alert.showAndWait();
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
				    				break;
				    				
				    			}
				    			
				    			MainApplication.sp.setMaxWidth(340);
				    			MainApplication.sp.setContent(Vb); 	
				    			MainApplication.sp.setVbarPolicy(ScrollBarPolicy.NEVER);
				    			MainApplication.sp.setHbarPolicy(ScrollBarPolicy.NEVER);
				    			if( MainApplication.MessageListItem.size() == 0){
				    				MainApplication.Main.setLeft(null);
				    				try {
										InitTalkVeiw(MainApplication.fromSid);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										 Alert  alert = new Alert(AlertType.ERROR);
										 alert.setTitle("未知错误");
										 alert.setContentText("初始化窗口失败看日志去 重试下就好了");
										 alert.showAndWait();
										e1.printStackTrace();
									}
				    				
				    			}else{
				    				MainApplication.Main.setLeft(MainApplication.sp);
				    				
				    			}
				    			
				    		});
				    		
				    		
				    		
				    		
						}

						VBox Vb = new VBox();
						Vb.setMaxWidth(341);
						for (Iterator iterator = MainApplication.MessageListItem.iterator(); iterator.hasNext();) {
							
							GridPane gridPane = (GridPane) iterator.next();
//							VBox.setMargin(gridPane, new Insets(5));
							Vb.getChildren().add(gridPane);
							Vb.setId("VbFriedList");
							
						} 
						//System.out.println("--------");
						MainApplication.sp = new ScrollPane();
						MainApplication.sp.setId("spFriedList");
						MainApplication.sp.setVbarPolicy(ScrollBarPolicy.NEVER);
						MainApplication.sp.setHbarPolicy(ScrollBarPolicy.NEVER);
						MainApplication.sp.setPrefWidth(341);
						MainApplication.sp.setContent(Vb);
						if(MainApplication.nearlyMessageList.size() == 0){
		    				MainApplication.Main.setLeft(null);
		    				Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Alert alert=new Alert(Alert.AlertType.INFORMATION);
				    		        alert.setHeaderText("提示");
				    		        alert.setContentText("还没有消息哦，快去找好友聊天吧");
				    		        alert.showAndWait();
								}
		    				
		    				});
		    				
		    			}else{
		    				MainApplication.Main.setLeft(MainApplication.sp);
		    			}
						
				      
				        //---------消息列表结束
					}
				});  

    }  
	
	
	
	/**
	 * 	FriendLsit
	 * @param event
	 * @throws IOException
	 * @throws JSONException
	 * @throws DocumentException
	 */
	@FXML
	public  void onMouseClickedFriend(MouseEvent event) throws IOException, JSONException, DocumentException {  
		if(MainApplication.LoginStatus == 0){
	        Alert alert=new Alert(Alert.AlertType.WARNING);
	        alert.setHeaderText("提示");
	        alert.setContentText("您未登录，请先登录后在执行此操作~~~");
	        alert.showAndWait();
	        return ;
		}
		
		// 用来缓存好友列表  添加好友后更新缓存 其余调用缓存
        if(  MainApplication.FriendStatus == 1){
        	try {
				new FriendList(MainApplication.fromSid);
				
			} catch (IOException | JSONException | DocumentException e) {
				// TODO Auto-generated catch block
				 Alert  alert = new Alert(AlertType.ERROR);
				 alert.setTitle("IOException JSONException  DocumentException");
				 alert.setContentText("初始化好友列表失败 看日志去 重试下就好了");
				 alert.showAndWait();
				e.printStackTrace();
			}
        	  MainApplication.FriendStatus = 0;
        }
		Platform.runLater(new Runnable() {

			private List<? extends Node> projects;

			@Override
			public void run() {
				MainApplication.newMsg.setText("");
				// TODO Auto-generated method stub
				GridPane g = new GridPane();
				TreeItem<GridPane> rootItem = new TreeItem<GridPane> (g);
				rootItem.setExpanded(true);
				
				SAXReader reader = new SAXReader();        
				// 读取指定文件       
				org.dom4j.Document doc;
				try {
					doc = reader.read(new File("./cache/xml/"+MainApplication.fromSid+"FriendList.xml"));
					projects = doc.selectNodes("/FriendList/sid[@id='"+MainApplication.fromSid+"']");
					MainApplication.friendList = projects;
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
				// 获取根节点list      
				
				if(projects.size() <= 0){
					try {
						new FriendList(MainApplication.fromSid);
						// 读取指定文件       
					} catch (IOException | JSONException | DocumentException e) {
						// TODO Auto-generated catch block
						Alert  alert = new Alert(AlertType.ERROR);
						 alert.setTitle("IOException JSONException  DocumentException");
						 alert.setContentText("初始化好友列表失败 看日志去 重试下就好了");
						 alert.showAndWait();
						e.printStackTrace();
					}
      
					
					
					
				}else{
					
				}
				// add FriednList card
				for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
					Element node = (Element) iterator.next();
					for (Node node2 : node) {
						TreeItem<GridPane> Frienditem;
						GridPane gp = new GridPane();
//						gp.setGridLinesVisible(true);
						gp.setUserData(((Element) node2).getText());
						gp.setId("friendlist");
						gp.setAlignment(Pos.CENTER);
						Image  ic =null;
						try {
							ic = new Image("http://api.kilingzhang.com/diantalk/diantalk/"+gp.getUserData()+".jpg");	
						} catch (Exception e) {
							ic = new Image(getClass().getResourceAsStream("/images/avatar.png"));	
							// TODO: handle exception
						}	
						ImagePattern patt = new ImagePattern(ic);
						Circle cir = new Circle(35);
						cir.setFill(patt);		
						
						Label l = null;
						try {
							l = new Label(Person.getPerson(((Element) node2).getText()).get("name")+":"+((Element) node2).getText());
						} catch (DocumentException | IOException | JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Label s = new Label("好友呦");

						s.setPadding(new Insets(0,0,0,50));
						gp.add(l, 2, 0, 2,1);
						gp.add(s, 2, 1, 2,1);
						gp.add(cir, 0, 0, 2, 2);
						gp.getColumnConstraints().add(new ColumnConstraints(40));
						gp.getColumnConstraints().add(new ColumnConstraints(40));
						gp.getColumnConstraints().add(new ColumnConstraints(150));
						gp.getRowConstraints().add(new RowConstraints(30));
						gp.setMinHeight(65);
						gp.setMinWidth(230);
						
						gp.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent arg0) {
								try {
									MainApplication.type = gp.getId();
									InitTalkVeiw(gp.getUserData().toString());
								} catch (JSONException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						});
						Frienditem = new TreeItem<GridPane> (gp);
						
						Frienditem.setValue(gp);
						rootItem.getChildren().add(Frienditem);
						
					}
				}
				
				//Group
				try {
					doc = reader.read(new File("./cache/xml/"+MainApplication.fromSid+"GroupList.xml"));
					projects = doc.selectNodes("/GroupList/sid[@id='"+MainApplication.fromSid+"']");
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
				// 获取根节点list      
				
				if(projects.size() <= 0){
					try {
						new GroupList(MainApplication.fromSid);
						//System.out.println("NEW GROUPLIST");
						// 读取指定文件       
					} catch (IOException | JSONException | DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
      
					
					
					
				}else{
					
				}
				
				for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
					Element node = (Element) iterator.next();
					//System.out.println(node.getText());
					for (Node node2 : node) {
						TreeItem<GridPane> Frienditem;
						GridPane gp = new GridPane();
//						gp.setGridLinesVisible(true);
						gp.setUserData(((Element) node2).getText());
						gp.setId("grouplist");
						
						gp.setAlignment(Pos.CENTER);
						Image ic =  null;
						ic = new Image(getClass().getResourceAsStream("/images/avatar.png"));	
						ImagePattern patt = new ImagePattern(ic);
						Circle cir = new Circle(35);
						cir.setFill(patt);		
						
						Label l = null;
						try {
							l = new Label(Group.getGroup(((Element) node2).getText()).get("name")+":"+((Element) node2).getText());
						} catch (DocumentException | IOException | JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Label s = new Label("(群)");
//						l.setPadding(new Insets(0,0,0,20));
						s.setPadding(new Insets(0,0,0,50));
						gp.add(l, 2, 0, 2,1);
						gp.add(s, 2, 1, 2,1);
						gp.add(cir, 0, 0, 2, 2);
						gp.getColumnConstraints().add(new ColumnConstraints(40));
						gp.getColumnConstraints().add(new ColumnConstraints(40));
						gp.getColumnConstraints().add(new ColumnConstraints(150));
						gp.getRowConstraints().add(new RowConstraints(30));
						gp.setMinHeight(65);
						gp.setMinWidth(230);
						
						gp.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent arg0) {
								try {
									MainApplication.type = gp.getId();
									InitTalkVeiw(gp.getUserData().toString());
									
								} catch (JSONException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						});
						Frienditem = new TreeItem<GridPane> (gp);
						
						Frienditem.setValue(gp);
						rootItem.getChildren().add(Frienditem);
						
					}
				}
				
				TreeView<GridPane> tree = new TreeView<GridPane> (rootItem);
				
				//Group
	
				MainApplication.Main.setLeft(tree);
				try {
					InitTalkVeiw(MainApplication.toSid);
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    }  

	
	/**
	 *    Application 
	 * @param event
	 */
	@FXML
	public void onMouseClickedApp(MouseEvent event) {  
		if(MainApplication.LoginStatus == 0){
	        Alert alert=new Alert(Alert.AlertType.WARNING);
	        alert.setHeaderText("提示");
	        alert.setContentText("您未登录，请先登录后在执行此操作~~~");
	        alert.showAndWait();
	        return ;
		}
		Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						MainApplication.newMsg.setText("");
						// TODO Auto-generated method stub
						
						 Parent App = null;
							try {
								App = FXMLLoader.load(getClass().getResource("/View/APP.fxml"));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
					         MainApplication.Main.setCenter(App);
					         MainApplication.Main.setLeft(null);
					
					}
				});
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
        MainApplication.SHOWHTML_end = "</div></div><script>window.onload=function(){function loadImg(src,src1){currImg=new Image();currImg.src=src;currImg.onload=function(){var rightDiv=document.getElementsByClassName('right');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src}currImg2=new Image();currImg2.src=src1;currImg2.onload=function(){var rightDiv=document.getElementsByClassName('left');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src1}window.scrollTo(0,document.body.scrollHeight)}}}loadImg('http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg','http://api.kilingzhang.com/diantalk/diantalk/"+to+".jpg')}</script></body></html>";       
        if(MainApplication.type.equals("friendlist")){
        	
        	VTalk.setUserData("friend_"+MainApplication.toSid);
        }else if(MainApplication.type.equals("grouplist")){
        	if(MainApplication.toSid.equals(to)){
        		 MainApplication.SHOWHTML_end = "</div></div><script>window.onload=function(){function loadImg(src,src1){currImg=new Image();currImg.src=src;currImg.onload=function(){var rightDiv=document.getElementsByClassName('right');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src}currImg2=new Image();currImg2.src=src1;currImg2.onload=function(){var rightDiv=document.getElementsByClassName('left');for(var i=0;i<rightDiv.length;i++){rightDiv[i].getElementsByTagName('img')[0].src=src1}window.scrollTo(0,document.body.scrollHeight)}}}loadImg('http://api.kilingzhang.com/diantalk/diantalk/"+MainApplication.fromSid+".jpg','http://api.kilingzhang.com/diantalk/diantalk/avatar.png')}</script></body></html>";       
        	       
        	}
        	VTalk.setUserData("group_"+MainApplication.toSid);
        }
       
        if(MainApplication.MsgMap.containsKey(toSid) ){
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
    		Document doc = Jsoup.connect(url).get();
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
				HTMLEditor inputH = new HTMLEditor();
//		        VTalk.getChildren().add(inputH);
		      
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
				MainApplication.InfoStage.setTitle(MainApplication.toSid+"的详细信息");
				// 设置窗口显示模式
				MainApplication.InfoStage.initModality(Modality.WINDOW_MODAL);
	            // 设置窗口的所有者
				MainApplication.InfoStage.initOwner(MainApplication.primaryStage);
				WebView w  = new WebView();
				WebEngine g = w.getEngine();
				g.load("http://api.kilingzhang.com/diantalk/userInfo.php?sid="+MainApplication.fromSid+"&searchSid="+MainApplication.toSid);
	            Scene MusicStageScene = new Scene(w);
	            MainApplication.InfoStage.setScene(MusicStageScene);
	            
	            // Show the dialog and wait until the user closes it
	            MainApplication.InfoStage.showAndWait();
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
					MainApplication.MusicStage = new Stage();
					MainApplication.MusicStage.setTitle("分享音乐");
		            // 设置窗口显示模式
					MainApplication.MusicStage.initModality(Modality.WINDOW_MODAL);
		            // 设置窗口的所有者
					MainApplication.MusicStage.initOwner(MainApplication.primaryStage);
		  
		   
		            Scene MusicStageScene = new Scene(MusicSearch,540,700);
		            MainApplication.MusicStage.setScene(MusicStageScene);
		            
		            // Show the dialog and wait until the user closes it
		            MainApplication.MusicStage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
				String html = MainApplication.htmlEditor.getHtmlText();
				MainApplication.htmlEditor.setHtmlText("");
				if(html.length()<= 0 || html == null){
					Alert a = new Alert(Alert.AlertType.WARNING);
					a.setHeaderText("提醒");
					a.setContentText("禁止信息框输入空");
					a.showAndWait();
					return ;
				}
				String pattern = "<html dir="+'"'+"ltr"+'"'+"><head></head><body contenteditable="+'"'+"true"+'"'+">(.*?)</body></html>";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(html);
//				//System.out.println(m.toString());
				if (m.find( )) {
//			         //System.out.println("Found value: " + m.group(0) );
//			         //System.out.println("Found value: " + m.group(1) );
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
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (DocumentException e) {
								// TODO Auto-generated catch block
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
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
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
			 		 
			 		
			     
			      } else {
			         //System.out.println("NO MATCH");
			      }

			}
		});
        
	}


}

