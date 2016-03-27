package play.ground.dao.exemple.dto;

import play.ground.dao.GenericDTO;

public class ExempleDTO extends GenericDTO {

	public int compute = 0;
	public String src = "";
	
	public String functionnalPK() {
		return src;
	}
	
}
