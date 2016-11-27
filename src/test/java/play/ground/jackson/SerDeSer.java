package play.ground.jackson;

import java.io.File;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.assertj.core.api.Assertions.*;

import play.ground.core.dao.mock.MockableDTO;
import play.ground.dao.simple.dto.SimpleDTOOut;


public class SerDeSer {

	public static SimpleDTOOut dto;
	public static HashMap<String, SimpleDTOOut> dtomap;
	
	static final Logger log = LoggerFactory.getLogger(SerDeSer.class);
	
	@Before
	public void filldto(){

		dtomap = new HashMap<String, SimpleDTOOut>();
	
		
		dto = new SimpleDTOOut();
		dto.id="123457";
		dto.name="simpledto2";
		dtomap.put(dto.id, dto);
		
		dto = new SimpleDTOOut();
		dto.id="123458";
		dto.name="simpledto3";
		dtomap.put(dto.id, dto);
		
		dto = new SimpleDTOOut();
		dto.id="default";
		dto.name="simpledto3";
		dtomap.put(dto.id, dto);		
		
		dto = new SimpleDTOOut();
		dto.id="123456";
		dto.name="simpledto";
		dtomap.put(dto.id, dto);
		
		log.info("init: ok");
	}

	
	@Test
	public void deserialize_simple() throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("/tmp/SimpleObject.json"), dto);
		} catch (Exception e) {
			log.error("serialize simple: ", e);
			throw e;
		}
		log.info("serialize object: ok");
		
		
		
		mapper = new ObjectMapper();
		try {
			SimpleDTOOut deser = mapper.readValue(new File("/tmp/SimpleObject.json"), SimpleDTOOut.class);
			assertThat(deser.id).isEqualTo("123456");
		} catch (Exception e) {
			log.error("deserialize simple: ", e);
			throw e;
		}
		log.info("deserialize object: ok");
		
	}
	
	
	@Test
	public void deserialize_list() throws Exception{
		
		log.info("serialize list: {}", dtomap.size());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("/tmp/ListObject.json"), dtomap);
		} catch (Exception e) {
			log.error("serialize list: ", e);
			throw e;
		}
		log.info("serialize list: ok");
		
		
		
		String key = "default";
		
		mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, SimpleDTOOut.class);
		HashMap<String, SimpleDTOOut> deserdtomap;
		try {
			deserdtomap = mapper.readValue(new File("/tmp/ListObject.json"), mapType);
			// recuperation du stub si donnée non présente dans le mock
			if (deserdtomap.containsKey(key)){
				dto = deserdtomap.get(key);
			}else{
				dto = deserdtomap.get("default");
			}
			assertThat(deserdtomap.size()).isEqualTo(4);
		} catch (Exception e) {
			log.error("deserialize: ", e);
			throw e;
		}
		log.info("deserialize list: ok");
		
	}
	
	
	
	@Test
	public void deserialize_simple_abstract() throws Exception{
		
		MockableDTO gdto = dto;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		
		dto.name="simple_abstract";
		try {
			
			mapper.writeValue(new File("/tmp/SimpleAbstractObject.json"), gdto);
		} catch (Exception e) {
			log.error("serialize simple: ", e);
			throw e;
		}
		log.info("serialize abstract object: ok");
		
		
		
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		//mapper.enableDefaultTyping();
		try {
			MockableDTO deser = mapper.readValue(new File("/tmp/SimpleAbstractObject.json"), MockableDTO.class);
			
			SimpleDTOOut specificdto = (SimpleDTOOut) deser;
			assertThat(specificdto.name).isEqualTo("simple_abstract");
		} catch (Exception e) {
			log.error("deserialize abstract object: ", e);
			throw e;
		}
		log.info("deserialize abstract object: ok");
		
	}
	
	@Test
	public void deserialize_list_abstract() throws Exception{
	
		//TODO: serialize abstract / deserialisat abstract
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		dto.name="list_abstract";
		try {
			
			mapper.writeValue(new File("/tmp/ListAbstractObject.json"), dtomap);
		} catch (Exception e) {
			log.error("serialize simple: ", e);
			throw e;
		}
		log.info("serialize abstract object: ok");
		
		
		
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		//mapper.enableDefaultTyping();
		try {
			
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);
			HashMap<String, MockableDTO>deser = mapper.readValue(new File("/tmp/ListAbstractObject.json"), mapType);
			assertThat(deser.size()).isEqualTo(4);
			SimpleDTOOut specificdto = (SimpleDTOOut) deser.get("default");
			
			assertThat(specificdto.name).isEqualTo("simpledto3");
		} catch (Exception e) {
			log.error("deserialize abstract list: ", e);
			throw e;
		}
		log.info("deserialize abstract list: ok");
		
	}	
	
	@Test
	public void deserialize_list_full_abstract() throws Exception{
	
		//TODO: serialize abstract / deserialisat abstract
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		HashMap<String, MockableDTO> abs_dtomap = new HashMap<String, MockableDTO>();
		
		dtomap.forEach((k,v)-> {
			abs_dtomap.put(k,  v);
		});
		
		
		
		try {
			
			mapper.writeValue(new File("/tmp/ListFullAbstractObject.json"), dtomap);
		} catch (Exception e) {
			log.error("serialize full abstract: ", e);
			throw e;
		}
		log.info("serialize full abstract object: ok");
		
		
		
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		try {
			
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);
			HashMap<String, MockableDTO>deser = mapper.readValue(new File("/tmp/ListFullAbstractObject.json"), mapType);
			assertThat(deser.size()).isEqualTo(4);
			SimpleDTOOut specificdto = (SimpleDTOOut) deser.get("default");
			
			assertThat(specificdto.name).isEqualTo("simpledto3");
		} catch (Exception e) {
			log.error("deserialize full abstract list: ", e);
			throw e;
		}
		log.info("deserialize full abstract list: ok");
		
	}	
	
}
