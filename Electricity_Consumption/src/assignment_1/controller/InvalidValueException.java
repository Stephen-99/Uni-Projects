package assignment_1.controller;

/*Used by the controller class when the command line arguments provided
are an invalid value*/
public class InvalidValueException extends Exception {
    public InvalidValueException(){
        super();
    }

    public InvalidValueException(String message)
    {
        super(message);
    }

    public InvalidValueException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
