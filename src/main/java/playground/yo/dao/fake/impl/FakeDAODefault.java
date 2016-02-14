package playground.yo.dao.fake.impl;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.dto.FakeDTO;

public class FakeDAODefault implements FakeDAO {

	
	static final Logger log = LoggerFactory.getLogger(FakeDAODefault.class);
	
	public FakeDTO doit(String data){
	
		
		FakeDTO dto = new FakeDTO();
		dto.src = data;
		dto.compute = data.length();
		try {
			log.info("I sleep...");
			Thread.sleep(2000);
			log.info("I wake !");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("interrupted", e);
		}
		return dto;
		
	}
	
}
