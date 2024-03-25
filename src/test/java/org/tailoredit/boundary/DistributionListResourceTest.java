package org.tailoredit.boundary;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.tailoredit.control.DistributionListController;
import org.tailoredit.control.DuplicateEntryException;
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
        final DistributionListEntry listEntry = new DistributionListEntry("Jane Doe", "0123456789");
        final DistributionList distributionList = new DistributionList(List.of(listEntry));

        Mockito.when(distributionListController.getAllEntries()).thenReturn(distributionList);

        final DistributionList responseDistributionList = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Assertions.assertEquals(1, responseDistributionList.size());
        Assertions.assertTrue(responseDistributionList.contains(listEntry));
    }

    @Test
    void testAddEntryToDistributionList() {
        final DistributionListEntry listEntry = new DistributionListEntry("Jane Doe", "0123456789");
        Mockito.when(distributionListController.getAllEntries()).thenReturn(new DistributionList());

        final DistributionList responseDistributionList = given()
                .when()
                .contentType(ContentType.JSON)
                .body(listEntry)
                .post("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Mockito.verify(distributionListController, Mockito.times(1)).addDistributionListEntry(listEntry);
    }

    @Test
    void testDeleteEntryFromDistributionList() {
        final DistributionListEntry listEntry = new DistributionListEntry("Jane Doe", "0123456789");
        Mockito.when(distributionListController.getAllEntries()).thenReturn(new DistributionList());

        final DistributionList distributionList = given()
                .when()
                .contentType(ContentType.JSON)
                .body(listEntry)
                .delete("/distribution-list")
                .then()
                .statusCode(200)
                .extract()
                .as(DistributionList.class);

        Mockito.verify(distributionListController, Mockito.times(1)).deleteEntry(listEntry);
    }

    @Test
    void testDeleteAllEntriesFromDistributionList() {
        given()
                .when()
                .delete("/distribution-list/all")
                .then()
                .statusCode(200);

        Mockito.verify(distributionListController, Mockito.times(1)).deleteAll();
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

    @Test
    void testDuplicateEntryExceptionsAreCorrectlyMapped() {
        final String exception = "Test Duplicate Entry Exception";
        final DistributionListEntry listEntry = new DistributionListEntry("Test Entry", "0123456789");

        Mockito.doThrow(new DuplicateEntryException(exception))
                .when(distributionListController).addDistributionListEntry(ArgumentMatchers.eq(listEntry));

        given()
                .when()
                .body(listEntry)
                .contentType(ContentType.JSON)
                .post("/distribution-list")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(DistributionListResource.EXCEPTION_PREFIX + exception));
    }

}