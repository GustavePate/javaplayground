package playground.yo.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class GenericMock extends GenericDAO {

	protected ObjectMapper mapper = new ObjectMapper();
	protected final String STUB_KEY = "default";
}
