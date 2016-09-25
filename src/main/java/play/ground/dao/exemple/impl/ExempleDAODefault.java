package play.ground.dao.exemple.impl;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.ground.dao.AbstractDAO;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;

import org.slf4j.Logger;

public class ExempleDAODefault extends AbstractDAO implements ExempleDAO {

	static final Logger log = LoggerFactory.getLogger(ExempleDAODefault.class);

	@Inject
	public ExempleDAODefault(){
	}
	
	public ExempleDTO doit(String data){
	
		ExempleDTO dto = new ExempleDTO();
		dto.src = data;
		
		// imagine un super appel externe
		// qui renvoi len
		int len = data.length();
				
		dto.compute = len;
		
		dump2json(data, dto);
		
		return dto;
		
	}
	
}
