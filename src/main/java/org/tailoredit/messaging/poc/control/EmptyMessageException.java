package org.tailoredit.messaging.poc.control;

public class EmptyMessageException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "Message cannot be empty";

    public EmptyMessageException() {
        super(EXCEPTION_MESSAGE);
    }

}
