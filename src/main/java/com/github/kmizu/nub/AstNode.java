package com.github.kmizu.nub;

public class AstNode {
    public static interface Visitor<E> {
        E visitBinaryOperation(BinaryOperation node);
        E visitNumber(Number node);
    }

    public static abstract class Expression extends AstNode {
        public abstract <E> E accept(Visitor<E> visitor);
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

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitBinaryOperation(this);
        }
    }

    public static class Number extends Expression {
        private final int value;
        public Number(int value) {
            this.value = value;
        }
        public int value() { return value; }

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitNumber(this);
        }
    }
}
