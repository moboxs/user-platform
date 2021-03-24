package com.github.moboxs.configuration.microprofile.config;

import com.github.moboxs.configuration.microprofile.config.source.ConfigSources;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.Optional;

public class DefaultConfig implements Config {

    private final ConfigSources configSources;

    DefaultConfig(ConfigSources configSources) {
        this.configSources = configSources;
    }

    @Override
    public <T> T getValue(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public ConfigValue getConfigValue(String s) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String s, Class<T> aClass) {
        return Optional.empty();
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return null;
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> aClass) {
        return Optional.empty();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
