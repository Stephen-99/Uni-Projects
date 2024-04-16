/******************************************************************************
* Filename:         Alliance.java                                             *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Template for Alliance class                               *
* Date created:     23/10/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 25/10/2019 | Updated toFileString to call banner's toFileString. *
*						  Added abstract method obtainType.					  *
*						  Made formating changes
******************************************************************************/

public abstract class Alliance implements Combatable
{

	//CLASS CONSTANTS
	public static final int YEARMIN = 1;
	public static final int YEARMAX = 10000;

	//private CLASS FIELDS:	
	private String name;
	private int years;
	private Banner banner;

	
	//CONSTRUCTORS:
		
		
	/*********************************************************************
 	* Default:
	* IMPORT:		
	* EXPORT:		address of new Alliance object
	* ASSERTION:	The Alliance "Hesketh" is 12 years old. It has a banner 
	*				with sigil: DRAGON, banner colour FF0FF0 and sigil colour 
	*				00F00F. This is the valid default state
	**********************************************************************/
	public Alliance()
	{
		name = "Hesketh";
		years = 12;
		banner = new Banner("DRAGON", "FF0FF0", "00F00F");
	} //END Default CONSTRUCTOR

	/*********************************************************************
	* Alternate1:
	* IMPORT:		inName(String), inYears(int), inSigil(String), 
	*				inBanCol(String), inSigCol(String)
	* EXPORT:		address of new alliance object
	* ASSERTION:	creates new object if imports are valid. 
	*				will fail otherwise
	**********************************************************************/
	public Alliance(String inName, int inYears, String inSigil, 
		String inBanCol,String inSigCol)
	{
		setName(inName);
		setYears(inYears);
		banner = new Banner(inSigil, inBanCol, inSigCol);
	} //END Alternate1 CONSTRUCTOR

	/*********************************************************************
	* Alternate2:
	* IMPORT:		inName(String), inYears(int), inBanner(Banner)
	* EXPORT:		address of new Alliance object
	* ASSERTION:	will construct the object if valid, fails otherwise
	**********************************************************************/
	public Alliance(String inName, int inYears, Banner inBanner)
	{
		setName(inName);
		setYears(inYears);
		setBanner(inBanner);
	} //END Alternate 2 CONSTRUCTOR

	/*********************************************************************
	* Copy:
	* IMPORT:		inAll(Alliance)
	* EXPORT:		address of new Alliance
	* ASSERTION:	will create a new object with identical state to import
	**********************************************************************/
	public Alliance(Alliance inAll)
	{
		name = inAll.getName();
		years = inAll.getYears();
		banner = inAll.getBanner();
	} //END copy CONSTRUCTOR

	
	//ACCESSORS:

	
	public String getName()
	{
		return new String(name);
	}
	
	public int getYears()
	{
		return years;
	}

