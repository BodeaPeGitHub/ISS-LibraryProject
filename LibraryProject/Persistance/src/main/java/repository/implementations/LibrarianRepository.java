package repository.implementations;

import model.Librarian;
import org.hibernate.Transaction;
import repository.interfaces.LibrarianRepositoryInterface;
import repository.utils.SessionFactoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibrarianRepository implements LibrarianRepositoryInterface {

    @Override
    public Optional<Librarian> findLibrarian(String username, String password) {
        List<Librarian> librarianList = new ArrayList<>();
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                librarianList = session.createQuery("from Librarian L where L.username = :username and L.password = :password", Librarian.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .list();
                transaction.commit();
            }catch (Exception ex) {
                ex.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return librarianList.size() == 0 ? Optional.empty() : Optional.of(librarianList.get(0));
    }

}
