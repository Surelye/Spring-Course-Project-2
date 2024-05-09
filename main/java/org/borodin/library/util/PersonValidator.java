package org.borodin.library.util;

import org.borodin.library.models.Person;
import org.borodin.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        Person person = (Person) o;
        if (peopleService.findByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "Person already exists");
        }
    }
}
