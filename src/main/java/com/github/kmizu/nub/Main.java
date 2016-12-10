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
            ANTLRInputStream input = new ANTLRInputStream(new StringReader("let x = 0; while(x < 10) { println(x); x = x + 1; } // Loop"));
            NubLexer lexer = new NubLexer(input);
            CommonTokenStream stream = new CommonTokenStream(lexer);
            AstNode.Expression expression = new NubParser(stream).program().e;
            System.out.println(expression.accept(new Evaluator()));
        } else {
            String fileName = args[0];
            ANTLRInputStream antlrInput = new ANTLRInputStream(new FileInputStream(new File(fileName)));
            CommonTokenStream stream = new CommonTokenStream(new NubLexer(antlrInput));
            NubParser parser = new NubParser(stream);
            AstNode.Expression program = parser.program().e;
            program.accept(new Evaluator());
        }
    }
}
