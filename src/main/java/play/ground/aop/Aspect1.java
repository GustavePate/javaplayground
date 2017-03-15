package play.ground.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
 
@Aspect
public class Aspect1 {
	
    static final Logger log = LoggerFactory.getLogger(Aspect1.class);
    
    //Defines a pointcut where the @YourAnnotation exists
    //And combines that with a catch all pointcut with the scope of execution
    @Around("@annotation(Test1) && execution(* *(..))")
    //ProceedingJointPoint = the reference of the call to the method.
    //Difference between ProceedingJointPoint and JointPoint is that a JointPoint can't be continued(proceeded)
    //A ProceedingJointPoint can be continued(proceeded) and is needed for a Around advice
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        //Default Object that we can use to return to the consumer
        Object returnObject = null;
        try {
            log.info("YourAspect's aroundAdvice's body is now executed Before yourMethodAround is called.");
            //We choose to continue the call to the method in question
            returnObject = joinPoint.proceed();
            //If no exception is thrown we should land here and we can modify the returnObject, if we want to.
        } catch (Throwable throwable) {
            //Here we can catch and modify any exceptions that are called
            //We could potentially not throw the exception to the caller and instead return "null" or a default object.
            throw throwable;
        }
        finally {
            //If we want to be sure that some of our code is executed even if we get an exception
           log.info("YourAspect's aroundAdvice's body is now executed After yourMethodAround is called.");
        }
        return returnObject;
    }
}
