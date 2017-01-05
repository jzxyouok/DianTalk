package __class;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import application.MainApplication;
import javafx.application.Platform;

public class Message {
	private Person persion;
	private String message;
	private int    msgStatus;
	private Group group;

	
	
	public Message(Person persion,String message,int msgStatus) {
		// TODO Auto-generated constructor stub
		
		// TODO Auto-generated method stub
		List<Message> allmsg =  MainApplication.nearlyMessageList;
		for (Iterator iterator = allmsg.iterator(); iterator.hasNext();) {
			Message message2 = (Message) iterator.next();
			if(persion.getSid().equals(message2.persion.getSid())){
				message2.message = message;
				message2.msgStatus = 1;
				MainApplication.nearlyMessageList = allmsg;
				return ;
			}
			
		}
			
		this.persion = persion;
		this.message = message;
		this.msgStatus = msgStatus;
		
		
		
	}
	
	public Message(Person persion,String message) {
		// TODO Auto-generated constructor stub
		
		
		// TODO Auto-generated method stub
		this.persion = persion;
		this.message = message;
		this.msgStatus = 1;
		
		
	}
	
	public Message(Group group,String message) {
		// TODO Auto-generated constructor stub
		
		
		// TODO Auto-generated method stub
		this.group = group;
		this.message = message;
		this.msgStatus = 1;
		
		
	}
	
	
	 
	/**
	 * @return 
	 * @return the group
	 */
	public  Object getGroup() {
		return group ;
	}
	
	public String getId(){
	   String id;
	if(group != null){
		   id = group.getGroupId();
	   }else{
		   id = persion.getSid();		
	   };
	   
	   return id;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the persion
	 */
	public Person getPersion() {
		return persion;
	}

	/**
	 * @param persion the persion to set
	 */
	public void setPersion(Person persion) {
		this.persion = persion;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the msgStatus
	 */
	public int getMsgStatus() {
		return msgStatus;
	}

	/**
	 * @param msgStatus the msgStatus to set
	 */
	public void setMsgStatus(int msgStatus) {
		this.msgStatus = msgStatus;
	}
	
	
}
