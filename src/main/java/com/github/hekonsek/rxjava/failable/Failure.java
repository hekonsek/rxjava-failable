package com.github.hekonsek.rxjava.failable;

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

}