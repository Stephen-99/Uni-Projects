FLAGS = -g -Werror -Wall -Wextra -pedantic
OBJ = book.o driver.o quicksort.o
EXE = quicksort

$(EXE) : $(OBJ)
	g++ $(OBJ) -o $(EXE) 

book.o : book.cpp book.h
	g++ $(FLAGS) -c book.cpp

driver.o : driver.cpp book.h
	g++ $(FLAGS) -c driver.cpp

quicksort.o : quicksort.cpp quicksort.h
	g++ $(FLAGS) -c quicksort.cpp

clean:
	rm $(EXE) $(OBJ)
