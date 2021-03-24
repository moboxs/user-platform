package com.github.moboxs.configuration.microprofile.config.source;

import java.util.Map;

public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource{

    public JavaSystemPropertiesConfigSource() {
        super("Java System Properties", 400);
    }

    /**
     * Java 系统属性最好通过本地保存，使用Map进可能运行期不去调整
     * -Dapplication.name=user-web
     * @param configData
     * @throws Throwable
     */
    @Override
    public void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(System.getProperties());
    }
}
