package playground.yo.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import playground.yo.service.YoService;


public class ServiceModule extends AbstractModule {

    protected void configure() {
    }
    
    @Provides
	@Named("YoService")
    YoService provideYoService() {
    	YoService service = new YoService();
    	return service;
    }
}