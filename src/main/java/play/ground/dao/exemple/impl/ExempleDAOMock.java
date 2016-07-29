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

	public ExempleDTO doit(String data) throws Exception {

		ExempleDTO dto = new ExempleDTO();

		dto = (ExempleDTO) getFromJson(data);

		if (conf.hasPath("mock.exempledao.sleep")) {
			Thread.sleep(conf.getInt("mock.exempledao.sleep"));
		}

		return dto;

	}

}
