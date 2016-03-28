package play.ground.injection;

import com.google.inject.Binder;
import com.google.inject.Module;

import play.ground.service.rest.RestEasyService;


public class ServiceModule implements Module {

    public void configure(final Binder binder) {
    	 binder.bind(RestEasyService.class);
    }
    
}