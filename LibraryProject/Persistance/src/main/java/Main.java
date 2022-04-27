import model.Subscriber;
import repository.implementations.LibrarianRepository;
import repository.implementations.SubscriberRepository;
import repository.interfaces.LibrarianRepositoryInterface;
import repository.utils.SessionFactoryUtils;

public class Main {

    public static void main(String[] args) {
        var x = new SubscriberRepository();
        var a = x.findSubscriber("Mara22", "2212");
        System.out.println(a.get().getBorrowedBooks());
        SessionFactoryUtils.close();
    }
}
