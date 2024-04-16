/******************************************************************************
* Filename:         Army.java                                                 *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Template for Army object                                  *
* Date created:     26/09/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 28/09/2019 | Added constants for year and validation for history *
*						  using a generic submodule for validating strings	  *
*	EDIT 2 | 18/10/2019 | implemented banner as a classfield. Every method 	  *
*						  has been updated accordingly.						  *
*						  population and battleStrength methods have been	  *
*						  implemented. updated validateString to check for 	  *
*						  additionaly empty conditions. toString and 		  *
*	EDIT 3 | 23/10/2019 | updated Army to inherit from Alliance. As a result, *
*						  only footmen, archers and history are classfields.  *
*						  This resulted in most method being updated and some *
*						  being deleted. additionally, population has been 	  *
*						  update to return a string as per worksheet 10		  *
*	EDIT 4 | 24/10/2019 | updated toString to round real values using 		  *
*						  UserInterface  									  *
*	EDIT 5 | 25/10/2019 | added method obtainType							  *
******************************************************************************/

public class Army extends Alliance
{
	//Class Constants
	public static final double TOL = 0.001;
    public static final double FOOTMIN = 3.5;
    public static final double FOOTMAX = 160.5;
    public static final double ARCHERMIN = 1.5;
    public static final double ARCHERMAX = 112.5;
 		
	//private class fields
	private String history;
	private double footmen, archers;


	//CONSTRUCTORS:


	/*********************************************************************
	* Default:
	* IMPORT:     	none
	* EXPORT:     	address of new army object
	* ASSERTION:  	10 groups of footmen and 3.4 groups of archers with a 
	*		    	history of: "The oldest army". 
	*				These are valid default values
	**********************************************************************/
	public Army()
	{
		super();	//initiates default values for super classfields
		footmen = 10;
		archers = 3.4;
		history = "The oldest army";
	} //END default CONSTRUCTOR    

	/*********************************************************************
	* Alternate1:
	* IMPORT:     	inName(String), inYears(Integer), inFootmen(Real),
	* 				inArchers(Real), inHistory(String), inSigil(String), 
	* 				inBanCol(String), inSigCol(String)
	* EXPORT:     	address of new army object
	* ASSERION:   	creates the object if imports are valid, FAILS otherwise
	**********************************************************************/
	public Army(String inName, int inYears, double inFootmen, double inArchers,
		String inHistory, String inSigil, String inBanCol, String inSigCol)
	{
		super(inName, inYears, inSigil, inBanCol, inSigCol); 
			//will set classfields in super
		setFootmen(inFootmen);
		setArchers(inArchers);
		setHistory(inHistory);
	
	} //END Alternate1 CONSTRUCTOR


	/*********************************************************************
	* Alternate2:
	* IMPORT:     	inName(String), inYears(Integer), inFootmen(Real), 
	*				inArchers(Real), inHistory(String), inBanner(Banner)
	* EXPORT:     	address of new army object
	* ASSERION:   	creates the object if imports are valid, FAILS otherwise
	**********************************************************************/
	public Army(String inName, int inYears, double inFootmen, double inArchers,
		String inHistory, Banner inBanner)
	{
		super(inName, inYears, inBanner); 	//will set the classfields in super
		setFootmen(inFootmen);
		setArchers(inArchers);
		setHistory(inHistory);
	} //END Alternate2 CONSTRUCTOR

	/*********************************************************************
	* Copy:
	* IMPORT:		inArmy (ArmyClass)
	* EXPORT:		address of new army object
	* ASSERTION:	Creates an object with identical object state as the import
	* ALGORITHM:
	**********************************************************************/
	public Army(Army inArmy)
	{
		super(inArmy);		//will copy all the classfields in super class
		footmen = inArmy.getFootmen();
		archers = inArmy.getArchers();
		history = inArmy.getHistory();

	} //END copy CONSTRUCTOR


	//ACCESSORS:


	/*********************************************************************
	* clone
	* IMPORT		none
	* EXPORT		newArmy(Army)
	* ASSERTION:	will make a copy of the current object
	**********************************************************************/
	@Override
	public Army clone()
	{
		return new Army(this);

	} //END clone ACCESSOR

	public double getFootmen()
	{
		return footmen;
	}
	
	public double getArchers()
	{
		return archers;
	}
	
	public String getHistory()
	{
		return new String(history);
	}
	
