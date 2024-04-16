#Sets the appropriate variables based on system arguments provided
#File intended to be imported by shrimp.py and simBase.py. Necassary to store 
#in a seperate file since simBase already imports shrimp, meaning shrimp 
#can't nicely import simBase.

import sys	#for sustem arguments

if len(sys.argv) < 6:	#if not enough arguments use default values
	print("not enough arguments provided. Using defaults")
	NUMSHRIMP = 50
	XMAX = 1400
	YMAX = 900
	MULTIPLIER = 1.3
	TIME2CHANGE = 10
else:	#enough vlaues provided, set to appropriate passed values
	NUMSHRIMP = int(sys.argv[1])	
	XMAX = int(sys.argv[2])
	YMAX = int(sys.argv[3])
	MULTIPLIER = float(sys.argv[4])
	TIME2CHANGE = int(sys.argv[5]) 
#END if-else
	


