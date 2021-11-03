package it.polimi.db2.services;

import it.polimi.db2.entitys.Package;
import it.polimi.db2.entitys.ServiceTypes.MobilePhoneServices;
import it.polimi.db2.entitys.User;
import it.polimi.db2.exception.AuthenticationFailed;
import it.polimi.db2.exception.NotUniqueUsername;
import it.polimi.db2.exception.RegistrationFailed;
import it.polimi.db2.exception.WrongCredential;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserServices extends BasicService{

    public UserServices()
    {

    }

    /**
     * Check if user credential for authentication are correct
     * @param username username inserted by user forms
     * @param password password inserted by user forms
     * @return user match (if exist) -> null if none found
     * @throws AuthenticationFailed some SQL error occurred
     * @throws NotUniqueUsername    Database integrity compromised, not unique username found
     */
    public User checkAuthentication(String username,String password) throws AuthenticationFailed, NotUniqueUsername, WrongCredential {
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

        if (user.isEmpty()) {
            throw new WrongCredential();
        }

        if(user.size()==1)  return user.get(0);

        if(user.size()>0)   throw new NotUniqueUsername();

            return null;
    }

    public void createUser(String username,String password,String email) throws RegistrationFailed {
        User tmp = new User();
        tmp.setUsername(username);
        tmp.setEmail(email);
        tmp.setPassword(password);

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            throw new RegistrationFailed();
        }

        this.em.persist(tmp);
    }


}
