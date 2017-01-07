package com.github.kmizu.nub;
import com.github.kmizu.nub.tool.Streams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        if(args.length == 0) {
            CommonTokenStream stream = Streams.streamFrom("def printRange(from, to) { let i = from; while(i < to) { println(i); i = i + 1; } } printRange(1, 10); // Loop");
            AstNode.ExpressionList program = new NubParser(stream).program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        } else {
            String fileName = args[0];
            CommonTokenStream stream = Streams.streamFrom(new File(fileName));
            NubParser parser = new NubParser(stream);
            AstNode.ExpressionList program = parser.program().e;
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(program);
        }
    }
}
