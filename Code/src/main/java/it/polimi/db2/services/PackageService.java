package it.polimi.db2.services;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.entitys.RateCost;
import it.polimi.db2.entitys.Service;
import it.polimi.db2.exception.NoPackageFound;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PackageService extends BasicService{

    /**
     * @return a list of all packages available
     */
    public List<Package> getAllPackages()
    {
        return this.em.createNamedQuery("Packages.All",Package.class).getResultList();
    }

    /**
     * @param id id of the package to search
     * @return a package with id
     */
    public Package getPackageById(int id) throws NoPackageFound {
        Package p = this.em.find(Package.class,id);

        if(p == null) throw new NoPackageFound();

        return p;
    }

    public void persistPackage(String name, List<OptionalProduct> optionals , List<RateCost> rates, List<Service> services)
    {
        Package p = new Package();
        p.setName(name);
        p.setProducts(optionals);
        this.em.persist(p);
        this.em.flush();
        for(int i=0;i<services.size();i++){
            services.get(i).setPackageId(p.getId());
            this.em.persist(services.get(i));
        }
        for(int i=0;i<rates.size();i++){
            rates.get(i).setPackageId(p.getId());
            this.em.persist(rates.get(i));
        }
        p.setServices(services);
        p.setRates(rates);
        this.em.flush();
        //this.em.persist(p);
    }


}
