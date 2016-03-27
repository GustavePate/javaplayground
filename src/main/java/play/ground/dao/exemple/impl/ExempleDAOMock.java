package play.ground.dao.exemple.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import play.ground.dao.GenericMock;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;

public class ExempleDAOMock extends GenericMock implements ExempleDAO {

	static final Logger log = LoggerFactory.getLogger(ExempleDAOMock.class);

	public ExempleDTO getValue(String key) throws JsonParseException, JsonMappingException, IOException{
		
			ExempleDTO dto = null;
			// Creation du type necessaire à la deserialisation
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, ExempleDTO.class);
			HashMap<String, ExempleDTO>dtomap = mapper.readValue(new File("conf/mocks/FakeDAOMock.json"), mapType);

			// recuperation du stub si donnée non présente dans le mock
			if (dtomap.containsKey(key)){
				dto = dtomap.get(key);
			}else{
				dto = dtomap.get(STUB_KEY);
			}
			return dto;
	}
	
	
	
	public ExempleDTO doit(String data){
	
		ExempleDTO dto = new ExempleDTO();
		
		try {
	
			//dto = getValue(data);
			dto = (ExempleDTO) getFromJson(data, dto.getClass().getSimpleName());

		
		} catch (Exception e) {
			log.error("Mock error",e);
		}
		return dto;
		
	}
	
}
