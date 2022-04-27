import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.Book;
import model.Genre;

import java.util.*;

public class AddBookController {
    public TextField nameTextField;
    public TextField authorTextField;
    public ComboBox<Genre> genreComboBox;
    public Spinner<Integer> numberOfReviewsSpinner;
    public ToggleButton fiveStartButton;
    public ImageView fiveStarImage;
    public ToggleButton fourStarButton;
    public ImageView fourStarImage;
    public ToggleButton threeStarButton;
    public ImageView threeStarImage;
    public ToggleButton twoStarButton;
    public ImageView twoStarImage;
    public ToggleButton oneStarButton;
    public ImageView oneStarImage;
    public Text errorTextLabel;
    public Button saveButton;
    public Text addBookTitle;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private List<ToggleButton> buttonList;
    private List<ImageView> imageList;
    private Map<ToggleButton, ImageView> buttonImageViewHashMap = new HashMap<>();
    private ObservableList<Genre> comboBoxValues = FXCollections.observableArrayList();

    public void setServiceBlockingStub(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub) {
        this.serviceBlockingStub = serviceBlockingStub;
        buttonList = List.of(oneStarButton, twoStarButton, threeStarButton, fourStarButton, fiveStartButton);
        imageList = List.of(oneStarImage, twoStarImage, threeStarImage, fourStarImage, fiveStarImage);
        var fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-full.png")));
        var emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-empty.png")));
        for(int i = 0; i < buttonList.size(); i++)
            buttonImageViewHashMap.put(buttonList.get(i), imageList.get(i));
        imageList.forEach(img -> img.setImage(emptyStar));
        buttonList.forEach(toggleButton -> toggleButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                setStarImages();
                return;
            }
            var temp = fullStar;
            for(var img : imageList) {
                img.setImage(temp);
                if (img == buttonImageViewHashMap.get(toggleButton))
                    temp = emptyStar;
            }
        }));
        buttonList.forEach(button ->
            button.setOnAction(event -> {
                buttonList.forEach(btn -> btn.setSelected(false));
                button.setSelected(true);
            })
        );
        comboBoxValues.addAll(EnumSet.allOf(Genre.class));
        genreComboBox.setItems(comboBoxValues);
    }


    private void setStarImages() {
        var fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-full.png")));
        var emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-empty.png")));
        for(int i = buttonList.size() - 1; i > -1; i--) {
            if(buttonList.get(i).isSelected())
                emptyStar = fullStar;
            buttonImageViewHashMap.get(buttonList.get(i)).setImage(emptyStar);
        }
    }

    private Float getNumberOfReviews() {
        int i = buttonList.size() - 1;
        for(; i > -1; i--)
            if(buttonList.get(i).isSelected())
                break;
        return (float) i + 1;
    }


    public void saveButtonOnAction(ActionEvent actionEvent) {
        var request = Library.Book
                .newBuilder()
                .setName(nameTextField.getText())
                .setAuthor(authorTextField.getText())
                .setGenre(Library.Book.Genre.valueOf(genreComboBox.getValue().name()))
                .setNumberOfReviews(numberOfReviewsSpinner.getValue())
                .setReview(getNumberOfReviews())
                .build();
        var response = serviceBlockingStub.addBook(request);
        if(response.hasErrorMessage()) {
            errorTextLabel.setText(response.getErrorMessage());
            errorTextLabel.setFill(Paint.valueOf("red"));
            return;
        }
        errorTextLabel.setText("Added successfully.");
        errorTextLabel.setFill(Paint.valueOf("green"));

    }

    public void setWindowForUpdate(Book book) {
        addBookTitle.setText("Update book");
        saveButton.setText("Update");
        nameTextField.setText(book.getName());
        authorTextField.setText(book.getAuthor());
        genreComboBox.getSelectionModel().select(book.getGenre());
        numberOfReviewsSpinner.getValueFactory().setValue(book.getNumberOfReviews());
        buttonList.get((int) (book.getReview() - 1)).setSelected(true);
        setStarImages();
        saveButton.setOnAction(event -> {
            var request = Library.Book
                    .newBuilder()
                    .setId(book.getId())
                    .setName(nameTextField.getText())
                    .setAuthor(authorTextField.getText())
                    .setGenre(Library.Book.Genre.valueOf(genreComboBox.getValue().name()))
                    .setNumberOfReviews(numberOfReviewsSpinner.getValue())
                    .setReview(getNumberOfReviews())
                    .build();
            var response = serviceBlockingStub.updateBook(request);
            if(response.hasErrorMessage()) {
                errorTextLabel.setText(response.getErrorMessage());
                errorTextLabel.setFill(Paint.valueOf("red"));
                return;
            }
            errorTextLabel.setText("Updated successfully.");
            errorTextLabel.setFill(Paint.valueOf("green"));
        });
    }
}
