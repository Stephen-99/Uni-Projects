####
# Colour.py
#
# Date created:     27/05/2020
# Date last edited: 27/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This holds the definition for functions allowing text to be printed in colour Attained from:
#   https://stackoverflow.com/questions/39473297/how-do-i-print-colored-output-with-python-3
#   The answer by Andriy Makukha
#    
####


fg = lambda text, color: "\33[38;5;" + str(color) + "m" + text + "\33[0m"
bg = lambda text, color: "\33[48;5;" + str(color) + "m" + text + "\33[0m"
