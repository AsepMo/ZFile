package com.store.data.root.command.util;

public class UnsupportedArchitectureException extends Exception {
    private static final long serialVersionUID = 7826528799780001655L;

    public UnsupportedArchitectureException() {
        super();
    }

    public UnsupportedArchitectureException(String detailMessage) {
        super(detailMessage);
    }

}
