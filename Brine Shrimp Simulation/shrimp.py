###############################################################################
# Author: 		Stephen den Boer
# ID: 			19761257
# File:			shrimp.py
# Purpose:		Contains class definitions for simulation of brine shrimp
# Revisions: 
#	1.0 | 02/10/2019 | base version for assignment
#	2.0 | 21/10/2019 | created four stages as subclasses of Shrimp class
#					 | Also adapted to use pygame. 
#	3.0 | 26/10/2019 | updated movement, now bounces off walls.
#	4.0 | 29/10/2019 | Now uses system arguments, 
#
# date created:			02/10/2019
# date last edited:		26/10/2019
###############################################################################

import pygame	#main implementation module
import random	#for creating random chance
import math		#to use square root for perpendicular distance	
from sysArgs import *	#importing system arguments, uses MULTIPLIET and 
						#TIME2CHANGE

#importing RLEACCELfor easier refrence, used to optoimize images 
from pygame.locals import(
	RLEACCEL, 
	)

#equation for calculating chances of death. Not part of class since 
#functionality not required outside of class
def deathEqn(x):
	#should be a peak just before change time from egg to hatchling
	#represents the lower chance of survival of an egg. Additionally, as
	#age increases from adult will increase as with age comes greater 
	#feebleness
	a = 21 / 20000
	b = -0.19763
	c = 5.18
	#cubic function
	y = a * x ** 3 + b * x **2 + c * x
	return y
#END deathEqn


#Shrimp extends pygame.sprite.Sprite, this allows it to hava a 'sprite' (image)
class Shrimp(pygame.sprite.Sprite):
	
	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Shrimp, self).__init__()
		self.age = 0
		self.stage = Egg(pos)
		self.rect = self.stage.rect	#rect initialised in .stage to image 
									#coordinates, then initialised here to be 
									#updated so don't have to .stage.rect all 
									#the time can just .rect. also collision 
									#method requires objcet to have a .rect 
									#attribute.
		#Note: coords start from top left of screen 
		self.xdirct = 0 	#0 is no mvmt, 1 is right, -1 is left
		self.ydirct = 1		#0 is no mvmt, 1 is down -1 is up
		
	#END __init__
	
	#method to update shrimp
	def update(self):
		#create a list to store booleans for death and new life
		bools = [0, False]
		if self.age >= TIME2CHANGE:		#if no longer an egg
			if random.randint(1, 8) == 3:	# if is a 3 (1 in 8 chance)
				self.changeDirc()	#change direction
			#END if
		#END if
		bools[0] = self.ageing()	#increase age, chance to die, true = death
		if self.stage.stage == "Couple":	#if able to create an egg
			if random.randint(1, 10) == 2:	#If a 2 (1 in 10 chance)
				bools[1] = True		#return true for egg, calling code will 
									#create a new egg
			#END if
		#END if
		return bools	#let calling code know results
	#END update
	
	#string function, returns what stage Shrimp is and position.
	#Not used currently but is useful when error handling
	def __str__(self):
		return self.stage.stage + " @ " + str(self.rect[0]) + ", " \
		+ str(self.stage.rect[1])
	#END __str__

	
	def ageing(self):
		self.age += 1
		#check if age is time to change to hatchling
		if (self.age  == TIME2CHANGE):
			#changing stage to hatchling, passing current position
			self.stage = Hatchling(self.rect)
			#making sure that self.rect is the current version, will have 
			#changed with dif image dimensions
			self.rect = self.stage.rect
			#change direction so most shrimps aren't still moving down
			self.changeDirc()
		#check if old enough to be juvenile
		elif (self.age == 2 * TIME2CHANGE):
			#changing stage to Juvenile, passing current position
			self.stage = Juvenile(self.rect)
			#making sure that self.rect is the current version, will have 
			#changed with dif image dimensions
			self.rect = self.stage.rect
		#check if old enough to be adult
		elif (self.age == 3 * TIME2CHANGE):
			#changing stage to Adult, passing current position
			self.stage = Adult(self.rect)
			#making sure that self.rect is the current version, will have 
			#changed with dif image dimensions
			self.rect = self.stage.rect
		#END if-elif

		#creating variable for death equation. Based on age and normalized 
		#time between stages to 10 i.e. since age increases as time 2 chang 
		#increases, normalized it so x will be the same at the end of a stage 
		#irrespective of time2change

		#Changed from commented section to section below. beacause otherwise 
		#moving along function not actually modifiying chance
