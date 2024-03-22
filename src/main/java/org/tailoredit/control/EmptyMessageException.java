package org.tailoredit.control;

public class EmptyMessageException extends RuntimeException {

    public EmptyMessageException() {
        super("Message cannot be empty");
    }

}
