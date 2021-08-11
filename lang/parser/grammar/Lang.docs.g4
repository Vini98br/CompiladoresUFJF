/* Arthur Dornelas: 201735004, Vinicius Soranço: 201735003 */
grammar Lang;

/* Gera qualquer quantidade de data, seguido por qualquer quantidade de func */
prog: (data)* (func)*;

/* data Ponto{ x :: Float; y :: Float; } */
data: DATA TYPE OPEN_CURLY_BRACER (decl)* CLOSE_CURLY_BRACER;

/* x :: Float; */
decl: ID DOUBLE_COLON type SEMICOLON;

/* f(x :: Int) : Int, Float, ... { (cmd)* } */
func: ID OPEN_PARENTESIS (params)? CLOSE_PARENTESIS (COLON type (COMMA type)*)? OPEN_CURLY_BRACER (cmd)* CLOSE_CURLY_BRACER;

/* x :: Int, y :: Float, ...  */
params: ID DOUBLE_COLON type (COMMA ID DOUBLE_COLON type)*;

/* Int/Ponto [ ] */
type: type OPEN_BRACKET CLOSE_BRACKET
      | btype;

/* Int/Ponto */
btype: BTYPE
       | TYPE;


cmd: OPEN_CURLY_BRACER (cmd)* CLOSE_CURLY_BRACER  /* { (cmd)* } */
     | IF OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd /* if(exp) cmd */
     | IF OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd ELSE cmd /* if(exp) cmd else cmd */
     | ITERATE OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd /* iterate(exp) cmd */
     | READ lvalue SEMICOLON /* read lvalue; */
     | PRINT exp SEMICOLON /* print exp; */
     | RETURN exp (COMMA exp)* SEMICOLON /* return exp, ...; */
     | lvalue ATTRIBUTION exp SEMICOLON /* lvalue = exp; */
     | ID OPEN_PARENTESIS (exps)? CLOSE_PARENTESIS (LESS_THAN (COLON)? lvalue (COMMA lvalue)* (COLON)? MORE_THAN)? SEMICOLON; /* divMod(n,q)< quo,res > */

/* rexp && rexp */
exp: exp AND exp
     | rexp ;


rexp:  rexp LESS_THAN aexp /* aexp < aexp */
      | rexp MORE_THAN aexp /* aexp > aexp */
      | rexp EQUAL aexp /* aexp == aexp */
      | rexp NOT_EQUAL aexp /* aexp != aexp */
      | aexp;

aexp:  aexp PLUS mexp /* mexp + mexp */
      | aexp MINUS mexp /* mexp - mexp */
      | mexp;

mexp:  mexp MULT sexp /* sexp * sexp */
     | mexp DIV sexp /* sexp / sexp */
     | mexp MOD sexp /* sexp % sexp */
     | sexp;

sexp:  NOT sexp
     | MINUS sexp
     | TRUE
     | FALSE
     | NULL
     | INT
     | FLOAT
     | CHAR
     | LITERAL
     | pexp;

pexp: lvalue
     | OPEN_PARENTESIS exp CLOSE_PARENTESIS /* x = (1 > 2) */
     | NEW type (OPEN_BRACKET exp CLOSE_BRACKET)? /* new Ponto() */
     | ID OPEN_PARENTESIS (exps)? CLOSE_PARENTESIS (OPEN_BRACKET exp CLOSE_BRACKET)?; /* divMod(n,q)[1] */

lvalue: ID /* Ponto *
       | lvalue OPEN_BRACKET exp CLOSE_BRACKET /* Ponto[i+1] */
       | lvalue ACCESSOR ID; /* Ponto.x */

/* exp, exp, ...*/
exps: exp (COMMA exp)*;

// Reserved keywords
DATA      : 'data' ;
PRINT     : 'print' ;
IF        : 'if' ;
ELSE      : 'else' ;
ITERATE   : 'iterate' ;
TRUE      : 'true'  ;
FALSE     : 'false' ;
NEW       : 'new' ;
READ      : 'read' ;
RETURN    : 'return' ;


// Math Operators
PLUS                : '+' ;
MINUS               : '-' ;
MULT                : '*' ;
DIV                 : '/' ;
MOD                 : '%' ;

// Logical Operators
AND                 : '&&' ;
OR                  : '||' ;
NOT                 : '!' ;
LESS_THAN           : '<' ;
MORE_THAN           : '>' ;
EQUAL               : '==' ;
NOT_EQUAL           : '!=' ;

// Base Types
BTYPE: Int | Float | Char | Bool;
Int   : 'Int';
Float : 'Float';
Char  : 'Char';
Bool  : 'Bool';

// Separators
OPEN_PARENTESIS     : '(' ;
CLOSE_PARENTESIS    : ')' ;
OPEN_BRACKET        : '[' ;
CLOSE_BRACKET       : ']' ;
OPEN_CURLY_BRACER   : '{' ;
CLOSE_CURLY_BRACER  : '}' ;

// Language operators
ACCESSOR         : '.' ;
COLON            : ':' ;
SEMICOLON        : ';' ;
DOUBLE_COLON     : '::' ;
COMMA            : ',' ;
ATTRIBUTION      : '=' ;

// Skippable tokens
COMMENT           : '--' .*? ~[\r\n]* -> skip;
MULTILINE_COMMENT : '{-' .*? '-}' -> skip;
NEWLINE           : ([\r\n]+ | [\n]+) -> skip ;
WS                : [ \t]+ -> skip ;

// Identifiers
TYPE      : UPPERCASE (ID)* ;
BOOLEAN   : TRUE | FALSE;
NULL      :  '\'' '\\0' '\'' | 'null';
ID        : CHAR (CHAR | NUMBER | '_')* ;
FLOAT     : (NUMBER)* '.' (NUMBER)+ ;
INT       : (NUMBER)+ ;
LITERAL   : '\'' (CHAR | '\\n' | '\\t' | '\\\\' | UNICODE ) '\'';
CHAR      : LOWERCASE | UPPERCASE ;
NUMBER    : [0-9] ;
UPPERCASE : [A-Z] ;
LOWERCASE : [a-z] ;
UNICODE   : '\u0000'..'\u00FF';

