/******************************************************************************
* Filename:         Banner.java                                               *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:          Template for Banner object                                *
* Date created:     26/09/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 28/09/2019 | fixed minor changes to output string formatting.    *
*						  added additional comments						      *
*   EDIT 2 | 18/10/2019 | updated toString and toFileString methods           *
*	EDIT 3 | 25/10/2019 | updated hex2int method to deal with neagative 	  *
*						  numbers								  			  *
******************************************************************************/

public class Banner
{
	
	//Class Constants 
		public static final String[] sigils = new String[] 
			{"WOLF", "TOTEM", "LION", "DRAGON"};	//All the valid sigils
		
	//Private class fields
	private String sigil;
	private int bannerColour, sigilColour;
	
	
	//CONSTRUCTORS:

	
	/*********************************************************************
	* Default:
	* IMPORT:		none
	* EXPORT: 		address of Banner object
	* ASSERTION:	sigil "WOLF", bannerColour 2222222, sigilColour 000000, 
	*				is a valid banner. This is the default state.
	**********************************************************************/
	public Banner()
	{
		sigil = sigils[0];	
		bannerColour = 2222222;
		sigilColour = 000000;
	} //END default CONSTRUCTOR

	/*********************************************************************
	* Alternate1:
	* IMPORT:		inSigil(String), inBannerColour(String), 
	*				inSigilColour(String)
	* EXPORT:		address of new banner object
	* ASSERTION: 	creates the object if imports are valid, fails otherwise
	**********************************************************************/
	public Banner(String inSigil, String inBannerColour, String inSigilColour)
	{
		setSigil(inSigil);
		setBannerColour(inBannerColour);
		setSigilColour(inSigilColour);
	} //END Alternate1 CONSTRUCTOR

	/*********************************************************************
	* copy:
	* IMPORT:		inBanner(Banner)
	* EXPORT:		Address of new Banner object
	* ASSERTION:	creates the object if import is valid, else will fail
	**********************************************************************/
	public Banner(Banner inBanner)
	{
		sigil = inBanner.getSigil();
		bannerColour = inBanner.getBannerColour();
		sigilColour = inBanner.getSigilColour();
	} //END copy CONSTRUCTOR


	//ACCESSORS:

	
	/*********************************************************************
	* SUBMODULE: 	clone
	* IMPORT:		none
	* EXPORT:		newBanner(Banner)
	* ASSERTION:	will make a copy of the current object
	**********************************************************************/
	public Banner clone()
	{	//Returns a copy of the current banner
		return new Banner(this);
	} //END SUBMODULE clone

	public String getSigil()
	{
		return new String(sigil);
	}
	
	public int getBannerColour()
	{
		return bannerColour;
	}

	public String getBannerColourString()
	{
		return int2Hex(bannerColour);	//converting to hex string 
	}

	public int getSigilColour()
	{
		return sigilColour;
	}
	
