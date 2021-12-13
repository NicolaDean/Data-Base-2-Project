package it.polimi.db2.entitys;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name="Packages.All",query="select p from Package p")
@Table(name = "Packages", schema = "test")
public class Package {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int     id;
    String  name;


    @OneToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "packageId")
    List<Service> services;

    @OneToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "packageId")
    List<RateCost> rates;

    @JoinTable(
            name = "Packages_OptionalProducts",
            schema = "test",
            joinColumns = @JoinColumn(name = "packageId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    @ManyToMany(cascade=CascadeType.PERSIST)
    List<OptionalProduct> products;

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public List<OptionalProduct> getOptionalProducts()
    {
        return this.products;
    }

    public List<RateCost> getRates()
    {
        return this.rates;
    }

    public List<Service> getServices()
    {
        return this.services;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRates(List<RateCost> newrates) {

        for(int i=0;i<newrates.size();i++) newrates.get(i).setPackageId(this);
        this.rates = newrates;
    }

    public void setProducts(List<OptionalProduct> products) {
        this.products = products;
    }

    public void setServices(List<Service> services) {
        for(int i=0;i<services.size();i++)services.get(i).setPackageId(this);
        this.services = services;
    }
}
