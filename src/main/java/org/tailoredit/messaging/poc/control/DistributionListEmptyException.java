package org.tailoredit.messaging.poc.control;

public class DistributionListEmptyException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "The distribution list is empty";

    public DistributionListEmptyException() {
        super(EXCEPTION_MESSAGE);
    }

}
