package com.github.kmizu.nub;

public class AstNode {
    public static interface ExpressionVisitor<E> {
        E visitBinaryOperation(BinaryOperation node);
        E visitNumber(Number node);
        E visitLetExpression(LetExpression node);
        E visitIdentifier(Identifier node);
    }

    public static abstract class Expression extends AstNode {
        public abstract <E> E accept(ExpressionVisitor<E> visitor);
    }

    public static class LetExpression extends Expression {
        private final String variableName;
        private final AstNode.Expression expression;
        private final AstNode.Expression body;
        public LetExpression(String variableName, AstNode.Expression expression, AstNode.Expression body) {
            this.variableName = variableName;
            this.expression = expression;
            this.body = body;
        }
        public String variableName() {
            return variableName;
        }
        public AstNode.Expression expression() {
            return expression;
        }
        public AstNode.Expression body() {
            return body;
        }

        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitLetExpression(this); }
    }

    public static class BinaryOperation extends Expression {
        private final String operator;
        private final Expression lhs, rhs;
        public BinaryOperation(String operator, Expression lhs, Expression rhs) {
            this.operator = operator;
            this.lhs = lhs;
            this.rhs = rhs;
        }
        public String operator() { return operator; }
        public Expression lhs() { return lhs; }
        public Expression rhs() { return rhs; }

        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitBinaryOperation(this);
        }
    }

    public static class Number extends Expression {
        private final int value;
        public Number(int value) {
            this.value = value;
        }
        public int value() { return value; }

        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitNumber(this);
        }
    }

    public static class Identifier extends Expression {
        private final String name;
        public Identifier(String name) {
            this.name = name;
        }
        public String name() { return name; }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitIdentifier(this);
        }
    }
}
