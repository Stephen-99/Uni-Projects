CFLAGS = -g -Wall -Werror
OBJ = scheduler.o LinkedList.o FileIO.o
EXE = scheduler
CC = gcc

$(EXE) : $(OBJ)
	$(CC) $(OBJ) -o $(EXE)


scheduler.o: scheduler.c scheduler.h LinkedList.o FileIO.o
	$(CC) $(CFLAGS) -c scheduler.c 

LinkedList.o: LinkedList.c LinkedList.h
	$(CC) $(CFLAGS) -c LinkedList.c 
	
FileIO.o: FileIO.c FileIO.h LinkedList.o
	$(CC) $(CFLAGS) -c FileIO.c 

clean:
	rm -f $(EXE) $(OBJ)

reset:
	make clean
	make


