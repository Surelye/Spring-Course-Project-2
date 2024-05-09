package org.borodin.library.services;

import org.borodin.library.models.Book;
import org.borodin.library.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("publicationYear"));
        }
        return booksRepository.findAll();
    }

    public List<Book> findAllWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage,
                    Sort.by("publicationYear"))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void deleteById(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void updateOwner(int bookId, Integer personId) {
        Book book = booksRepository.findById(bookId).orElseThrow();
        if (personId == null) {
            book.setOwner(null);
            book.setTakenAt(null);
        } else {
            book.setOwner(peopleService.findById(personId).orElseThrow());
            book.setTakenAt(new Date());
        }
        booksRepository.save(book);
    }

    public List<Book> findByTitleStartingWith(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }
}
