import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Book;
import model.Genre;
import model.Subscriber;

import java.util.List;
import java.util.function.Predicate;

public class SubscriberMainController {

    public ListView<Book> booksListView;
    public TextField searchBarTextField;
    public CheckBox adventureCheck;
    public CheckBox SFCheck;
    public CheckBox romanceCheck;
    public CheckBox actionCheck;
    public CheckBox crimeCheck;
    public CheckBox oneStarCheck;
    public CheckBox fourStarCheck;
    public CheckBox threeStarCheck;
    public CheckBox twoStarChesk;
    public CheckBox fiveStarCheck;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private Subscriber subscriber;
    private ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    private List<Book> books;

    public void setServiceBlockingStubAndUser(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub, Subscriber subscriber) {
        this.serviceBlockingStub = serviceBlockingStub;
        this.subscriber = subscriber;
        initModel();
        searchBarTextField.textProperty().addListener(event -> filterBooks(null));
    }

    private void initModel() {
        books = serviceBlockingStub.getAllBooks(Library.GetAllBooksRequest.newBuilder().build())
                .getBooksList()
                .stream()
                .map(Converters::fromProtoBookToBook)
                .toList();
        bookObservableList.setAll(books);
    }

    @FXML
    public void initialize() {
        booksListView.setItems(bookObservableList);
        booksListView.setCellFactory(cell -> new BookCellController(serviceBlockingStub, subscriber));
    }

    public void filterBooks(ActionEvent actionEvent) {
        Predicate<Book> filterByGenre = book ->
                adventureCheck.isSelected() && book.getGenre() == Genre.Adventure ||
                actionCheck.isSelected() && book.getGenre() == Genre.Action ||
                SFCheck.isSelected() && book.getGenre() == Genre.Other ||
                romanceCheck.isSelected() && book.getGenre() == Genre.Other ||
                crimeCheck.isSelected() && book.getGenre() == Genre.Drama ||
                !crimeCheck.isSelected() && !actionCheck.isSelected() && !SFCheck.isSelected() && !romanceCheck.isSelected() && !adventureCheck.isSelected();
        Predicate<Book> filterByReview = book ->
                oneStarCheck.isSelected() && book.getReview().intValue() == 1 ||
                twoStarChesk.isSelected() && book.getReview().intValue() == 2 ||
                threeStarCheck.isSelected() && book.getReview().intValue() == 3 ||
                fourStarCheck.isSelected() && book.getReview().intValue() == 4 ||
                fiveStarCheck.isSelected() && book.getReview().intValue() == 5 ||
                !oneStarCheck.isSelected() && !twoStarChesk.isSelected() && !threeStarCheck.isSelected() && !fourStarCheck.isSelected() && !fiveStarCheck.isSelected();
        bookObservableList.setAll(books
                .stream()
                .filter(book -> book.getAuthor().startsWith(searchBarTextField.getText()) ||
                        book.getName().startsWith(searchBarTextField.getText()))
                .filter(filterByGenre)
                .filter(filterByReview)
                .toList());
    }
}
