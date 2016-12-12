package com.github.kmizu.nub;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    private static CommonTokenStream streamFrom(String input) throws IOException {
        ANTLRInputStream antlrStream = new ANTLRInputStream(new StringReader(input));
        NubLexer lexer = new NubLexer(antlrStream);
        return new CommonTokenStream(lexer);
    }
    private static CommonTokenStream streamFrom(File file) throws IOException {
        ANTLRInputStream antlrStream = new ANTLRInputStream(new FileInputStream(file));
        NubLexer lexer = new NubLexer(antlrStream);
        return new CommonTokenStream(lexer);
    }
    public static void main(String[] args) throws IOException{
        if(args.length == 0) {
            CommonTokenStream stream = streamFrom("def printRange(from, to) { let i = from; while(i < to) { println(i); i = i + 1; } } printRange(1, 10); // Loop");
            AstNode.ExpressionList program = new NubParser(stream).program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        } else {
            String fileName = args[0];
            CommonTokenStream stream = streamFrom(new File(fileName));
            NubParser parser = new NubParser(stream);
            AstNode.ExpressionList program = parser.program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        }
    }
}
