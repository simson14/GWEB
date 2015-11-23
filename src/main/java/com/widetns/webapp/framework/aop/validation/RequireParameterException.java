package com.widetns.webapp.framework.aop.validation;

class RequireParameterException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * �ʼ� �Է°��� �ʿ���ϴ� �ʵ� �̸�
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
