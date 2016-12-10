package com.github.kmizu.nub;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static class Evaluator implements AstNode.ExpressionVisitor<Integer> {
        private Map<String, Integer> environment = new HashMap<>();
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

        @Override
        public Integer visitNumber(AstNode.Number node) {
            return node.value();
        }

        @Override
        public Integer visitLetExpression(AstNode.LetExpression node) {
            environment.put(node.variableName(), node.expression().accept(this));
            return node.body().accept(this);
        }

        @Override
        public Integer visitIdentifier(AstNode.Identifier node) {
            return environment.get(node.name());
        }
    }
    public static void main(String[] args) throws IOException{
        ANTLRInputStream input = new ANTLRInputStream(new StringReader("let x = (1 + 2 + 3) * 4 / 5 in x * x;"));
        NubLexer lexer = new NubLexer(input);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        AstNode.Expression expression = new NubParser(stream).toplevel().e;
        System.out.println(expression.accept(new Evaluator()));
    }
}
