package it.polimi.db2.services;

import it.polimi.db2.entitys.Package;

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
     * //TODO ADD ALL POSSIBLE QUERIES FOR PACKAGE
     */
}