	public Banner getBanner()
	{	//returning a copy of the banner
		return banner.clone();
	}

	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		inName(String), inYears(Intrger), inBanner(Banner)
	* EXPORT:		equal(Boolean)
	* ASSERTION:	will return true if objects are equivelent
	**********************************************************************/
	public boolean equals(String inName, int inYears, Banner inBanner)
	{
		boolean equal;
		equal = ((name.equals(inName)) && (years == inYears) && 
			(banner.equals(inBanner)));		//checking values are equal
		return equal;
	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		inObj(Object)
	* EXPORT:		equal(Boolean)
	* ASERTION:		Will return true if objects are equivelent 
	**********************************************************************/
	public boolean equals(Object inObj)
	{	
		boolean equal;
		equal = false;
		if (inObj instanceof Alliance) 
		{	
			Alliance inAll = (Alliance)inObj;	//casting to Alliance
			equal = equals(inAll.getName(), inAll.getYears(), 
				inAll.getBanner());		//calling other equals method
		} //END if 
		return equal;
	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE:	toString
	* IMPORT:		
	* EXPORT:		outStr(String)
	**********************************************************************/
	public String toString()
	{	
		/* This method will be called by toString methods of House & 
			Army since a newAlliance.toString() call will access the subclass 
			method. However, still functions as a stand alone method */
		
		// Checking if an 's' is needed
		String outStr, yStr;
		if ( years == 1 )
		{	//No 's'
			yStr = ", ";
		} //END if 
		else
		{ //With 's'
			yStr = "s, ";
		} //END else
		
		outStr = name + " has existed for " + years + " year" + yStr 
			+ banner.toString(); //obtains information about banner classfields
		return outStr;
	} //END SUBMODULE toString

	/*********************************************************************
	* SUBMODULE:	toFileString
	* EXPORT:		outStr(String)
	**********************************************************************/
	public String toFileString()
	{
		// As in toString, method will be called from House/Army
		String outStr;

		outStr = name + "," + years + "," + banner.toFileString();	
			//using banner.toFileString method to obtain appropriate arguments
		return outStr;
	} //END toFileString
	

	//MUTATORS:


	/**********************************************************************
	* SUBMODULE: 	setName													
	* IMPORTS:		inName(String)										
	* ASSERTION: 	sets the name to inName if valid. fails otherise		
	**********************************************************************/
	public void setName(String inName)
	{
		if (validateName(inName))
		{	//Valid; set name
			name = new String(inName);
		} //END if
		else
		{	//Invalid! let calling code know
			throw new IllegalArgumentException("Empty name!");
		} //END else

	} //END SUBMODULE setName

	/**********************************************************************
	* SUBMODULE: 	setYears											
	* IMPORTS:		inYears(Integer)									
	* ASSERTION:	sets the age of the house if valid and fails otherwise
	**********************************************************************/
	public void setYears(int inYears)
	{
		if (validateYears(inYears))
		{	//valid; set years
			years = inYears;
		} //END if
		else
		{	//Invalid! let calling code know
			throw new IllegalArgumentException("Invalid Age of Alliance");
		} //END else
	} //END SUBMODULE setYears
	
	/*********************************************************************
	* SUBMODULE:	setBanner
	* IMPORTS:		inBan(Banner)
	* ASSERTION:	sets the banner to a copy of the banner passed
	**********************************************************************/
	public void setBanner(Banner inBan)
	{	//set to copy of banner passed
		banner = inBan.clone();
	} //END SUBMODULE setBanner


	//ABSTRACT SUBMODULES:

		
	/*********************************************************************
	* SUBMODULE:	population
	* EXPORT:		String
	* ASSERTION:	will return a string with the population of the alliance
	**********************************************************************/
	public abstract String population();
		//will be implemented in sub-classes

	/*********************************************************************
	* SUBMODULE:	clone
	* EXPORT:		newAlliance(Alliance)
	* ASSERTION:	make a copy of the current object
	**********************************************************************/
	public abstract Alliance clone();
		//will be implemented in sub-classes
	
	/*********************************************************************
	* SUBMODULE:	obtainType
	* EXPORT:		type(String)
	* ASSERTION:	will return the type of the subclass
	**********************************************************************/
	public abstract String obtainType();
	

	//PRIVATE SUBMODULES:


	/*********************************************************************
	* SUBMODULE:	validateName											
	* IMPORTS:		inName(String)											
	* EXPORT:		valid(Boolean)											
	* ASSERTION:	name cannot be null										
	**********************************************************************/
	private boolean validateName(String inName)
	{
		return !((inName == null) || (inName.equals("") || 
			(inName.equals(" "))));	//Name cannot be empty
	} //END SUBMODULE validateYears

	/*********************************************************************
	* SUBMODULE:	validateYears											
	* IMPORTS:		inYears(Integer)										
	* EXPORT:		valid(Boolean)											
	* ASSERTION:	years msut be between 1 and 10,000 inclusive			
	**********************************************************************/
	public boolean validateYears(int inYears)
	{	//checking years is within limits
		return (inYears > YEARMIN - 1) && (inYears < YEARMAX + 1);
	} //END SUBMODULE validateYears


} //END ABSTRACT CLASS ALLIANCE











