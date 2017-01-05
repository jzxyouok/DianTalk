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
public class GroupList {

	/**
	 * 
	 */
	private int    		userId;
	private String   	sid;
	private String 		account;
	private JSONArray   groupListJSONArray; 
	private JSONArray   applygroupListArray;
	private List        groupList = new ArrayList<Group>();
	private List        applygroupList = new ArrayList<Group>();
	private String group_list;
	private String d;
	public GroupList(String sid) throws IOException, JSONException, DocumentException {
		// TODO Auto-generated constructor stub
		
		this.sid = sid;
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("GroupList");
		
		this.sid = sid;
		String url = ApiURL.MYSQL_GET_GROUP_LIST.getUrl()+config.Parme+"userSid="+sid;
		System.out.println(url);
		System.out.println(url);
		Document doc1 = Jsoup.connect(url).get();
		JSONObject json = new JSONObject(doc1.body().text());
		
		if(json != null && json.getString("code").equals("0")){
			if(json.toString().substring(36, 37).equals("[")){
				Element ele = root.addElement("sid");
				ele.addAttribute("id", this.sid);
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"groupList.xml");    
				writer.setOutputStream(fos);
				writer.write(doc);           
				System.out.println("new groupList 生成文档完毕！"); 
				return;
			}
			JSONObject data = new JSONObject(json.get("data").toString());
			System.out.println(data.toString());
			sid = data.getString("sid");
			account = data.getString("account");
			//好友列表
			if(data.getString("group_list").toString() == null || data.getString("group_list").toString().isEmpty() || data.getString("group_list").toString().length() <= 2){
				group_list = "[]";
			}else{
				System.out.println(data.getString("group_list"));
				group_list = data.getString("group_list");
			}
			groupListJSONArray = new JSONArray(group_list.toString());
			Element ele = root.addElement("sid");
			ele.addAttribute("id", this.sid);
			for (int i = 0; i < groupListJSONArray.length(); i++) {
				groupList.add(new Group(groupListJSONArray.get(i).toString()));					 
				ele.addElement("Group").addText(groupListJSONArray.get(i).toString());
//					System.out.println(ele);
			}
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"groupList.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("new groupList 生成文档完毕！"); 
			
		}else{
			return ;
		}
			
		
		
		
		
		

		

		
	}
	public static void main(String[] args) throws IOException, JSONException, DocumentException {
//		GroupList groupList = new GroupList("2015551439");
//		ArrayList<String> grouplist = getgroupList("2015551439");
//		for (String string : grouplist) {
//			System.out.println(string);
//		}
//		Map Group = Group.getGroup("2015551439");
//		System.out.println(Group.get("name"));
//		resetgroupList("2015551439");
	}
	
	
	
	
	public static ArrayList getgroupList(String sid) throws DocumentException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+sid+"groupList.xml"));     
		// 获取根节点list      
		List projects = doc.selectNodes("/GroupList/sid[@id='"+ sid +"']");
		ArrayList groupList = new ArrayList<String>();
		
		for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
			int i = 0;
			Element object = (Element) iterator.next();
			for (Node node : object) {
				groupList.add(node.getText());
				i++;
			}	
		}
		return groupList;
	}
	
	
	public static void resetgroupList(String sid) throws DocumentException, IOException, JSONException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+sid+"groupList.xml"));     
		// 获取根节点list      
		Element groupList = doc.getRootElement();
		Element parent = groupList;
		for (Node node : groupList) {
			if(node.asXML().substring(9, 9+sid.length()).equals(sid)){
				parent.remove(node);
				org.dom4j.Document doc1 = DocumentHelper.parseText(parent.asXML());
				System.out.println(parent.asXML());
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/"+sid+"groupList.xml");    
				writer.setOutputStream(fos);
				writer.write(doc1);           
				System.out.println("生成文档完毕！"); 
				break;
			}
		}
		System.out.println("生成文档完毕！ no chaged-"); 
		new GroupList(sid);
		
	
	
	}

	
	
	
	
}
