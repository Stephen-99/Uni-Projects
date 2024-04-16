%{
    #include <stdio.h>
    #include "y.tab.h"
%}

%%
[a-zA_Z][a-zA-Z0-9]*    return WORD;
-?[0-9]+                  yylval=atoi(yytext); return NUMBER;
\[                      return LH_SQ_BRACKET;
\]                      return RH_SQ_BRACKET;
,                       return COMMA;
\n                      /*ignore EOL*/
[ \t]+                  /*ignore spaces and tabs*/
%%
