package assignment_1.view;

/*For displaying errors and some formatting sicne there is no 
real interaction with the user*/
public class UserInterface {
    
    public static void displayError(String message)
    {
        System.err.println("\nError: " + message + "\n");
    }

    //Rounds the number to the specified number of decimal places
    public static double round(double num, int dp)
    {
        num *= Math.pow(10, dp);
        num = Math.round(num);
        num /= Math.pow(10, dp);
        return num;
    }
}
