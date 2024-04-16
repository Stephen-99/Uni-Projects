/******************************************************************************
* Filename:         house.java                                                *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Template for House object                                 *
* Date created:     20/09/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
*	  EDIT 1 | 28/09/2019 | added constants and validate name submodule
*     EDIT 2 | 18/10/2019 | implemented banner as a classfield. Every method  *
*							has been updated accordingly.					  *
*							population and battleStrength methods have been	  *
*							added. validateName updated to check for 		  *
*							additional empty conditions. toString and 		  *
*							toFileString have also been updated in this 	  *
*							manner						  					  *
*	  EDIT 3 | 23/10/2019 | updated house to extend Alliance. This means only *
*							castle and bannermen are classfields, with the 	  *
*							others taken care of in Alliance. All methods 	  *
*							have been appropiately updated or removed to 	  *
*							accomadate for this change. Population has also   *
*							been updated to return a string as specified in   *
*							worksheet 10.	  								  *
*	  EDIT 4 | 25/10/2019 |	added submodule obtainType						  *
******************************************************************************/

public class House extends Alliance
{

    public static final int BANMIN = 25;
    public static final int BANMAX = 80000;
     
	//private class fields
	private String castle;
	private int bannermen;

	//CONSTRUCTORS:

	/*********************************************************************
	* Default:    												
	* IMPORT:     	none													
	* EXPORT:     	address of new house object								
	* ASSERTION:  	100 bannermen and a castle named: "Fortress of Abadon"	
	*				are valid values for the default state				 	
	**********************************************************************/
	public House()
	{
		super();
		bannermen = 100;
		castle = "Fortress of Abadon";
	} //END default CONSTRUCTOR    

	
	/*********************************************************************
	* Alternate1:    											
	* IMPORT:     inName(String), inYears(Integer), inBannermen(Integer), 	
	* 			  inCastle(String), inSigil(String), inBanCol(String), 		
	*			  inSigCol(String											
	* EXPORT:     address of new house object								
	* ASSERION:   creates the object if imports are valid, FAILS otherwise	
	**********************************************************************/
	public House(String inName, int inYears, int inBannermen, String inCastle, 
		String inSigil, String inBanCol, String inSigCol)
	{
		super(inName, inYears, inSigil, inBanCol, inSigCol);
		//setters make set classfields to the given values
		setBannermen(inBannermen);
		setCastle(inCastle);

	} //END Alternate1 CONSTRUCTOR


	/*********************************************************************
	* Alternate2:    											
	* IMPORT:     inName(String), inYears(Integer), inBannermen(Integer),	
	*			  inCastle(String), inBanner(Banner)						
	* EXPORT:     address of new house object								
	* ASSERION:   creates the object if imports are valid, FAILS otherwise	
	**********************************************************************/
	public House(String inName, int inYears, int inBannermen, String inCastle,
		Banner inBanner)
	{
		super(inName, inYears, inBanner);
		setBannermen(inBannermen);
		setCastle(inCastle);
		
	} //END Alternate2 CONSTRUCTOR

	/************************************************************************
	* copy:    													
	* IMPORT:		inHouse (House)											
	* EXPORT:		address of new house object								
	* ASSERTION:	Creates an object with identical object state as the 	
	*				import													
	************************************************************************/
	public House(House inHouse)
	{
		super(inHouse);		//copies name, years and banner
		bannermen = inHouse.getBannermen();
		castle = inHouse.getCastle();

	} //END copy CONSTRUCTOR


	//ACCESSORS:


	/*********************************************************************
	* clone:     															
	* IMPORT		none													
	* EXPORT		newHouse(House)								
	* ASSERTION:	will make a copy of the current object					
	**********************************************************************/
	@Override
	public House clone()
	{
		return new House(this);		//calls copy constructor

	} //END clone ACESSOR


	public int getBannermen()
	{
		return bannermen;
	}
	
	public String getCastle()
	{
		return new String(castle);	//creates a copy of the classfield
	}


