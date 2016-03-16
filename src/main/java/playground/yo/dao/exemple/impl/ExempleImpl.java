package playground.yo.dao.exemple.impl;

import java.util.Date;

import playground.yo.dao.exemple.ExempleDAO;
import playground.yo.dao.exemple.dto.ExempleDTO;
import playground.yo.dao.exemple.dto.ExempleDTO.Message;

public class ExempleImpl implements ExempleDAO {

	public ExempleDTO getListMessages() {
		
		ExempleDTO res = new ExempleDTO();
		Message msg1 = res.new Message();
		Message msg2 = res.new Message();
		Message msg3 = res.new Message();
	
		msg1.author = "toto";
		msg1.date = new Date();
		msg1.message = "super message de toto";
		res.listMessage.add(msg1);
		
		msg2.author = "titi";
		msg2.date = new Date();
		msg2.message = "super message de titi";
		res.listMessage.add(msg2);
		
		msg3.author = "tata";
		msg3.date = new Date();
		msg3.message = "super message de tata";
		res.listMessage.add(msg3);		
		
		return res;
	}

}
