package org.borodin.library.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Book title should not be empty")
    @Size(min = 1, max = 100, message = "Book title should be between 1 and 50 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Book author should not be empty")
    @Size(min = 2, max = 100, message = "Book author should be between 2 and 100 characters")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Year should be greater than or equal 1500")
    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    private boolean expired;

    public Book() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isExpired() {
        if (expired) {
            return true;
        }
        if (takenAt == null) {
            return false;
        }
        long diff = takenAt.getTime() - System.currentTimeMillis();
        expired = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) >= 10;
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                '}';
    }
}
