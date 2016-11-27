
package play.ground.functionalinterface;

@FunctionalInterface
public interface FuncInter {

    void doSomething();

    default int methodWithDefaultImpl() {
        return 0;
    }

    default int method2WithDefaultImpl() {
        return 0;
    }
}