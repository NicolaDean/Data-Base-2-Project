package it.polimi.db2.entitys;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "services", schema = "test")
@DiscriminatorValue(value = "FPS")
public class Service {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    int packageId;
    @Column(name = "DTYPE")
    String type;

    /*@ManyToOne
    @JoinColumn(name="packageId", nullable=false)
    private Package p;*/

    public Service()
    {

    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getType()
    {
        return this.type.toString();
    }
}
