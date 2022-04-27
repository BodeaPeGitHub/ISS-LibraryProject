package repository.interfaces;

import model.Librarian;

import java.util.Optional;

public interface LibrarianRepositoryInterface {
    Optional<Librarian> findLibrarian(String username, String password);
}
