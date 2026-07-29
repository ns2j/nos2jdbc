package org.seasar.framework.beans;

public class NotRecordException extends RuntimeException {
    public NotRecordException(Class<?> clazz) {
        super(clazz.getName() + " is not record");
    }
}
