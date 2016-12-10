grammar Nub;
@header {
  package com.github.kmizu.nub;
}

toplevel returns [AstNode.Expression e]
   : w=letExpression {$e = $w.e;}
   | v=expressionStatement {$e = $v.e;}
   ;

expressionStatement returns [AstNode.Expression e]
   : v=expression SEMICOLON {$e = $v.e;}
   ;

letExpression returns [AstNode.LetExpression e]
    : LET name=IDENTIFIER EQ v=expression IN b=expression SEMICOLON {$e = new AstNode.LetExpression($name.getText(), $v.e, $b.e);}
    ;

expression returns [AstNode.Expression e]
    : v=additive {$e = $v.e;}
    ;

additive returns [AstNode.Expression e]
    : l=additive op='+' r=multitive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=additive op='-' r=multitive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | v=multitive {$e = $v.e;}
    ;

multitive returns [AstNode.Expression e]
    : l=multitive op='*' r=primary {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=multitive op='/' r=primary {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | v=primary {$e = $v.e;}
    ;

primary returns [AstNode.Expression e]
    : v=identifier {$e = $v.e;}
    | n=NUMBER {$e = new AstNode.Number(Integer.parseInt($n.getText()));}
    | '(' x=expression ')' {$e = $x.e;}
    ;

identifier returns [AstNode.Identifier e]
    : i=IDENTIFIER {$e = new AstNode.Identifier($i.getText());}
    ;

fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

LET
    : 'let'
    ;

IN
    : 'in'
    ;

IDENTIFIER
   : IDENTIFIER_START IDENTIFIER_PART*
   ;

fragment IDENTIFIER_START
   : 'a'..'z'
   | 'A'..'Z'
   | '_'
   ;

fragment IDENTIFIER_PART
   : IDENTIFIER_START
   | '0'..'9'
   ;

EQ
   : '='
   ;

NUMBER
    : '-'? INT
    ;

SEMICOLON
    : ';'
    ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros

WS  :   [ \t\n\r]+ -> skip ;