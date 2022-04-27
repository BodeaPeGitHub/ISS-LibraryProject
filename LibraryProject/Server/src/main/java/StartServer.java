import com.google.protobuf.ServiceException;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.AbstractStub;
import repository.implementations.BookRepository;
import repository.implementations.LibrarianRepository;
import repository.implementations.SubscriberRepository;
import repository.utils.SessionFactoryUtils;

import java.io.IOException;
import java.util.Properties;

public class StartServer {

    private Server server;
    private static Integer serverPort = 22122;

    public static void main(String[] args) throws ServiceException {
        Properties properties = new Properties();
        try {
            properties.load(StartServer.class.getResourceAsStream("server.properties"));
            properties.list(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var repositoryLibrarian = new LibrarianRepository();
        var repositorySubscriber = new SubscriberRepository();
        var repositoryBook = new BookRepository();
        var service = new ServiceImplementation(repositoryLibrarian, repositorySubscriber, repositoryBook);
        try {
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        StartServer startServer = new StartServer();
        try {
            startServer.start(service);
            startServer.blockUntilShutdown();
            SessionFactoryUtils.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            SessionFactoryUtils.close();
        }
    }

    private void start(ServiceImplementation service) throws IOException {
        server = ServerBuilder
                .forPort(serverPort)
                .addService(service)
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.awaitTermination();
    }

}