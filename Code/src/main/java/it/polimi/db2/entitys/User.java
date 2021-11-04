package it.polimi.db2.entitys;


import javax.persistence.*;

@Entity
@NamedQuery(name = "User.authentication", query = "select usr from User usr WHERE usr.username = :username and usr.password = :password")
@Table(name="Users", schema = "test")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;

    String username;
    String password;
    String email;

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
