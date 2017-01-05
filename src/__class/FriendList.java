/**
 * 
 */
package __class;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author Administrator
 *
 */
public class FriendList {

	/**
	 * 
	 */
	private int    		userId;
	private String   	sid;
	private String 		account;
	private JSONArray   friendListJSONArray; 
	private JSONArray   applyFriendListArray;
	private List        friendList = new ArrayList<Person>();
	private List        applyFriendList = new ArrayList<Person>();
	private String friend_list;
	private String d;
	public FriendList(String sid) throws IOException, JSONException, DocumentException {
		// TODO Auto-generated constructor stub
		
		this.sid = sid;
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("FriendList");
		
		this.sid = sid;
		String url = ApiURL.MYSQL_GET_FRIEND_LIST.getUrl()+config.Parme+"userSid="+sid;
		Document doc1 = Jsoup.connect(url).get();
		JSONObject json = new JSONObject(doc1.body().text());
		System.out.println(json.toString());
		if(json != null && json.getString("code").equals("0")){
			if(json.toString().substring(36, 37).equals("[")){
				Element ele = root.addElement("sid");
				ele.addAttribute("id", this.sid);
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"FriendList.xml");    
				writer.setOutputStream(fos);
				writer.write(doc);           
				System.out.println("new FriendList 生成文档完毕！"); 
				return;
			}
			JSONObject data = new JSONObject(json.get("data").toString());
			System.out.println(data.toString());
			sid = data.getString("sid");
			account = data.getString("account");
			//好友列表
			if(data.getString("friend_list").toString() == null || data.getString("friend_list").toString().isEmpty() || data.getString("friend_list").toString().length() <= 2){
				friend_list = "[]";
			}else{
				System.out.println(data.getString("friend_list"));
				friend_list = data.getString("friend_list");
			}
			friendListJSONArray = new JSONArray(friend_list.toString());
			Element ele = root.addElement("sid");
			ele.addAttribute("id", this.sid);
			for (int i = 0; i < friendListJSONArray.length(); i++) {
				friendList.add(new Person(friendListJSONArray.get(i).toString()));					 
				ele.addElement("Person").addText(friendListJSONArray.get(i).toString());
//					System.out.println(ele);
			}
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"FriendList.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("new FriendList 生成文档完毕！"); 
			
		}else{
			return ;
		}
			
		
		
		
		
		

		
	}
	public static void main(String[] args) throws IOException, JSONException, DocumentException {
//		FriendList friendList = new FriendList("2015551439");
//		ArrayList<String> friendlist = getFriendList("2015551439");
//		for (String string : friendlist) {
//			System.out.println(string);
//		}
//		Map person = Person.getPerson("2015551439");
//		System.out.println(person.get("name"));
//		resetFriendList("2015551439");
	}
	
	
	
	
	public static ArrayList getFriendList(String sid) throws DocumentException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+sid+"FriendList.xml"));     
		// 获取根节点list      
		List projects = doc.selectNodes("/FriendList/sid[@id='"+ sid +"']");
		ArrayList FriendList = new ArrayList<String>();
		
		for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
			int i = 0;
			Element object = (Element) iterator.next();
			for (Node node : object) {
				FriendList.add(node.getText());
				i++;
			}	
		}
		return FriendList;
	}
	
	
	public static void resetFriendList(String sid) throws DocumentException, IOException, JSONException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+sid+"FriendList.xml"));     
		// 获取根节点list      
		Element FriendList = doc.getRootElement();
		Element parent = FriendList;
		for (Node node : FriendList) {
			if(node.asXML().substring(9, 9+sid.length()).equals(sid)){
				parent.remove(node);
				org.dom4j.Document doc1 = DocumentHelper.parseText(parent.asXML());
				System.out.println(parent.asXML());
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"FriendList.xml");    
				writer.setOutputStream(fos);
				writer.write(doc1);           
				System.out.println("生成文档完毕！"); 
				break;
			}
		}
		System.out.println("生成文档完毕！ no chaged-"); 
		new FriendList(sid);
		
	
	
	}

	
	
	
	
}
