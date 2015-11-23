package com.widetns.webapp.framework.aop.validation;

class RequireParameterException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 필수 입력값을 필요로하는 필드 이름
     */
    private final String field;

    public RequireParameterException(String field) {
        super("parameter " + field + " is required.");
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
