package it.polimi.db2.Entitys;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "services", schema = "test")
public class Service {
    @Id
    @GeneratedValue
    int id;
    int packageId;

    /*@ManyToOne
    @JoinColumn(name="packageId", nullable=false)
    private Package p;*/

    public Service()
    {

    }
}
