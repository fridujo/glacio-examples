package com.github.fridujo.glacio.examples;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.fridujo.glacio.examples.persistence.ContactAlreadyExistingException;
import com.github.fridujo.glacio.examples.persistence.ContactNotFoundException;
import com.github.fridujo.glacio.examples.persistence.ContactRepository;

@RestController
@RequestMapping("/api/contact")
class ContactController {

    private final ContactRepository contactRepository;

    ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    String add(@RequestBody ContactInfoDto contactInfo) throws ContactAlreadyExistingException {
        return contactRepository.insert(contactInfo.persistable());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void update(@RequestBody ContactInfoDto contactInfo) throws ContactNotFoundException {
        contactRepository.update(contactInfo.persistable());
    }

    @DeleteMapping
    void delete(String id) throws ContactNotFoundException {
        contactRepository.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<ContactInfoDto> getAll() {
        return contactRepository
            .findAll()
            .stream()
            .map(ContactInfoDto::from)
            .collect(Collectors.toSet());
    }

    @GetMapping(path = "/{nameRegex}", produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<ContactInfoDto> getByName(@PathVariable("nameRegex") String nameRegex) {
        return contactRepository
            .findByName(nameRegex)
            .stream()
            .map(ContactInfoDto::from)
            .collect(Collectors.toSet());
    }
}
