package com.github.fridujo.glacio.examples.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
class InMemoryContactRepository implements ContactRepository {

    private final Map<String, ContactInfo> contactsById = new ConcurrentHashMap<>();

    @Override
    public String insert(ContactInfo contactInfo) throws ContactAlreadyExistingException {
        if (contactsById.containsValue(contactInfo)) {
            throw new ContactAlreadyExistingException();
        }
        String id = UUID.randomUUID().toString();
        contactsById.put(id, contactInfo.withId(id));
        return id;
    }

    @Override
    public void update(ContactInfo contactInfo) throws ContactNotFoundException {
        if (!contactsById.containsKey(contactInfo.getId())) {
            throw new ContactNotFoundException();
        }
        contactsById.put(contactInfo.getId(), contactInfo);
    }

    @Override
    public void delete(String id) throws ContactNotFoundException {
        if (!contactsById.containsKey(id)) {
            throw new ContactNotFoundException();
        }
        contactsById.remove(id);
    }

    @Override
    public Collection<ContactInfo> findAll() {
        return contactsById.values();
    }

    @Override
    public Collection<ContactInfo> findByName(String nameRegex) {
        Pattern namePattern = Pattern.compile(nameRegex, Pattern.CASE_INSENSITIVE);
        return contactsById
            .values()
            .stream()
            .filter(c ->
                namePattern.matcher(c.getName()).matches() ||
                    namePattern.matcher(c.getForename()).matches()
            )
            .collect(Collectors.toSet());
    }
}
