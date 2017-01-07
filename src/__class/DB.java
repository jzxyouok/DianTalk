/**
 * 
 */
package __class;


import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class DB {
	
	public static int SERVERPORT;
	public static String IP;
	private String dateNowStr ;
	public Statement stmt;
	private Connection conn;
	private JSONArray json;
	private String apply_friend_list;
	private Object friend_list;
	
	/**
	 * 
	 */
	public DB() {
		// TODO Auto-generated constructor stub
		Date d = new Date();  
//        System.out.println(d);  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        dateNowStr = sdf.format(d); 
        System.out.println("格式化后的日期：" + dateNowStr);  
        
		try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }
		
		String url="jdbc:mysql://localhost:3306/diantalk?characterEncoding=UTF-8";    //JDBC的URL    
        try {
        	
            conn = DriverManager.getConnection(url,    "root","");
            stmt = conn.createStatement();
            System.out.println("成功连接到数据库！");
            String sql = "SELECT * FROM config WHERE name = 'DianTalk' ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
//            	System.out.println(rs.getInt("id"));
//            	System.out.println(rs.getString("name"));
//            	System.out.println(rs.getInt("serverport"));
            	DB.SERVERPORT = rs.getInt("serverport");
            	DB.IP = rs.getString("serverip");;	
            }
            
           
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        
		
	}
	
	public void InitPerson(String sid,String password ,JSONObject jsonObject) throws SQLException, JSONException{
		String sql = "SELECT * FROM userinfo WHERE sid = "+ sid;
        ResultSet rs = stmt.executeQuery(sql);
        int i =0;
        while(rs.next()){
        	i++;
//        	System.out.println("->->->SQL");
        }
        if(i == 0){
        	sql = "INSERT INTO `userinfo`(`sid`, `account`, `name`, `major`, `class`, `college`, `date_of_birth`, `sid_password`, `password`, `email`, `img_src`, `signature`, `introduction`, `login_status`, `login_ip`, `qq`, `sign_status`, `home_status`) "
        			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    		PreparedStatement  pst = conn.prepareStatement(sql);
    		System.out.println(jsonObject.toString());
    		pst.setString(1, sid);
    		pst.setString(2, "");
    		pst.setString(3, jsonObject.getString("name"));
    		pst.setString(4, jsonObject.getString("major"));
    		pst.setString(5, jsonObject.getString("class"));
    		pst.setString(6, jsonObject.getString("college"));
    		pst.setString(7, jsonObject.getString("data_of_birth"));
    		pst.setString(8, password);
    		pst.setString(9, "");
    	
    		pst.setString(10, "@kilingzhang.com");
    		pst.setString(11, "2015551439.png");
    		pst.setString(12, "你还未填写个人信息，快去填写分享吧！~");
    		pst.setString(13, "你还未填写个人信息，快去填写分享吧！~");
    		pst.setInt(14, 1);
    		pst.setString(15, "127.0.0.1");
    		pst.setInt(16, 1353693508);
    		pst.setInt(17, 0);
    		pst.setInt(18, 0);
    		pst.executeUpdate();
        }
	}
	
	public void saveMessage(String formSid,String toSid ,String msg,int msgtype ,String ip) throws SQLException {
		String sql = "INSERT INTO chatrecord(fromSid,toSid,sendtime,msgtype,ip,msg) VALUES(?,?,?,?,?,?)";
		PreparedStatement  pst = conn.prepareStatement(sql);
		pst.setString(1, formSid);
		pst.setString(2, toSid);
		pst.setString(3, this.dateNowStr.toString());
		pst.setInt(4, msgtype);
		pst.setString(5, ip);
		msg = msg.replaceAll("<", "^");
		pst.setString(6,msg );
		pst.executeUpdate();
	}
	
	
	
	
	/**
	 * 	申请好友
	 *  0 申请
	 *  1 同意
	 *  2 拒绝 
	 * @param fromSid
	 * @param toSid
	 * @throws SQLException
	 * @throws JSONException
	 */
	public void applyFriend(String fromSid,String toSid) throws SQLException, JSONException{
		String sql = "SELECT * FROM friendlist WHERE sid =" + toSid;
		ResultSet rs = stmt.executeQuery(sql);
		int flag = 0;
		int f = 0;
		
    	while(rs.next()){
    		//列表中存在申请队列
    	
    	    apply_friend_list = rs.getString("apply_friend_list");
//    		System.out.println(apply_friend_list);
    		if(apply_friend_list == null || apply_friend_list.length() <= 2){
				System.out.println("NO HAVE");
			}else{
				json = new JSONArray(apply_friend_list);
	    		for (int i = 0; i < json.length(); i++) {
					JSONObject j =  (JSONObject) json.get(i);
					//判断是否为重复申请 重复跳过
					if(j.getString("sid").equals(fromSid)){
						System.out.println(fromSid + "已存在 pass");
						f = 1;
						break;
					}
				}
	    		flag ++;
			}
    		
    		if(f == 1){
    			
    		}else{
    			//第一次申请 添加申请队列
    			JSONObject  data = new JSONObject();
        		data.put("sid",fromSid);
        		data.put("status",0);
        		JSONArray json = new JSONArray();
        		json.put(data);
        		
    		}
    		
    	}
    	
    	
    	if(flag == 1){
    		apply_friend_list = json.toString();
    		sql = "UPDATE friendlist SET apply_friend_list ='" + apply_friend_list + "' WHERE sid = " + toSid;
    		System.out.println(sql);
    		int n = stmt.executeUpdate(sql);
    		if(n>0){
    			 System.out.println("update ok");
    		}else{
    			 System.out.println("update failed");
    		}
    	}else if(flag == 0){
    		//如果列表中没有申请队列  添加入申请队列
    		JSONArray json = new JSONArray();
    		JSONObject  data = new JSONObject();
    		data.put("sid",fromSid);
    		data.put("status",0);
    		json.put(data);
    		String apply_friend_list = json.toString();
    		sql = "INSERT INTO friendlist (friend_list,apply_friend_list,sid,account) VALUES(?,?,?,?)";
    		PreparedStatement  pst = conn.prepareStatement(sql);
    		pst.setString(1, "");
    		pst.setString(2, apply_friend_list);
    		pst.setString(3, toSid);
    		pst.setString(4, "");
    		pst.executeUpdate();
    		System.out.println("insert ok");
    	}else{
    		
    	}
       
	}
	
	
	/**
	 * 更新申请好友的状态
	 * @param fromSid   申请者
	 * @param toSid	          被申请者
	 * @param status    状态
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	public int updateApplyFriendStatus(String fromSid,String toSid,int status) throws SQLException, JSONException {
		String sql = "SELECT * FROM friendlist WHERE sid =" + toSid;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			apply_friend_list = rs.getString("apply_friend_list");
			friend_list = rs.getString("friend_list");
			System.out.println(apply_friend_list);
    		JSONArray json = new JSONArray(apply_friend_list);
    		
    		//
    		if(status == 1){
    			System.out.println(friend_list);
    			if(friend_list == null || friend_list.toString().isEmpty()  || friend_list.toString().length() <= 0){
    				friend_list = "[]";
    			}
    			JSONArray j = new JSONArray(friend_list.toString());
    			for (int i = 0; i < j.length(); i++) {
					String s = j.get(i).toString();
					if(s.equals(fromSid)){
						return 0;
					}
					
				}
    			j.put(fromSid);
    			friend_list = j.toString().replaceAll("\"", "");
    		}
    		apply_friend_list = apply_friend_list.replaceAll("\"sid\":\""+fromSid+"\",\"status\":\\d{1}", "\"sid\":\""+fromSid+"\",\"status\":"+status);
    		sql = "UPDATE friendlist SET friend_list = '"+ friend_list +"' , apply_friend_list ='" + apply_friend_list + "' WHERE sid = " + toSid;
    		System.out.println(sql);
    		int n = stmt.executeUpdate(sql);
    		if(n>0){
        		System.out.println("update "+fromSid+" apply " + toSid + "friend status :" + status);
        		return 0;
    		}else{
        		System.out.println("update "+fromSid+" apply " + toSid + "friend status :" + status + "failed");
    		}
    		
    		//-1 不存在此申请
			return -1;
		}
		//1  未知错误
		return 4;
	
	}
	
	public int deleteApplyFriendStatus(String fromSid,String toSid) throws SQLException, JSONException {
		String sql = "SELECT * FROM friendlist WHERE sid =" + toSid;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			String apply_friend_list = rs.getString("apply_friend_list");
			System.out.println(apply_friend_list);
    		JSONArray json = new JSONArray(apply_friend_list);
    		//
    		apply_friend_list = apply_friend_list.replaceAll("\\{\"sid\":\""+fromSid+"\",\"status\":\\d?\\},", "");
    		apply_friend_list = apply_friend_list.replaceAll(",\\{\"sid\":\""+fromSid+"\",\"status\":\\d?\\}", "");
    		apply_friend_list = apply_friend_list.replaceAll("\\{\"sid\":\""+fromSid+"\",\"status\":\\d?\\}", "");
    		
    		sql = "UPDATE friendlist SET apply_friend_list ='" + apply_friend_list + "' WHERE sid = " + toSid;
    		int n = stmt.executeUpdate(sql);
    		if(n>0){
        		System.out.println("update "+fromSid+" apply " + toSid + "friend status :" + apply_friend_list);
    		}else{
        		System.out.println("update "+fromSid+" apply " + toSid + "friend status :" + apply_friend_list + "failed");
    		}

    		//-1 不存在此申请
			return -1;
		}
		//1  未知错误
		return 4;
	
	}
	
	
	
	public void DbClose() throws SQLException {
		 conn.close();
         stmt.close();
	}
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws SQLException, JSONException {
		// TODO Auto-generated method stub
//		DB db = new DB();
//		db.updateApplyFriendStatus("2015551439", "2015551401",1);
	}

	public void applyGroup(String fromSid, String toSid) {
		// TODO Auto-generated method stub
		
	}

	

}
