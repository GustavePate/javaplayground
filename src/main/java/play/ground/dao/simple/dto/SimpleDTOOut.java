package play.ground.dao.simple.dto;

import play.ground.dao.MockableDTO;

public class SimpleDTOOut extends MockableDTO {

	public String id;
	public String name;
	
	public String getName(){
		return name;
	}

	public String functionnalPK(){
		return id;
	}
	
	
}
