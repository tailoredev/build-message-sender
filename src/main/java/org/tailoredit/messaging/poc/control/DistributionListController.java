package org.tailoredit.messaging.poc.control;

import jakarta.enterprise.context.ApplicationScoped;
import org.tailoredit.messaging.poc.entity.DistributionList;
import org.tailoredit.messaging.poc.entity.DistributionListEntry;

@ApplicationScoped
public class DistributionListController {

    private final DistributionList distributionList;

    public DistributionListController() {
        distributionList = new DistributionList();
    }

    public void addDistributionListEntry(final DistributionListEntry distributionListEntry) {
        distributionList.forEach(listEntry -> {
            if (listEntry.getName().equals(distributionListEntry.getName())) {
                throw new DuplicateEntryException("An entry already exists with the name: " + distributionListEntry.getName());
            } else if (listEntry.getNumber().equals(distributionListEntry.getNumber())) {
                throw new DuplicateEntryException("An entry already exists with the number: " + distributionListEntry.getNumber());
            }
        });

        distributionList.add(distributionListEntry);
    }

    public DistributionList getAllEntries() {
        return distributionList;
    }

    public void deleteEntry(final DistributionListEntry distributionListEntry) {
        if (!distributionList.contains(distributionListEntry)) {
            throw new DistributionListEntryNotFoundException();
        }

        distributionList.remove(distributionListEntry);
    }

    public void deleteAll() {
        distributionList.clear();
    }

}
