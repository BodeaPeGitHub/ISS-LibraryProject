import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.Book;

public class ExtendLoanController {
    public Label textLabel;
    public DatePicker borrowDatePicker;
    public DatePicker returnDatePicker;
    public Button extendLoanButton;
    public Text errorLabel;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private Book book;


    public void extendLoanButtonOnAction(ActionEvent actionEvent) {
        book.setDateToReturn(returnDatePicker.getValue());
        var response = serviceBlockingStub.extendLoan(Converters.fromBookToProtoBook(book));
        if(response.hasErrorMessage()) {
            errorLabel.setText(response.getErrorMessage());
            errorLabel.setFill(Paint.valueOf("red"));
            return;
        }
        errorLabel.setText("Updated successfully.");
        errorLabel.setFill(Paint.valueOf("green"));
    }

    public void setServiceBlockingStubAndBook(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub, Book item) {
        this.serviceBlockingStub = serviceBlockingStub;
        this.book = item;
        borrowDatePicker.setValue(item.getDateBorrowed());
        textLabel.setText("Extend loan on book " + book.getName() + "\nwritten by " + book.getAuthor());
    }
}
