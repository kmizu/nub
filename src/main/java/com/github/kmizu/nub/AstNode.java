package com.github.kmizu.nub;

import java.util.List;

public class AstNode {
    public static interface ExpressionVisitor<E> {
        E visitBinaryOperation(BinaryOperation node);
        E visitNumber(Number node);
        E visitLetExpression(LetExpression node);
        E visitIdentifier(Identifier node);
        E visitPrintExpression(PrintExpression node);
        E visitExpressionList(ExpressionList node);
        E visitIfExpression(IfExpression node);
        E visitWhileExpression(WhileExpression node);
        E visitAssignmentOperation(AssignmentOperation node);
    }

    public static abstract class Expression extends AstNode {
        public abstract <E> E accept(ExpressionVisitor<E> visitor);
    }

    public static class LetExpression extends Expression {
        private final String variableName;
        private final AstNode.Expression expression;
        public LetExpression(String variableName, AstNode.Expression expression) {
            this.variableName = variableName;
            this.expression = expression;
        }
        public String variableName() {
            return variableName;
        }
        public AstNode.Expression expression() {
            return expression;
        }

        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitLetExpression(this); }
    }

    public static class AssignmentOperation extends Expression {
        private final String variableName;
        private final AstNode.Expression expression;
        public AssignmentOperation(String variableName, AstNode.Expression expression) {
            this.variableName = variableName;
            this.expression = expression;
        }
        public String variableName() {
            return variableName;
        }
        public AstNode.Expression expression() {
            return expression;
        }

        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitAssignmentOperation(this); }
    }

    public static class IfExpression extends Expression {
        private final AstNode.Expression condition;
        private final List<AstNode.Expression> thenClause, elseClause;
        public IfExpression(
            AstNode.Expression condition,
            List<AstNode.Expression> thenClause,
            List<AstNode.Expression> elseClause) {
            this.condition = condition;
            this.thenClause = thenClause;
            this.elseClause = elseClause;
        }

        public AstNode.Expression condition() {
            return condition;
        }

        public List<AstNode.Expression> thenClause() {
            return thenClause;
        }

        public List<AstNode.Expression> elseClause() {
            return elseClause;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitIfExpression(this);
        }
    }

    public static class WhileExpression extends Expression {
        private final AstNode.Expression condition;
        private final List<AstNode.Expression> body;
        public WhileExpression(AstNode.Expression condition, List<AstNode.Expression> body) {
            this.condition = condition;
            this.body = body;
        }

        public AstNode.Expression condition() {
            return condition;
        }

        public List<AstNode.Expression> body() {
            return body;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitWhileExpression(this);
        }
    }

    public static class PrintExpression extends Expression {
        private final AstNode.Expression target;
        public PrintExpression(AstNode.Expression target) {
            this.target = target;
        }
        public AstNode.Expression target() {
            return target;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitPrintExpression(this);
        }
    }

    public static class ExpressionList extends Expression {
        private final List<Expression> expressions;
        public ExpressionList(List<Expression> expressions) {
            this.expressions = expressions;
        }
        public List<Expression> expressions() {
            return expressions;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitExpressionList(this);
        }
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
