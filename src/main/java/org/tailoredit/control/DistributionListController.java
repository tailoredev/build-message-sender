package org.tailoredit.control;

import jakarta.enterprise.context.ApplicationScoped;
import org.tailoredit.entity.DistributionListEntry;
import org.tailoredit.entity.DistributionList;

import java.util.ArrayList;

@ApplicationScoped
public class DistributionListController {

    private final DistributionList distributionList;

    public DistributionListController() {
        this.distributionList = new DistributionList();
    }

    public void addDistributionListEntry(final DistributionListEntry distributionListEntry) {
        this.distributionList.getDistributionEntries().add(distributionListEntry);
    }

    public DistributionList getAllEntries() {
        return this.distributionList;
    }

    public void deleteEntry(final DistributionListEntry distributionListEntry) {
        this.distributionList.getDistributionEntries().remove(distributionListEntry);
    }

    public void deleteAll() {
        this.distributionList.setDistributionEntries(new ArrayList<>());
    }

}
