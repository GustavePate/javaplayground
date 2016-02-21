package playground.yo.dao.fake.impl;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import playground.yo.dao.GenericMock;
import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.dto.FakeDTO;

public class FakeDAOMock extends GenericMock implements FakeDAO {

	static final Logger log = LoggerFactory.getLogger(FakeDAOMock.class);
	
	public FakeDTO doit(String data){
	
		FakeDTO dto = null;
		
		try {
		
			// Creation du type necessaire à la deserialisation
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, FakeDTO.class);
			HashMap<String, FakeDTO>dtomap = mapper.readValue(new File("conf/mocks/FakeDAOMock.json"), mapType);

			// recuperation du stub si donnée non présente dans le mock
			if (dtomap.containsKey(data)){
				dto = dtomap.get(data);
			}else{
				dto = dtomap.get(STUB_KEY);
			}
		
		} catch (Exception e) {
			log.error("Mock error",e);
		}
		return dto;
		
	}
	
}
