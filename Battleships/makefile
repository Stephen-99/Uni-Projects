CFLAGS = -g -Wall -ansi -pedantic -Werror
OBJ = start.o Board.o Ship.o LinkedList.o FileIO.o UserInterface.o Missile.o Coord.o Menu.o CreateFile.o Game.o
EXE = battleships
CC = gcc

ifdef DEBUG
CFLAGS += -D DEBUG
endif

ifdef MONO
CFLAGS += -D MONO
endif

$(EXE) : $(OBJ)
	$(CC) $(OBJ) -o $(EXE)

start.o: Board.h LinkedList.h FileIO.h Ship.h UserInterface.h Missile.h Coord.h Menu.h 
	$(CC) $(CFLAGS) -c start.c 

Board.o: Board.h Ship.h UserInterface.h Coord.h
	$(CC) $(CFLAGS) -c Board.c 
	
LinkedList.o: LinkedList.h
	$(CC) $(CFLAGS) -c LinkedList.c 
	
FileIO.o: FileIO.h Board.h LinkedList.h Ship.h UserInterface.h Missile.o Coord.h
	$(CC) $(CFLAGS) -c FileIO.c 

UserInterface.o : UserInterface.h Coord.h
	$(CC) $(CFLAGS) -c UserInterface.c
	
Ship.o: Ship.h Coord.h
	$(CC) $(CFLAGS) -c Ship.c 

Missile.o: Missile.h UserInterface.h
	$(CC) $(CFLAGS) -c Missile.c 

Coord.o: Coord.h UserInterface.h
	$(CC) $(CFLAGS) -c Coord.c 

Menu.o: Menu.h UserInterface.h LinkedList.h Missile.h Board.h CreateFile.h Game.h
	$(CC) $(CFLAGS) -c Menu.c 

CreateFile.o: CreateFile.h UserInterface.h
	$(CC) $(CFLAGS) -c CreateFile.c 

Game.o: Game.h LinkedList.h Board.h Missile.h UserInterface.h
	$(CC) $(CFLAGS) -c Game.c 

clean:
	rm -f $(EXE) $(OBJ)

reset:
	make clean
	make


