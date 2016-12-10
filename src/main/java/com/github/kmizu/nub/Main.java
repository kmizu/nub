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
                case "<=":
                    return (node.lhs().accept(this) <= node.rhs().accept(this)) ? 1 : 0;
                case ">=":
                    return (node.lhs().accept(this) >= node.rhs().accept(this)) ? 1 : 0;
                case "<":
                    return (node.lhs().accept(this) < node.rhs().accept(this)) ? 1 : 0;
                case ">":
                    return (node.lhs().accept(this) > node.rhs().accept(this)) ? 1 : 0;
                case "==":
                    return (node.lhs().accept(this) == node.rhs().accept(this)) ? 1 : 0;
                case "!=":
                    return (node.lhs().accept(this) == node.rhs().accept(this)) ? 1 : 0;
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
            Integer value = node.expression().accept(this);
            environment.put(node.variableName(), value);
            return value;
        }

        @Override
        public Integer visitPrintExpression(AstNode.PrintExpression node) {
            Integer value = node.target().accept(this);
            System.out.println(value);
            return value;
        }

        @Override
        public Integer visitExpressionList(AstNode.ExpressionList node) {
            Integer last = null;
            for(AstNode.Expression e:node.expressions()) {
                last = e.accept(this);
            }
            return last;
        }

        @Override
        public Integer visitIdentifier(AstNode.Identifier node) {
            return environment.get(node.name());
        }
    }
    public static void main(String[] args) throws IOException{
        ANTLRInputStream input = new ANTLRInputStream(new StringReader("let x = (1 + 2 + 3) * 4 / 4; print(x * x); x + x;"));
        NubLexer lexer = new NubLexer(input);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        AstNode.Expression expression = new NubParser(stream).program().e;
        System.out.println(expression.accept(new Evaluator()));
    }
}
