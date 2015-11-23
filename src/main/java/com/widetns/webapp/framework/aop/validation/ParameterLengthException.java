package com.widetns.webapp.framework.aop.validation;

public class ParameterLengthException extends Exception {

    private static final long serialVersionUID = -1;

    private String field = null;
    private int min = -1;
    private int max = -1;
    private int current = -1;

    public ParameterLengthException(String field, int current, int min, int max) {
        this.field = field;
        this.current = current;
        this.min = min;
        this.max = max;
    }

    public String toString() {
        return field + " must be " + min + " ~ " + max + ", current " + current;
    }

}
