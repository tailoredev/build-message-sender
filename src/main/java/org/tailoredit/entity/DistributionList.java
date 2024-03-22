package org.tailoredit.entity;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DistributionList extends ArrayList<DistributionListEntry> {

    public DistributionList(final List<DistributionListEntry> list) {
        super(list);
    }

}
