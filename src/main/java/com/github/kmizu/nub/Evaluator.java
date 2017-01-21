package com.github.kmizu.nub;

import java.util.*;

public class Evaluator implements AstNode.ExpressionVisitor<Object> {
    public static class Environment {
        public final Map<String, Object> mapping = new HashMap<>();
        public final Environment parent;
        public Environment(Environment parent) {
            this.parent = parent;
        }

        public Object find(String name) {
            Object value = mapping.get(name);
            if(value == null && parent != null) {
                value = parent.find(name);
            }
            return value;
        }

        public Optional<Environment> findEnvironment(String name) {
            if(mapping.containsKey(name))
                return Optional.of(this);
            else
                return parent != null ? parent.findEnvironment(name) : Optional.empty();
        }

        public boolean contains(String name) {
            boolean contains = mapping.containsKey(name);
            if(contains)
                return true;
            else
                return parent != null ? parent.contains(name) : false;
        }

        public Object register(String name, Object value) {
            return mapping.put(name, value);
        }
    }

    private Environment environment = new Environment(null);
    private Environment globalEnvironment = environment;
    private Map<String, AstNode.DefFunction> functions = new HashMap<>();

    private boolean asBoolean(Object value) {
        return ((Boolean)value).booleanValue();
    }

    private int asInt(Object value) {
        return ((Integer)value).intValue();
    }

    public Object visitBinaryOperation(AstNode.BinaryOperation node) {
        switch (node.operator()) {
            case "+":
                Object lhs = node.lhs().accept(this);
                Object rhs = node.rhs().accept(this);
                if(lhs instanceof String || rhs instanceof String) {
                    return lhs.toString() + rhs.toString();
                } else {
                    return asInt(lhs) + asInt(rhs);
                }
            case "-":
                return asInt(node.lhs().accept(this)) - asInt(node.rhs().accept(this));
            case "*":
                return asInt(node.lhs().accept(this)) * asInt(node.rhs().accept(this));
            case "/":
                return asInt(node.lhs().accept(this)) / asInt(node.rhs().accept(this));
            case "<=":
                return asInt((node.lhs().accept(this))) <= asInt(node.rhs().accept(this)) ? 1 : 0;
            case ">=":
                return (asInt(node.lhs().accept(this)) >= asInt(node.rhs().accept(this))) ? 1 : 0;
            case "<":
                return (asInt(node.lhs().accept(this)) < asInt(node.rhs().accept(this))) ? 1 : 0;
            case ">":
                return (asInt(node.lhs().accept(this)) > asInt(node.rhs().accept(this))) ? 1 : 0;
            case "==":
                return (node.lhs().accept(this).equals(node.rhs().accept(this))) ? 1 : 0;
            case "!=":
                return (!(node.lhs().accept(this).equals(node.rhs().accept(this)))) ? 1 : 0;
            case "&&": {
                Integer value1 = asInt(node.lhs().accept(this));
                return value1.equals(0) ? 0 : node.rhs().accept(this);
            }
            case "||": {
                Integer value1 = asInt(node.lhs().accept(this));
                return (!value1.equals(0)) ? value1 : node.rhs().accept(this);
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
    public Object visitLetExpression(AstNode.LetExpression node) {
        Object value = node.expression().accept(this);
        if(environment.contains(node.variableName())) {
            throw new NubRuntimeException("variable " + node.variableName() + " is already defined");
        }
        environment.register(node.variableName(), value);
        return value;
    }

    @Override
    public Object visitAssignmentOperation(AstNode.AssignmentOperation node) {
        Object value = node.expression().accept(this);
        Optional<Environment> found = environment.findEnvironment(node.variableName());
        if(!found.isPresent()) {
            throw new NubRuntimeException("variable " + node.variableName() + " is not defined");
        } else {
            found.get().register(node.variableName(), value);
        }
        return value;
    }

    @Override
    public Object visitPrintExpression(AstNode.PrintExpression node) {
        Object value = node.target().accept(this);
        System.out.print(value);
        return value;
    }

    @Override
    public Object visitPrintlnExpression(AstNode.PrintlnExpression node) {
        Object value = node.target().accept(this);
        System.out.println(value);
        return value;
    }

    @Override
    public Object visitExpressionList(AstNode.ExpressionList node) {
        Object last = 0;
        for (AstNode.Expression e : node.expressions()) {
            last = e.accept(this);
        }
        return last;
    }

    @Override
    public Object visitWhileExpression(AstNode.WhileExpression node) {
        Object last = 0;
        while(asInt(node.condition().accept(this)) != 0) {
            for(AstNode.Expression e:node.body()) {
                last = e.accept(this);
            }
        }
        return last;
    }

    @Override
    public Object visitIfExpression(AstNode.IfExpression node) {
        Integer condition = asInt(node.condition().accept(this));
        Object last = 0;
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
    public Object visitDefFunction(AstNode.DefFunction node) {
        return null;
    }

    @Override
    public Object visitIdentifier(AstNode.Identifier node) {
        Object ret = environment.find(node.name());
        if (ret == null)
            throw new NubRuntimeException(node.name() + " is not defined");
        else
            return ret;
    }

    public Object evaluate(AstNode.ExpressionList program) {
        for(AstNode.Expression top:program.expressions()) {
            if(top instanceof AstNode.DefFunction) {
                AstNode.DefFunction f = (AstNode.DefFunction)top;
                functions.put(f.name(), f);
            }
        }
        return program.accept(this);
    }

    @Override
    public Object visitFunctionCall(AstNode.FunctionCall node) {
        AstNode.DefFunction function = functions.get(node.name().name());
        if(function == null) {
            throw new NubRuntimeException("function " + node.name().name() + " is not defined");
        }
        List<String> args = function.args();
        if(args.size() != node.params().size()) {
            throw new NubRuntimeException("function " + node.name().name() + " arity mismatch! required length: " + args.size() + " actual length:" + node.params().size());
        }
        {
            Environment prevEnvironment = environment;
            List<Object> values = new ArrayList<>();
            //params must be evaluated before switching environment
            for(AstNode.Expression e:node.params()) {
                values.add(e.accept(this));
            }
            environment = new Environment(globalEnvironment);
            for (int i = 0; i < args.size(); i++) {
                environment.register(args.get(i), values.get(i));
            }
            Object last = null;
            try {
                for (AstNode.Expression e : function.body()) {
                    last = e.accept(this);
                }
            }catch(ReturnException ex) {
                last = ex.value();
            }
            environment = prevEnvironment;
            return last;
        }
    }

    @Override
    public Object visitReturn(AstNode.Return node) {
        Object value = node.expression().accept(this);
        throw new ReturnException(value);
    }
}
