package playground.yo.dao.exemple.impl;

import java.util.Date;

import playground.yo.dao.exemple.ExempleDAO;
import playground.yo.dao.exemple.dto.ExempleDTO;
import playground.yo.dao.exemple.dto.ExempleDTO.Message;

public class ExempleMock implements ExempleDAO {

	public ExempleDTO getListMessages() {
		
		ExempleDTO res = new ExempleDTO();
		Message msg1 = res.new Message();
	
		msg1.author = "mock";
		msg1.date = new Date();
		msg1.message = "super message mocké";
		res.listMessage.add(msg1);
		
		return res;
	}
}
