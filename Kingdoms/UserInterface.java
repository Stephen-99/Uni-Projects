/******************************************************************************
* Filename:         UserInterface.java                                        *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:         	handles all user input and output for kingdom             *
* Date created:     05/10/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 18/10/2019 | added banner initialisation for init house/army	  *
*   EDIT 2 | 22/10/2019 | submoduled banner initialisation in init House/Army *
*	EDIT 3 | 24/10/2019 | fixed round module								  *
*	EDIT 4 | 25/10/2019 | changed variable 'valid' to 'invalid' in method 	  *
*						  initBanner for more clarity
******************************************************************************/

import java.util.*;

public class UserInterface
{

	/*********************************************************************
	* SUBMODULE:	inputNum
	* IMPORT:		prompt(String), min(int), max(int)
	* EXPORT:		value(int)
	* ASSERTION:	will ensure user value is an int between min and max
	**********************************************************************/
	public static int inputNum(String prompt, int min, int max)
	{
		Scanner sc = new Scanner(System.in);
		int value;
		String error, outStr;

		value = min - 1;	//to continue loop if no value set
		error = "ERROR: value must be between " + min + " and " + max;
		outStr = prompt;

		do
		{
			try
			{
				System.out.println(outStr);	//ask for value
				value = sc.nextInt();	//read value
				outStr = error + "\n" + prompt;	/*set output to error msg for 
												next iteration*/
			} //END try	

			catch(InputMismatchException e)
			{	//wasn't an integer
				sc.nextLine();	//clear buffer
				outStr = "ERROR: input must be an integer" + "\n" + prompt;
			} //END catch

		}while ((value < min) || (value > max));	/*loop till a valid value 
													provided*/
		return value;
	} //END SUBMODULE inputNum

	/*********************************************************************
	* SUBMODULE:	inputNum
	* IMPORT:		prompt(String), min(double), max(double)
	* EXPORT:		value(double)
	* ASSERTION:	will ensure user value is a real number between min and max
	**********************************************************************/
	public static double inputNum(String prompt, double min, double max)
	{
		Scanner sc = new Scanner(System.in);
		double value;
		String outStr, error;
		
		value = min - 1;	//to continue loop if no value set
		error = "ERROR: value must be between " + min + " and " + max;
		outStr = prompt;

		 do
		{
			try
			{
				System.out.println(outStr);	//ask for value
				value = sc.nextDouble();	//read value
				outStr = error + "\n" + prompt;	/*get output ready for next 
												iteration where value was 
												outside bounds*/
			} //END try

			catch(InputMismatchException e)
			{	//wasn't a real number
				sc.nextLine();	//clear buffer
				outStr = "ERROR: input must be a real number" + "\n" + prompt;
			} //END catch

		}while((value < min) || (value > max));	//loop til valid value given
		//END  do	
		return value;	
	} //END SUBMODULE inputNum

	/*********************************************************************
	* SUBMODULE:	initHouse
	* EXPORT:		newHouse(House)
	* ASSERTION:	Will create a valid house object based on user input.
	**********************************************************************/
	public static House initHouse()
	{
		Scanner sc = new Scanner(System.in);
		House newHouse;

		String inName, inCastle;
		int inYears, inBannermen;

		inName = getString("Please enter a name for the house");
			//getString will loop till name is valid

		inYears = inputNum("Please provide the age of the house",
				House.YEARMIN, House.YEARMAX);
			//inputNum will loop till years are valid

		inBannermen = inputNum("Please provide the number of "
			+ "bannermen serving the house", House.BANMIN, House.BANMAX);
			//inputNum will loop till bannermen are valid
	
		System.out.println("Please enter the name of the castle occupied by "
			+ "this house. If no castle is occupied leave blank");
		inCastle = sc.nextLine(); 
			/*because castle is allowed to be null while getString rejects 
			null, therfore had to do it here
			castle name cannot be invalid
				maybe validate user has right choice...
				 could get annoying tho... */
        
        newHouse  = new House(inName, inYears, inBannermen, inCastle, 
			initBanner());	/*create new House with obtained values. Will not 
							throw an error since values have been validated*/

		return newHouse;
	} //END SUBMODULE initHouse


