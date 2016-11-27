
package play.ground.core.dao.mock.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.mock.business.JSONSupaMockManager;

public class MockableSupaInterceptor implements MethodInterceptor {

    static final Logger LOG = LoggerFactory.getLogger(MockableSupaInterceptor.class);

    private String getDAOInterfaceName(Class<?> klass) {

        // get DAO Interface name
        String realDaoInterfaceSimpleName = "randomDao";

        //LOG.debug("getDAOName for: " + klass.getCanonicalName());
        if (klass.getCanonicalName().contains("EnhancerByGuice")) {
            klass = klass.getSuperclass();
            LOG.debug("guiceenhancer found, now gettingDAOName for: " + klass.getCanonicalName());
        }

        final Class<?>[] interfaceList = klass.getInterfaces();
        if (interfaceList.length > 1) {
            String candidate = "";
            for (final Class<?> element : interfaceList) {
                candidate = element.getSimpleName();
                if (candidate.endsWith("DAO") && candidate != "AbstractDAO") {
                    realDaoInterfaceSimpleName = candidate;
                    break;
                }
            }
        } else if (interfaceList.length == 1) {
            realDaoInterfaceSimpleName = klass.getInterfaces()[0].getSimpleName();
        } else {
            LOG.warn("Your DAO should implement an interface which ends with DAO, like this 'MyNameForThisDAO' data stored in " + realDaoInterfaceSimpleName + ".json");
        }
        return realDaoInterfaceSimpleName;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Object res = null;
        final String methodname = invocation.getMethod().getName();
        final String interfacename = getDAOInterfaceName(invocation.getMethod().getDeclaringClass());
        //LOG.debug("before class: {} method: {} interface: {}", invocation.getMethod().getDeclaringClass().getSimpleName(), methodname, interfacename);

        if (JSONSupaMockManager.shouldIMock(interfacename, methodname)) {
            res = JSONSupaMockManager.getDTOFromJson(interfacename, methodname, invocation.getArguments());
        } else {
            res = invocation.proceed();
        }

        if (JSONSupaMockManager.shouldIRecord(interfacename, methodname)) {
            JSONSupaMockManager.record(interfacename, methodname, invocation.getArguments(), res);
        }

        return res;
    }
}