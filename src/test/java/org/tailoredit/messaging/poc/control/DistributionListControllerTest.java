package org.tailoredit.messaging.poc.control;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tailoredit.messaging.poc.entity.DistributionListEntry;

@QuarkusTest
public class DistributionListControllerTest {

    @Inject
    DistributionListController distributionListController;

    @BeforeEach
    public void setup() {
        // Ensure that the distribution list is empty before each test
        distributionListController.deleteAll();
    }

    @Test
    void testAddDistributionListEntryShouldAddValidEntry() {
        final DistributionListEntry listEntry = new DistributionListEntry("Bob Jones", "0123456789");

        distributionListController.addDistributionListEntry(listEntry);

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 1);
        Assertions.assertEquals(distributionListController.getAllEntries().get(0), listEntry);
    }

    @Test
    void testAddDistributionListEntryShouldNotAcceptDuplicateEntryNames() {
        final DistributionListEntry listEntryOne = new DistributionListEntry("Bob Jones", "0123456789");
        final DistributionListEntry listEntryTwo = new DistributionListEntry("Bob Jones", "9876543210");

        distributionListController.addDistributionListEntry(listEntryOne);

        Assertions.assertThrows(DuplicateEntryException.class, () -> distributionListController.addDistributionListEntry(listEntryTwo));
    }

    @Test
    void testAddDistributionListEntryShouldNotAcceptDuplicateEntryNumber() {
        final DistributionListEntry listEntryOne = new DistributionListEntry("Bob Jones", "0123456789");
        final DistributionListEntry listEntryTwo = new DistributionListEntry("Jim Smith", "0123456789");

        distributionListController.addDistributionListEntry(listEntryOne);

        Assertions.assertThrows(DuplicateEntryException.class, () -> distributionListController.addDistributionListEntry(listEntryTwo));
    }

    @Test
    void testDeleteEntryShouldThrowExceptionWhenTheEntryIsUnknown() {
        final DistributionListEntry firstEntry = new DistributionListEntry("Bob Jones", "0123456789");
        final DistributionListEntry secondEntry = new DistributionListEntry("Jim Smith", "9876543210");
        final DistributionListEntry thirdEntry = new DistributionListEntry("Jane Doe", "9632587410");

        distributionListController.addDistributionListEntry(firstEntry);
        distributionListController.addDistributionListEntry(secondEntry);

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 2);

        Assertions.assertThrows(DistributionListEntryNotFoundException.class, () -> distributionListController.deleteEntry(thirdEntry));
        Assertions.assertEquals(distributionListController.getAllEntries().size(), 2);
    }

    @Test
    void testDeleteEntryShouldDeleteEntryWhenTheEntryIsKnown() {
        final DistributionListEntry firstEntry = new DistributionListEntry("Bob Jones", "0123456789");
        final DistributionListEntry secondEntry = new DistributionListEntry("Jim Smith", "9876543210");

        distributionListController.addDistributionListEntry(firstEntry);
        distributionListController.addDistributionListEntry(secondEntry);

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 2);

        distributionListController.deleteEntry(firstEntry);

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 1);
        Assertions.assertFalse(distributionListController.getAllEntries().contains(firstEntry));
        Assertions.assertTrue(distributionListController.getAllEntries().contains(secondEntry));
    }

    @Test
    void testDeleteAllShouldDeleteAllEntries() {
        distributionListController.addDistributionListEntry(new DistributionListEntry("Bob Jones", "0123456789"));
        distributionListController.addDistributionListEntry(new DistributionListEntry("Jim Smith", "9876543210"));

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 2);

        distributionListController.deleteAll();

        Assertions.assertEquals(distributionListController.getAllEntries().size(), 0);
    }

}
