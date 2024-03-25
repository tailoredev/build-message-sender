package org.tailoredit.control;

public class DistributionListEntryNotFoundException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "The distribution list entry could not be found";

    public DistributionListEntryNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }

}
