package repository.interfaces;

import model.Book;
import model.Status;
import model.Subscriber;

import java.util.List;

public interface BookRepositoryInterface {

    List<Book> findAll();

    void updateStatus(int idBook, Status status, int subscriberId);

    void extendLoan(Book book);

    void save(Book book);

    void update(Book book);

    void remove(Book book);
}
