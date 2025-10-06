package org.example.servingwebcontent;

import org.example.AddressBook;
import org.example.AddressBookRepository;
import org.example.BuddyInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AddressBookController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    // web endpoint

    @GetMapping("/addressbook")
    public String listAddressBooks(Model model) {
        Iterable<AddressBook> books = addressBookRepository.findAll();
        model.addAttribute("addressBooks", books);
        return "addressbook";
    }

    // REST endpoints

    @GetMapping("/addressBooks/{id}/buddies")
    @ResponseBody
    public List<BuddyInfo> getBuddies(@PathVariable Integer id) {
        AddressBook book = addressBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AddressBook not found: " + id));
        return book.getBuddies();
    }

    @PostMapping("/addressBooks")
    @ResponseBody
    public ResponseEntity<AddressBook> createAddressBook(@RequestBody AddressBook book) {
        AddressBook saved = addressBookRepository.save(book);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/buddyInfoes/{id}")
    @ResponseBody
    public ResponseEntity<AddressBook> updateAddressBook(
            @PathVariable Integer id,
            @RequestBody AddressBook updatedBook) {
        Optional<AddressBook> existing = addressBookRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AddressBook book = existing.get();
        book.getBuddies().clear();
        book.getBuddies().addAll(updatedBook.getBuddies());
        AddressBook saved = addressBookRepository.save(book);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/addressBooks/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteAddressBook(@PathVariable Integer id) {
        if (!addressBookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        addressBookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
