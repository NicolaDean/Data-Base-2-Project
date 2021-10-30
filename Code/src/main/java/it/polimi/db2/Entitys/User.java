package it.polimi.db2.Entitys;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users", schema = "test")
public class User {
    @Id
    @GeneratedValue
    int id;

    String username;
    String password;
    String email;
}
