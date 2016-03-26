package play.ground.dao.simple.dto;


import play.ground.dao.GenericDTO;

public class SimpleDTOIn extends GenericDTO {

	public String id = "123456";

	public String functionnalPK() {
		return id;
	}

}