	/*********************************************************************
	* SUBMODULE:	initArmy
	* EXPORT:		newArmy(Army)
	* ASSERTION:	To create a valid Army opbject based on user input
	**********************************************************************/
	public static Army initArmy()
	{
		Scanner sc = new Scanner(System.in);
		Army newArmy;

		String inName, inHistory;
		int inYears, inBannermen;
		double inArchers, inFootmen;

		inName = getString("Please enter a name for the army");
			//getString ensures name is valid

		inYears = inputNum("Please provide the age of the army",
			Army.YEARMIN, Army.YEARMAX);
			//inputNum ensures years is valid

		inFootmen = inputNum("Please enter the number of groups of footmen "
			+ "serving the army", Army.FOOTMIN, Army.FOOTMAX);
			//inputNum ensures footmen is valid

		inArchers = inputNum("Please enter the number of squads of archers "
			+ "serving the army", Army.ARCHERMIN, Army.ARCHERMAX);
			//inputNum ensures archers is valid
	
		System.out.println("Please enter the history of the army, if there "
			+ "is no history leave blank");
		inHistory = sc.nextLine();
			//History cannot be invalid
       
        newArmy = new Army(inName, inYears, inFootmen, inArchers, inHistory, 
			initBanner()); 
			/*Constructing a new Amry with the values obtained. Will not throw
			 an erro since all values have been validated where necassary*/

		return newArmy;
	} //END SUBMODULE initArmy
	
	/*********************************************************************
	* SUBMODULE:	getString
	* IMPORT:		prompt(String)
	* EXPORT:		userInput(String)
	* ASSERTION:	will obtain a non-null string from the user
	**********************************************************************/
	public static String getString(String prompt)
	{
		Scanner sc = new Scanner(System.in);
		String userInput, outStr;
	
		outStr = prompt;
	
		do
		{
			System.out.println(outStr);	//ask for String
			userInput = sc.nextLine(); 	/*read String, anything can be a string
										so no input mismatch*/
			outStr = "ERROR: please provide text" + "\n" + prompt;
		}while(userInput == null || userInput.equals(""));	/*loop till 
															non-empty values 
															provided*/
		//END  do

		return userInput;
	} //END SUBMODULE getString

	/*********************************************************************
	* SUBMODULE:	round
	* IMPORT:		value(double)
	* EXPORT:		value(double)
	* ASSERTION:	will round the value to 2 decimal places
	**********************************************************************/
	public static double round(double value)
	{
		int intValue;
		intValue = (int)((value + 0.005) * 100);
		//will be truncated, + 0.005 to round, * 100 to save 2 decimal places
		value = intValue / 100.0;	//return to original value
		return value;
		
	} //END SUBMODULE round

	/*********************************************************************
	* SUBMODULE:	showError
	* IMPORT:		error(String)
	* ASSERTION:	will output error
	**********************************************************************/
	public static void showError(String error)
	{
		System.out.println(error);	//print error msg passed
	} //END SUBMODULE showError

	/*********************************************************************
	* SUBMODULE:	initBanner
	* IMPORT:		none
    * EXPORT:       newBan(Banner)
	* ASSERTION:	will construct a new banner based on user input
	**********************************************************************/
    public static Banner initBanner()
    {
        String inSigil, inBanCol, inSigCol;
        boolean invalid;
        Banner newBan = new Banner();
            //making default banner and changing values.
            //allows access to banner validation 

		invalid = true;
		while (invalid)
		{
			try
			{
				inSigil = getString("Please enter a sigil for the banner");
				newBan.setSigil(inSigil);	//validation inside Banner class
				invalid = false;  //will only execute if previous line doesn't 
								//throw an error
			} //END try
			catch (IllegalArgumentException e)
			{	
				showError(e.getMessage());
			} //END catch
		} //END while
					
		invalid = true;
		while (invalid)
		{
			try
			{
				inBanCol = getString("Please enter a banner colour for the "
					+ "banner");
				newBan.setBannerColour(inBanCol);	//validation inside Banner
				invalid = false;  //will only execute if previous line doesn't 
								//throw an error
			} //END try
			catch (IllegalArgumentException e)
			{
				showError(e.getMessage());
			} //END catch
		} //END while

		invalid = true;
		while (invalid)
		{
			try
			{
				inSigCol = getString("Please enter a sigil colour for the "
					+ "banner");
				newBan.setSigilColour(inSigCol);	//validation inside banner
				invalid = false;  //will only execute if previous line doesn't 
								//throw an error
			} //END try
			catch (IllegalArgumentException e)
			{
				showError(e.getMessage());
			} //END catch
		} //END while
        
        return newBan;
    } //END initBanner

} //END CLASS:	UserInterface


