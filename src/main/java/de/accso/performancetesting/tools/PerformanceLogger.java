/*
 * Copyright 2016 Accso - Accelerated Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.accso.performancetesting.tools;

import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;
import static net.logstash.logback.marker.Markers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public class PerformanceLogger {

    private final Logger logger = LoggerFactory.getLogger("Performance");

    private class Ctx {
        public long reqId;
    }
    
    private static final ThreadLocal<Ctx> CTX_HOLDER = new ThreadLocal<>();
    
    private Ctx createCtx() {
        Ctx ctx = new Ctx();
        ctx.reqId = new Random().nextLong();
        return ctx;
    }
    
    @Around("execution(public * de.accso.performancetesting.service.*Service.*(..))")
    public Object measureServiceCall(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return measureTime(thisJoinPoint, "service");
    }

    @Around("execution(public * de.accso.performancetesting.web.*Controller.*(..))")
    public Object measureControllerCall(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object result = null;
        CTX_HOLDER.set(createCtx());
        try {
            result = measureTime(thisJoinPoint, "controller");
        } finally {
            CTX_HOLDER.remove();
        }
        return result;
    }

    private String getCtxUrl() {
        String url = "";
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra != null && ra instanceof ServletRequestAttributes) {
            HttpServletRequest r = ((ServletRequestAttributes) ra).getRequest();
            url = r.getRequestURI();
        }
        return url;
    }
    
    private Object measureTime(ProceedingJoinPoint thisJoinPoint, String tag) throws Throwable {
        StopWatch sp = new StopWatch();
        sp.start();
        Object result = thisJoinPoint.proceed();
        sp.stop();
        logger.info(
                append("durationinmillis", sp.getTotalTimeMillis())
                .and(append("tag", tag))
                .and(append("req_id", CTX_HOLDER.get().reqId))
                .and(append("url", getCtxUrl()))
                .and(append("servicename", thisJoinPoint.getTarget().getClass().getCanonicalName()
                        + "." + thisJoinPoint.getSignature().getName())),
                "Performance");

        return result;
    }
}
