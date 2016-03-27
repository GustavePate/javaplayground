package play.ground.dao.exemple.impl;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.ground.dao.GenericDAO;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;

import org.slf4j.Logger;

public class ExempleDAODefault extends GenericDAO implements ExempleDAO {

	static final Logger log = LoggerFactory.getLogger(ExempleDAODefault.class);

	@Inject
	public ExempleDAODefault(){
	}
	
	public ExempleDTO doit(String data){
	
		ExempleDTO dto = new ExempleDTO();
		dto.src = data;
		dto.compute = data.length();
		try {
			if (conf.hasPath("mock.fakedao.sleep")){ 
				Thread.sleep(conf.getInt("mock.fakedao.sleep"));
			}
			
		} catch (InterruptedException e) {
			log.error("interrupted", e);
		}
		dump2json(data, dto);
		
		return dto;
		
	}
	
}
