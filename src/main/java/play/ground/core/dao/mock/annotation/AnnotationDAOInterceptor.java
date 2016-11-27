
package play.ground.core.dao.mock.annotation;

import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.AbstractDAO;
import play.ground.metric.annotation.MetricsManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class AnnotationDAOInterceptor implements MethodInterceptor {

    @Named("props.application")
    @Inject
    protected Properties props;

    @Inject
    protected Provider<MetricsManager> metricmanagerprovider;

    private final static Logger LOG = LoggerFactory.getLogger(AnnotationDAOInterceptor.class);

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        // Context variables
        final String className = AbstractDAO.getDAOInterfaceName(invocation.getMethod().getDeclaringClass());
        final String methodName = invocation.getMethod().getName();
        boolean invokeSuccess = true;
        Object invokeRes = null;

        // Metrics management
        final MetricsManager mmanager = metricmanagerprovider.get();
        mmanager.start(className, methodName);

        // Call
        try {
            invokeRes = invocation.proceed();
        } catch (final Exception t) {
            invokeSuccess = false;
            throw t;
        } finally {

            // Metrics management
            mmanager.stop(invokeSuccess);
        }
        return invokeRes;
    }
}
