package org.example.clientprocessing.aop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {

    private final org.example.clientprocessing.cache.CacheStore cacheStore;

    @Value("${t1.cache.ttl-ms:60000}")
    private long ttlMs;

    @Around("@annotation(annotations.Cached)")
    public Object cacheLogic(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String key = generateKey(signature, joinPoint.getArgs());

        Object cached = cacheStore.get(key);
        if (cached != null) {
            return cached;
        }

        Object result = joinPoint.proceed();
        cacheStore.put(key, result, ttlMs);
        return result;
    }

    private String generateKey(MethodSignature signature, Object[] args) {
        String signaturePart = signature.getDeclaringTypeName() + "." + signature.getMethod().getName();
        String argumentsPart = Arrays.deepToString(args);
        return signaturePart + "|" + argumentsPart;
    }
}
