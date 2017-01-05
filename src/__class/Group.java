/**
 * 
 */
package __class;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * @author Slight
 *
 */
public class Group {
	
	/**
	 * 
	 */
	
	private String groupId;
	private String name;
	private String creator;
	private JSONArray userList;
	
	public static void main(String[] args) throws IOException, JSONException, DocumentException {
//		Group	p = new Group("6666666667");
//		System.out.println(getGroup("6666666666"));
		
	}

	
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}


	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}


	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}


	/**
	 * @return the userList
	 */
	public JSONArray getUserList() {
		return userList;
	}


	/**
	 * @param userList the userList to set
	 */
	public void setUserList(JSONArray userList) {
		this.userList = userList;
	}


	public Group(String groupId) throws IOException, JSONException, DocumentException {
		// TODO Auto-generated constructor stub
		this.groupId = groupId;
		File file =new File("./cache/");    
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file.isDirectory()){       
		    System.out.println("//不存在");  
		    file.mkdir();    
		}
		file =new File("./cache/xml/");    
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file.isDirectory()){       
		    System.out.println("//不存在");  
		    file.mkdir();    
		}
		file = new File("./cache/xml/"+this.groupId+"Group.xml");
		if(!file.exists()  && !file.isDirectory()){
			System.out.println("//不存在");  
			SAXReader reader = new SAXReader();        
			// 读取指定文件       
			org.dom4j.Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Group");
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("new FriendList 生成文档完毕！"); 
			
		}
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+this.groupId+"Group.xml"));     
		// 获取根节点list      
		List rs = doc.selectNodes("/Group/groupId[@id='"+this.groupId+"']");
		
		
		String url = ApiURL.MYSQL_GET_GROUP_INFO.getUrl()+config.Parme+"groupId="+groupId;
		System.out.println(url);
		Document doc1 = Jsoup.connect(url).get();
		System.out.println("New Group `1 Doc:->" + doc1.body().text());
		
		JSONObject json = new JSONObject(doc1.body().text());
		if(json != null && json.getString("code").equals("0")){
			if(json.toString().substring(36, 37).equals("[")){
				System.out.println(json.toString().substring(36, 37));
				return ;
			}
			JSONObject data = new JSONObject(json.getString("data"));
//				System.out.println(data);
			
			this.groupId = data.getString("id");
			this.name = data.getString("name");
			this.creator = data.getString("creator");
			this.userList = new JSONArray(data.getString("userlist").toString());
			
			Element Group = doc.getRootElement();
			Element ele = Group.addElement("groupId");
			ele.addAttribute("id", this.groupId); 
			ele.addElement("groupId").addText(this.groupId);
			ele.addElement("name").addText(this.name);
			ele.addElement("creator").addText(this.creator);
			ele.addElement("userList").addText(this.userList.toString());
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("生成文档完毕！"); 
				
			
		}else{
			
		}

		
	}
	
	
	public Group(String groupId,String type) throws IOException, JSONException, DocumentException {
		// TODO Auto-generated constructor stub
		this.groupId = groupId;
		File file =new File("./cache/");    
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file.isDirectory()){       
		    System.out.println("//不存在");  
		    file.mkdir();    
		}
		file =new File("./cache/xml/");    
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file.isDirectory()){       
		    System.out.println("//不存在");  
		    file.mkdir();    
		}
		file = new File("./cache/xml/"+this.groupId+"Group.xml");
		if(!file.exists()  && !file.isDirectory()){
			System.out.println("//不存在");  
			SAXReader reader = new SAXReader();        
			// 读取指定文件       
			org.dom4j.Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Group");
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("new Group 生成文档完毕！"); 
			
		}
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+this.groupId+"Group.xml"));     
		// 获取根节点list      
		List rs = doc.selectNodes("/Group/groupId[@id='"+this.groupId+"']");
		if(rs.size() >= 1){
			
			for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
				Element object = (Element) iterator.next();
				this.groupId = object.elementText("id");
				this.name = object.elementText("name");
				this.creator = object.elementText("creator");
				this.userList = new JSONArray(object.elementText("userList").toString());
				
				
			}
		}else if(rs.size() <= 0){
			String url = ApiURL.MYSQL_GET_GROUP_INFO.getUrl()+config.Parme+"groupId="+groupId;
			System.out.println(url);
			Document doc1 = Jsoup.connect(url).get();
			System.out.println("New Group Doc:->" + doc1.body().text());
			
			JSONObject json = new JSONObject(doc1.body().text());
			if(json != null && json.getString("code").equals("0")){
				if(json.toString().substring(36, 37).equals("[")){
					System.out.println(json.toString().substring(36, 37));
					return ;
				}
				JSONObject data = new JSONObject(json.getString("data"));
//					System.out.println(data);
				
				this.groupId = data.getString("id");
				this.name = data.getString("name");
				this.creator = data.getString("creator");
				this.userList = new JSONArray(data.getString("userlist").toString());
				
				Element Group = doc.getRootElement();
				Element ele = Group.addElement("groupId");
				ele.addAttribute("id", this.groupId); 
				ele.addElement("groupId").addText(this.groupId);
				ele.addElement("name").addText(this.name);
				ele.addElement("creator").addText(this.creator);
				ele.addElement("userList").addText(this.userList.toString());
				
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
				writer.setOutputStream(fos);
				writer.write(doc);           
				System.out.println("生成文档完毕！"); 
					
				
			}else{
				
			}

		}
		
	}
	


	public Group(JSONObject data) throws JSONException, DocumentException, IOException{
		
		
		this.groupId = data.getString("id");
		this.name = data.getString("name");
		this.creator = data.getString("creator");
		this.userList = new JSONArray(data.getString("userList").toString());
		
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+this.groupId+"Group.xml"));     
		// 获取根节点list      
		List rs = doc.selectNodes("/Group/groupId[@id='"+this.groupId+"']");
	
		if(rs.size() == 1){
			     
			Element Group = doc.getRootElement();
			Element ele = Group.addElement("groupId");
		
			ele.addAttribute("id", this.groupId); 
			ele.addElement("groupId").addText(this.groupId);
			ele.addElement("name").addText(this.name);
			ele.addElement("creator").addText(this.creator);
			ele.addElement("userList").addText(this.userList.toString());
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("生成文档完毕！"); 
			
		}else if(rs.size() <= 0){
			
			Element Group = doc.getRootElement();
			Element ele = Group.addElement("groupId");
			ele.addAttribute("id", this.groupId); 
			ele.addElement("groupId").addText(this.groupId);
			ele.addElement("name").addText(this.name);
			ele.addElement("creator").addText(this.creator);
			ele.addElement("userList").addText(this.userList.toString());
			
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/"+this.groupId+"Group.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("生成文档完毕！"); 

		}else{
			return ;
		}
		
	}
	
	
	
	public static Map getGroup(String groupId) throws DocumentException, IOException, JSONException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		   
		// 获取根节点list     
		File file = new File("./cache/xml/"+groupId+"Group.xml");
		if(!file.exists()  && !file.isDirectory()){
			return null;
		}
		org.dom4j.Document doc = reader.read(new File("./cache/xml/"+groupId+"Group.xml"));  
		List projects = doc.selectNodes("/Group/groupId[@id='"+ groupId +"']");
		Map<String, String> Group = new HashMap();
		if(projects.size() <= 0){
			if(groupId == "" || groupId == null){
				return null ;
			}
			new Group(groupId);
			doc = reader.read(new File("./cache/xml/"+groupId+"Group.xml"));     
			// 获取根节点list      
			projects = doc.selectNodes("/Group/groupId[@id='"+ groupId +"']");
		}
		for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
			Element object = (Element) iterator.next();
			Group.put("groupId", object.elementText("groupId"));
			Group.put("name", object.elementText("name"));
			Group.put("creator", object.elementText("creator"));
			Group.put("userList",object.elementText("userList"));
			
//			
		}

		return Group;
	}


	
	
	

	
}
