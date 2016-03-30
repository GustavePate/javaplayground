package play.ground.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

public abstract class GenericDAO {

	@Inject
	@Named("conf")
	protected Config conf;

	static final Logger log = LoggerFactory.getLogger(GenericDAO.class);
	
	protected String getDAOInterfaceName(){
		
		// get DAO Interface name
		String realDaoInterfaceSimpleName = "randomDao";
		Class<?>[] interfaceList = this.getClass().getInterfaces();
		if (interfaceList.length > 1){
			String candidate = "";
			for (int i=0;i<interfaceList.length;i++){
				candidate = interfaceList[i].getSimpleName();
				if (candidate.endsWith("DAO") && (candidate!="GenericDAO")){
					realDaoInterfaceSimpleName = candidate;
					break;
				}
			}
		}else if (interfaceList.length == 1) {
			realDaoInterfaceSimpleName = this.getClass().getInterfaces()[0].getSimpleName();
		}else{
			log.error("Your DAO should implement an interface which ends with DAO, like this 'MyNameForThisDAO'");
		}
		return realDaoInterfaceSimpleName;
	}

	protected void dump2json(String key, GenericDTO dto) {
		
		if (conf.hasPath("dao.record.mode") && conf.getBoolean("dao.record.mode")){
			ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, GenericDTO.class);
			
			String mockDir = "";
			if (conf.hasPath("mock.json.write.path")){
				mockDir = conf.getString("mock.json.write.path");
			}else{
				mockDir = System.getProperty("java.io.tmpdir");
			}
			
			String daoName = getDAOInterfaceName();
			File f = new File(mockDir, daoName + ".json");
			try {
				//First read file content
				
				HashMap<String, GenericDTO>deser;
				if (f.exists()){
					deser = mapper.readValue(f, mapType);
					// replace eventual existing key
					if (deser.containsKey(key)){
						deser.remove(key);
					}
				}else{
					// First entry is default one
					deser = new HashMap<String, GenericDTO>();
					deser.put(GenericMock.STUB_KEY, dto);
				}
				deser.put(key, dto);
				
				//Then Serialize it
				mapper.writeValue(f, deser);
			} catch (Exception e) {
				log.error("Erreur de serialisation de {}", dto.getClass().getSimpleName(), e);
			}
			log.info("Serialisation de {} dans {}: ok", dto.getClass().getSimpleName(), f.getAbsolutePath());
		}
		

	}
	
}
