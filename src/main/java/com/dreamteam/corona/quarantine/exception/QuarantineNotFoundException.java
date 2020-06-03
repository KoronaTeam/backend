package com.dreamteam.corona.quarantine.exception;

public class QuarantineNotFoundException extends RuntimeException {
    public QuarantineNotFoundException(Long id) {
        super("Could not find Quarantine: " + id);
    }
    public QuarantineNotFoundException(String message) { super(message); }
}
