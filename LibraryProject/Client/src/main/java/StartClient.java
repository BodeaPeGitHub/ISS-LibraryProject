import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.channelz.v1.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 22122)
                .usePlaintext()
                .build();
        var service = LibraryServiceGrpc.newBlockingStub(channel);
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("log-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<LogInController>getController().setServiceBlockingStub(service);
        stage.setTitle("Library");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
