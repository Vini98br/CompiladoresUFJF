// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003
package lang.interpreter;

import lang.parser.grammar.antlr.LangBaseVisitor;
import lang.parser.grammar.antlr.LangParser;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.*;

public class Interpreter extends LangBaseVisitor<Object> {
    private final Vocabulary vocabulary;

    private final Stack<HashMap<String, Object>> programMemory;
    private final HashMap<String, LangParser.FuncContext> functions;
    private final ArrayList<Object> parametersBeingPassed;
    private final ArrayList<Object> expressionsBeingReturned;

    public Interpreter(ParseTree parseTree, Vocabulary vocabulary) {
        this.programMemory = new Stack<>();
        // esse seria o escopo global do programa, onde ficam os tipos criados pelo
        // usuário
        programMemory.push(new HashMap<>());

        this.functions = new HashMap<>();

        this.parametersBeingPassed = new ArrayList<>();
        this.expressionsBeingReturned = new ArrayList<>();

        this.vocabulary = vocabulary;

        this.visit(parseTree);
    }

    @Override
    public Object visitProg(LangParser.ProgContext ctx) {
        for (LangParser.DataContext customType : ctx.data()) {
            visitData(customType);
        }

        for (LangParser.FuncContext funcContext : ctx.func()) {
            functions.put(funcContext.ID().toString(), funcContext);
        }

        if (functions.get("main") != null) {
            return visitFunc(functions.get("main"));
        }

        // System.exit(40);
        return null;
    }

    @Override
    public Object visitData(LangParser.DataContext ctx) {
        // Como sabemos que toas as declarações de tipo vão rodar antes de tudo, sabemos
        // que o
        // `programMemory` só o primeiro item da pilha, que serve como o escopo global
        // do programa.
        HashMap<String, Object> globalProgramState = this.programMemory.peek();

        HashMap<String, Object> typeProperties = new HashMap<>();

        for (LangParser.DeclContext property : ctx.decl()) {
            Object propertyDefinition = visitDecl(property);

            if (propertyDefinition instanceof HashMap) {
                typeProperties.put(((HashMap) propertyDefinition).get("identifierName").toString(),
                        ((HashMap) propertyDefinition).get("identifierType"));
            }
        }

        globalProgramState.put(ctx.TYPE().getText(), typeProperties);

        return null;
    }

    @Override
    public Object visitDecl(LangParser.DeclContext ctx) {
        HashMap<String, Object> propertyDefinition = new HashMap<>();

        propertyDefinition.put("identifierName", ctx.ID().getText());
        propertyDefinition.put("identifierType", visitType(ctx.type()));

        return propertyDefinition;
    }

    @Override
    public Object visitFunc(LangParser.FuncContext ctx) {
        HashMap<String, Object> functionState = new HashMap<>();

        String functionName = ctx.ID().toString();

        LangParser.ParamsContext params = ctx.params();
        if (!functionName.equals("main") && params != null) {
            if (this.parametersBeingPassed.size() != params.ID().size()) {
                // System.exit(42);
                return null;
            }

            for (Object parameterIdentifier : params.ID()) {
                Object parameterValue = this.parametersBeingPassed.get(0);
                this.parametersBeingPassed.remove(0);

                functionState.put(parameterIdentifier.toString(), parameterValue);
            }
        }

        this.programMemory.push(functionState);

        for (LangParser.CmdContext command : ctx.cmd()) {
            Object commandReturnedValue = visitCmd(command);

            if (commandReturnedValue != null) {
                this.programMemory.pop();
                return commandReturnedValue;
            }
        }

        this.programMemory.pop();
        return null;
    }

    @Override
    public Object visitParams(LangParser.ParamsContext ctx) {
        return new Object();
    }

    @Override
    public Object visitType(LangParser.TypeContext ctx) {
        return new Object();
    }

    @Override
    public Object visitBtype(LangParser.BtypeContext ctx) {
        return super.visitBtype(ctx);
    }

