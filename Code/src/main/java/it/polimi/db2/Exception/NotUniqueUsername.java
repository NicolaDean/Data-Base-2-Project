package it.polimi.db2.Exception;

public class NotUniqueUsername extends Exception{

    public NotUniqueUsername()
    {
        super("Not unique username found in database");
    }
}

