package com.github.kmizu.nub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator implements AstNode.ExpressionVisitor<Integer> {
    public static class Environment {
        public final Map<String, Integer> mapping = new HashMap<>();
        public final Environment parent;
        public Environment(Environment parent) {
            this.parent = parent;
        }

        public Integer find(String name) {
            Integer value = mapping.get(name);
            if(value == null && parent != null) {
                value = parent.find(name);
            }
            return value;
        }

        public boolean contains(String name) {
            boolean contains = mapping.containsKey(name);
            if(contains)
                return true;
            else
                return parent != null ? parent.contains(name) : false;
        }

        public Integer register(String name, Integer value) {
            return mapping.put(name, value);
        }
    }
    private Environment environment = new Environment(null);
    private Map<String, AstNode.DefFunction> functions = new HashMap<>();

    public Integer visitBinaryOperation(AstNode.BinaryOperation node) {
        switch (node.operator()) {
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
                return (node.lhs().accept(this).equals(node.rhs().accept(this))) ? 1 : 0;
            case "!=":
                return (!(node.lhs().accept(this).equals(node.rhs().accept(this)))) ? 1 : 0;
            case "&&": {
                Integer value1 = node.lhs().accept(this);
                return value1 == 0 ? 0 : node.rhs().accept(this);
            }
            case "||": {
                Integer value1 = node.lhs().accept(this);
                return value1 != 0 ? value1 : node.rhs().accept(this);
            }
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
        if(environment.contains(node.variableName())) {
            throw new NubRuntimeException("variable " + node.variableName() + " is already defined");
        }
        environment.register(node.variableName(), value);
        return value;
    }

    @Override
    public Integer visitAssignmentOperation(AstNode.AssignmentOperation node) {
        Integer value = node.expression().accept(this);
        if(!environment.contains(node.variableName())) {
            throw new NubRuntimeException("variable " + node.variableName() + " is not defined");
        }
        environment.register(node.variableName(), value);
        return value;
    }

    @Override
    public Integer visitPrintExpression(AstNode.PrintExpression node) {
        Integer value = node.target().accept(this);
        System.out.print(value);
        return value;
    }

    @Override
    public Integer visitPrintlnExpression(AstNode.PrintlnExpression node) {
        Integer value = node.target().accept(this);
        System.out.println(value);
        return value;
    }

    @Override
    public Integer visitExpressionList(AstNode.ExpressionList node) {
        Integer last = 0;
        for (AstNode.Expression e : node.expressions()) {
            last = e.accept(this);
        }
        return last;
    }

    @Override
    public Integer visitWhileExpression(AstNode.WhileExpression node) {
        Integer last = 0;
        while(node.condition().accept(this) != 0) {
            for(AstNode.Expression e:node.body()) {
                last = e.accept(this);
            }
        }
        return last;
    }

    @Override
    public Integer visitIfExpression(AstNode.IfExpression node) {
        Integer condition = node.condition().accept(this);
        Integer last = 0;
        if (condition != 0) {
            for (AstNode.Expression e : node.thenClause()) {
                last = e.accept(this);
            }
        } else {
            for (AstNode.Expression e : node.elseClause()) {
                last = e.accept(this);
            }
        }
        return last;
    }

    @Override
    public Integer visitDefFunction(AstNode.DefFunction node) {
        return null;
    }

    @Override
    public Integer visitIdentifier(AstNode.Identifier node) {
        return environment.find(node.name());
    }

    public Integer evaluate(AstNode.ExpressionList program) {
        for(AstNode.Expression top:program.expressions()) {
            if(top instanceof AstNode.DefFunction) {
                AstNode.DefFunction f = (AstNode.DefFunction)top;
                functions.put(f.name(), f);
            }
        }
        return program.accept(this);
    }

    @Override
    public Integer visitFunctionCall(AstNode.FunctionCall node) {
        AstNode.DefFunction function = functions.get(node.name().name());
        if(function == null) {
            throw new NubRuntimeException("function " + node.name().name() + "is not defined");
        }
        List<String> args = function.args();
        if(args.size() != node.params().size()) {
            throw new NubRuntimeException("function arity mismatch! required length: " + args.size() + " actual length:" + node.params().size());
        }
        Environment newEnvironment = new Environment(environment);
        {
            for (int i = 0; i < args.size(); i++) {
                newEnvironment.register(args.get(i), node.params().get(i).accept(this));
            }
            environment = newEnvironment;
            Integer last = 0;
            for (AstNode.Expression e : function.body()) {
                last = e.accept(this);
            }
            environment = environment.parent;
            return last;
        }
    }
}
