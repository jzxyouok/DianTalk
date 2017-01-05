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

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/**
 * @author Slight
 *
 */
public class Person {
	
	/**
	 * 
	 */
	
	private String    userId; 
	private String   sid;
	private String account;
	private String major;
	private String _class;
	private String college;
	private String date_of_birth;
	private String sid_password;
	private String password;
	private String   telephone;
	private String email;
	private String img_src;
	private String signature;
	private String introduction; 
	private String    login_status; //登陆状态
	private String login_ip;
	private String   qq;
	private String    sign_status;  //签名更新状态
	private String    home_status;  //空间更新状态
	private String name;
	
	public static void main(String[] args) throws IOException, JSONException, DocumentException {
//		Person	p = new Person("2015551439");
//		System.out.println(getPerson("2015551434"));
	}

	
	public Person(String sid) throws IOException, JSONException, DocumentException {
		// TODO Auto-generated constructor stub
		this.sid = sid;
		File file =new File("./cache/");    
		//如果文件夹不存在则创建    
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("警告");
					alert.setContentText("初始化文件失败,请检本软件查是给予权限");
					alert.showAndWait();
				}
			});
		}
		
		file = new File("./cache/xml/Person.xml");
		if(!file.exists()  && !file.isDirectory()){
			System.out.println("//不存在");  
			SAXReader reader = new SAXReader();        
			// 读取指定文件       
			org.dom4j.Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Person");
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/Person.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("new FriendList 生成文档完毕！"); 
			
		}
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/Person.xml"));     
		// 获取根节点list      
		List rs = doc.selectNodes("/Person/sid[@id='"+this.sid+"']");
		
		if(rs.size() >= 1){
			
			for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
				Element object = (Element) iterator.next();
				this.userId = object.elementText("userId");
				this.account = object.elementText("account");
				this.name = object.elementText("name");
				this.major = object.elementText("major");
				this._class = object.elementText("class");
				this.college = object.elementText("college");
				this.date_of_birth = object.elementText("date_of_birth");
				this.telephone = object.elementText("telephone");
				this.email = object.elementText("email");
				this.img_src = object.elementText("img_src");
				this.signature = object.elementText("signature");
				this.introduction = object.elementText("introduction"); 
				this.login_status = object.elementText("login_status"); //登陆状态
				this.login_ip = object.elementText("login_ip");
				this.qq = object.elementText("qq");
				this.sign_status = object.elementText("sign_status");  //签名更新状态
				this.home_status = object.elementText("home_status");  //空间更新状态
				
			}
		}else if(rs.size() <= 0){
			String url = ApiURL.MYSQL_GET_USER_INFO.getUrl()+config.Parme+"sid="+sid;
			System.out.println(url);
			Document doc1= null;
			try {
				 doc1 = Jsoup.connect(url).get();
			} catch (Exception e) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("NetWork Wrong");
						alert.setContentText("网络不稳定  我也没办法 重试下 ");
						alert.showAndWait();
					}
				});
				// TODO: handle exception
			}
			System.out.println("Doc:->" + doc1.body().text());
			
			JSONObject json = new JSONObject(doc1.body().text());
			if(json != null && json.getString("code").equals("0")){
				if(json.toString().substring(36, 37).equals("[")){
					System.out.println(json.toString().substring(36, 37));
					return ;
				}
				JSONObject data = new JSONObject(json.getString("data"));
//				System.out.println(data);
				this.userId = data.getString("id");
				this.account = data.getString("account");
				this.major = data.getString("major");
				this.name = data.getString("name");
				this._class = data.getString("class");
				this.college = data.getString("college");
				this.date_of_birth = data.getString("date_of_birth");
//				this.telephone = data.getString("telephone");
				this.email = data.getString("email");
				this.img_src = data.getString("img_src");
				this.signature = data.getString("signature");
				this.introduction = data.getString("introduction"); 
				this.login_status = data.getString("login_status"); //登陆状态
				this.login_ip = data.getString("login_ip");
				this.qq = data.getString("qq");
				this.sign_status = data.getString("sign_status");  //签名更新状态
				this.home_status = data.getString("home_status");  //空间更新状态
				
				Element Person = doc.getRootElement();
				Element ele = Person.addElement("sid");
				ele.addAttribute("id", this.sid); 
				ele.addElement("name").addText(this.name);
				ele.addElement("sid").addText(this.sid);
				ele.addElement("login_ip").addText(this.login_ip);
				ele.addElement("home_status").addText(this.home_status);
				ele.addElement("sign_status").addText(this.sign_status);
				ele.addElement("qq").addText(this.qq);
				ele.addElement("date_of_birth").addText(this.date_of_birth);
				ele.addElement("college").addText(this.college);
				ele.addElement("_class").addText(this._class);
				ele.addElement("major").addText(this.major);
				ele.addElement("account").addText(this.account);
				ele.addElement("login_status").addText(this.login_status);
				ele.addElement("introduction").addText(this.introduction);
				ele.addElement("img_src").addText(this.img_src);
				ele.addElement("email").addText(this.email);
//				ele.addElement("telephone").addText(this.telephone);
				ele.addElement("signature").addText(this.signature);
				ele.addElement("userId").addText(this.userId);
				XMLWriter writer = new XMLWriter();           
				FileOutputStream fos = new FileOutputStream("./cache/xml/Person.xml");    
				writer.setOutputStream(fos);
				writer.write(doc);           
				System.out.println("生成文档完毕！"); 
				
			}else{
				return ;
			}
		}else{
			
		}

		
	}
	
	
	
	public Person(JSONObject data) throws JSONException, DocumentException, IOException{
		
		
		this.userId = data.getString("id");
		this.sid = data.getString("sid");
		this.account = data.getString("account");
		this.major = data.getString("major");
		this._class = data.getString("class");
		this.college = data.getString("college");
		this.date_of_birth = data.getString("date_of_birth");
		this.telephone = data.getString("telephone");
		this.email = data.getString("email");
		this.img_src = data.getString("img_src");
		this.signature = data.getString("signature");
		this.introduction = data.getString("introduction"); 
		this.login_status = data.getString("login_status"); //登陆状态
		this.login_ip = data.getString("login_ip");
		this.qq = data.getString("qq");
		this.name = data.getString("name");
		this.sign_status = data.getString("sign_status");  //签名更新状态
		this.home_status = data.getString("home_status");  //空间更新状态
		
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/Person.xml"));     
		// 获取根节点list      
		List rs = doc.selectNodes("/Person/sid[@id='"+this.sid+"']");
	
		if(rs.size() == 1){
			     
			Element Person = doc.getRootElement();
			Element ele = Person.addElement("sid");
			ele.addAttribute("id", this.sid); 
			ele.addElement("name").addText(this.name);
			ele.addElement("sid").addText(this.sid);
			ele.addElement("login_ip").addText(this.login_ip);
			ele.addElement("home_status").addText(this.home_status);
			ele.addElement("sign_status").addText(this.sign_status);
			ele.addElement("qq").addText(this.qq);
			ele.addElement("date_of_birth").addText(this.date_of_birth);
			ele.addElement("college").addText(this.college);
			ele.addElement("_class").addText(this._class);
			ele.addElement("major").addText(this.major);
			ele.addElement("account").addText(this.account);
			ele.addElement("login_status").addText(this.login_status);
			ele.addElement("introduction").addText(this.introduction);
			ele.addElement("img_src").addText(this.img_src);
			ele.addElement("email").addText(this.email);
			ele.addElement("telephone").addText(this.telephone);
			ele.addElement("signature").addText(this.signature);
			ele.addElement("userId").addText(this.userId);
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/Person.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("生成文档完毕！"); 
			
		}else if(rs.size() <= 0){
			
			Element Person = doc.getRootElement();
			Element ele = Person.addElement("sid");
			ele.addAttribute("id", this.sid); 
			ele.addElement("name").addText(this.name);
			ele.addElement("sid").addText(this.sid);
			ele.addElement("login_ip").addText(this.login_ip);
			ele.addElement("home_status").addText(this.home_status);
			ele.addElement("sign_status").addText(this.sign_status);
			ele.addElement("qq").addText(this.qq);
			ele.addElement("date_of_birth").addText(this.date_of_birth);
			ele.addElement("college").addText(this.college);
			ele.addElement("_class").addText(this._class);
			ele.addElement("major").addText(this.major);
			ele.addElement("account").addText(this.account);
			ele.addElement("login_status").addText(this.login_status);
			ele.addElement("introduction").addText(this.introduction);
			ele.addElement("img_src").addText(this.img_src);
			ele.addElement("email").addText(this.email);
			ele.addElement("telephone").addText(this.telephone);
			ele.addElement("signature").addText(this.signature);
			
			XMLWriter writer = new XMLWriter();           
			FileOutputStream fos = new FileOutputStream("./cache/xml/Person.xml");    
			writer.setOutputStream(fos);
			writer.write(doc);           
			System.out.println("生成文档完毕！"); 

		}else{
			return ;
		}
		
	}
	
	public static Map getPerson(String sid) throws DocumentException, IOException, JSONException{
		
		SAXReader reader = new SAXReader();        
		// 读取指定文件       
		org.dom4j.Document doc = reader.read(new File("./cache/xml/Person.xml"));     
		// 获取根节点list      
		List projects = doc.selectNodes("/Person/sid[@id='"+ sid +"']");
		Map<String, String> Person = new HashMap();
		if(projects.size() <= 0){
			if(sid == "" || sid == null){
				return null ;
			}
			new Person(sid);
			doc = reader.read(new File("./cache/xml/Person.xml"));     
			// 获取根节点list      
			projects = doc.selectNodes("/Person/sid[@id='"+ sid +"']");
		}
		for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
			Element object = (Element) iterator.next();
			
			Person.put("name", object.elementText("name"));
			Person.put("sid", object.elementText("sid"));
			Person.put("login_ip", object.elementText("login_ip"));
			Person.put("home_status", object.elementText("home_status"));
			Person.put("sign_status", object.elementText("sign_status"));
			Person.put("qq", object.elementText("qq"));
			Person.put("date_of_birth", object.elementText("date_of_birth"));
			Person.put("college", object.elementText("college"));
			Person.put("_class", object.elementText("_class"));
			Person.put("major", object.elementText("major"));
			Person.put("account", object.elementText("account"));
			Person.put("login_status", object.elementText("login_status"));
			Person.put("introduction", object.elementText("introduction"));
			Person.put("img_src", object.elementText("img_src"));
			Person.put("email", object.elementText("email"));
			Person.put("telephone", object.elementText("telephone"));
			Person.put("signature", object.elementText("signature"));
//			
		}

		return Person;
	}
	
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=account&values"+account;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.account = account;
	}

	/**
	 * @return the major
	 */
	public String getMajor() {
		return major;
	}


	/**
	 * @return the _class
	 */
	public String get_class() {
		return _class;
	}



	/**
	 * @return the college
	 */
	public String getCollege() {
		return college;
	}

	
	/**
	 * @return the date_of_birth
	 */
	public String getDate_of_birth() {
		return date_of_birth;
	}


	/**
	 * @return the sid_password
	 */
	public String getSid_password() {
		return sid_password;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=password&values"+password;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.password = password;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=telephone&values"+telephone;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.telephone = telephone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=email&values"+email;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.email = email;
	}

	/**
	 * @return the img_src
	 */
	public String getImg_src() {
		return img_src;
	}

	/**
	 * @param img_src the img_src to set
	 */
	public void setImg_src(String img_src) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=img_src&values"+img_src;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.img_src = img_src;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=signature&values"+signature;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.signature = signature;
	}

	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=introduction&values"+introduction;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.introduction = introduction;
	}

	/**
	 * @return the login_status
	 */
	public String getLogin_status() {
		return login_status;
	}


	/**
	 * @return the login_ip
	 */
	public String getLogin_ip() {
		return login_ip;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=qq&values"+qq;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.qq = qq;
	}

	/**
	 * @return the sign_status
	 */
	public String getSign_status() {
		return sign_status;
	}

	/**
	 * @param sign_status the sign_status to set
	 */
	public void setSign_status(String sign_status) {
		
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=sign_status&valuse"+sign_status;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.sign_status = sign_status;
	}

	/**
	 * @return the home_status
	 */
	public String getHome_status() {
		return home_status;
	}

	/**
	 * @param home_status the home_status to set
	 */
	public void setHome_status(String home_status) {
		String url = ApiURL.MYSQL_SET_USER_INFO.getUrl()+config.Parme+"type=home_status&values"+home_status;
		System.out.println(url);
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.home_status = home_status;
	}
	
}
