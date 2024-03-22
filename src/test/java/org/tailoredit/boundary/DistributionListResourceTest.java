package org.tailoredit.boundary;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.tailoredit.control.DistributionListController;
import org.tailoredit.entity.DistributionList;
import org.tailoredit.entity.DistributionListEntry;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class DistributionListResourceTest {

    @InjectMock
    DistributionListController distributionListController;

    @Test
    void testGetDistributionList() {
        Mockito.when(distributionListController.getAllEntries()).thenReturn(new DistributionList());

        final DistributionList distributionList = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Assertions.assertTrue(distributionList.isEmpty());
    }

    @Test
    void testAddEntryToDistributionListEndpoint() {
        final DistributionListEntry expectedListEntry = new DistributionListEntry("Test Entry", "0123456789");
        Mockito.when(distributionListController.getAllEntries()).thenReturn(new DistributionList(List.of(expectedListEntry)));

        final DistributionList distributionList = given()
                .when()
                .body(expectedListEntry)
                .contentType(ContentType.JSON)
                .post("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Mockito.verify(distributionListController, Mockito.times(1)).addDistributionListEntry(ArgumentMatchers.eq(expectedListEntry));
        Assertions.assertEquals(distributionList.size(), 1);
    }

}