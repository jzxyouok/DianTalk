/**
 * 
 */
package __class;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.json.JSONException;

/**
 * @author Slight
 *
 */
public class NearlyChatItem {


	/**
	 * 
	 */
	
	public String sid;
	private String msg;
	public Map person ;
	public NearlyChatItem(String toSid ,String msg) throws DocumentException, IOException, JSONException {
	// TODO Auto-generated constructor stub
		this.sid 	= sid;
		this.msg 	= msg;
		this.person = Person.getPerson(this.sid);
	}
	
	
	/**
	 * @return the person
	 */
	public Map getPerson() {
		return person;
	}
	/**
	 * @param person the person to set
	 */
	public void setPerson(Map person) {
		this.person = person;
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
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
