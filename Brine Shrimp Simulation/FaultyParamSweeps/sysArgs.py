import sys

if len(sys.argv) < 6:
	print("not enough arguments provided. Using defaults")
	NUMSHRIMP = 50
	XMAX = 1400
	YMAX = 900
	MULTIPLIER = 1.4
	TIME2CHANGE = 10
else:
	NUMSHRIMP = int(sys.argv[1])
	XMAX = int(sys.argv[2])
	YMAX = int(sys.argv[3])
	MULTIPLIER = float(sys.argv[4])
	TIME2CHANGE = int(sys.argv[5]) 
#END if-else
	


