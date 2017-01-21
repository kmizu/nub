package com.github.kmizu.nub;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class AstNode {
    public interface ExpressionVisitor<E> {
        E visitBinaryOperation(BinaryOperation node);
        E visitNumber(Number node);
        E visitLetExpression(LetExpression node);
        E visitIdentifier(Identifier node);
        E visitPrintExpression(PrintExpression node);
        E visitExpressionList(ExpressionList node);
        E visitIfExpression(IfExpression node);
        E visitWhileExpression(WhileExpression node);
        E visitAssignmentOperation(AssignmentOperation node);
        E visitPrintlnExpression(PrintlnExpression node);
        E visitDefFunction(DefFunction node);
        E visitFunctionCall(FunctionCall node);
        E visitReturn(Return node);
    }

    public static abstract class Expression extends AstNode {
        public abstract <E> E accept(ExpressionVisitor<E> visitor);
    }

    public static class FunctionCall extends Expression {
        private final AstNode.Identifier       name;
        private final List<AstNode.Expression> params;
        public FunctionCall(AstNode.Identifier name, List<AstNode.Expression> params) {
            this.name = name;
            this.params = params;
        }
        public AstNode.Identifier name() {
            return name;
        }
        public List<AstNode.Expression> params() {
            return params;
        }

        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitFunctionCall(this); }
    }

    public static class Return extends Expression {
        private final Expression expression;

        public Return(Expression expression) {
            this.expression = expression;
        }

        public Expression expression() {
            return expression;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            return visitor.visitReturn(this);
        }
    }

    public static class DefFunction extends Expression {
        private final String                   name;
        private final List<String>             args;
        private final List<AstNode.Expression> body;
        public DefFunction(String name, List<String> args, List<AstNode.Expression> body) {
            this.name = name;
            this.args = args;
            this.body = body;
        }
        public String name() {
            return name;
        }
        public List<String> args() {
            return args;
        }
        public List<AstNode.Expression> body() {
            return body;
        }

        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitDefFunction(this); }
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

    public static class PrintlnExpression extends Expression {
        private final AstNode.Expression target;
        public PrintlnExpression(AstNode.Expression target) {
            this.target = target;
        }
        public AstNode.Expression target() {
            return target;
        }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) { return visitor.visitPrintlnExpression(this); }
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

    public static class StringLiteral extends Expression {
        private final String value;
        public StringLiteral(String value) { this.value = value; }
        public String value() { return value.substring(1, value.length() - 1); }

        @Override
        public <E> E accept(ExpressionVisitor<E> visitor) {
            throw new NotImplementedException();
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
