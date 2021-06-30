/* Arthur Dornelas: 201735004, Vinicius Soranço: 201735003 */
grammar Lang;

prog: (data)* (func)*;

data: DATA TYPE OPEN_CURLY_BRACER (decl)* CLOSE_CURLY_BRACER;

decl: ID DOUBLE_COLON type SEMICOLON;

func: ID OPEN_PARENTESIS (params)? CLOSE_PARENTESIS (COLON type (COMMA type)*)? OPEN_CURLY_BRACER (cmd)* CLOSE_CURLY_BRACER;

params: ID DOUBLE_COLON type (COMMA ID DOUBLE_COLON type)*;

type: type OPEN_BRACKET CLOSE_BRACKET
      | btype;

btype: BTYPE
       | TYPE;

cmd: OPEN_CURLY_BRACER (cmd)* CLOSE_CURLY_BRACER
     | IF OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd
     | IF OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd ELSE cmd
     | ITERATE OPEN_PARENTESIS exp CLOSE_PARENTESIS cmd
     | READ lvalue SEMICOLON
     | PRINT exp SEMICOLON
     | RETURN exp (COMMA exp)* SEMICOLON
     | lvalue ATTRIBUTION exp SEMICOLON
     | ID OPEN_PARENTESIS (exps)? CLOSE_PARENTESIS (LESS_THAN (COLON)? lvalue (COMMA lvalue)* (COLON)? MORE_THAN)? SEMICOLON;


exp: exp AND exp
     | rexp ;

rexp:  rexp LESS_THAN aexp
      | rexp MORE_THAN aexp
      | rexp EQUAL aexp
      | rexp NOT_EQUAL aexp
      | aexp;

aexp:  aexp PLUS mexp
      | aexp MINUS mexp
      | mexp;

mexp:  mexp MULT sexp
     | mexp DIV sexp
     | mexp MOD sexp
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
     | OPEN_PARENTESIS exp CLOSE_PARENTESIS
     | NEW type (OPEN_BRACKET exp CLOSE_BRACKET)?
     | ID OPEN_PARENTESIS (exps)? CLOSE_PARENTESIS (OPEN_BRACKET exp CLOSE_BRACKET)?;

lvalue: ID
       | lvalue OPEN_BRACKET exp CLOSE_BRACKET
       | lvalue ACCESSOR ID;

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

