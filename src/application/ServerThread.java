/**
 * 
 */
package application;
/**
 * 
 */


import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.ws.soap.Addressing;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import __class.ApiURL;
import __class.DB;
import __class.config;


/**
 * @author Slight
 *
 */
public class ServerThread {
	
	
	static ServerSocket server = null;
	
	static String[] preserveWords = {"fuck","操你妈"};
	


	private static DB dB;
	
	
	/**
	 * 
	 */
	public ServerThread() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//DianTalk服务器 开启
		
		System.out.println("DianTalk  Server starting ------");
		dB = new DB();
		try {
			//创建服务端接收
			server = new ServerSocket(DB.SERVERPORT);
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error:"+e);
		}
		
		System.out.println("Server listening on port "+DB.SERVERPORT);
		while (true) {
			
				try {
					//接受到客户端连接
					Socket socket = server.accept();
					//创建新线程
					Client c = new Client(socket);
					//线程开始
					c.start();
					if(socket!=null){
						//线程建立
						System.out.println(socket+"has been connected----");
					}
					
				} catch (IOException e) {
					// TODO: handle exception
					System.out.println("Error:"+e);
				}
		
			
		}
	}

}

//服务端线程类 处理获取到客户端进程发送的信息
/**
 * @author Slight
 *
 */
class Client extends Thread {
	
	Socket socket = null;  //
	String sid;			   // 客户端用户学号
	String account;		   // 客户端用户账号
	String name;		   // 客户端用户姓名
	InetAddress ip;		   // 客户端用户  IP

	DataInputStream ReadStream;  //客户端发送到服务器的消息
	PrintStream WriteStream;	 //服务器发送给客户端的消息
	DB dB;						 //连接服务器数据库
	private String dateNowStr;   //现在时间
	private String password;
	private String friend_list;
	private String AccountType;
	
	//创建安全线程Vector向量  保存客户端的线程
	protected static Vector<Client>clientlist = new Vector<Client>();  
	
