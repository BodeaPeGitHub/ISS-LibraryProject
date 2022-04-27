import io.grpc.stub.StreamObserver;
import model.Status;
import repository.implementations.BookRepository;
import repository.interfaces.BookRepositoryInterface;
import repository.interfaces.LibrarianRepositoryInterface;
import repository.interfaces.SubscriberRepositoryInterface;

public class ServiceImplementation extends LibraryServiceGrpc.LibraryServiceImplBase {

    private LibrarianRepositoryInterface librarianRepository;
    private SubscriberRepositoryInterface subscriberRepository;
    private BookRepositoryInterface bookRepository;

    public ServiceImplementation(LibrarianRepositoryInterface librarianRepository, SubscriberRepositoryInterface subscriberRepository, BookRepository repositoryBook) {
        this.librarianRepository = librarianRepository;
        this.subscriberRepository = subscriberRepository;
        this.bookRepository = repositoryBook;
    }

    @Override
    public void logIn(Library.LogInRequest request, StreamObserver<Library.LogInResponse> responseObserver) {
        if(request.hasLibrarian()) {
            var logInUser = librarianRepository.findLibrarian(request.getLibrarian().getUsername(),
                    request.getLibrarian().getPassword());
            logInUser.ifPresentOrElse(librarian -> responseObserver.onNext(Library.LogInResponse
                    .newBuilder()
                    .setLibrarian(Converters.fromLibrarianToProtoLibrarian(librarian))
                    .build()), () -> responseObserver.onNext(Library.LogInResponse.newBuilder().setErrorMessage("Invalid credentials.").build()));
        }
        if(request.hasSubscriber()) {
            var logInUser = subscriberRepository.findSubscriber(request.getSubscriber().getUsername(), request.getSubscriber().getPassword());
            logInUser.ifPresentOrElse(subscriber -> responseObserver.onNext(Library.LogInResponse
                    .newBuilder()
                    .setSubscriber(Converters.fromSubscriberToProtoSubscriberWithBooks(subscriber))
                    .build()), () -> responseObserver.onNext(Library.LogInResponse.newBuilder().setErrorMessage("Invalid credentials.").build()));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addSubscriber(Library.Subscriber request, StreamObserver<Library.AddSubscriberResponse> responseObserver) {
        var saved = subscriberRepository.save(
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getTel(),
                request.getAddress()
        );
        saved.ifPresentOrElse(subscriber ->
            responseObserver.onNext(Library.AddSubscriberResponse.newBuilder().setErrorMessage("Subscriber can't be added.").build()),
                    () -> responseObserver.onNext(Library.AddSubscriberResponse.newBuilder().setGood(1).build()));
        responseObserver.onCompleted();
    }

    @Override
    public void borrowBook(Library.BorrowBookRequest request, StreamObserver<Library.BasicResponse> responseObserver) {
        var borrowBookID = request.getIdBook();
        var subscriberID = request.getIdSubscriber();
        Library.BasicResponse response;
        try {
            bookRepository.updateStatus(borrowBookID, Status.Borrowed, subscriberID);
            response = Library.BasicResponse.newBuilder().setDoneSuccessfully(0).build();
        } catch (Exception exception) {
            response = Library.BasicResponse.newBuilder().setErrorMessage(exception.getMessage()).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllBooks(Library.GetAllBooksRequest request, StreamObserver<Library.GetAllBooksResponse> responseObserver) {
        var books = bookRepository.findAll();
        var response = Library.GetAllBooksResponse
                .newBuilder()
                .addAllBooks(
                            books.stream()
                                    .map(Converters::fromBookToProtoBook)
                                    .toList()
                            )
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(Library.Book request, StreamObserver<Library.BasicResponse> responseObserver) {
        Library.BasicResponse response = null;
        try {
            bookRepository.update(Converters.fromProtoBookToBook(request));
            response = Library.BasicResponse.newBuilder().setDoneSuccessfully(0).build();
        } catch (Exception ex) {
            response = Library.BasicResponse.newBuilder().setErrorMessage(ex.getMessage()).build();
            }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(Library.Book request, StreamObserver<Library.BasicResponse> responseObserver) {
        Library.BasicResponse response;
        try {
            bookRepository.remove(Converters.fromProtoBookToBook(request));
            response = Library.BasicResponse.newBuilder().setDoneSuccessfully(0).build();
        } catch (Exception exception) {
            response = Library.BasicResponse.newBuilder().setErrorMessage(exception.getMessage()).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void extendLoan(Library.Book request, StreamObserver<Library.BasicResponse> responseObserver) {
        Library.BasicResponse response = null;
        try {
            bookRepository.extendLoan(Converters.fromProtoBookToBook(request));
            response = Library.BasicResponse.newBuilder().setDoneSuccessfully(0).build();
        } catch (Exception ex) {
            response = Library.BasicResponse.newBuilder().setErrorMessage(ex.getMessage()).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(Library.Book request, StreamObserver<Library.BasicResponse> responseObserver) {
        var book = Converters.fromProtoBookToBook(request);
        Library.BasicResponse response;
        try {
            bookRepository.save(book);
            response = Library.BasicResponse.newBuilder().setDoneSuccessfully(0).build();
        } catch (Exception exception) {
            response = Library.BasicResponse.newBuilder().setErrorMessage(exception.getMessage()).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
