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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageId")
    Package packageId;
    @Column(name = "DTYPE")
    String type;

    public Service()
    {

    }

    public void setPackageId(Package packageId) {
        this.packageId = packageId;
    }

    public String getType()
    {
        return this.type.toString();
    }
}
