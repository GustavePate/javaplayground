package play.ground.dao.fake.impl;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.ground.dao.GenericDAO;
import play.ground.dao.fake.FakeDAO;
import play.ground.dao.fake.dto.FakeDTO;

import org.slf4j.Logger;

public class FakeDAODefault extends GenericDAO implements FakeDAO {

	static final Logger log = LoggerFactory.getLogger(FakeDAODefault.class);

	@Inject
	public FakeDAODefault(){
	}
	
	public FakeDTO doit(String data){
	
		FakeDTO dto = new FakeDTO();
		dto.src = data;
		dto.compute = data.length();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			log.error("interrupted", e);
		}
		return dto;
		
	}
	
}
