package org.borodin.library.controllers;

import org.borodin.library.models.Person;
import org.borodin.library.services.PeopleService;
import org.borodin.library.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    public final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "/people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Person> personToShow = peopleService.findByIdEager(id);
        if (personToShow.isPresent()) {
            model.addAttribute("person", personToShow.get());
            model.addAttribute("books", personToShow.get().getBooks());
        } else {
            return "redirect:/people";
        }
        return "/people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Person> personToEdit = peopleService.findById(id);
        if (personToEdit.isPresent()) {
            model.addAttribute("person", personToEdit.get());
        } else {
            return "redirect:/people";
        }
        return "/people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid @ModelAttribute("person") Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/people/edit";
        }
        person.setId(id);
        peopleService.save(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
