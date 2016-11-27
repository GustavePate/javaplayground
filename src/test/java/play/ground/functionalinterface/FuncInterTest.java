
package play.ground.functionalinterface;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuncInterTest {

    private final static Logger LOG = LoggerFactory.getLogger(FuncInterTest.class);

    public class FIClass implements FuncInter

    {

        public void doSomething() {
            LOG.info("something");

        }

        public void somethingElse() {

            LOG.info("something else");
        }

        public int method2WithDefaultImpl() {
            return 5;
        }

    }

    @Test
    public void testInterface() {

        FIClass fic = new FIClass();
        fic.doSomething();
        fic.doSomething();
        LOG.info("{}", fic.methodWithDefaultImpl());
        LOG.info("{}", fic.method2WithDefaultImpl());
    }

}
