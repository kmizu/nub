grammar Nub;
@header {
  package com.github.kmizu.nub;
}

expression returns [AstNode.Expression e] : v=additive {$e = $v.e;}
    ;

additive returns [AstNode.Expression e]
    : l=additive op='+' r=multitive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=additive op='-' r=multitive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | v=multitive {$e = $v.e;}
    ;

multitive returns [AstNode.Expression e]
    : l=multitive op='*' r=primary {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e); }
    | l=multitive op='/' r=primary {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e); }
    | v=primary {$e = $v.e;}
    ;

primary returns [AstNode.Expression e]
    :   n=NUMBER {$e = new AstNode.Number(Integer.parseInt($n.getText())); }
    |   '(' v=expression ')' {$e = $v.e;}
    ;

fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

NUMBER
    : '-'? INT
    ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros

WS  :   [ \t\n\r]+ -> skip ;