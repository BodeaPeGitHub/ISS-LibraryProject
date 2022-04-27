import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import model.Librarian;

import java.io.IOException;
import java.util.List;

public class AdminMainController {

    public Button addSubscriberButton;
    public Text loggedInAsLabel;
    public TextField searchBar;
    public ListView<Book> listView;
    public ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    public Button AddBookButton;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private Librarian librarian;
    private List<Book> books;

    public void addSubscriberOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("add-subscriber.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fxmlLoader.<AddSubscriberController>getController().setServiceBlockingStub(serviceBlockingStub);
        var stageAddSubscriber = new Stage();
        stageAddSubscriber.setScene(scene);
        stageAddSubscriber.show();
    }

    public void setServiceBlockingStubAndUser(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub, Librarian librarian) {
        this.serviceBlockingStub = serviceBlockingStub;
        this.librarian = librarian;
        loggedInAsLabel.setText("Logged in as " + librarian.getUsername());
        initModel();
    }

    @FXML
    public void initialize() {
        listView.setItems(bookObservableList);
        listView.setCellFactory(cell -> new BookCellAdminController(serviceBlockingStub, librarian));
    }

    private void initModel() {
        books = serviceBlockingStub.getAllBooks(Library.GetAllBooksRequest.newBuilder().build())
                .getBooksList()
                .stream()
                .map(Converters::fromProtoBookToBook)
                .toList();
        bookObservableList.setAll(books);
    }

    public void AddBookButtonOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("add-book.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fxmlLoader.<AddBookController>getController().setServiceBlockingStub(serviceBlockingStub);
        var stageAddSubscriber = new Stage();
        stageAddSubscriber.setScene(scene);
        stageAddSubscriber.show();
    }
}
