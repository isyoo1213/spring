package user.userservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
//@Component // Bean 등록 방법 중 하나. but SpringConfig에 Bean 직접 등록해서 사용하는 편.
public class TimeTraceAop {

    @Around("execution(* user.userservice..*(..)) && !target(user.userservice.SpringConfig) && !target(user.userservice.JasyptConfig)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        System.out.println("START : " + joinPoint.toString());

        try {
             return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            System.out.println("END : " + joinPoint.toString());
            System.out.println(timeMs);
        }
        
    }

}