#		x = self.age * (TIME2CHANGE / 10) * MULTIPLIER
#		chance = deathEqn(x)
		x = self.age * (TIME2CHANGE / 10)
		chance = deathEqn(x) * MULTIPLIER
		#if chance is greater than the random integer, shrimp dies. Range has 
		#been adjusted to fit chance equations. If going from 1, then would 
		#die too early
		if random.randint(15, 850) < chance:
			return True		#death!
		else:
			return False	#life!
		#END if-else		
	
	#END age
	
	
	def move(self, XMAX, YMAX):
		#for easier reference
		x = self.xdirct
		y = self.ydirct

		#calulating distance to move
		dist = self.stage.speed	
 
		#calculates perpendicular distance for when moving diagonally so total
		#distance moved is equal.
		pd = math.sqrt((dist ** 2)/2)		

		#will move the shrimp in it's set x and y direction
		if ((x == 0) or (y == 0)):	#moving straight
			self.rect.move_ip(x * dist, y * dist)
		else:	#moving diagonally
			self.rect.move_ip(x * pd, y * pd)
		#END if else
	
		#checking if off the screen. 
			#is it off left or right edge?
		if (self.rect.left < 0): #is left side of image off left edge?
			self.rect.left = 0 #making sure still on screen
			self.xdirct *= -1 #reversing x direction
		elif (self.rect.right > XMAX):	#is right side off right edge?
			self.rect.right = XMAX	#making sure still on screen
			self.xdirct *= -1 #reversing x direction
		#END if-elif
			#is it off top or bottom edge?
		if (self.rect.top < 0): #is top side of image off top edge?
			self.rect.top = 0 #making sure still on screen
			self.ydirct *= -1 #reversing y direction
		elif (self.rect.bottom > YMAX):  #is bottom side off bottom edge?
			self.rect.bottom = YMAX	#making sure still on screen

			if self.stage.stage == "Egg":	#if an egg want it to stay at the 
												#bottom, set direction to 0
				self.ydirct = 0
			else:	#Not an egg
				self.ydirct *= -1 #reversing y direction
			#END if-else
		#END if-elif	
		
	#END move

	def collide(self, shrimp):

		#if shrimp was moving right
		if self.xdirct == 1:
			#make right edge equal to left edge of collided shrimp
			#i.e. shrimp will be side by side
			self.rect.right = shrimp.rect.left
		#opposite now where shrimp is moving to the left
		elif self.xdirct == -1:
			self.rect.left = shrimp.rect.right
		#END if-elif

		#if shrimp was moving downwards then set bottom edge to the 
		#top of collided shrimp edge
		if self.ydirct == 1:
			self.rect.bottom = shrimp.rect.top
		#opposite for moving upwards
		elif self.ydirct == -1:
			self.rect.top = shrimp.rect.bottom
		#END if-elif

	#END collide


	def changeDirc(self):
		#sets direction to a random direction, could be same as current 
			#direction
		self.xdirct = random.choice((0, -1, 1))		#-1 is left, 1 is right
		if (self.xdirct == 0):	#making sure not 0 and 0, else Shrimp would 
								#be stationary
			self.ydirct = random.choice((-1, 1))	#1 is down, -1 is up
		else:
			self.ydirct = random.choice((0, -1, 1))	
		#END else if

	#END changeDirc


	def initCouple(self):
		#changing stage to Couple, passing current position
		self.stage = Couple(self.rect)
		#making sure that self.rect is the current version, will have 
		#changed with dif image dimensions
		self.rect = self.stage.rect
	#END initBoth


#END class Shrimp


#class for egg state 
class Egg(pygame.sprite.Sprite):
	stage = "Egg"

	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Egg, self).__init__()

		#number of pixels to move
		self.speed = 10
		#loading image for this stage 
		self.surf = pygame.image.load("pics/egg.png").convert()
		#setting white in image to be set to clear
		self.surf.set_colorkey((255, 255, 255), RLEACCEL)
		#setting visual dimensions; will be 10 X 14
		self.rect = self.surf.get_rect(
			#setting center of object to the position passed
			center = (
				pos[0], pos[1]
				)
			) 

	#END __init__

#END class Egg


#Class for hatchling stat
class Hatchling(pygame.sprite.Sprite):
	stage = "Hatchling"

	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Hatchling, self).__init__()

		#number of pixels to move
		self.speed = 10
		#loading image for shrimp 
		self.surf = pygame.image.load("pics/hatchling.png").convert()
		#setting black in image to be set to clear
		self.surf.set_colorkey((0, 0, 0), RLEACCEL)
		#setting visual dimensions will be 13 X 7
		self.rect = self.surf.get_rect(
			#setting center of object to the position passed
			center = (
				pos[0], pos[1]
				)
			)
	#END __init__

#END class Hatchling


#class for juvenile state
class Juvenile(pygame.sprite.Sprite):
	stage = "Juvenile"

	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Juvenile, self).__init__()

		#number of pixels to move
		self.speed = 15	
		#loading image for shrimp 
		self.surf = pygame.image.load("pics/juvenile.png").convert()
		#setting black in image to be set to clear
		self.surf.set_colorkey((0, 0, 0), RLEACCEL)
		#setting visual dimensions will be 17 X 11
		self.rect = self.surf.get_rect(
			center = (
				pos[0], pos[1]
				)
			)
	#END __init__

#END class Juvenile


#class for adult state
class Adult(pygame.sprite.Sprite):		
	stage = "Adult"

	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Adult, self).__init__()
		
		#setting gender, ~50% chance for either
		self.gender = random.choice(('M', 'F'))

		#number of pixels to move
		self.speed = 20	
		#loading image for shrimp 
		if self.gender == 'M':
			self.surf = pygame.image.load("pics/adult_male.png").convert()
		else:	#will be female
			self.surf = pygame.image.load("pics/adult_female.png").convert()
		#END if-else

		#setting black in image to be set to clear
		self.surf.set_colorkey((0, 0, 0), RLEACCEL)
		#setting visual dimensions will be 18/19 X 13
		self.rect = self.surf.get_rect(
			center = (
				pos[0], pos[1]
				)
			)
	#END __init__

#END class Adult


#class for Couple state
class Couple(pygame.sprite.Sprite):		
	stage = "Couple"

	def __init__(self, pos):
		#passing the class to the sprite class to initialise
		super(Couple, self).__init__()

		#number of pixels to move
		self.speed = 15	
		#loading image for shrimp 
		self.surf = pygame.image.load("pics/adult_both.png").convert()

		#setting black in image to be set to clear
		self.surf.set_colorkey((0, 0, 0), RLEACCEL)
		#setting visual dimensions will be 19 X 26
		self.rect = self.surf.get_rect(
			center = (
				pos[0], pos[1]
				)
			)
	#END __init__

#END class Adult


