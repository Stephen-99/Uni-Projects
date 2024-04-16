package assignment_1.model;

//Used by ReadNetwork, when the file format is not as expected
public class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(){
        super();
    }

    public InvalidFileFormatException(String message)
    {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
