package com.github.kmizu.nub;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException{
        if(args.length == 0) {
            ANTLRInputStream input = new ANTLRInputStream(new StringReader("def printRange(from, to) { let i = from; while(i < to) { println(i); i = i + 1; } } printRange(1, 10); // Loop"));
            NubLexer lexer = new NubLexer(input);
            CommonTokenStream stream = new CommonTokenStream(lexer);
            AstNode.ExpressionList program = new NubParser(stream).program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        } else {
            String fileName = args[0];
            ANTLRInputStream antlrInput = new ANTLRInputStream(new FileInputStream(new File(fileName)));
            CommonTokenStream stream = new CommonTokenStream(new NubLexer(antlrInput));
            NubParser parser = new NubParser(stream);
            AstNode.ExpressionList program = parser.program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        }
    }
}
