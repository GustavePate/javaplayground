package play.ground.dao;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class AbstractMock extends AbstractDAO {

	protected ObjectMapper mapper = new ObjectMapper();
	public final static String STUB_KEY = "default";
	
	
	protected MockableDTO getFromJson(String functionnalPK) throws Exception{

		ObjectMapper mapper = new ObjectMapper();
		MockableDTO res;
		String daoName = getDAOInterfaceName();

		String mockDir = "";
		if (conf.hasPath("mock.json.read.path")) {
			mockDir = conf.getString("mock.json.write.path");
		} else {
			mockDir = System.getProperty("java.io.tmpdir");
		}

		File f = new File(mockDir, daoName + ".json");

		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		try {

			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);
			
			
			HashMap<String, MockableDTO>deser = mapper.readValue(f, mapType);

			if (deser.containsKey(functionnalPK)) {
				res = deser.get(functionnalPK);
			} else {
				res = deser.get("default");
			}

		} catch (Exception e) {
			log.error("deserialize {} from {}: ", daoName, f.getAbsolutePath());
			throw e;
		}
		log.info("deserialize {} from {}: ok", daoName, f.getAbsolutePath());
		return res;
	}

}
