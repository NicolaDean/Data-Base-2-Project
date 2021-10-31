package it.polimi.db2.Services;

import it.polimi.db2.Entitys.User;
import it.polimi.db2.Exception.AuthenticationFailed;
import it.polimi.db2.Exception.NotUniqueUsername;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserServices extends BasicService{

    public UserServices()
    {

    }

    public User checkAuthentication(String username,String password) throws AuthenticationFailed, NotUniqueUsername {
        List<User> user = null;

        try{
            user = this.em.createNamedQuery("User.authentication",User.class)
                    .setParameter("username",username)
                    .setParameter("password",password)
                    .getResultList();
        }catch (Exception e)
        {
            throw new AuthenticationFailed("Database error occurred during authentication");
        }

        if (user.isEmpty()) return null;

        if(user.size()==1)  return user.get(0);

        if(user.size()>0)   throw new NotUniqueUsername();

            return null;
    }


}
