package play.ground.dao.exemple.dto;

import play.ground.dao.MockableDTO;

public class ExempleDTO extends MockableDTO {

	public int compute = 0;
	public String src = "";
	
	public String functionnalPK() {
		return src;
	}
	
}
