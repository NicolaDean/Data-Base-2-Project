package it.polimi.db2.entitys;

import javax.persistence.*;

@Entity
@NamedQuery(name="Packages.All",query="select p from Package p")
@Table(name = "Packages", schema = "test")
public class Package {
    @Id
    @GeneratedValue
    int     id;
    String  name;
    //@OneToMany //TODO find a way to do ManyToMany relations
    //List<OptionalProduct> optionalProducts;
    //TODO find a way to do OneToMany relations
    /*@OneToMany(mappedBy = "p",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
    List<Service> services;*/

}
