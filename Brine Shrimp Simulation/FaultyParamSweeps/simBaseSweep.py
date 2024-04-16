##############################################################################
# Author: 		Stephen den Boer
# ID:			19761257
# Unit:			COMP1005 (Fundamentals of Programming)
# File:			simBase.py
# purpose:		runs simulation for brine shrimp assignment S2 2019
# Revisions: 
#	1.0 | 02/10/2019 | Base version for assignment
#	2.0 | 26/10/2019 | updated to use pygame, successfully moves, bounces off 
#					   walls, changes stages (lags here), but not yet off 
#					   each other
#
# date created:			02/10/2019
# date last edited:		28/10/2019
###############################################################################

import sys
import random
import matplotlib.pyplot as plt
import time
import pygame
from sysArgs import *
from shrimp import Shrimp


#importing things from pygame.locals for easier access
#RLEACCEL is used to optomize image processing
#Quit and Mousebuttonup are events triggered by the user
from pygame.locals import(
	RLEACCEL,
	QUIT,
	MOUSEBUTTONUP,
)

#groups to store sprites, use for graphing etc.
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
numEggs =[]
numHatchlings = []
numJuveniles = []
numAdults = []
numMales = []
numFemales = []
numCouples = []

#initialising display with width and height as provided in XMAX and YMAX
screen = pygame.display.set_mode((XMAX, YMAX))

#adding shrimp, as eggs
for ii in range(NUMSHRIMP):
	#obtaining random start position
	randX = random.randint(0, XMAX)
	randY = random.randint(0, YMAX)
	#creating a new shrimp at thes coordianates
	newEgg = Shrimp(([randX, randY]))
	shrimps.add(newEgg)
#END for
	
#setting variable to continue loop until all shrimps die, or program exited
alive = True

#Main execution loop. will loop till all die or exited
while alive:

	#checking the events in the event queue
	for event in pygame.event.get():
		
		#If the user tries to exit:
		if event.type == QUIT:
			alive = False	#loop will end
		#END IF
		#If clicks mouse add an egg
		elif event.type == MOUSEBUTTONUP:
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
		#added extra vals to the end of the list so in range
		if shrimpList[ii] == 0:
			break
		#END if

		#moving shrimp
		#passing screen limits to keep shrimp on screen
		shrimpList[ii].move(XMAX, YMAX)
		#making a list for collisions, without current shrimp or 0's
		colList = [shrimp for shrimp in shrimpList if shrimp != 0]
		del colList[ii]

		#any collisions with other shrimp?
		collidees = pygame.sprite.spritecollide(shrimpList[ii].stage, colList, False)

		
		for hit in collidees:
			if hit.stage.stage == shrimpList[ii].stage.stage == "Adult":
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

			else:	#not adults
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

	#converting from list to sprite group again for display
	shrimps = pygame.sprite.Group(shrimpList)

	for shrimp in shrimps:
		#Updating shrimp
		#chance to update shrimps direction. will update age, chance to die, 
		#if a Couple, chance to have an egg
		bools = shrimp.update()
		if bools[0]:
			#death!
			shrimps.remove(shrimp)
		elif bools[1]:	
			newEgg = Shrimp(([shrimp.rect[0] - 10, shrimp.rect[1]]))
			shrimps.add(newEgg)
			#END if
		#END if
	#END for

	#Fill the screen with blue (note: rgb values)
#	screen.fill((0, 0, 55))

	#Draw shrimps to screen, i.e. updating position on screen
#	for shrimp in shrimps:
#		screen.blit(shrimp.stage.surf, shrimp.rect)
	#END for

	#update actual display
#	pygame.display.flip()

	#this will update the program ar a rate of 50 frames per second
	clock.tick(10000)

	#increase iteration count and add to list	
	time += 1
	times.append(time)

	#if iteration greater than 2000(40secs) then will end
	if time > 1500:
		alive = False
	#END if

	n = len(shrimps)
	#ending if no more alive or if more than 500 end program
	if n > 500:
		alive = False
	elif n == 0:
		alive = False
	#END if-elif	

	numShrimps.append(n)
		
#END OF LOOP

plt.figure()
plt.title("Shrimp fluctuation")
plt.xlabel("Iteration")
plt.ylabel("Shrimp")
plt.plot(times, numShrimps)
plt.savefig("shrimp_" + str(NUMSHRIMP) + "_" + str(MULTIPLIER) + "_" + \
	str(XMAX) + " X " + str(YMAX) + ".png")
