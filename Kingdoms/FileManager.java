/******************************************************************************
* Filename:         FileManager.txt                                           *
* Author:           Stephen den Boer                                          *
* Unit code:		COMP 1001												  *
* Student ID:		19761257												  *
* Purpose:        	Handles any file interaction for kingdom class            *
* Date created:     09/10/2019                                                *
* Last modified:    25/10/2019                                                *
* changes:                                                                    *
* 	EDIT 1 | 18/10/2019 | updated process line to construct alliances with 	  *
*						  banners.                                            *
*	EDIT 2 | 23/10/2019 | updated output to use alliance array instead of 	  *
*						  house and army arrays								  *
*	EDIT 3 | 25/10/2019 | updated so import is a kingdom and no longer 		  *
*						  checking that it is.								  *
******************************************************************************/

import java.util.*;
import java.io.*;

public class FileManager
{

	/*********************************************************************
	* SUBMODULE:	input
	* IMPORT:		kingdom(Kingdom)
	* EXPORT:		read(boolean)
	* ASSERTION:	Will fill the kingdom from file
	**********************************************************************/
	public static boolean input(Kingdom kingdom)
	{
	
		boolean read;
		String filename;	

		filename = UserInterface.getString("Please enter the name "
			+ "of the file storing the kingdom you wish to load");

		read = readFile(filename, kingdom);
			
		return read;
	} //END SUBMODULE input

	/*********************************************************************
	* SUBMODULE:	readFile
	* IMPORT:		filename(String), kingdom(Kingdom)
	* EXPORT:		read(boolean)
	* ASSERTION:	will read and process each line of the given file
	**********************************************************************/
	public static boolean readFile(String filename, Kingdom kingdom)
	{
		FileInputStream fileStrm = null;
		InputStreamReader rdr;
		BufferedReader bufRdr;

		boolean read;
		String line;

		read = true;
        try
		{
			fileStrm = new FileInputStream(filename);
			rdr = new InputStreamReader(fileStrm);
			bufRdr = new BufferedReader(rdr);

  			line = bufRdr.readLine();	//read first line of File

          	while(line != null)	//while not end of file
			{
                processLine(line, kingdom);	//try read alliance
                line = bufRdr.readLine();	//read next line
			} //END while 
			
			fileStrm.close();	//close file
			
		} //END try
		catch(IOException e)
		{	//problem reading file
			if (fileStrm != null)
			{	//file open
				try
				{
					fileStrm.close();	//try close file
				} //END try
				catch(IOException e2)
				{	//can't close
					// Nothing further can be done
				} //END catch
			} //END if
			
			UserInterface.showError("Could not read the file: " + filename);
				//Tell user file couldn't be read
			read = false;
				//let calling code know file could not be read
		} //END catch
		return read;
    } //END SUBMODULE readFile
	
	/*********************************************************************
	* SUBMODULE: 	processLine
	* IMPORT:		line(String), kingdom(Kingdom)
	* ASSERTION:	will add army or house to kingdom if valid
	**********************************************************************/
	public static void processLine(String line, Kingdom kingdom)
	{
		try
		{
			String[] lineArray = line.split(","); //string array to hold csv's
			Banner newBanner;
			House newHouse;
			Army newArmy;

			int years, bannermen;
			char type;
			String name, sigil, bannerColour, sigilColour, castle, history;
			double footmen, archers;

			if ((lineArray.length != 8) && (lineArray.length != 9))
			{	//Invalid length for alliance, throw error
				throw new IllegalArgumentException("ERROR: line was not "
					+ "correct length");
			} //END if
			if(lineArray[0].length() != 1)
			{	//Invalid lenght for first value, throw error
				throw new IllegalArgumentException("ERROR: First entry on "
					+ "line must be a single character");
			} //END if
		
			type = lineArray[0].charAt(0);	//House or Army

			name = lineArray[1];	
			years = Integer.parseInt(lineArray[2]);
			sigil = lineArray[3];
			bannerColour = lineArray[4];
			sigilColour = lineArray[5];
				// both colours are stored as strings and converted to 
					//int in construction of Banner below:
			newBanner = new Banner(sigil, bannerColour, sigilColour);
	
			switch (Character.toUpperCase(type))
			{	//House or Army?
				case 'H':	//House
					bannermen = Integer.parseInt(lineArray[6]);
					castle = lineArray[7];
				
					newHouse = new House(name, years, bannermen, castle, 
						newBanner);
						// all validation is taken care of in house class
						//any error will be illegal argument and will be caught
			
					kingdom.addAlliance(newHouse);	//add new house to kingdom
					break;

				case 'A':	//Army
					footmen = Double.parseDouble(lineArray[6]);
					archers = Double.parseDouble(lineArray[7]);
					history = lineArray[8];
						// validation taken care of in Army class, any
						//errors will be illegal arg and will be caught

					newArmy = new Army(name, years, footmen, archers, history,
						newBanner);	/*all validation taken care of in Army 	
									Class. any errors will be illegal argument
									and will be caught*/
			
					kingdom.addAlliance(newArmy);	//add new Army to kingdom
					break;

				default:	//Wan't a House or Army...
					throw new IllegalArgumentException("ERROR: first entry on "
						+ "line must be either 'H' for house or 'A' for Army");
			} //END switch
		} //END try
			/*Catches will print an error msg and method will return to 
			readFile which will read the next line*/
		catch(NumberFormatException e)
		{	//catch error from incorrect data type stored
			UserInterface.showError("Stored values were not correct type");
		} //END catch
		catch(IllegalArgumentException e)
		{	//catch error from creating house or army
			UserInterface.showError(e.getMessage());
		} //END catch
	} //END SUBMODULE processLine
	
	/*********************************************************************
	* SUBMODULE:	output
	* IMPORT:		kingdom(Kingdom)
	* ASSERTION:	will write kingdom to csv file
	**********************************************************************/
	public static void output(Kingdom kingdom)
	{
		// will fail when this file opens since banner not incl. atm

		FileOutputStream fileStrm = null;
		PrintWriter pw;

		Alliance[] allies;
		String filename;
		int count;

		filename = UserInterface.getString("Please enter the name "
			+ "of the file where you wish to store the kingdom");

		try
		{		
			fileStrm = new FileOutputStream(filename);
			pw = new PrintWriter(fileStrm);
	
			allies = kingdom.getAllies();	//obtaining copy of alliances
			count = kingdom.getAlCount();	//obtainign copy of allianc count

			for(int ii = 0; ii < count; ii++)
			{	//looping though array
				pw.println(allies[ii].toFileString());	/*print each alliance
														in csv format*/
			} //END for
	
			pw.close();	//close file
		} //END try


		catch(IOException e)
		{	//problem reading or writing the file
			if(fileStrm != null)
			{	//file is open
				try
				{
					fileStrm.close();	//try close
				} //END try
				catch(IOException e2)
				{	//couldn't close
					// Nothing further can be done
				} //END catch
			} //END if
			
			UserInterface.showError("Could not write the file ");
		} //END catch
		
	} //END SUBMODULE output

} //END CLASS FileManager