    @Override
    public Object visitCmd(LangParser.CmdContext ctx) {
        HashMap<String, Object> currentState = this.programMemory.peek();

        if (ctx.ATTRIBUTION() != null) {
            Object identifier = visitLvalue(ctx.lvalue(0));
            Object expressionResult = visitExp(ctx.exp(0));

            currentState.put(identifier.toString(), expressionResult);
        }

        if (ctx.PRINT() != null) {
            Object expressionToBePrinted = visitExp(ctx.exp(0));

            if (expressionToBePrinted instanceof TerminalNodeImpl) {
                int tokenType = ((TerminalNodeImpl) expressionToBePrinted).getSymbol().getType();
                String tokenName = this.vocabulary.getSymbolicName(tokenType);
                if (tokenName.equals("ID")) {
                    System.out.println(currentState.get(expressionToBePrinted.toString()));
                }
            } else if (expressionToBePrinted instanceof String && ((String) expressionToBePrinted).contains(".")) {
                System.out.println(currentState.get(expressionToBePrinted));
            } else {
                System.out.println(expressionToBePrinted);
            }
        }

        if (ctx.READ() != null) {
            String variableToBeAssigned = ctx.lvalue(0).getText();
            Scanner reader = new Scanner(System.in);

            Object value = reader.next();

            currentState.put(variableToBeAssigned, parseToNumberIfPossible(value));
        }

        if (ctx.IF() != null) {
            Object expresionValue = visitExp(ctx.exp(0));

            if ((Boolean) expresionValue) {
                return visitCmd(ctx.cmd(0));
            } else if (ctx.ELSE() != null) {
                return visitCmd(ctx.cmd(1));
            }
        }

        if (ctx.ITERATE() != null) {
            Object expresionValue = visitExp(ctx.exp(0));

            while ((Boolean) expresionValue) {
                Object commandValue = visitCmd(ctx.cmd(0));

                if (commandValue != null) {
                    return commandValue;
                }

                expresionValue = visitExp(ctx.exp(0));
            }
        }

        if (ctx.RETURN() != null) {
            for (LangParser.ExpContext expression : ctx.exp()) {
                Object expressionToBeReturned = visitExp(expression);

                if (expressionToBeReturned instanceof TerminalNodeImpl) {
                    expressionToBeReturned = getVariableValue((TerminalNodeImpl) expressionToBeReturned);
                }

                this.expressionsBeingReturned.add(expressionToBeReturned);
            }

            return this.expressionsBeingReturned.get(0);
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().toString();
            LangParser.FuncContext function = this.functions.get(functionName);

            if (function == null) {
                // System.exit(41);
                return null;
            }

            if (ctx.exps() != null) {
                visitExps(ctx.exps());
            }

            visitFunc(function);

            if (ctx.LESS_THAN() != null) {
                if (ctx.lvalue().size() != this.expressionsBeingReturned.size()) {
                    // System.exit(43);
                }

                for (LangParser.LvalueContext identifier : ctx.lvalue()) {
                    currentState.put(identifier.getText(), this.expressionsBeingReturned.get(0));
                    this.expressionsBeingReturned.remove(0);
                }
            }
        }

        if (ctx.getChild(0) == ctx.OPEN_CURLY_BRACER()) {
            for (LangParser.CmdContext command : ctx.cmd()) {
                Object commandValue = visitCmd(command);

                if (commandValue != null) {
                    return commandValue;
                }
            }
        }

        return null;
    }

    @Override
    public Object visitExp(LangParser.ExpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitRexp(ctx.rexp());
        }

        Object leftSide = visitExp(ctx.exp(0));
        Object rightSide = visitExp(ctx.exp(1));

