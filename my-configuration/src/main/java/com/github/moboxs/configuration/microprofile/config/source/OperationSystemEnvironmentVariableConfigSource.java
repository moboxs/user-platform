package com.github.moboxs.configuration.microprofile.config.source;

import java.util.Map;

/**
 * 操作系统环境变量 ConfigSource
 */
public class OperationSystemEnvironmentVariableConfigSource extends MapBasedConfigSource{

    public OperationSystemEnvironmentVariableConfigSource() {
        super("Operation System Environment Variables", 300);
    }

    @Override
    public void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(System.getenv());
    }
}
