package play.ground.dao.simple.dto;


import play.ground.dao.MockableDTO;

public class SimpleDTOIn extends MockableDTO {

	public String id = "123456";

	public String functionnalPK() {
		return id;
	}

}
