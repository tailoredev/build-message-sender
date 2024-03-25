package org.tailoredit.messaging.poc.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@RegisterForReflection
public class DistributionList extends ArrayList<DistributionListEntry> {

    public DistributionList(final List<DistributionListEntry> list) {
        super(list);
    }

}
