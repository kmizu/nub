package com.github.kmizu.nub;
import com.github.kmizu.nub.NubLexer;
import com.github.kmizu.nub.NubParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static class Evaluator implements AstNode.Visitor<Integer> {
        public Integer visitBinaryOperation(AstNode.BinaryOperation node) {
            switch(node.operator()) {
                case "+":
                    return node.lhs().accept(this) + node.rhs().accept(this);
                case "-":
                    return node.lhs().accept(this) - node.rhs().accept(this);
                case "*":
                    return node.lhs().accept(this) * node.rhs().accept(this);
                case "/":
                    return node.lhs().accept(this) / node.rhs().accept(this);
                default:
                    throw new RuntimeException("cannot reach here");
            }
        }

        public Integer visitNumber(AstNode.Number node) {
            return node.value();
        }
    }
    public static void main(String[] args) throws IOException{
        ANTLRInputStream input = new ANTLRInputStream(new StringReader("(1+2)*(3+4)"));
        NubLexer lexer = new NubLexer(input);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        AstNode.Expression expression = new NubParser(stream).expression().e;
        System.out.println(expression.accept(new Evaluator()));
    }
}
