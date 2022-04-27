import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class AddSubscriberController {

    public TextField telTextField;
    public TextField addressTextField;
    public TextField nameTextField;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Text errorTextLabel;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;

    public void setServiceBlockingStub(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub) {
        this.serviceBlockingStub = serviceBlockingStub;
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        var response = serviceBlockingStub.addSubscriber(Library.Subscriber
                .newBuilder()
                .setUsername(usernameTextField.getText())
                .setName(nameTextField.getText())
                .setAddress(addressTextField.getText())
                .setTel(telTextField.getText())
                .setPassword(passwordTextField.getText())
                .build());
        if(response.hasErrorMessage()) {
            errorTextLabel.setText(response.getErrorMessage());
            errorTextLabel.setFill(Paint.valueOf("red"));
            passwordTextField.setText("");
            return;
        }
        errorTextLabel.setText("Added successfully.");
        errorTextLabel.setFill(Paint.valueOf("green"));
    }
}