	/*********************************************************************
	* SUBMODULE: 	equals
	* IMPORT:		inName(String), inYears(Integer), inFootmen(Real),
	*				inArchers(Real), inHistory(Integer), inBanner(Banner)
	* EXPORT:		equal(Boolean)
	* ASERTION:		Will return true if objects are equivelent and false 
	*				otherwise
	**********************************************************************/
	public boolean equals(String inName, int inYears, double inFootmen, 
		double inArchers, String inHistory, Banner inBanner)
	{
		boolean equal;
		equal = super.equals(inName, inYears, inBanner)	
				//checks super classfields are equal
			&& (footmen - inFootmen < TOL) && (archers - inArchers < TOL) 
			&& (history.equals(inHistory));
		return equal;

	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		inObj(Object)
	* EXPORT:		equal(Boolean)
	* ASERTION:		Will return true if objects are equivelent and false 
	*				otherwise
	**********************************************************************/
	@Override
	public boolean equals(Object inObj)
	{
		boolean equal = false;
		if(inObj instanceof Army);
		{
			Army inArmy = (Army)inObj;	//casting to Army
			equal = equals(inArmy.getName(), inArmy.getYears(), 
				inArmy.getFootmen(), inArmy.getArchers(), inArmy.getHistory(),
				inArmy.getBanner()); 	//calls other equals method
		} //END IF
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
		
		if ((history.equals("")) || (history.equals(" ")) || (history == null))
		{	//If History is empty, return without history
			outStr = "The Army of " + super.toString() + " This Army has " 
				+ UserInterface.round(footmen) + " groups of footmen and " 
				+ UserInterface.round(archers)	//UI.round rounds to 2 d.p.
				+ " squads of archers at its ready.";
		} //END IF
		else
		{	//Has History, return with History
			outStr = "The Army of " + super.toString() + " This Army has " 
				+ UserInterface.round(footmen) + " groups of footmen and " 
				+ UserInterface.round(archers)	//UI.round rounds to 2 d.p.
				+ " squads of archers at its ready. " 
				+ "The history of this army is that it is " + history + ".";
		} //END ELSE
		return outStr;
	} //END SUBMODULE toString

	/*********************************************************************
	* SUBMODULE: 	toFileString
	* EXPORT:		outStr(String)
	**********************************************************************/
	@Override
	public String toFileString()
	{
		String outStr;
		if ((history.equals("")) || (history == null))
		{
			//If history is empty, return as a space so file can be read
			outStr = "A" + "," + super.toFileString() + "," + footmen + "," 
				+ archers + "," + " ";
		} //END if
		else
		{	//If not empty return with History
			outStr = "A" + "," + super.toFileString() + "," + footmen + "," 
				+ archers + "," + history;
		}
		return outStr;
	} //END SUBMODULE toFileString

	/*********************************************************************
	* SUBMODULE:	population
	* EXPORT:		pop(String)
	**********************************************************************/
	@Override
	public String population()
	{
		double popReal = footmen * 120.0 + archers * 450;	//calculating pop
		int popInt = (int)popReal;	/*converting to whole number, can't have 
									a fraction of people*/
		String pop = "The Army of " + super.getName() + " consists of " 
			+ popInt + " people.";
		return pop;
		/*footmen *120 and arehcers *450 may not be integers. I truncate after
		adding them. Truncating individually may produce a different result*/
	} //END SUBMODULE population
	
	
	/*********************************************************************
	* SUBMODULE:	battleStrength
	* EXPORT:		strInt(Integer)	
	**********************************************************************/
	@Override
	public int battleStrength()
	{
		double strReal = footmen * 50.0 + archers * 14.0; 	/*Calculating 
															strength*/
		int strInt = (int)strReal;	/*converting to whole number as per 
									assignment specification*/
		return strInt;
		/*same thing here as in population. Truncating result to integer. 
		/rounding or truncating sperately may produce different results.*/
	} //END SUBMODULE battleStrength

	/*********************************************************************
	* SUBMODULE:	obtainType
	* EXPORT:		type(String)
	* ASSERTION:	will return class type
	**********************************************************************/
	public String obtainType()
	{
		return "Army";
	} //END obtainType
		

	//MUTATORS:


	/*********************************************************************
	* SUBMODULE:	setFootmen
	* IMPORTS:		inFootmen(Real)
	* ASSERTION:	sets the number of footmen if valid, fails otherwise
	**********************************************************************/
	public void setFootmen(double inFootmen)
	{
		if (validateFootmen(inFootmen)) 
		{	//If valid, set footmen
			footmen = inFootmen;
		} //END IF
		else
		{	//Invalid! let calling code know
			throw new IllegalArgumentException("Invalid number of footmen");
		} //END ELSE
	} //END SUBMODULE setFootmen

	/*********************************************************************
	* SUBMODULE:	setArchers
	* IMPORTS:		inArchers(Real)
	* ASSERTION:	sets the number of squads of archers if valid, fails 
	*				otherwise
	**********************************************************************/
	public void setArchers(double inArchers)
	{
		if (validateArchers(inArchers)) 
		{	//If valid, set Archers
			archers = inArchers;
		} //END IF
		else
		{	//Invalid! let calling code know
			throw new IllegalArgumentException("Invalid number of archers");
		} //END ELSE
	} //END SUBMODULE setArchers

	/*********************************************************************
	* SUBMODULE:	setHistory
	* IMPORTS:		inHistory
	* ASSERTION:	sets history to inHistory
	**********************************************************************/
	public void setHistory(String inHistory)
	{
			history = new String(inHistory);
	} //END SUBMODULE setHistory


	//PRIVATE SUBMODULES:


	/*********************************************************************
	* SUBMODULE:	validateFootmen
	* IMPORTS:		inFootmen(Real)
	* EXPORT:		valid(Boolean)
	* ASSERTION:	footmen must be between 3.5 and 160.5
	**********************************************************************/
	private boolean validateFootmen(double inFootmen)
	{	//checking footmen is within limits
		return (inFootmen > FOOTMIN) && (inFootmen < FOOTMAX);
	} //END SUBMODULE validateFootmen

	/*********************************************************************
	* SUBMODULE:	validateArchers
	* IMPORTS:		inArchers(Real)
	* EXPORT:		valid(Boolean)
	* ASSERTION:	archers must be between 1.5 and 112.5
	**********************************************************************/
	private boolean validateArchers(double inArchers)
	{	//checking archers are within limits
		return (inArchers > ARCHERMIN) && (inArchers < ARCHERMAX);
	} //END SUBMODULE validateArchers

} //END CLASS: Army


