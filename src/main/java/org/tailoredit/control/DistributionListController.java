package org.tailoredit.control;

import jakarta.enterprise.context.ApplicationScoped;
import org.tailoredit.entity.DistributionList;
import org.tailoredit.entity.DistributionListEntry;

import java.util.ArrayList;

@ApplicationScoped
public class DistributionListController {

    private final DistributionList distributionList;

    public DistributionListController() {
        distributionList = new DistributionList();
    }

    public void addDistributionListEntry(final DistributionListEntry distributionListEntry) {
        distributionList.getDistributionEntries().forEach(listEntry -> {
            if (listEntry.getName().equals(distributionListEntry.getName())) {
                throw new DuplicateEntryException("An entry already exists with the name: " + distributionListEntry.getName());
            } else if (listEntry.getNumber().equals(distributionListEntry.getNumber())) {
                throw new DuplicateEntryException("An entry already exists with the number: " + distributionListEntry.getNumber());
            }
        });

        distributionList.getDistributionEntries().add(distributionListEntry);
    }

    public DistributionList getAllEntries() {
        return distributionList;
    }

    public void deleteEntry(final DistributionListEntry distributionListEntry) {
        distributionList.getDistributionEntries().remove(distributionListEntry);
    }

    public void deleteAll() {
        distributionList.setDistributionEntries(new ArrayList<>());
    }

}
