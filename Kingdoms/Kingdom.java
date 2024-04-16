/******************************************************************************
* Filename:         Kingdom.java                                              *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Kindgom class, responsible for managing alliance          *
* Date created:     27/09/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 18/10/2019 | updated houses and allies getters to return a copy  *
*						  of the arrays. implemented allianceBattleStrength   *
*                         and kingdomBattleStrength. updated find duplicates  *
*                         to remove double ups. Updated equals methods into   *
*                         one general method to check if arrays are equal.    *
*						  Multiple instances where return statemnet was blank *
*						  instead return "no alliances implemented" or        *
*                         similar. Implemented view alliance population       *
*	EDIT 2 | 23/10/2019 | updated to use alliance arrays instead of seperate  *
*						  house and army arrays.This involved updating almost *
*						  all methods.										  *
*						  implemented updateAlliance fully					  *
*						  added a class constant for array size				  *
*   EDIT 3 | 25/10/2019 | implemented combvatable. kingdomBattleStrength now  *
*                         calls battleStrength. Also updated findDuplicates   *
*						  so not checking if alliance is Hosue or Army	 	  *
******************************************************************************/

public class Kingdom implements Combatable
{
	//class constants
	public static int MAXARRAY = 30;
	
	//private class fields 
		private Alliance[] allies;
		private int alCount;
   

//	CONSTRUCTORS:
	
 
	/*********************************************************************
    * default:
    * IMPORT: none
    * ASSERTION: will construct the array and initialise count var to 0
    **********************************************************************/
	public Kingdom()
  	{
        allies = new Alliance[MAXARRAY];
        alCount = 0;
    }  //END default CONSTRUCTOR

	/*********************************************************************
	* Copy
	* IMPORT:		inKing(Kingdom)
	* EXPORT:		address of new Kingdom object
	* ASSERTION:	Creates an identical object as imported
	**********************************************************************/
	public Kingdom(Kingdom inKing)
	{
		allies = inKing.getAllies();
		alCount = inKing.getAlCount();
	}  //END copy CONSTRUCTOR


//	ACCESSORS:


	/*********************************************************************
	* clone
	* IMPORT:		
	* EXPORT:		newKingdom(Kingdom)
	* ASSERTION:	will make a copy of the current object
	**********************************************************************/
	public Kingdom clone()
	{
		return new Kingdom(this);
	}  //END clone

	public Alliance[] getAllies()
	{	//return copy of array
		Alliance[] newAllies = new Alliance[MAXARRAY];
		for (int ii = 0; ii < alCount; ii++)
		{
			newAllies[ii] = allies[ii].clone();	//copy allies item to newAllies
		} //END for
		return newAllies;
	}
	
	public int getAlCount()
	{
		return alCount;
	}
	

//	PUBLIC SUBMODULES


    /*********************************************************************
	* SUBMODULE:  addAlliance
    * EXPORT:     statement(String)
    * ASSERTION:  Will create either an object of either house or alliance
    **********************************************************************/
	public String addAlliance()
	{
		int type;
		Alliance newAlliance;
		String statement;
		
		type = UserInterface.inputNum("Please enter the integer "
			+ "that corresponds with your choice" + "\n" 
			+ " 1. Add a new house to the kingdom" + "\n" 
			+ " 2. Add a new alliance to the kingdom", 1, 2);
			//determining if user wants to add a House or an Army
		try
		{
			if (type == 1 )
			{	//Wants a House
				newAlliance = UserInterface.initHouse();	//set up House
				addAlliance(newAlliance);	//add House to array
			}  //END if
			else
			{			// type will be 2, Army; handled in UserInterface
				newAlliance = UserInterface.initArmy();		//set up Army
				addAlliance(newAlliance);	//add Army to array
			}  //END else
			statement = "A new Alliance has been added to the kingdom";
				//if not added, will throw an error and not execute this line
		}  //END try

		catch (IllegalArgumentException e)
		{
			UserInterface.showError(e.getMessage());
			statement = "Could not add Alliance";
		}  //END catch

		return statement;
	}  //END SUBMODULE addAlliance

	/*********************************************************************
	* SUBMODULE:	addAlliance
	* IMPORT:		inAlliance(Alliance)
	* ASSERTION:	if inAlliance is not empty, and there is space in the 
	*				array it will be added to the array
	**********************************************************************/
	public void addAlliance(Alliance inAlliance)
	{
		if (inAlliance != null) 
		{	//not an empty alliance
			if (alCount < MAXARRAY)
			{	//Room in the array
				allies[alCount] = inAlliance;	//Add to array
				alCount = alCount + 1;	//Increase array count
			}  //END if
			else
			{	//no room in array; let calling code know
				throw new IllegalArgumentException("The maximum number of "
					+ "Alliances has been reached.");
			}  //END else
		}  //END if
		else
		{	//Empty alliance; let calling code know
			throw new IllegalArgumentException("The alliance provided is "
				+ "empty.");
		}  //END else
	}  //END SUBMODULE addAlliance
    
