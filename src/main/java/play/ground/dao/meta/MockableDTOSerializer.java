package play.ground.dao.meta;

import play.ground.dao.MockableDTO;

public class MockableDTOSerializer {
	
	public Object req;
	public Object resp;
	
	public MockableDTOSerializer(MockableDTO zereq, MockableDTO zeresp) {
		// TODO Auto-generated constructor stub
		req = zereq;
		resp = zeresp;
	}
	
	public Object getResp(){
		return resp;
	}

	
}
