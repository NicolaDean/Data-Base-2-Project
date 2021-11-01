package it.polimi.db2.exception;

public class NotUniqueUsername extends Exception{

    public NotUniqueUsername()
    {
        super("Not unique username found in database");
    }
}

