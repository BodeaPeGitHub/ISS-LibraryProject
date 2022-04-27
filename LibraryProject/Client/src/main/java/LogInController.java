import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Librarian;
import model.Subscriber;

import java.io.IOException;

public class LogInController {

    public Text errorLabel;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Button logInButton;
    public ComboBox<String> toggleButton;
    LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private ObservableList<String> users = FXCollections.observableArrayList();

    public void setServiceBlockingStub(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub) {
        this.serviceBlockingStub = serviceBlockingStub;
        users.setAll("Librarian", "Subscriber");
        toggleButton.setItems(users);
    }

    public void logInButtonOnAction(ActionEvent actionEvent) {
        Library.LogInRequest request;
        if(toggleButton.getValue().equals("Librarian"))
            request = Library.LogInRequest
                    .newBuilder()
                    .setLibrarian(Library.Librarian
                            .newBuilder()
                            .setUsername(usernameTextField.getText())
                            .setPassword(passwordTextField.getText())
                            .build())
                    .build();
        else
            request = Library.LogInRequest
                    .newBuilder()
                    .setSubscriber(Library.Subscriber
                            .newBuilder()
                            .setUsername(usernameTextField.getText())
                            .setPassword(passwordTextField.getText())
                            .build())
                    .build();
        var response = serviceBlockingStub.logIn(request);
        if(response.hasErrorMessage())
            errorLabel.setText(response.getErrorMessage());
        else if(response.hasLibrarian())
            changeToAdminMainWindow(Converters.fromProtoLibrarianToLibrarian(response.getLibrarian()));
        else
            changeToSubscriberMainWindow(Converters.fromProtoSubscriberWithBooksToSubscriber(response.getSubscriber()));
    }

    private void changeToSubscriberMainWindow(Subscriber subscriber) {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("subscriber.fxml"));
        try {
            var stage = (Stage) logInButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            fxmlLoader.<SubscriberMainController>getController().setServiceBlockingStubAndUser(serviceBlockingStub, subscriber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeToAdminMainWindow(Librarian librarian) {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("admin.fxml"));
        try {
            var stage = (Stage) logInButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            fxmlLoader.<AdminMainController>getController().setServiceBlockingStubAndUser(serviceBlockingStub, librarian);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
