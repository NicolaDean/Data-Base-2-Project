package it.polimi.db2.Entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Services", schema = "test")
public class Service {
    @Id
    @GeneratedValue
    int id;

}
