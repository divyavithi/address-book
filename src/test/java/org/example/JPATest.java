package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JPATest {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private BuddyInfoRepository buddyInfoRepository;

    @Test
    @Transactional
    public void testAddressBookRepository() {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Flintstone", "613-906-8590"));
        book.addBuddy(new BuddyInfo("Greg", "613-654-3210"));

        addressBookRepository.save(book);

        List<AddressBook> books = (List<AddressBook>) addressBookRepository.findAll();
        AddressBook savedBook = books.get(0);
        assertThat(savedBook.getBuddies()).hasSize(2);
    }

    @Test
    @Transactional
    public void testBuddyInfoRepository() {
        BuddyInfo h = new BuddyInfo("Harvey", "613-904-7493");
        BuddyInfo i = new BuddyInfo("Ian", "613-749-8292");

        buddyInfoRepository.save(h);
        BuddyInfo savedIan = buddyInfoRepository.save(i);

        BuddyInfo retrievedIan = buddyInfoRepository.findById(savedIan.getId()).orElse(null);
        assertThat(retrievedIan).isNotNull();
        assertThat(retrievedIan.getName()).isEqualTo("Ian");

        List<BuddyInfo> ian = buddyInfoRepository.findByName("Ian");
        assertThat(ian).hasSize(1);
        assertThat(ian.get(0).getPhoneNumber()).isEqualTo("613-749-8292");
    }
}

