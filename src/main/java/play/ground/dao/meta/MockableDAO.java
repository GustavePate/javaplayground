package play.ground.dao.meta;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import play.ground.annotation.mockable.MockableTest;

public class MockableDAO implements MethodInterceptor {

	static final Logger LOG = LoggerFactory.getLogger(MockableTest.class);

	@Inject
	@Named("conf")
	private Config conf;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object res = null;
		String classname = this.getClass().getSimpleName();
		String methodname = invocation.getMethod().getName();
		LOG.info("before class: {} method: {}",classname, methodname);
		
		if (JSONMockManager.shouldIMock(classname, methodname)){	
			res = (Object) JSONMockManager.getMockData(classname, methodname, invocation.getArguments());
		}else{
			res = invocation.proceed();
		}
		
		if (JSONMockManager.shouldIRecord(classname, methodname)){
			JSONMockManager.record(classname, methodname, invocation.getArguments(), res);
		}

		return res;
	}
}