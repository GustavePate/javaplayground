package play.ground.dao;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class GenericMock extends GenericDAO {

	protected ObjectMapper mapper = new ObjectMapper();
	public final static String STUB_KEY = "default";
	
	protected GenericDTO getFromJson(String functionnalPK, String dtoClassSimpleName) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		GenericDTO res;
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		try {
			
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, GenericDTO.class);
			HashMap<String, GenericDTO>deser = mapper.readValue(new File("/tmp/"+dtoClassSimpleName+".json"), mapType);
			if (deser.containsKey(functionnalPK)){
				res = deser.get(functionnalPK);
			}else{
				res = deser.get("default");
			}
			
		} catch (Exception e) {
			log.error("deserialize {}: ",dtoClassSimpleName, e);
			throw e;
		}
		log.info("deserialize {}: ok",dtoClassSimpleName);
		return res;
	}
	
	
}
