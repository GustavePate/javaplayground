package playground.yo.dao.fake;

import playground.yo.dao.fake.impl.FakeDAODefault;

public class FakeDAOFactory {

	public static FakeDAO get(){
		
		return new FakeDAODefault();
		
	}
	
}
