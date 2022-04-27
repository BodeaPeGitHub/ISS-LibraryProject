package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "librarians")
public class Librarian extends Entity {

    private String password;
    private String username;

    public Librarian() {
        username = "";
        password = "";
    }

    public Librarian(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Librarian(Integer id, String username, String password) {
        this.password = password;
        this.username = username;
        setId(id);
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "librarian_id")
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
