/******************************************************************************
* Filename:         KingdomManager.txt                                        *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Template for starting class for Kingdom program           *
* Date created:     10/10/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
*	EDIT 1 | 25/10/2019 | Removed stack trace so catch provides suitable 	  *
*						  outputfor the user to read.						  *
******************************************************************************/

public class KingdomManager
{

	public static void main(String[] args)
	{
		Kingdom kingdom;
		try
		{
			kingdom = new Kingdom();	//make a new kingdom
			Menu.menu(kingdom);		//pass it to menu, will loop till did
		} //END try
		catch(Exception e)
		{	//unexpected error!
			UserInterface.showError("Something went wrong. Program will now "
				+ "exit." + "\n" + "Goodbye");	//let user know program crashed
		} //END catch
	} //END MAIN
} //END CLASS

		

