package repository.interfaces;

import model.Subscriber;

import java.util.Optional;

public interface SubscriberRepositoryInterface {
    Optional<Subscriber> save(String username, String password, String name, String tel, String address);
    Optional<Subscriber> findSubscriber(String username, String password);
}
