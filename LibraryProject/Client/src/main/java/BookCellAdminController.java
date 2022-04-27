import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import model.Librarian;
import model.Status;
import model.Subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookCellAdminController extends ListCell<Book> {

    public Text bookNameLabel;
    public Text authorNameLabel;
    public AnchorPane anchorPane;
    public Button extendLoanButton;
    public Button removeButton;
    public Button updateButton;
    private Librarian librarian;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;


    public BookCellAdminController(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub, Librarian librarian) {
        this.librarian = librarian;
        this.serviceBlockingStub = serviceBlockingStub;
    }

    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        loadFxml();
        bookNameLabel.setText(item.getName());
        authorNameLabel.setText(item.getAuthor());
        updateButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("add-book.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            fxmlLoader.<AddBookController>getController().setServiceBlockingStub(serviceBlockingStub);
            fxmlLoader.<AddBookController>getController().setWindowForUpdate(item);
            var stageAddSubscriber = new Stage();
            stageAddSubscriber.setScene(scene);
            stageAddSubscriber.show();
        });
        removeButton.setOnAction(event -> {
            var response = serviceBlockingStub.deleteBook(Converters.fromBookToProtoBook(item));
            if(response.hasErrorMessage())
                new Alert(Alert.AlertType.ERROR, response.getErrorMessage()).show();
        });
        if (item.getStatus() == Status.Borrowed)
        extendLoanButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("admin/extend_loan.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            fxmlLoader.<ExtendLoanController>getController().setServiceBlockingStubAndBook(serviceBlockingStub, item);
            var stageAddSubscriber = new Stage();
            stageAddSubscriber.setScene(scene);
            stageAddSubscriber.show();
        });
        else
            extendLoanButton.setDisable(true);
        setText(null);
        setGraphic(anchorPane);
    }

    private void loadFxml() {
        var loader = new FXMLLoader(StartClient.class.getResource("admin/book-cell.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
