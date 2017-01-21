grammar Nub;

program returns [AstNode.ExpressionList e]
   : v=toplevels[new java.util.ArrayList<AstNode.Expression>()] EOF {$e = new AstNode.ExpressionList($v.e); }
   ;

block returns [List<AstNode.Expression> e]
   :  LB v=toplevels[new java.util.ArrayList<AstNode.Expression>()] RB {$e = $v.e;}
   ;

toplevels[List<AstNode.Expression> es] returns [List<AstNode.Expression> e]
   : (v=toplevel {$es.add($v.e);})+ {$e = $es;}
   ;

toplevel returns [AstNode.Expression e]
   : letExpression {$e = $letExpression.e;}
   | lineExpression {$e = $lineExpression.e;}
   | printExpression {$e = $printExpression.e;}
   | printlnExpression {$e = $printlnExpression.e;}
   | ifExpression {$e = $ifExpression.e;}
   | whileExpression {$e = $whileExpression.e;}
   | defFunction {$e = $defFunction.e;}
   | returnExpression {$e = $returnExpression.e;}
   ;

defFunction returns [AstNode.DefFunction e]
   : DEF n=IDENTIFIER LP ps=params RP b=block { $e = new AstNode.DefFunction($n.getText(), $ps.result, $b.e); }
   ;

params returns [List<String> result]
   @init {
     List<String> ps = new ArrayList<String>();
   }
   : (n=IDENTIFIER {ps.add($n.getText());} (',' n=IDENTIFIER {ps.add($n.getText());})*)? { $result = ps; }
   ;

returnExpression returns [AstNode.Return e]
  : RETURN c=expression SEMICOLON {$e = new AstNode.Return($c.e);}
  ;

whileExpression returns [AstNode.WhileExpression e]
   : WHILE c=expression b=block {$e = new AstNode.WhileExpression($c.e, $b.e);}
   ;

ifExpression returns [AstNode.IfExpression e]
   : IF c=expression th=block ELSE el=block {$e = new AstNode.IfExpression($c.e, $th.e, $el.e);}
   | IF c=expression th=block {$e = new AstNode.IfExpression($c.e, $th.e, new ArrayList<AstNode.Expression>());}
   ;

lineExpression returns [AstNode.Expression e]
   : v=expression SEMICOLON {$e = $v.e;}
   ;

letExpression returns [AstNode.LetExpression e]
    : LET name=IDENTIFIER EQ v=expression SEMICOLON {$e = new AstNode.LetExpression($name.getText(), $v.e);}
    ;

printExpression returns[AstNode.PrintExpression e]
    : PRINT LP v=expression RP SEMICOLON {$e = new AstNode.PrintExpression($v.e);}
    ;

printlnExpression returns[AstNode.PrintlnExpression e]
    : PRINTLN LP v=expression RP SEMICOLON {$e = new AstNode.PrintlnExpression($v.e);}
    ;

expression returns [AstNode.Expression e]
    : v=assignment {$e = $v.e;}
    ;

assignment returns [AstNode.Expression e]
    : name=IDENTIFIER EQ x=expression {$e = new AstNode.AssignmentOperation($name.getText(), $x.e);}
    | v=logical {$e = $v.e;}
    ;
    
logical returns [AstNode.Expression e]
    : l=logical op='&&' r=conditional {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=logical op='||' r=conditional {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | v=conditional {$e = $v.e;}
    ;

conditional returns [AstNode.Expression e]
    : l=conditional op='<=' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=conditional op='>=' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=conditional op='<' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=conditional op='>' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=conditional op='==' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | l=conditional op='!=' r=additive {$e = new AstNode.BinaryOperation($op.getText(), $l.e, $r.e);}
    | v=additive {$e = $v.e;}
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
    @init {
      List<AstNode.Expression> params = new ArrayList<AstNode.Expression>();
    }
    : v=identifier {$e = $v.e;} (LP ((p=expression {params.add($p.e);} (',' p=expression {params.add($p.e);})*)?) RP {$e = new AstNode.FunctionCall($v.e, params);})?
    | n=NUMBER {$e = new AstNode.Number(Integer.parseInt($n.getText()));}
    | s=STRING {$e = new AstNode.StringLiteral($s.getText());}
    | '(' x=expression ')' {$e = $x.e;}
    | ifExpression {$e = $ifExpression.e;}
    | whileExpression {$e = $whileExpression.e;}
    ;

identifier returns [AstNode.Identifier e]
    : i=IDENTIFIER {$e = new AstNode.Identifier($i.getText());}
    ;


DEF
    : 'def'
    ;

PRINTLN
    : 'println'
    ;

PRINT
    : 'print'
    ;

LET
    : 'let'
    ;

IN
    : 'in'
    ;

IF  : 'if'
    ;

ELSE : 'else'
    ;

WHILE : 'while'
   ;

RETURN : 'return'
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

LB : '{'
   ;

RB : '}'
   ;

LP
   : '('
   ;

RP : ')'
   ;

NUMBER
    : INT
    ;

SEMICOLON
    : ';'
    ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros

LINE_COMMENT : ('//' (~[\r\n])* (EOF | '\r\n' | '\n' | '\r')) -> skip;

NESTED_COMMENT : '/*' (NESTED_COMMENT | .)*? '*/' -> skip;

WS  :   [ \t\n\r]+ -> skip ;

STRING
   : '"' (ESC | ~ ["\\])* '"'
   ;

fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;
