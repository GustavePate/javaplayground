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
	
	protected void dump2json(String key, GenericDTO dto) {
		
		if (conf.hasPath("dao.record.mode") && conf.getBoolean("dao.record.mode")){
			ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, GenericDTO.class);
			try {
				//First read file content
				File f = new File("/tmp/" + dto.getClass().getSimpleName() + ".json");
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
			log.info("Serialisation de {}: ok", dto.getClass().getSimpleName());
		}
		

	}
	
}
