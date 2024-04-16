##############################################################################
# Author: 		Stephen den Boer
# ID:			19761257
# Unit:			COMP1005 (Fundamentals of Programming)
# File:			simBaseSweep.py
# purpose:		simBase.py with output commented out in an atempt to make 
#				sweep faster
# Revisions: 
#	1.0 | 02/10/2019 | Base version for assignment
#	2.0 | 26/10/2019 | updated to use pygame, successfully moves, bounces off 
#					   walls, changes stages (lags here), but not yet off 
#					   each other
#	3.0 | 29/10/2019 | enabled collisions between shrimp, graphical results and
#					   conditions to end program 
#
# date created:			02/10/2019
# date last edited:		29/10/2019
###############################################################################

import sys	#for command line args
import random	#for random star positions
import matplotlib.pyplot as plt		#for final plot
import time		#for controlling fps
import pygame	#basic module used for simulation implementation
from sysArgs import *	#holds global variables defined by system arguments
from shrimp import Shrimp	#holds class definition


#importing things from pygame.locals for easier access
#RLEACCEL is used to optomize image processing
#Quit and Mousebuttonup are events triggered by the user
from pygame.locals import(
	RLEACCEL,
	QUIT,
	MOUSEBUTTONUP,
)

#groups to store sprites, use for drawing to screen
shrimps = pygame.sprite.Group()

#initialising pygame
pygame.init()

#setting up the clock so can control how fast the program iterates.
clock = pygame.time.Clock()

#time var to count how many iterations and list to store them
time = 0 
times = [0]

#creating lists to store number of shrimps
numShrimps = [NUMSHRIMP]

#initialising display with width and height as provided in XMAX and YMAX
screen = pygame.display.set_mode((XMAX, YMAX))

#adding shrimp, as eggs
for ii in range(NUMSHRIMP):
	#obtaining random start position
	randX = random.randint(0, XMAX)
	randY = random.randint(0, YMAX)
	#creating a new shrimp at these coordianates, shrimp init will create eggs
	newEgg = Shrimp(([randX, randY]))
	shrimps.add(newEgg)
#END for
	
#setting variable to continue loop until all shrimps die, more than 500 shrimp
#are alive, more than 1500 iterations executed or program manually exited
alive = True

#Main execution loop.
while alive:

	#checking the events in the event queue
	for event in pygame.event.get():
		
		#If the user tries to exit:
		if event.type == QUIT:
			alive = False	#loop will end
		#END IF
		#If clicks mouse add an egg
		elif event.type == MOUSEBUTTONUP:
			#New egg at mouse position
			newEgg = Shrimp((pygame.mouse.get_pos()))
			shrimps.add(newEgg)
		#END elif
	#END for

	#putting sprite group into a list for processing	
	shrimpList = []	
	for shrimp in shrimps:
		shrimpList.append(shrimp)
	#END for


	for ii in range(len(shrimpList)):
		#added extra vals to the end of the list while looping, inorder to 
		#ensure lne(shrimpList) doesn't change
		if shrimpList[ii] == 0:
			break
		#END if

		#moving shrimp
		#passing screen limits to keep shrimp on screen
		shrimpList[ii].move(XMAX, YMAX)
		#making a list for collisions, without current shrimp or 0's
		colList = [shrimp for shrimp in shrimpList if shrimp != 0]
		del colList[ii]

		#any collisions with other shrimp? method compares rect coordinates
		#return any shrimp colliding with as a list
		collidees = pygame.sprite.spritecollide(shrimpList[ii].stage, \
		colList, False)

		#Iterating through list of collidees
		for hit in collidees:
			#If both adults...
			if hit.stage.stage == shrimpList[ii].stage.stage == "Adult":
				#If the same gender...
				if hit.stage.gender == shrimpList[ii].stage.gender:
					#same gender, no reproduction
					#updating position
					shrimpList[ii].collide(hit)

					#reverse direction
					shrimpList[ii].xdirct *= -1
					shrimpList[ii].ydirct *= -1

				else:	#will be male and female, join together
					#now are a mating couple
					shrimpList[ii].initCouple()

					#remove one of the inital adults 
					shrimpList.remove(hit)
					shrimpList.append(0)					
				#END if-else
			#If not both adults, are they both eggs?
			elif hit.stage.stage == shrimpList[ii].stage.stage == "Egg":
				#if two eggs collide, remove both eggs
				del shrimpList[ii]
				shrimpList.remove(hit)
				#replacing the two values to maintain number of items in list
				shrimpList.append(0)
				shrimpList.append(0)
				
				#since egg no lnger exists, cannot be in any other collisions
				#therefore, end loop and continue to next shrimp
				break

			else:	#not 2 adults or 2 eggs
				#updating position
				shrimpList[ii].collide(hit)

				#we do not want to reverse egg direction eggs cannot swim can 
				#only fall down with gravity
				if shrimpList[ii].stage.stage != "Egg":
					#reverse direction
					shrimpList[ii].xdirct *= -1
					shrimpList[ii].ydirct *= -1
				#END if	
			#END if-elif-else
		#END for
	#END for

	#updating list to have only non-zeros, i.e. remove zeros I added
	shrimpList = [shrimp for shrimp in shrimpList if shrimp != 0]

	#converting from list to sprite group again for displaying to screen
	shrimps = pygame.sprite.Group(shrimpList)

	for shrimp in shrimps:
		#Updating shrimp
		#chance to update shrimps direction. will update age; chance to die, 
		#if a Couple, chance to have an egg
		bools = shrimp.update()	#returns list with 2 booleans, for death and 
								#new life
		if bools[0]:	#did shrimp die?
			#death!
			shrimps.remove(shrimp)	#remove the shrimp! i.e. kill the shrimp
		elif bools[1]:	#was an egg created?
			#create the new egg 10 pixels below centre of parents
			newEgg = Shrimp(([shrimp.rect[0] - 10, shrimp.rect[1]]))	
			shrimps.add(newEgg)
			#END if
		#END if
	#END for

#updating to screen commented out in an attempt to improve effeciency for 
#parameter sweep

	#Fill the screen with blue (note: rgb values)
#	screen.fill((0, 0, 55))

	#Draw shrimps to screen, i.e. updating position on screen
#	for shrimp in shrimps:
#		screen.blit(shrimp.stage.surf, shrimp.rect)
	#END for

	#update actual display
#	pygame.display.flip()

#Note higher fps used for parameter sweep. In reality, speed ends up being 
#limited by processing speed, especially for large numbers of shrimp. 
#Therfore, does not actually process at 10000 fps but as fast as possible on 
#the machine. 

	#this will update the program ar a rate of 50 frames per second
	clock.tick(10000)

	#increase iteration count and add to list	
	time += 1
	times.append(time)

	#if iteration greater than 1500(30secs @ 50fps) then will end
	if time > 1500:
		alive = False
	#END if

	#for easier reference
	n = len(shrimps)
	#ending if no more alive or if more than 500 alive
	if n > 500:
		alive = False
	elif n == 0:
		alive = False
	#END if-elif	

	numShrimps.append(n)
		
#END OF LOOP

#creates a plot of number of shrimp against iterations. Uses parameter values 
#as a file title.
plt.figure()
plt.title("Shrimp fluctuation")
plt.xlabel("Iteration")
plt.ylabel("Shrimp")
plt.plot(times, numShrimps)
plt.savefig("shrimp_" + str(NUMSHRIMP) + "_" + str(MULTIPLIER) + "_" + \
	str(XMAX) + " X " + str(YMAX) + ".png")
