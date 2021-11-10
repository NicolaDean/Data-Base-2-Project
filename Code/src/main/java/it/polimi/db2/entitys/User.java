package it.polimi.db2.entitys;


import javax.persistence.*;

@Entity
@NamedQuery(name = "User.authentication", query = "select usr from User usr WHERE usr.username = :username and usr.password = :password")
@NamedQuery(name = "User.insolvent"     , query = "select usr from User usr WHERE usr.insolvent = true")
@Table(name="Users", schema = "test")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;

    String  username;
    String  password;
    String  email;
    String  type;
    boolean insolvent;

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

    public int getId() {
        return this.id;
    }

    public String getType() {
        return type;
    }

    public boolean getInsolvent()
    {
        return this.insolvent;
    }

    public boolean isInsolvent() {
        return this.insolvent;
    }

    public void setInsolvent(boolean b) {
        this.insolvent = true;
    }

    public String getEmail() {
        return email;
    }
}
