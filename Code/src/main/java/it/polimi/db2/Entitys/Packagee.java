package it.polimi.db2.Entitys;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Packages", schema = "test")
public class Packagee {
    @Id
    @GeneratedValue
    int     id;
    String  name;
    //@OneToMany
    //List<OptionalProduct> optionalProducts;

    /*@OneToMany(mappedBy = "p",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
    List<Service> services;*/

}