	/*********************************************************************
	* SUBMODULE: 	allianceBattleStrength
	* EXPORT:		statement(String)
	* ASSERTION:	find the alliance with the greatest battle strength 
	*				and return return that strength
	**********************************************************************/
	public String allianceBattleStrength()
	{
		
		int batStr, tempStr, count;
		String statement;

		count = 0;
		batStr = 0;

		for (int ii = 0; ii < alCount; ii++)
		{ 	//calculating strength for each item in allies
			tempStr = allies[ii].battleStrength();

			if (tempStr > batStr) 
			{	//current strength is greater than previous greatest strength
				count = ii + 1;	
					//+1 to distinguish 0th alliance strongest from an 
																//empty array 
				batStr = tempStr;
			}  //END if
		}  //END for
			

		if (count == 0)
		{	/*no alliances present, count not iterated since any alliance 
			strength will be greater than initial batStr of 0*/
			statement = "No alliances present";
		} //END if
		else
		{ 
			statement = "The strongest alliance:\n" 
				+ allies[count-1].toString() + "\nIt has a strength of: " 
				+ batStr;	//count -1 to correct +1 before
		}

		return statement;
	}  //END SUBMODULE allianceBattleStrength

	/*********************************************************************
	* SUBMODULE:	kingdomBattleStrength
	* EXPORT:		statement(String)
	* ASSERTION:	calculate and return the total strength of the kingdom
	**********************************************************************/
	public String kingdomBattleStrength()
	{
		String statement;		
		int kingStr = battleStrength();
		statement = "The strength of the kingdom is: " + kingStr;
		return statement;
	}  //END SUBMODULE kingdomBattleStrength

    

	/*********************************************************************
    * SUBMODULE:    battleStrength
    * EXPORT:       strength(Integer)
    * ASSERTION:    returns strength of kingdom
	**********************************************************************/
    public int battleStrength()
    {
		int kingStr, tempStr;

		kingStr = 0;
		for (int ii = 0; ii < alCount; ii++)
		{
			tempStr = allies[ii].battleStrength();	/*calculating current 
													alliance strength*/
			kingStr = kingStr + tempStr;	//updating kingdom strength
		}  //END for
        return kingStr;
    } //END battleStrength
    
	/*********************************************************************
	* SUBMODULE:	findDuplicates
	* EXPORT:		statement(String)
	* ASSERTION:	find any duplicate allies and houses
	**********************************************************************/
	public String findDuplicates()
	{
		boolean equal;
		String statement = "";

		for (int ii = 0; ii < alCount; ii++)
		{	//looping through allies
			for	(int jj = ii + 1; jj < alCount; jj++)
			{	/*jj start value ensures jj and ii are never the same, 
				and don;'t double up e.g. (1 v 2 and 2 v 1)*/
				equal = allies[ii].equals(allies[jj]);
					//testing alliance in allies against each other
				if (equal) 
				{
					statement = statement + allies[ii].obtainType() /* returns 
																  "House" or 
																  "Army"*/
						+ (ii + 1) + " and "+ (jj + 1) + " are the same" 
						+ "\n";
						// adding 1 to ii and jj so no 0th alliance
				}  //END if
			}  //END for
			
		}  //END for
		if (statement.equals(""))
		{	//Statement unchanged; No alliances in array 
			statement = "No Alliances are duplicated";
		}
		return statement;
	}  //END SUBMODULE findDuplicates

	/*********************************************************************
	* SUBMODULE:	compareKingdoms
	* EXPORT:		statement(String)
	* ASSERTION:	constructs a new kingdom from the file manager and compares
	*				it to the current kingdom
	**********************************************************************/
	public String compareKingdoms()
	{
		boolean read, equal;
		Kingdom newKingdom;
		String statement;

		newKingdom = new Kingdom();
			/*passing new Kingdom to FileManager to add alliances to it from
			file*/
		read = FileManager.input(newKingdom);
		
		if (read)
		{	//File read	
			if (alCount > 0)
			{	//Alliances added from file
				statement = "The Kingdoms are not the same"; /*setting default
															 to not the same*/
				equal = equals(allies, alCount, newKingdom.getAllies(),
					 newKingdom.getAlCount());	//testing if kingdoms are equa

				if (equal) 
				{	//They are equal!
					statement = "The kingdoms are the same";
				}  //END if
			} //END if	
			else
			{	//No valide alliances stored
				statement = "No alliances could succesfully be loaded";
			} //END else
		}  //END if
		else
		{	//file couldn't be read
			statement = "The file for the kingdom could not be read";
		}  //END else
		return statement;
	}  //END SUBMODULE compareKingdoms	