	/*********************************************************************
	* SUBMODULE: 	equals													
	* IMPORT:		other(Object)											
	* EXPORT:		equal(Boolean)											
	* ASERTION:		Will return true if objects are equivelent and false 	
	*				otherwise												
	**********************************************************************/
	@Override
	public boolean equals(Object other)
	{
		boolean equal = false;

		if (other instanceof House) 
		{
			House inHouse = (House)other;	//casting to House
			equal = equals(inHouse.getName(), inHouse.getYears(), 
				inHouse.getBannermen(), inHouse.getCastle(), 
				inHouse.getBanner());	//callling other equals method
		} //END if
		return equal;
	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE: 	equals													
	* IMPORT:		inName(String), inYears(Integer), inBannermen(Integer), 
	*				inCastle(String), inBanner(Banner)						
	* EXPORT:		equal(Boolean)											
	* ASERTION:		Will return true if objects are equivelent and false 	
					otherwise												
	**********************************************************************/
	public boolean equals(String inName, int inYears, int inBannermen, 
		String inCastle, Banner inBanner)
	{
		boolean equal;
		equal = super.equals(inName, inYears, inBanner) && 
			(bannermen ==inBannermen) && (castle.equals(inCastle));
			//checking all classfields are equal
		return equal;
	} //END SUBMODULE equals



	/*********************************************************************
	* SUBMODULE:	toString												
	* EXPORT:		outStr(String)											
	**********************************************************************/
	@Override
	public String toString()
	{
		String outStr;
		if ((castle.equals("")) || (castle.equals(" ")) || (castle == null))
		{	//If castle is empty don't print it
			outStr = "The House of " + super.toString()  + " This House has " 
				+ bannermen + " bannermen."; 	/*super.toString returns a 
												string containing information 
												about the super classfields*/
		} //END if
		else
		{	//print with castle
			outStr = "The House of " + super.toString() + " This House has " 
				+ bannermen + " bannermen" + " and a castle named: " + castle 
				+ ".";
		} //END else
		return outStr;
	} //END SUBMODULE toString

	/*********************************************************************
	* SUBMODULE:	toFileString											
	* EXPORT:		outStr(String)											
	**********************************************************************/
	@Override
	public String toFileString()
	{
		String outStr;
		if ((castle.equals("")) || (castle == null))
		{
			//If empty castle, returning a space instead so file can be read
			outStr = "H" + "," + super.toFileString() + "," + bannermen + "," 
				+ " ";
		} //END if
		else
		{	//non-void castle
			outStr = "H" + "," + super.toFileString() + "," + bannermen + "," 
				+ castle;
		} //END else
		return outStr;
	} //END SUBMODULE toFileString

	/*********************************************************************
	* SUBMODULE:	population
	* EXPORT:		pop(String)
	* ASSERTION:	Will return a string with the houses population
	**********************************************************************/
	@Override
	public String population()
	{
		int popI;
		String pop;
		//calculating population
		if ((castle == null) || (castle.equals("")) || (castle.equals(" ")))
		{	//No castle
			popI = bannermen;
		} //END if
		else
		{	//With castle; adds population
			popI = bannermen + 450;
		} //END else
		//creating population string
		pop = "The House of " + super.getName() + " has a population of " 
			+ popI + ".";
		return pop;	
	} //END SUBMODULE population

	/*********************************************************************
	* SUBMODULE:	battleStrength
	* EXPORT:		strength(Integer)	
	**********************************************************************/
	@Override
	public int battleStrength()
	{
		int strength;
		if ((castle == null) || (castle.equals("")) || (castle.equals(" ")))
		{ 	//No castle
			strength = bannermen * 2;
		} //END if
		else
		{	//with castle; adds strength
			strength = bannermen * 2 + 20000;
		} //END else
		return strength;
	} //END SUBMODULE battleStrength

	/*********************************************************************
	* SUBMODULE:	obtainType
	* EXPORT:		type(String)
	* ASSERTION:	will return a string indicate class type
	**********************************************************************/
	public String obtainType()
	{
		return "House";
	} //END obtainType	


	//MUTATORS:


	/*********************************************************************
	* SUBMODULE:	setBannermen											
	* IMPORTS:		inBannermen(Integer)									
	* ASSERTION:	sets the number of bannermen if valid, fails otherwise	
	**********************************************************************/
	public void setBannermen(int inBannermen)
	{
		if (validateBannermen(inBannermen))
		{	//If within limits set bannermen
			bannermen = inBannermen;
		} //END if
		else
		{	//not within limits, let calling code know
			throw new IllegalArgumentException("Invlaid number of Bannermen");
		} //END else
	} //END SUBMODULE setBannermen

	/*********************************************************************
	* SUBMODULE:	setCastle												
	* IMPORTS:		inCastle(String)										
	* ASSERTION:	sets castle to inCastle									
	************************************************************************/
	public void setCastle(String inCastle)
	{
		castle = new String(inCastle);
	} //END SUBMODULE setCastle


	//PRIVATE SUBMODULES:


	/*********************************************************************
	* SUBMODULE:	validateBannermen										
	* IMPORTS:		inBannermen(Integer)									
	* EXPORT:		valid(Boolean)											
	* ASSERTION:	bannermen msut be between 25 and 80,000 inclusive		
	**********************************************************************/
	public boolean validateBannermen(int inBannermen)
	{	//check bannermen within limits
		return (inBannermen > BANMIN - 1) && (inBannermen < BANMAX + 1);
	} //END SUBMODULE validateBannermen

} //END CLASS: House


