package playground.yo.dao.fake.impl;

import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.dto.FakeDTO;

public class FakeDAOMock implements FakeDAO {

	
	public FakeDTO doit(String data){
	
		FakeDTO dto = new FakeDTO();
		dto.src = "mock";
		dto.compute = -1;
		return dto;
		
	}
	
}
