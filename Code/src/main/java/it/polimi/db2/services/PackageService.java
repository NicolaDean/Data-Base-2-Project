package it.polimi.db2.services;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.Package;
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


}
