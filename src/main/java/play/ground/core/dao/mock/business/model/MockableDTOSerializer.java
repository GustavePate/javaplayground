package play.ground.core.dao.mock.business.model;

import play.ground.core.dao.mock.MockableDTO;

public class MockableDTOSerializer {
	
	public Object req;
	public Object resp;
	
	public MockableDTOSerializer(MockableDTO zereq, MockableDTO zeresp) {
		// TODO Auto-generated constructor stub
		req = zereq;
		resp = zeresp;
	}
	
	public MockableDTOSerializer() {
		// TODO Auto-generated constructor stub
	}

	public Object getResp(){
		return resp;
	}

	
}
