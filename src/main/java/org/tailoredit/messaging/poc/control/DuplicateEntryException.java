package org.tailoredit.messaging.poc.control;

public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(final String message) {
        super(message);
    }

}
