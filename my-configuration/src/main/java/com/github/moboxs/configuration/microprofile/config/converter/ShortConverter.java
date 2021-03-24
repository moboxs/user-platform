package com.github.moboxs.configuration.microprofile.config.converter;

public class ShortConverter extends AbstractConverter<Short>{
    @Override
    protected Short doConvert(String value) {
        return Short.valueOf(value);
    }
}
