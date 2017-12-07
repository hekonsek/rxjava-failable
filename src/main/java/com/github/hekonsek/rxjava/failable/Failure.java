package com.github.hekonsek.rxjava.failable;

import static java.lang.String.format;

public class Failure<Upstream> {

    private final Upstream value;

    private final Throwable cause;

    public Failure(Upstream value, Throwable cause) {
        this.value = value;
        this.cause = cause;
    }

    public Upstream value() {
        return value;
    }

    public Throwable cause() {
        return cause;
    }

    @Override public String toString() {
        String message = cause.getMessage() != null ? cause.getMessage() : "";
        return format("Upstream value: %s. Cause: %s: %s", value, cause.getClass().getSimpleName(), message);
    }

}