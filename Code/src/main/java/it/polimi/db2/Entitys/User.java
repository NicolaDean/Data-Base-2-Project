package it.polimi.db2.Entitys;


import javax.persistence.*;

@Entity
@NamedQuery(name = "User.authentication", query = "select usr from User usr WHERE usr.username = :username and usr.password = :password")
@Table(name="Users", schema = "test")
public class User {
    @Id
    @GeneratedValue
    int id;

    String username;
    String password;
    String email;

    public String getUsername() {
        return this.username;
    }
}
