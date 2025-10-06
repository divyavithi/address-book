package org.example.servingwebcontent;

import org.example.AddressBook;
import org.example.AddressBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddressBookController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/addressbook")
    public String listAddressBooks(Model model) {
        Iterable<AddressBook> books = addressBookRepository.findAll();
        model.addAttribute("addressBooks", books);
        return "addressbook";
    }
}


