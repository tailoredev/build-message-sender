package org.tailoredit.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DistributionList {

    private List<DistributionListEntry> distributionEntries;

    public DistributionList() {
        distributionEntries = new ArrayList<>();
    }

}
