package org.tailoredit.control;

public class DistributionListEmptyException extends RuntimeException {

    public DistributionListEmptyException() {
        super("The distribution list is empty");
    }

}
