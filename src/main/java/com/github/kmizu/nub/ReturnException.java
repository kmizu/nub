package com.github.kmizu.nub;

public class ReturnException extends RuntimeException {
    private final Integer value;
    public ReturnException(Integer value) {
        super("return");
        this.value = value;
    }
    public Integer value() {
        return value;
    }
}
