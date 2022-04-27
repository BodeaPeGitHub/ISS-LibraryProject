import com.google.protobuf.Timestamp;
import model.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

public class Converters {

    public static Librarian fromProtoLibrarianToLibrarian(Library.Librarian librarian) {
        return new Librarian(librarian.getId(), librarian.getUsername(), librarian.getPassword());
    }

    public static Library.Librarian fromLibrarianToProtoLibrarian(Librarian librarian) {
        return Library.Librarian.newBuilder().setUsername(librarian.getUsername()).setPassword(librarian.getPassword()).build();
    }

    public static Library.Subscriber fromSubscriberToProtoSubscriberNoBooks(Subscriber subscriber) {
        return Library.Subscriber.newBuilder()
                .setUsername(subscriber.getUsername())
                .setPassword(subscriber.getPassword())
                .setTel(subscriber.getTel())
                .setAddress(subscriber.getAddress())
                .setName(subscriber.getName())
                .build();
    }

    public static Subscriber fromProtoSubscriberToSubscriberNoBooks(Library.Subscriber subscriber) {
        return new Subscriber(
                subscriber.getId(),
                subscriber.getName(),
                subscriber.getUsername(),
                subscriber.getPassword(),
                subscriber.getTel(),
                subscriber.getAddress()
        );
    }

    public static Subscriber fromProtoSubscriberWithBooksToSubscriber(Library.SubscriberWithBooks subscriber) {
        return new Subscriber(
                subscriber.getId(),
                subscriber.getName(),
                subscriber.getUsername(),
                subscriber.getPassword(),
                subscriber.getTel(),
                subscriber.getAddress(),
                subscriber.getBooksList()
                        .stream()
                        .map(Converters::fromProtoBookToBook)
                        .collect(Collectors.toSet())
        );
    }

    public static Library.SubscriberWithBooks fromSubscriberToProtoSubscriberWithBooks(Subscriber subscriber) {
        return Library.SubscriberWithBooks.newBuilder()
                .setId(subscriber.getId())
                .setUsername(subscriber.getUsername())
                .setPassword(subscriber.getPassword())
                .setName(subscriber.getName())
                .setAddress(subscriber.getAddress())
                .setTel(subscriber.getTel())
                .addAllBooks(subscriber.getBorrowedBooks()
                        .stream()
                        .map(Converters::fromBookToProtoBook)
                        .toList())
                .build();
    }

    public static Library.Book fromBookToProtoBook(Book book) {
        var instantDateBorrowed = LocalDateTime.of(book.getDateBorrowed(), LocalTime.MIN).toInstant(ZoneOffset.UTC);
        var instantDateToReturn = LocalDateTime.of(book.getDateToReturn(), LocalTime.MIN).toInstant(ZoneOffset.UTC);
        return Library.Book
                .newBuilder()
                .setId(book.getId())
                .setName(book.getName())
                .setAuthor(book.getAuthor())
                .setDateBorrowed(Timestamp.newBuilder().setSeconds(instantDateBorrowed.getEpochSecond()).setNanos(instantDateBorrowed.getNano()).build())
                .setDateToReturn(Timestamp.newBuilder().setSeconds(instantDateToReturn.getEpochSecond()).setNanos(instantDateToReturn.getNano()).build())
                .setStatus(book.getStatus() == Status.Borrowed ? Library.Book.Status.Borrowed : Library.Book.Status.Returned)
                .setReview(book.getReview())
                .setNumberOfReviews(book.getNumberOfReviews())
                .setGenreValue(book.getGenre().ordinal())
                .build();
    }

    public static Book fromProtoBookToBook(Library.Book book) {
        return new Book(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                Instant.ofEpochSecond( book.getDateBorrowed().getSeconds() , book.getDateBorrowed().getNanos())
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate(),
                book.getStatus().getNumber() == 0 ? Status.Borrowed : Status.Returned,
                book.getReview(),
                book.getNumberOfReviews(),
                Genre.valueOf(book.getGenre().name()),
                Instant.ofEpochSecond( book.getDateToReturn().getSeconds() , book.getDateToReturn().getNanos())
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
        );
    }
}
