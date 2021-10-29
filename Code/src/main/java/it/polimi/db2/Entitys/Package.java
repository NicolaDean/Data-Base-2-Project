package it.polimi.db2.Entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Packages", schema = "test")
public class Package {
    @Id
    @GeneratedValue
    int     id;
    String  name;

}