        return leftSide.equals(rightSide);
    }

    @Override
    public Object visitRexp(LangParser.RexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitAexp(ctx.aexp());
        }

        Object leftSide = visitRexp(ctx.rexp());
        Object rightSide = visitAexp(ctx.aexp());

        ParseTree operator = ctx.getChild(1);

        if (operator == ctx.LESS_THAN()) {
            return compareTwoObjects(leftSide, rightSide, "<");
        } else if (operator == ctx.MORE_THAN()) {
            return compareTwoObjects(leftSide, rightSide, ">");
        } else if (operator == ctx.EQUAL()) {
            return compareTwoObjects(leftSide, rightSide, "==");
        } else {
            return compareTwoObjects(leftSide, rightSide, "!=");
        }
    }

    @Override
    public Object visitAexp(LangParser.AexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitMexp(ctx.mexp());
        }

        Object leftSide = visitAexp(ctx.aexp());
        Object rightSide = visitMexp(ctx.mexp());

        if (ctx.PLUS() != null) {
            return executeMathOperation(leftSide, rightSide, '+');
        } else {
            return executeMathOperation(leftSide, rightSide, '-');
        }
    }

    @Override
    public Object visitMexp(LangParser.MexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitSexp(ctx.sexp());
        }

        Object leftSide = visitMexp(ctx.mexp());
        Object rightSide = visitSexp(ctx.sexp());

        if (ctx.MULT() != null) {
            return executeMathOperation(leftSide, rightSide, '*');
        } else if (ctx.DIV() != null) {
            return executeMathOperation(leftSide, rightSide, '/');
        } else {
            return executeMathOperation(leftSide, rightSide, '%');
        }
    }

    @Override
    public Object visitSexp(LangParser.SexpContext ctx) {
        ParseTree firstChild = ctx.getChild(0);

        if (firstChild == ctx.NOT()) {
            Object valueToBeNegated = visitSexp(ctx.sexp());

            if (valueToBeNegated instanceof TerminalNodeImpl) {
                valueToBeNegated = getVariableValue((TerminalNodeImpl) valueToBeNegated);
            }

            if (valueToBeNegated instanceof Boolean) {
                return !(Boolean) valueToBeNegated;
            }

            // System.exit(32);
        }

        if (firstChild == ctx.MINUS()) {
            Object valueToBeInverted = visitSexp(ctx.sexp());

            if (valueToBeInverted instanceof TerminalNodeImpl) {
                valueToBeInverted = getVariableValue((TerminalNodeImpl) valueToBeInverted);
            }

            if (valueToBeInverted instanceof Integer) {
                return -1 * (Integer) valueToBeInverted;
            }

            if (valueToBeInverted instanceof Double) {
                return -1 * (Double) valueToBeInverted;
            }

            // System.exit(23);
        }

        if (firstChild == ctx.INT()) {
            return Integer.parseInt(firstChild.getText());
        }

        if (firstChild == ctx.FLOAT()) {
            return Double.parseDouble(firstChild.getText());
        }

        if (firstChild == ctx.LITERAL()) {
            return firstChild.getText();
        }

        if (firstChild == ctx.TRUE()) {
            return true;
        }

        if (firstChild == ctx.FALSE()) {
            return false;
        }

        if (firstChild == ctx.NULL()) {
            return null;
        }

        return super.visitSexp(ctx);
    }

    @Override
    public Object visitPexp(LangParser.PexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitLvalue(ctx.lvalue());
        }

        if (ctx.getChild(0) == ctx.OPEN_PARENTESIS()) {
            return visitExp(ctx.exp());
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().toString();
            LangParser.FuncContext function = this.functions.get(functionName);

            if (function == null) {
                // System.exit(41);
                return null;
            }

            if (ctx.exps() != null) {
                visitExps(ctx.exps());
            }

            Object functionReturnValue = visitFunc(function);
            this.expressionsBeingReturned.clear();

            return functionReturnValue;
        }

        return super.visitPexp(ctx);
    }

    @Override
    public Object visitLvalue(LangParser.LvalueContext ctx) {
        if (ctx.ACCESSOR() != null) {
            return visitLvalue(ctx.lvalue()) + "." + ctx.ID().toString();
        }

        if (ctx.getChild(0) == ctx.ID()) {
            return ctx.getChild(0);
        }

        return super.visitLvalue(ctx);
    }

    @Override
    public Object visitExps(LangParser.ExpsContext ctx) {
        for (LangParser.ExpContext parameterExpression : ctx.exp()) {
            Object expressionResult = visitExp(parameterExpression);

            parametersBeingPassed.add(expressionResult);
        }

        return null;
    }

    private Number executeMathOperation(Object leftSide, Object rightSide, char operation) {
        if (leftSide instanceof TerminalNodeImpl) {
            leftSide = getVariableValue((TerminalNodeImpl) leftSide);
        }

        if (rightSide instanceof TerminalNodeImpl) {
            rightSide = getVariableValue((TerminalNodeImpl) rightSide);
        }

        if (rightSide.getClass() != leftSide.getClass()) {
            // System.exit(22);
        }

        switch (operation) {
            case '+':
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide + (Double) rightSide;
                }
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide + (Integer) rightSide;
                }
                break;

            case '-':
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide - (Double) rightSide;
                }
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide - (Integer) rightSide;
                }
                break;

            case '*':
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide * (Double) rightSide;
                }
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide * (Integer) rightSide;
                }
                break;

            case '/':
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide / (Double) rightSide;
                }
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide / (Integer) rightSide;
                }
                break;

            case '%':
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide % (Double) rightSide;
                }
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide % (Integer) rightSide;
                }
                break;

            default:
                // System.exit(20);
        }

        // System.exit(21);
        return 0;
    }

    private Object compareTwoObjects(Object leftSide, Object rightSide, String comparison) {
        if (leftSide instanceof TerminalNodeImpl) {
            leftSide = getVariableValue((TerminalNodeImpl) leftSide);
        }

        if (rightSide instanceof TerminalNodeImpl) {
            rightSide = getVariableValue((TerminalNodeImpl) rightSide);
        }

        switch (comparison) {
            case "<":
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide < (Integer) rightSide;
                }
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide < (Double) rightSide;
                }
                break;

            case ">":
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return (Integer) leftSide > (Integer) rightSide;
                }
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return (Double) leftSide > (Double) rightSide;
                }
                break;

            case "==":
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return leftSide.equals(rightSide);
                }
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return leftSide.equals(rightSide);
                }
                break;

            case "!=":
                if (leftSide instanceof Integer && rightSide instanceof Integer) {
                    return !leftSide.equals(rightSide);
                }
                if (leftSide instanceof Double && rightSide instanceof Double) {
                    return !leftSide.equals(rightSide);
                }
                break;

            default:
                // System.exit(30);
        }

        // System.exit(31);

        return false;
    }

    private Object getVariableValue(TerminalNodeImpl identifier) {
        int tokenType = identifier.getSymbol().getType();
        String tokenName = this.vocabulary.getSymbolicName(tokenType);

        String identifierName = identifier.toString();

        if (tokenName.equals("ID")) {
            if (this.programMemory.peek().containsKey(identifierName)) {
                return this.programMemory.peek().get(identifierName);
            }
        }

        // System.exit(10);
        return 0;
    }

    /**
     * Retorna o valor lido do teclado como um Integer ou Double se possível. Se
     * não, retorna uma String da mesma forma Sabemos que ficou estranho, mas foi o
     * jeito que pensamos para diferenciar entre os tipos;
     *
     * @param value valor à ser transformado
     * @return Integer | Double | String
     */
    private Object parseToNumberIfPossible(Object value) {
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException exception) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                return value.toString();
            }
        }
    }
}
