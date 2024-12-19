package dev.uliana.socks_accounting.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final String SERVICE_PACKAGE_PATH = "execution(* dev.uliana.socks_accounting.service.*.*(..))";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before(SERVICE_PACKAGE_PATH)
    public void logBefore(JoinPoint joinPoint) {
        log.info("Начал свое выполнение метод: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = SERVICE_PACKAGE_PATH, returning = "returnedValue")
    public void logAround(JoinPoint joinPoint, Object returnedValue) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.debug("Метод {} с параметрами {} выполняется...", methodName, Arrays.asList(args));
        log.debug("Метод выполнен успешно и вернул значение: {}", returnedValue);
    }

    @AfterThrowing(pointcut = SERVICE_PACKAGE_PATH, throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Исключение в {}.{}() с причиной = {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getCause() != null ? exception.getCause() : "NULL"
        );
    }
}