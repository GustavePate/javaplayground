package play.ground.dao.simple.dto;

import play.ground.dao.GenericDTO;

public class SimpleDTOOut extends GenericDTO {

	public String id;
	public String name;
	
	public String getName(){
		return name;
	}

	public String functionnalPK(){
		return id;
	}
	
	
}
