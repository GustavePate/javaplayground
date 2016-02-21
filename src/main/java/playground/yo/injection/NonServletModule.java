package playground.yo.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.impl.FakeDAODefault;
import playground.yo.service.YoService;

public class NonServletModule extends AbstractModule {

    protected void configure() {
        //bind(YoService.class);
    }
    
    @Provides
	@Named("YoService")
    YoService provideYoService() {
    	YoService service = new YoService();
    	return service;
    }

    @Provides
	@Named("FakeDAO")
    FakeDAO provideFakeDAO() {
    	FakeDAO dao = new FakeDAODefault();
    	return dao;
    }
    
    
    
}
