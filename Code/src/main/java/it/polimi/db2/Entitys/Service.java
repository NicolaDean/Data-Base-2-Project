package it.polimi.db2.Entitys;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "Services", schema = "test")
public class Service {
    @Id
    @GeneratedValue
    int id;
}