	//初始化线程类
	/**
	 * @param socket  客户端的线程
	 */
	public Client(Socket socket){
		
		//获取客户端请求
		this.socket = socket;
		try {
			//创建服务端对客户端的发送获取消息操作变量
			ReadStream = new  DataInputStream(this.socket.getInputStream());			
			WriteStream = new PrintStream(this.socket.getOutputStream());
			//创建数据库
			dB = new DB();
			
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error:"+e);
		}
		
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 *  重构线程运行类
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			//等待客户端发送消息
			while(true){
				
				String line = null;
				try {
					//获取客户端发送消息 
					line = ReadStream.readLine();
					
					try {
						if(line==null){
							//如果获取到消息为空  出现未知错误 此线程关闭
							return;
						}else{
							
							//消息过滤  屏蔽非法文字
							System.out.println("client:->>"+line);
							line = new String(line.getBytes("UTF-8"),"UTF-8");
							line = URLDecoder.decode(line,"UTF-8");
							System.out.println("client:->>"+line);
							for(int i=0;i<ServerThread.preserveWords.length;i++){
								line = line.replace(ServerThread.preserveWords[i], "***");
								//调试获取到的客户端消息
								System.out.println(line.toString());
								
							}
						}
							
					} catch (Exception exxx) {
						// TODO: handle exception
						exxx.printStackTrace();
					}
	
				} catch (IOException exx) {
					// TODO: handle exception
					System.out.println("Error:"+exx);
//					ChatServer.ReadStreamconnect(this);
					return;
				}
				
				//所有消息以json格式传输 创建json对象
				JSONObject json = new JSONObject(line);
				//根据code 判断消息类型 根据code传入不同方法中处理数据请求
				String code = json.getString("code");
				//输出调试code
				System.out.println(code);
				//所有在线线程
				Enumeration<Client> allclients = clientlist.elements();
			switch (code) {
			//DianTalk登录
			case "0001":
				Login(json); 
				SendLoginStatus(json,1);
				break;
			//DianTalk注销	
			case "0002":
				Logout();
				SendLoginStatus(json,2);
				break;
			case "0003":
				
				break;
			case "0004":
				
				break;
				
				
			//会话信息事件	
				
				case "1001":
					// 好友会话消息
					String fromSid;  //客户端账号
					String toSid;    //消息接收着账号
					String msg;		 //消息
					String fromIp;   //客户端 IP
					
						//判断好友消息类型
						switch (json.getString("status")) {
						
							//100101       发送消息
							case "100101":
		//						System.out.println(json.toString());
								fromSid = json.getString("from");
								toSid = json.getString("to");
								msg = json.getString("msg");
								fromIp = json.getString("ip");
								sendFriend(fromSid,toSid,msg,fromIp);
								break;
							//100102      语音消息
							case "100102":
								
								break;
							
							case "100103":
								
								break;
								
							//100104        添加好友
							case "100104":
								fromSid = json.getString("from");
								toSid = json.getString("to");
								System.out.println(fromSid+"请求添加"+toSid+"好友");
								dB.applyFriend(fromSid,toSid);
								//循环寻找被申请者线程
								while(allclients.hasMoreElements()){            
						       	 Client item = (Client)allclients.nextElement();  
						       	 	if(item.name.equals(toSid)){
						       	 		JSONObject Sjson = new JSONObject(); 
						       	 		Sjson.put("code", "2003");
					       	 			Sjson.put("msg", fromSid + "请求加您为好友");
					       	 			Sjson.put("from", fromSid);
					       	 		    Sjson.put("to", toSid);
					       	 			
					       	 		    item.send(Sjson);
						       	 		break;
						       	 	}
								}
								break;
								
							//100105       同意添加好友
							case "100105":
								fromSid = json.getString("from");
								toSid = json.getString("to");
								
								//循环寻找被申请者线程
								while(allclients.hasMoreElements()){            
						       	 Client item = (Client)allclients.nextElement(); 
						       	 	//找到请求者  发送请求成功通知
						       	 	if(item.name.equals(fromSid)){
						       	 		JSONObject Sjson = new JSONObject(); 
						       	 		Sjson.put("code", "1001051");
					       	 			Sjson.put("msg", fromSid + "同意您的好友请求");
					       	 			Sjson.put("from", fromSid);
					       	 		    Sjson.put("to", toSid);
					       	 		    
					       	 			//同意好友状态
//					       	 			dB.updateApplyFriendStatus(fromSid,toSid,1);
					       	 			// 修改Friendlist
						       	 		String url = ApiURL.MYSQL_SET_FRIEND_LIST.getUrl()+config.Parme+"fromSid="+fromSid+"&toSid="+toSid+"&status="+"1";
						       			System.out.println("MYSQL_GET_FRIEND_LIST->"+ url);
						       			//获取用户信息判断登陆
						       			Document doc = Jsoup.connect(url).get();
						       			JSONObject data = new JSONObject(doc.body().text());
						       			if(data !=null && data.getString("code").equals("0")){
						       				
						       			}else{
						       				Sjson.put("msg", toSid + "同意您的好友请求,添加失败请重试");
						       			}
						       			item.send(Sjson);
						       	 		break;
						       	 	}
								}
								break;
								
							//100106       拒绝添加好友
							case "100106":
								fromSid = json.getString("from");
								toSid = json.getString("to");
								
								//循环寻找被申请者线程
								while(allclients.hasMoreElements()){            
						       	 Client item = (Client)allclients.nextElement(); 
						       	 	//找到请求者  发送请求成功通知
						       	 	if(item.name.equals(fromSid)){
						       	 		JSONObject Sjson = new JSONObject(); 
						       	 		Sjson.put("code", "1001061");
					       	 			Sjson.put("msg", toSid + "拒绝您的好友请求");
					       	 			Sjson.put("from", toSid);
					       	 			//同意好友状态
					       	 			dB.updateApplyFriendStatus(fromSid,toSid,2);
					       	 			item.send(Sjson);
					       	 			
						       	 		break;
						       	 	}
								}
								break;
								
							//100107       单方面删除好友
							case "100107":
								
								break;
							
							//100108       双方删除好友
							case "100108":
								
								break;
		
							default:
								break;
						}
						
						
					break;
				
				//1002  群消息
				case "1002":
					String  toGroup;
						//判断好友消息类型
						switch (json.getString("status")) {
							
							//100101       发送消息
							case "100201":
		//						System.out.println(json.toString());
								fromSid = json.getString("from");
								toGroup =  json.getString("to");
								msg = json.getString("msg");
								fromIp = json.getString("ip");
								
								sendGroup(fromSid,toGroup,msg,fromIp);
								break;
							
		
							default:
								break;
						}
						
					break;
					
				
				//1004           分享音乐
				case "1004":
					fromSid = json.getString("from");
					toSid = json.getString("to");
					msg = json.getString("msg");
					System.out.println("MUSICSERVERS->"+msg);
					//循环寻找被申请者线程
					if(json.getString("status").equals("100401")){
						while(allclients.hasMoreElements()){            
					       	 Client item = (Client)allclients.nextElement(); 
					       	 	//找到请求者  发送请求成功通知
					       	 	if(item.name.equals(toSid)){
					       	 		JSONObject Sjson = new JSONObject(); 
					       	 		Sjson.put("code", "2004");
					       	 		Sjson.put("status", "20043");
				       	 			Sjson.put("msg",msg );
				       	 			Sjson.put("data", json.getString("data"));
				       	 			Sjson.put("from", fromSid);
				       	 			Sjson.put("to", toSid);
				       	 			item.send(Sjson);
				       	 			
					       	 		break;
					       	 	}
							}
					}else if(json.getString("status").equals("100402")){
						
						String url = ApiURL.MYSQL_GET_GROUP_INFO.getUrl()+config.Parme+"groupId="+toSid;
						System.out.println(url);
						Document doc1 = Jsoup.connect(url).get();
						JSONObject json1 = new JSONObject(doc1.body().text());
						JSONArray data = null;
						if(json1 != null && json1.getString("code").equals("0")){
							if(json1.toString().substring(36, 37).equals("[")){
								data = new JSONArray();
							}else{
								JSONObject j = new JSONObject(json1.get("data").toString());
								String group_list;
								if(j.getString("userlist").toString() == null || j.getString("userlist").toString().isEmpty() || j.getString("userlist").toString().length() <= 2){
									group_list = "[]";
								}else{
									System.out.println(j.getString("userlist"));
									group_list = j.getString("userlist");
								}
								
								data = new JSONArray(group_list.toString());
							}
							System.out.println("GroupListUser->:"+data.toString());
						}else{
							
						}
						int t = 0;
						for (int i = 0; i < data.length(); i++) {
							Enumeration<Client> allclients11 = clientlist.elements(); 
							String user = (String) data.get(i).toString();
							
							if(user.equals(fromSid)){
			       	 			continue;
			       	 		}
							System.out.println("user->:"+i+"->"+user);
							//循环寻找发送着线程
							while(allclients11.hasMoreElements()){            
					       	 Client item = (Client)allclients11.nextElement(); 
					       	 
					       	 	if(item.name.equals(user)){
					    
						       	 	JSONObject JsonServer = new JSONObject();
						       	 	//客户端接收消息 2004 消息通知   通知客户端这是一个音乐
						       	 JSONObject Sjson = new JSONObject(); 
					       	 		Sjson.put("code", "2004");
					       	 		Sjson.put("status", "20043");
				       	 			Sjson.put("msg",msg );
				       	 			Sjson.put("data", json.getString("data"));
				       	 			Sjson.put("from", fromSid);
				       	 			Sjson.put("to", toSid);
				       	 			item.send(Sjson);
									t = 1;
									break;
									
					       	 	}
					       	 
					       	 System.out.println("Client->:"+item.name);
						}
							
							if(t == 1){
				       	 		break;
				       	 	}
					}

					}
					break;
				//1005            传送文件
				case "1005":
					
					break;
				
				//2001个人信息
				case "2001":
					
					break;
					
				//2002          签名
				case "2002":
					
					break;
					
				//2004          消息通知
				case "2004":
					
					break;
					
				//2005          签名更新
				case "2005":
					
					break;
					
					
				//2006          空间更新	
				case "2006":
					
					break;
					
				//3001 某人申请加入群
				case "3001":
					fromSid = json.getString("from");
					toGroup = json.getString("group");
					toSid = json.getString("to");
					msg   = json.getString("msg");
					System.out.println(msg);
					dB.applyGroup(fromSid,toGroup);
					//循环寻找被申请者线程
					while(allclients.hasMoreElements()){            
			       	 Client item = (Client)allclients.nextElement();  
			       	 	if(item.name.equals(toSid)){
			       	 		JSONObject Sjson = new JSONObject(); 
			       	 		Sjson.put("code", "3001");
		       	 			Sjson.put("msg", msg);
		       	 			Sjson.put("from", fromSid);
		       	 		    Sjson.put("to", toSid);
		       	 		    Sjson.put("group", toGroup);
		       	 		    item.send(Sjson);
			       	 		break;
			       	 	}
					}
					break;
					
				//3002 某人被邀请加入群
				case "3002":
					
					break;
				case "3003":
					
					break;
				case "3004":
					
					break;
					
				//3005 某人被批准加入了群
				case "3005":
					
					break;
					
				//3006 某人退出群
				case "3006":
					
					break;
					
				//3007 某人被管理移除群
				case "3007":
					
					break;
					
				//3008 某群被解散
				case "3008":
					
					break;
					
				//3009 某人成为管理员
				case "3009":
					
					break;
					
				//3010 某人被取消管理员
				case "3010":
					
					break;
					
				//3011 群名片变动
				case "3011":
					
					break;
				
				//3012 群名变动//暂未解析
				case "3012":
				break;
				
				//3013 群公告变动
				case "3013":
				break;
				
				//3014 对象被禁言
				case "3014":
				break;
				
				//3015 对象被解除禁言
				case "3015":
				break;
				
				//3016 群管开启全群禁言
				case "3016":
				break;
				
				//3017 群管关闭全群禁言
				case "3017":
				break;
				
				//3018 群管开启匿名聊天
				case "3018":
				break;
				
				//3019 群管关闭匿名聊天
				case "3019":
				break;
				
				default:
					
					break;
				}
				
				
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	private void SendLoginStatus(JSONObject json,int status) throws JSONException, SQLException {
		// TODO Auto-generated method stub
		sid = this.sid;
		String sql = "SELECT * FROM friendlist WHERE sid =" + sid;
		ResultSet rs = dB.stmt.executeQuery(sql);
		
    	while(rs.next()){
    		//列表中存在申请队列
    	   friend_list = rs.getString("friend_list");
    	}
    	if(friend_list == null || friend_list.length() <= 2){
    		friend_list = "[]";
		}
    	JSONArray j = new JSONArray(friend_list);
		Enumeration<Client> allclients = clientlist.elements(); 
		for (int i = 0; i < j.length(); i++) {
			String u = j.get(i).toString();
			for (Iterator iterator = clientlist.iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				if(client.name.equals(u)){
					JSONObject JsonServer = new JSONObject();
					JsonServer.put("code","2007");
					if(status == 1){
						JsonServer.put("status", "20071");
						JsonServer.put("msg", sid+"好友已上线");
					}else{
						JsonServer.put("status", "20072");
						JsonServer.put("msg", sid+"好友已下线");
					}
					JsonServer.put("from", sid);
					
					client.send(JsonServer);
					break;
				}
				
			}
			
		}
		
	}


	private void sendGroup(String fromSid, String toGroup, String msg, String fromIp) throws SQLException, JSONException, IOException {
		// TODO Auto-generated method stub
				//创建变量保存客户端用户列表
				
				String url = ApiURL.MYSQL_GET_GROUP_INFO.getUrl()+config.Parme+"groupId="+toGroup;
				System.out.println(url);
				Document doc1 = Jsoup.connect(url).get();
				JSONObject json = new JSONObject(doc1.body().text());
				JSONArray data = null;
				if(json != null && json.getString("code").equals("0")){
					if(json.toString().substring(36, 37).equals("[")){
						data = new JSONArray();
					}else{
						JSONObject j = new JSONObject(json.get("data").toString());
						String group_list;
						if(j.getString("userlist").toString() == null || j.getString("userlist").toString().isEmpty() || j.getString("userlist").toString().length() <= 2){
							group_list = "[]";
						}else{
							System.out.println(j.getString("userlist"));
							group_list = j.getString("userlist");
						}
						
						data = new JSONArray(group_list.toString());
					}
					System.out.println("GroupListUser->:"+data.toString());
				}else{
					
				}
				
				for (int i = 0; i < data.length(); i++) {
					Enumeration<Client> allclients = clientlist.elements(); 
					String user = (String) data.get(i).toString();
					
					if(user.equals(fromSid)){
	       	 			continue;
	       	 		}
					System.out.println("user->:"+i+"->"+user);
					//循环寻找发送着线程
					while(allclients.hasMoreElements()){            
			       	 Client item = (Client)allclients.nextElement(); 
			       	 
			       	 	if(item.name.equals(user)){
			       	 		
				       	 	JSONObject JsonServer = new JSONObject();
				       	 	//客户端接收消息 2004 消息通知   通知客户端这是一个好友消息
							JsonServer.put("code","2004");
							JsonServer.put("status", "20042");
							JsonServer.put("from", fromSid);
							JsonServer.put("to", toGroup);
							JsonServer.put("msg", msg);
							//发送时间
							Date d = new Date();  
							//System.out.println(d);  
					        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					        dateNowStr = sdf.format(d); 
							JsonServer.put("sendtime",dateNowStr );
							//向客户端发送消息
							System.out.println("SendGroupMsg"+JsonServer.toString());
							item.send(JsonServer);
							System.out.println(fromSid + " to " + toGroup + " msg: " + msg);
							//保存好友聊天记录
							break;
							
			       	 	}
			       	 	
			       	 System.out.println("Client->:"+item.name);
				}
			}
				dB.saveMessage(fromSid, toGroup, msg, 1, fromIp);

	}


	/**
	 * 
	 * 发送聊天消息到好友
	 * @param fromSid   客户端用户帐号
	 * @param toSid     消息接受者账号
	 * @param msg	          消息
	 * @throws JSONException
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void sendFriend(String fromSid, String toSid, String msg, String fromIp) throws JSONException, SQLException, IOException {
		// TODO Auto-generated method stub
		//创建变量保存客户端用户列表
		Enumeration<Client> allclients = clientlist.elements(); 
		//循环寻找发送着线程
		while(allclients.hasMoreElements()){            
       	 Client item = (Client)allclients.nextElement();  
       	 	if(item.name.equals(toSid)){
       	 		if(toSid.equals(fromSid)){
       	 			msg = URLEncoder.encode(msg,"UTF-8");
       	 			System.out.println("toMySelfServer->"+msg);
       	 			String u = "http://www.tuling123.com/openapi/api?key=b2d39a28066f4457abe73939cdf86a7b&info="+msg+"&userid="+fromSid;
       	 			System.out.println(u);
       	 			Document doc1 = Jsoup.connect(u).get();
       	 			JSONObject json = new JSONObject(doc1.body().text());
       	 			if(json.getString("code").equals("100000")){
       	 				msg = json.getString("text");
       	 			}else{
       	 				msg = json.getString("text");
       	 			}
       	 		}
       	 	JSONObject JsonServer = new JSONObject();
       	 	//客户端接收消息 2004 消息通知   通知客户端这是一个好友消息
			JsonServer.put("code","2004");
			JsonServer.put("status", "20041");
			JsonServer.put("from", fromSid);
			JsonServer.put("to", toSid);
			JsonServer.put("msg", msg);
			
			//发送时间
			Date d = new Date();  
			//System.out.println(d);  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        dateNowStr = sdf.format(d); 
			JsonServer.put("sendtime",dateNowStr );
			//向客户端发送消息
			item.send(JsonServer);
			break;
       	 	}
       	 	
		
	}
		System.out.println(fromSid + " to " + toSid + " msg: " + msg);
		//保存好友聊天记录
		dB.saveMessage(fromSid, toSid, msg, 1, 	fromIp);
}

	/**
	 * @param c   客户端线程
	 * @param json  客户端消息
	 * @throws JSONException
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void  Login(JSONObject json) throws JSONException, IOException, SQLException {
		int i = 0;
		Enumeration<Client> allclients = clientlist.elements(); 
		this.ip = this.socket.getInetAddress();
		this.sid = json.getString("user");
		this.name = json.getString("user");
		this.password = json.getString("password");
		this.AccountType = json.getString("type");
		
		if(AccountType.equals("Account:")){
			String sql = "SELECT * FROM `userinfo` WHERE `account` = '" + sid + "'";
			System.out.println(sql);
			ResultSet rs = dB.stmt.executeQuery(sql);
			i = 0;
            while(rs.next()){
            	this.sid = rs.getString("sid");
            	System.out.println("Sid = "+ sid);
            	if(sid.length() <= 0 || sid == null){
            		continue;
            	}          	
            	i++;
            }
            System.out.println("i="+i);
            if(i == 0){
            	JSONObject JsonServer = new JSONObject();
       			JsonServer.put("code","2");
       			JsonServer.put("sid",this.sid);
       			JsonServer.put("msg", "无此账号，看看是否账号错误还是未绑定学号呢?");
       			send(JsonServer);
       			return ;
            }
		
		}
		//JWXT  Login登陆验证
		String url = ApiURL.EDU_LOGIN.getUrl()+config.Parme+"sid="+sid+"&password="+password;
		System.out.println("EDU_LOGIN"+ url);
		//获取用户信息判断登陆
		Document doc = Jsoup.connect(url).get();
		String str = new String(doc.body().text().getBytes("UTF-8"),"UTF-8");
		JSONObject data = new JSONObject(str);
	
		//跳过验证
	/*	JSONObject data = new JSONObject("{'code': 0,'msg': '成功','data': {'name': '"+this.sid+"','sex': '秀吉','date_of_birth': '19970110','province': '湖南省','nation': '汉族','college': '信息工程学院','major': '软件工程','class': '15软件工程2'}}");
	*/	
		String code = data.getString("code");
	
		
	
		if(!code.equals("0")){
			JSONObject JsonServer = new JSONObject();
   			JsonServer.put("code","2");
   			JsonServer.put("sid",this.sid);
   			JsonServer.put("msg", data.getString("msg"));
   			send(JsonServer);
		}else{
		
			if(i == 0){
				dB.InitPerson(this.sid,password,data.getJSONObject("data"));
			}
			
			while(allclients.hasMoreElements()){            
		       	 Client item = (Client)allclients.nextElement();  
//		       	 System.out.println(item.name.toString());
//		       	 System.out.println(json.getString("user"));
		       	 	    //判断是否登陆 登陆则强制第一个账号下线 
		       	 		if(item.name.equals(json.getString("user"))){
			       	 		JSONObject JsonServer = new JSONObject();
			       			JsonServer.put("code","1");
			       			JsonServer.put("sid",this.sid);
			       			JsonServer.put("msg", "您已经登陆在ip为:"+item.ip+"登陆,已将其强制下线请重新登录");
			       			JsonServer.put("ip", item.ip);
			       			send(JsonServer);
			       			try {
								Logout(item);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			       			return ;	       	 		
		       	 		}
		           }  
			
			JSONObject JsonServer = new JSONObject();
			JsonServer.put("code","0");
			JsonServer.put("msg", "success");
			JsonServer.put("sid",this.sid);
			JsonServer.put("data", data.getJSONObject("data"));
			send(JsonServer);
			clientlist.addElement(this); 
	         //遍历访问每个客户端对象  
			allclients = clientlist.elements(); 
			System.out.println("当前在线人数:-------------");
			while(allclients.hasMoreElements()){            
	       	 Client item = (Client)allclients.nextElement();  		
	       	 		System.out.println(item.name+":IP "+item.ip+item.socket);
	           }  
			
		}      
	}

	/**
	 * @param item
	 * @param inetAddress
	 * @throws JSONException
	 * @throws IOException
	 */
	private void Logout(Client item) throws JSONException, IOException {
		// TODO Auto-generated method stub
		JSONObject JsonServer = new JSONObject();
			clientlist.removeElement(item);
			JsonServer.put("code","2");
			JsonServer.put("msg", "由于您已经在其余地点登陆,此帐号强制退出");
			JsonServer.put("ip", item.ip);
			item.send(JsonServer);
			System.out.println("在"+item.ip+item.name+"已被在"+item.ip+"被强制下线");
			item.socket.close();
	}
	
	private void Logout() throws JSONException, IOException {
		// TODO Auto-generated method stub
		JSONObject JsonServer = new JSONObject();
			clientlist.removeElement(this);
			JsonServer.put("code","1");
			JsonServer.put("msg", "成功下线");
			this.send(JsonServer);
			System.out.println("在"+this.ip+this.name+"已下线");
			this.socket.close();
	}
	
	
	/**
	 *  获取用户的账号
	 * @return （学号或账号）
	 */
	public String getUserAccount() {
		return name = (sid != null ? sid :account) ;
	}
	
	
	
	/**
	 *  向客户端发送信息  json字符串
	 *  
	 * @param jsonServer 
	 */
	public void  send(JSONObject jsonServer) {
		
		String j;
		try {
			j = URLEncoder.encode(jsonServer.toString(), "UTF-8");
			WriteStream.println(j);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		WriteStream.flush();
		System.out.println(this.getUserAccount() +"-> Thread sends ->"+jsonServer);
	}
	
	
	
}
