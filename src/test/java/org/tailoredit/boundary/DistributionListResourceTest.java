package org.tailoredit.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tailoredit.control.DistributionListController;
import org.tailoredit.entity.DistributionList;
import org.tailoredit.entity.DistributionListEntry;

import static io.restassured.RestAssured.given;

@QuarkusTest
class DistributionListResourceTest {

    @Inject
    DistributionListController distributionListController;

    @BeforeEach
    void setup() {
        // Ensure that the distribution list is empty before each test
        distributionListController.deleteAll();
    }

    @Test
    void testGetDistributionList() {
        final DistributionList distributionList = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Assertions.assertTrue(distributionList.getDistributionEntries().isEmpty());
    }

    @Test
    void testAddEntryToDistributionListEndpoint() {
        final DistributionListEntry expectedListEntry = new DistributionListEntry("Test Entry", "0123456789");

        final DistributionList distributionList = given()
                .when()
                .body(expectedListEntry)
                .contentType(ContentType.JSON)
                .post("/distribution-list")
                .then()
                .extract()
                .as(DistributionList.class);

        Assertions.assertEquals(distributionList.getDistributionEntries().size(), 1);

        final DistributionListEntry resultEntry = distributionList.getDistributionEntries().get(0);
        Assertions.assertEquals(resultEntry.getName(), expectedListEntry.getName());
        Assertions.assertEquals(resultEntry.getNumber(), expectedListEntry.getNumber());
    }

}