	public String getSigilColourString()
	{
		return int2Hex(sigilColour);	//converting to hex string
	}

	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		inSigil(String), inBannerColour(String), 
	*				inSigilColour(String)
	* EXPORT:		equal(Boolean)
	* ASSERTION:	will return true values are equivelent to the current 
	*				object and false otherwise
	**********************************************************************/
	public boolean equals(String inSigil, String inBannerColour, 
		String inSigilColour)
	{
		boolean equal;
		int inBanCol, inSigCol;
		inBanCol = hex2Int(inBannerColour);
			//validation happens in hex2Int submodule
		inSigCol = hex2Int(inSigilColour);
			//validation happens in hex2Int submodule
		/*If invalid, hex2Int throws an error which will be passed to the 
		calling code*/
		equal = (sigil.toUpperCase().equals(inSigil.toUpperCase())) && 
			(bannerColour== inBanCol) && (sigilColour== inSigCol);
			//Sigil case doesn't matter, therefore compared in uppercase
		return equal;
	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE:	equals
	* IMPORT:		inObj(Object)
	* EXPORT:		equal(Boolean)
	* ASSERTION:	will return true if objects are equivelent and false 
	*				otherwise
	**********************************************************************/
	public boolean equals(Object inObj)
	{
		boolean equal = false;
		if (inObj instanceof Banner);
		{
			Banner inBanner = (Banner)inObj;	//casting to banner
			equal = (sigil.toUpperCase().equals(inBanner.sigil.toUpperCase())) 
				&& (bannerColour == inBanner.bannerColour) 
				&&  (sigilColour == inBanner.sigilColour);
			//Sigil case doesn't matter so compared in uppercase
		} //END if
		return equal;
	} //END SUBMODULE equals

	/*********************************************************************
	* SUBMODULE:	toString
	* EXPORT:		outStr(String)
	* ASSERTION:	will return the object as a string
	**********************************************************************/
	public String toString()
	{
		return "its " + int2Hex(bannerColour) + " Banner consists of a " 
			+ int2Hex(sigilColour) + " Sigil in the shape of a " + sigil + ".";
				//validation happens in the int2Hex submodule
			//colours need to be hex string for output
	} //END SUBMODULE toString
		
	/*********************************************************************
	* SUBMODULE:	toFileString
	* EXPORT:		outStr(String
	* ASSERTION:	will return the object as a string in format suitable for 
	*				csv file storage
	**********************************************************************/
	public String toFileString()
	{
		return sigil + "," + int2Hex(bannerColour) + "," 
			+ int2Hex(sigilColour);
				//validation happens in the int2Hex submodule
			//colours need to be hex string for output
	} //END boolean SUBMODULE toFileString

	
	//MUTATORS:


	/*********************************************************************
	* SUBMODULE: 	setSigil
	* IMPORT:		inSigil(String)
	* ASSERTION:	sets the sigil if valid, else Fails
	**********************************************************************/
	public void setSigil(String inSigil)
	{

		if (validateSigil(inSigil))
		{	//valid; set Sigil
			sigil = new String(inSigil);
		} //END if
		else
		{	//Invalid! let calling code know
			throw new IllegalArgumentException("Invalid sigil");
		} //END else
	} //END SUBMODULE setSigil
		
	/*********************************************************************
	* SUBMODULE: 	setBannerColour
	* IMPORT:		inBannerColour(String)
	* ASSERTION:	sets the bannerColour if valid, else Fails
	**********************************************************************/
	public void setBannerColour(String inBannerColour)
	{
		int inBanCol = hex2Int(inBannerColour);
			//validation happens in hex2Int submodule
		bannerColour = inBanCol;	/*if invalid, will throw an error and 
									will not execute this line of code*/
	} //END SUBMODULE setBannerColour
		
	/*********************************************************************
	* SUBMODULE: 	setSigilColour
	* IMPORT:		inSigilColour(String)
	* ASSERTION:	sets the sigilColour if valid, else Fails
	**********************************************************************/
	public void setSigilColour(String inSigilColour)
	{
		int inSigCol = hex2Int(inSigilColour);
			//validation happens in hex2Int submodule
		sigilColour = inSigCol;		/*if invalid, will throw an error and will 
									not execute this line*/
	} //END SUBMODULE setSigilColour


	//PRIVATE SUBMODULES:
	
	
	/*********************************************************************
	* SUBMODULE:	validateSigil
	* IMPORT:		inSigil(Sring)
	* EXPORT:		valid(Boolean)
	* ASSERTION:	will return true if sigil is valid
	**********************************************************************/
	private boolean validateSigil(String inSigil)
	{
		boolean valid = false;
		for (int ii = 0; ii < sigils.length && !valid; ii++)
		{
			valid = (sigils[ii].equals(inSigil.toUpperCase()));	 
				//compares sigils in uppercase to array of valid sigils
		} //END FOR
		return valid;
	} //END SUBMODULE validateSigil

	/*********************************************************************
	* SUBMODULE:	hex2Int
	* IMPORT:		hex(String)
	* EXPORT:		inte(Integer)
	* ASSERTION:	will convert input to Integer or fail if invalid input
	**********************************************************************/
	private int hex2Int(String hex)
	{
		int inte;
		try
		{
			if (hex.charAt(0) == '-')
			{	//negative number, not allowed!
				throw new IllegalArgumentException("Please ensure hexadecimal "
					+ "numbers are positive");
			} //END if
			if (hex.length() == 6)	/*doesn't need else-if. if other if 
									executes will jump to catch*/
			{	//Correct length! convert to integer
				inte = Integer.parseInt(hex, 16);
			} //END if
			else
			{	//Inncorrect length! let calling code know
				throw new IllegalArgumentException("Invalid colour. Please use"
					+ " Hexadecimal notation");
			} //END else
		} //END TRY
		catch (NumberFormatException e)
		{	//Invalid format, thrown by Integer.parseInt
			throw new IllegalArgumentException("Invaid colour. Please use "
				+ "Hexadecimal notation");	/*only want object to throw 
											Illegal argument exceptions*/
		} //END CATCH
		return inte;
	} //END SUBMODULE hex2Int

	/*********************************************************************
	* SUBMODULE:	int2Hex
	* IMPORT:		inte(Integer)
	* EXPORT:		hex(String)
	* ASSERTION:	will convert input to hex string or fail if invalid input
	**********************************************************************/
	private String int2Hex(int inte)
	{
		String hex;
		try
		{
			hex = Integer.toHexString(inte);
			if (hex.length() < 6)
			{	//inncorrect length! restore to correct length
				hex = "0".repeat(6 - hex.length()) + hex;
					/*zeros had to be added to the beginning of the string
						since when converting to integer and then back, it 	
						would remove leading zeros */
			} //END if
		} //END TRY
		catch (NumberFormatException e)
		{	//Invalid format, thrown by Integer.toesString
			throw new IllegalArgumentException("ERROR converting to hex");
		} //END CATCH
		return hex;
	} //END SUBMODULE int2Hex

} //END CLASS: Banners

