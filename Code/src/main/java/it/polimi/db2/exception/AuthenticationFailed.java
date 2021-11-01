package it.polimi.db2.exception;

public class AuthenticationFailed extends Exception{

    public AuthenticationFailed(String msg)
    {
        super(msg);
    }
}
