package playground.yo.dao.exemple.dto;

import java.util.ArrayList;
import java.util.Date;


public class ExempleDTO{

	public class Message {
		public String author;
		public Date date;
		public String message;
	}

	public ArrayList<Message> listMessage;
	
	public ExempleDTO(){
		listMessage = new ArrayList<Message>();
	}
	
}
