package it.polimi.db2.exception;

public class WrongCredential extends  Exception{

    public WrongCredential()
    {
        super("Username or password wrong, user used wrong credential");
    }

}
