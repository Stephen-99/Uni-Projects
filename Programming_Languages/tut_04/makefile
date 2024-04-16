CFILES = lex.yy.c y.tab.c LinkedList.c
EXE = lists

$(EXE) : lex.yy.c y.tab.c
	cc -g $(CFILES) -o $(EXE)

y.tab.c : $(EXE).y
	yacc -d $(EXE).y

lex.yy.c : $(EXE).l
	flex $(EXE).l

clean:
	rm -f $(EXE) $(CFILES)

reset:
	make clean
	make

