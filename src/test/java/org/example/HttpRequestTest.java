package org.example;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpRequestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Integer createdId;

    private AddressBook createSampleAddressBook() {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("A", "111-111-1111"));
        book.addBuddy(new BuddyInfo("B", "222-222-2222"));
        return book;
    }

    @Test
    @Order(1)
    void postAddressBookTest() {
        AddressBook book = createSampleAddressBook();
        ResponseEntity<AddressBook> response = restTemplate.postForEntity(
                "/addressBooks", book, AddressBook.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        AddressBook created = response.getBody();
        assertNotNull(created);
        assertEquals(2, created.getBuddies().size());

        createdId = created.getId();
    }

    @Test
    @Order(2)
    void getAddressBookTest() {
        assertNotNull(createdId);
        ResponseEntity<BuddyInfo[]> getResponse = restTemplate.getForEntity(
                "/addressBooks/" + createdId + "/buddies", BuddyInfo[].class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        BuddyInfo[] buddies = getResponse.getBody();
        assertNotNull(buddies);
        assertEquals(2, buddies.length);
        assertTrue(Arrays.stream(buddies).anyMatch(b -> b.getName().equals("A")));
        assertTrue(Arrays.stream(buddies).anyMatch(b -> b.getName().equals("B")));
    }

    @Test
    @Order(3)
    void deleteAddressBookTest() {
        assertNotNull(createdId);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/addressBooks/" + createdId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
