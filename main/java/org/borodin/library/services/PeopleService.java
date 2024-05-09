package org.borodin.library.services;

import org.borodin.library.models.Person;
import org.borodin.library.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    public Optional<Person> findByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public Optional<Person> findByIdEager(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        person.ifPresent(p -> Hibernate.initialize(p.getBooks()));
        return person;
    }

    @Transactional(readOnly = false)
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
