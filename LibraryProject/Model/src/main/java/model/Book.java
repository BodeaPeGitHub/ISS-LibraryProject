package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@javax.persistence.Entity
@Table(name = "books")
public class Book extends Entity {
    private String name;
    private String author;
    private LocalDate dateBorrowed;
    private Status status;
    private Float review;
    private Integer numberOfReviews;
    private Genre genre;
    private LocalDate dateToReturn;

    public Book(Integer id, String name, String author, LocalDate dateBorrowed, Status status, Float review, Integer numberOfReviews, Genre genre, LocalDate dateToReturn) {
        setId(id);
        this.name = name;
        this.author = author;
        this.dateBorrowed = dateBorrowed;
        this.status = status;
        this.review = review;
        this.numberOfReviews = numberOfReviews;
        this.genre = genre;
        this.dateToReturn = dateToReturn;
    }

    public Book() {}

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "book_id")
    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "date_borrowed")
    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getReview() {
        return review;
    }

    public void setReview(Float review) {
        this.review = review;
    }

    @Column(name = "number_of_reviews")
    public Integer getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(Integer numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Column(name = "date_to_return")
    public LocalDate getDateToReturn() {
        return dateToReturn;
    }

    public void setDateToReturn(LocalDate dateToReturn) {
        this.dateToReturn = dateToReturn;
    }
}