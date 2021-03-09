package com.github.moboxs.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

public class HandlerMethodInfo {

    private final String requestPath;
    
    private  final Method handleMethod;
    
    private final Set<String>supportedHttpMethods;


    public HandlerMethodInfo(String requestPath, Method method, Set<String> supportedHttpMethods) {
        this.handleMethod = method;
        this.requestPath = requestPath;
        this.supportedHttpMethods = supportedHttpMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getHandleMethod() {
        return handleMethod;
    }

    public Set<String> getSupportedHttpMethods() {
        return supportedHttpMethods;
    }
}
