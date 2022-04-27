package repository.implementations;

import model.Librarian;
import model.Subscriber;
import org.hibernate.Transaction;
import repository.interfaces.SubscriberRepositoryInterface;
import repository.utils.SessionFactoryUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubscriberRepository implements SubscriberRepositoryInterface {

    @Override
    public Optional<Subscriber> save(String username, String password, String name, String tel, String address) {
        Serializable index = null;
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                index = session.save(new Subscriber(name, username, password, address, tel));
                transaction.commit();
            }catch (Exception ex) {
                ex.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return index == null ? Optional.of(new Subscriber(name, username, password, address, tel)) : Optional.empty();
    }

    public Optional<Subscriber> findSubscriber(String username, String password) {
        List<Subscriber> subscriberList = new ArrayList<>();
        try(var session = SessionFactoryUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                subscriberList = session.createQuery("from Subscriber L where L.username = :username and L.password = :password", Subscriber.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .list();
                transaction.commit();
            }catch (Exception ex) {
                ex.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return subscriberList.size() == 0 ? Optional.empty() : Optional.of(subscriberList.get(0));
    }
}
