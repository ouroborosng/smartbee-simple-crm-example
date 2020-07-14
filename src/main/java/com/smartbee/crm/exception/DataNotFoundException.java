package com.smartbee.crm.exception;

public class DataNotFoundException extends RuntimeException {

    private final Class type;

    public DataNotFoundException(final Class<?> type, final String msg){
        super(msg);
        this.type = type;
    }

    public Class getType() {
        return type;
    }
}
