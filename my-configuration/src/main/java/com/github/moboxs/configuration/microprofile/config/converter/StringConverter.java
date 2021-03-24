package com.github.moboxs.configuration.microprofile.config.converter;

public class StringConverter extends AbstractConverter<String>{
    @Override
    protected String doConvert(String value) throws IllegalArgumentException, NullPointerException{
        return value;
    }
}
