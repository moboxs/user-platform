package com.github.moboxs.web.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

public class HandlerMethodInfo {

    private final String requestPath;
    
    private  final Method handleMethod;
    
    private final Set<String>supportedHttpMethods;

    private final Parameter[] parameters;


    public HandlerMethodInfo(String requestPath, Method method, Set<String> supportedHttpMethods, Parameter[] parameters) {
        this.handleMethod = method;
        this.requestPath = requestPath;
        this.supportedHttpMethods = supportedHttpMethods;
        this.parameters = parameters;
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

    public Parameter[] getParameters() {
        return parameters;
    }
}