	/*********************************************************************
	* SUBMODULE:	viewAlliance
	* EXPORT:		statement(String)
	* ASSERTION:	will return a string with information from all alliances.
	**********************************************************************/
	public String viewAlliance()
	{
		String statement = "";
		
		for (int ii = 0; ii < alCount; ii++) 
		{	//looping through allies
			statement = statement + allies[ii].toString() + "\n\n";
				//adding each alliance string to statement
		}  //END for
		
		if (statement.equals(""))
		{	//statement unchanged, no alliances in kingdom
			statement = "No alliances currently in kingdom";
		}
	
		return statement;
	}  //END SUBMODULE viewAlliance

	/*********************************************************************
	* SUBMODULE:	viewAlliancePopulation
	* EXPORT:		statment(String)
	* ASSERTION:	will return a string with the population of all alliances
	**********************************************************************/
	public String viewAlliancePopulation()
	{
		String statement = "";

		for (int ii = 0; ii < alCount; ii++)
		{ 	//looping through allies
			statement = statement + allies[ii].population() + "\n";
				//adding alliance population strings to satement
		} //END for
		if (statement.equals(""))
		{	//statement unchanged, no alliances present
			statement = "No alliances currently in kingdom";
		} //END if
		return statement;
	}  //END SUBMODULE viewAlliancePopulation

	/*********************************************************************
	* SUBMODULE:	updateAlliance
	* EXPORT:		statement(String)
	* ASSERTION:	Will update the banner of alliance if input is valid
	**********************************************************************/
	public String updateAlliance()
	{
		String statement, inName, tempName;
		int count;

		inName = UserInterface.getString("Please enter the name "
			+ "of the alliance you wish to update."); 	/*Obtaining name of 
														alliance to update*/
		inName = inName.toUpperCase();	/*converting to uppercase for
										comparison*/			
		tempName = "";
		count = 0;
			
		while (!(inName.equals(tempName)) && (count < alCount))
		{	//looping through allies till name is found or end of array reached
			tempName = allies[count].getName();	//obtaining current name
			tempName = tempName.toUpperCase();	//converting for comparison
			count = count + 1;	
		} //END while
		if (count == 0)
		{	/*loop hasn't run any times i.e. alCount = 0, since name 
			cannot be empty. Therefore allies is empty*/
			statement = "Alliance not found";
		} //END if
		else if (inName.equals(tempName))
		{ 	//Found alliance!
			allies[count - 1].setBanner(UserInterface.initBanner());
				//update banner
			statement = "Banner successfully updated";
		}  //END if
		else
		{
			statement = "Alliance not found";
		}  //END else
		return statement;
	}  //END SUBMODULE updateAlliance

	/*********************************************************************
	* SUBMODULE: 	loadAlliance
	* EXPORT:		statement(String)
	* ASSERTION:	Will load a Kingdom from file
	**********************************************************************/
	public String loadAlliance()
	{
		boolean read;
		String statement;

		read = FileManager.input(this);		/*loading alliances to current 
											kingdom from file*/
		if (read) 
		{	
			if (alCount > 0)
			{	//At least one alliance has been loaded
				statement = "Alliances have successfully been loaded";
			} //END if
			else
			{	//no alliances loaded
				statement = "No alliances could succesfully be loaded";
			} //END else
		}  //END if
		else
		{	//not successfully read
			statement = "The Kingdom could not be loaded";
		}  //END else

			/* The input method will ask userinterface for filename, 
				than read file, throwing errors if incorrect. If correctly 
				formatted, will call kingdom to add alliances to the array...*/
		return statement;
	}  //END SUBMODULE loadAlliance		

	/*********************************************************************
	* SUBMODULE:	saveAlliance
	* EXPORT:		statement(String)
	* ASSERTION:	will save the current kingdom to file.
	**********************************************************************/
	public String saveAlliance()
	{
		FileManager.output(this);	
			/* will write current kingdom to csv file. (by format not 
			necassarily file extension)*/
		return "Kingdom has successfully been saved";
	
	}  //END SUBMODULE saveAlliance
	
	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		arr1(ARRAY OF Object), arr1Count(int), 
	*				arr2(ARRAY OF Object), arr2Count(int)
	* EXPORT:		equal(Boolean)
	* ASSERTION:	will compare houses to that of current kingdom
	**********************************************************************/
	public boolean equals(Object[] arr1, int arr1Count, Object[] arr2, 
		int arr2Count)
	{
		boolean equal;
		int maxCount, count;
	
		if (arr1Count == arr2Count)
		{	//same number of objects
			if (arr1Count == 0)
			{	//both must be 0
				equal = true;  	//both are empty
			} //END if
			else
			{	//both have at least one object
				maxCount = arr1Count;
				count = 0;
				do
				{
					equal = arr1[count].equals(arr2[count]);/*check if objects 
															are equal*/
					count = count + 1;
				}while((equal) && (count < maxCount));	
					//loop until not equal or end of array reached
			} //END else
		}  //END if
		else
		{	//not same length, can not be equal
			equal = false;
		}  //END else
			
		return equal;
	}  //END SUBMODULE equals


} //END CLASS Kingdom
