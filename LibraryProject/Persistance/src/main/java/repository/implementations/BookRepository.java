package repository.implementations;

import model.Book;
import model.Status;
import model.Subscriber;
import org.hibernate.Transaction;
import repository.interfaces.BookRepositoryInterface;
import repository.utils.SessionFactoryUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements BookRepositoryInterface {

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bookList = session.createQuery("from Book", Book.class).list();
                transaction.commit();
            }catch (Exception ex) {
                ex.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return bookList;
    }

    @Override
    public void updateStatus(int idBook, Status status, int subscriberId) {
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                var book = session.load(Book.class, idBook);
                var subscriber = session.load(Subscriber.class, subscriberId);
                book.setDateBorrowed(LocalDate.now());
                book.setDateToReturn(LocalDate.now().plusDays(7));
                book.setStatus(status);
                subscriber.getBorrowedBooks().add(book);
                transaction.commit();
            } catch (Exception exception) {
                exception.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
                //TODO throw error when things go wrong.
            }
        }
    }

    @Override
    public void extendLoan(Book book) {
        try (var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                var oldBook = session.load(Book.class, book.getId());
                oldBook.setDateToReturn(book.getDateToReturn());
                transaction.commit();
            } catch (Exception exception) {
                exception.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
                //TODO throw error.
            }
        }
    }

    @Override
    public void save(Book book) {
        book.setStatus(Status.Returned);
        book.setDateBorrowed(LocalDate.now());
        book.setDateToReturn(LocalDate.now());
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(book);
                transaction.commit();
            } catch (Exception exception) {
                exception.printStackTrace();
                if(transaction != null)
                    transaction.rollback();
                //TODO throw error.
            }
        }
    }

    @Override
    public void update(Book book) {
        try (var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                var oldBook = session.load(Book.class, book.getId());
                oldBook.setName(book.getName());
                oldBook.setAuthor(book.getAuthor());
                oldBook.setGenre(book.getGenre());
                oldBook.setNumberOfReviews(book.getNumberOfReviews());
                oldBook.setReview(book.getReview());
                transaction.commit();
            } catch (Exception exception) {
                exception.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
                //TODO throw error.
            }
        }
    }

    @Override
    public void remove(Book book) {
        try (var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                var toRemoveBook = session.load(Book.class, book.getId());
                session.remove(toRemoveBook);
                transaction.commit();
            } catch (Exception exception) {
                exception.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
                //TODO throw error.
            }
        }
    }
}
