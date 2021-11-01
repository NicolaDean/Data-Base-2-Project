package it.polimi.db2.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class BasicService{

    @PersistenceContext(unitName = "default")
    protected EntityManager em;

    public void BasicService()
    {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("default" );
        this.em = emfactory.createEntityManager( );
    }
}
