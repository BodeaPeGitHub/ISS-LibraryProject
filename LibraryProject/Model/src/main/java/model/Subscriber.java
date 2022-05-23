package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "subscribers")
public class Subscriber extends Entity {
    private String name;
    private String username;
    private String password;
    private String address;
    private String tel;
    private Set<Book> borrowedBooks;

    public Subscriber(Integer id, String name, String username, String password, String address, String tel, Set<Book> borrowedBooks) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.tel = tel;
        this.borrowedBooks = borrowedBooks;
        setId(id);
    }

    public Subscriber(String name, String username, String password, String address, String tel) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.tel = tel;
    }

    public Subscriber(Integer id, String name, String username, String password, String address, String tel) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.tel = tel;
        setId(id);
    }


    public Subscriber() {}

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "subscriber_id")
    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscriber_id")
    public Set<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Set<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
