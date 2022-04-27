import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Book;
import model.Status;
import model.Subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookCellController extends ListCell<Book> {

    public Text bookNameLabel;
    public Text authorNameLabel;
    public Text reviewText;
    public Text notAvailableText;
    public Label notAvailableLabel;
    public ToggleButton fiveStartButton;
    public ToggleButton fourStarButton;
    public ToggleButton threeStarButton;
    public ToggleButton twoStarButton;
    public ToggleButton oneStarButton;
    public Button borrowButton;
    public AnchorPane anchorPane;
    public ImageView fiveStarImage;
    public ImageView fourStarImage;
    public ImageView threeStarImage;
    public ImageView twoStarImage;
    public ImageView oneStarImage;
    private Subscriber subscriber;
    private LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub;
    private List<ToggleButton> buttonList;
    private List<ImageView> imageList;
    private Map<ToggleButton, ImageView> buttonImageViewHashMap = new HashMap<>();


    public BookCellController(LibraryServiceGrpc.LibraryServiceBlockingStub serviceBlockingStub, Subscriber subscriber) {
        this.subscriber = subscriber;
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
        buttonList = List.of(oneStarButton, twoStarButton, threeStarButton, fourStarButton, fiveStartButton);
        imageList = List.of(oneStarImage, twoStarImage, threeStarImage, fourStarImage, fiveStarImage);
        var fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-full.png")));
        var emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-empty.png")));
        borrowButton.setDisable(item.getStatus() == Status.Borrowed);
        notAvailableLabel.setVisible(item.getStatus() == Status.Borrowed);
        borrowButton.setOnAction(event -> {
                    var response = serviceBlockingStub.borrowBook(
                            Library.BorrowBookRequest
                                    .newBuilder()
                                    .setIdBook(item.getId())
                                    .setIdSubscriber(subscriber.getId())
                                    .build());
                    if(response.hasErrorMessage()) {
                        var alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(response.getErrorMessage());
                        alert.setTitle("Error at borrowing book." + subscriber.getId());
                        alert.show();
                    }
                });
        for(int i = 0; i < imageList.size(); i++)
            buttonImageViewHashMap.put(buttonList.get(i), imageList.get(i));
        setStarImages(item);
        buttonList.forEach(toggleButton -> toggleButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                setStarImages(item);
                return;
            }
            var temp = fullStar;
            for(var img : imageList) {
                img.setImage(temp);
                if (img == buttonImageViewHashMap.get(toggleButton))
                    temp = emptyStar;
            }
        }));
        setText(null);
        setGraphic(anchorPane);
    }

    private void setStarImages(Book item) {
        var fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-full.png")));
        var emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("pictures/star-empty.png")));
        for(int i = 0; i < imageList.size(); i++) {
            if (i < item.getReview().intValue())
                imageList.get(i).setImage(fullStar);
            else
                imageList.get(i).setImage(emptyStar);
        }
    }


    private void loadFxml() {
        var loader = new FXMLLoader(StartClient.class.getResource("subscriber/book-cell.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
