package com.github.moboxs.configuration.microprofile.config.source;

import java.util.Map;

/**
 * 动态配置源
 */
public class DynamicConfigSource extends MapBasedConfigSource{

    private Map configData;

    public DynamicConfigSource() {
        super("dynamic config source", 500);
    }

    @Override
    public void prepareConfigData(Map configData) throws Throwable {
        this.configData = configData;
    }
}
