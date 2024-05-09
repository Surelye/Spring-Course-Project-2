package org.borodin.library.controllers;

import org.borodin.library.models.Book;
import org.borodin.library.models.Person;
import org.borodin.library.services.BooksService;
import org.borodin.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear, Model model) {
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(sortByYear));
        } else {
            if (page < 0 || booksPerPage < 1) {
                return "redirect:/books";
            }
            model.addAttribute("books", booksService.findAllWithPagination(page, booksPerPage, sortByYear));
        }
        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        Optional<Book> bookToShow = booksService.findById(id);
        if (bookToShow.isPresent()) {
            Book book = bookToShow.get();
            model.addAttribute("book", book);
            if (book.getOwner() == null) {
                model.addAttribute("people", peopleService.findAll());
            } else {
                model.addAttribute("owner", book.getOwner());
            }
        } else {
            return "redirect:/books";
        }
        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> book = booksService.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
        } else {
            return "redirect:/books";
        }
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid @ModelAttribute("book") Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        System.out.println(book.getOwner());
        book.setId(id);
        booksService.save(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.deleteById(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.updateOwner(id, person.getId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/retract")
    public String retract(@PathVariable("id") int id) {
        booksService.updateOwner(id, null);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "/books/search";
    }

    @PostMapping("/search")
    public String searchBooks(@RequestParam("query") String query, Model model) {
        model.addAttribute("books", booksService.findByTitleStartingWith(query));
        return "/books/search";
    }
}
