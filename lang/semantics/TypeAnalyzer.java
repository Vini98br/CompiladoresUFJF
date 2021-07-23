// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics;

import lang.parser.grammar.antlr.LangBaseVisitor;
import lang.parser.grammar.antlr.LangParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import lang.semantics.types.Float;
import lang.semantics.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class TypeAnalyzer extends LangBaseVisitor<Object> {
    private final HashMap<String, FunctionEnvironment> programEnvironment = new HashMap<>();
    private final HashMap<String, LangParser.FuncContext> functions = new HashMap<>();
    private final HashMap<String, Data> abstractDataTypes = new HashMap<>();

    private final Stack<FunctionEnvironment> functionEnvironments = new Stack<>();

    private final String filename;

    public TypeAnalyzer(ParseTree parseTree, String filename) {
        this.filename = filename;

        this.visit(parseTree);
    }

    public HashMap<String, FunctionEnvironment> getProgramEnvironment() {
        return programEnvironment;
    }

    @Override
    public Object visitProg(LangParser.ProgContext ctx) {
        for (LangParser.DataContext typeDeclaration : ctx.data()) {
            visitData(typeDeclaration);
        }

        for (LangParser.FuncContext functionContext : ctx.func()) {
            String functionName = functionContext.ID().getText();

            functions.put(functionName, functionContext);

            ArrayList<Type> functionParametersTypes = new ArrayList<>();
            if (functionContext.params() != null) {
                functionParametersTypes = visitParams(functionContext.params());
            }

            ArrayList<Type> functionReturnsTypes = new ArrayList<>();
            for (LangParser.TypeContext typeContext : functionContext.type()) {
                functionReturnsTypes.add(visitType(typeContext));
            }

            FunctionEnvironment functionEnvironment = new FunctionEnvironment(functionName, functionContext,
                    functionParametersTypes, functionReturnsTypes);

            programEnvironment.put(functionName, functionEnvironment);
        }

        if (functions.get("main") == null) {
            error(40, "Programa não contém a função 'main'", ctx);
        }

        return visitFunc(functions.get("main"));
    }

    @Override
    public Object visitData(LangParser.DataContext ctx) {
        String typeName = ctx.TYPE().toString();

        abstractDataTypes.put(typeName, new Data(typeName));

        for (LangParser.DeclContext property : ctx.decl()) {
            visitDecl(property, typeName);
        }

        return null;
    }

    public Object visitDecl(LangParser.DeclContext ctx, String typeName) {
        Data abstractDataType = abstractDataTypes.get(typeName);

        String propertyName = ctx.ID().getText();
        Type propertyType = visitType(ctx.type());

        abstractDataType.declareProperty(propertyName, propertyType);

        return null;
    }

    @Override
    public Object visitFunc(LangParser.FuncContext ctx) {
        String currentFunctionName = ctx.ID().getText();

        functionEnvironments.push(programEnvironment.get(currentFunctionName));

        FunctionEnvironment currentFunctionEnvironment = functionEnvironments.peek();
        currentFunctionEnvironment.markAsVisited();

        for (LangParser.CmdContext command : ctx.cmd()) {
            visitCmd(command);
        }

        ArrayList<Type> declaredFunctionReturns = currentFunctionEnvironment.getFunctionReturnTypes();
        ArrayList<ArrayList<Type>> valuesActuallyBeingReturned = currentFunctionEnvironment
                .getVariablesActuallyBeingReturned();

        if (declaredFunctionReturns.size() == 0 && valuesActuallyBeingReturned.size() > 0) {
            error(48,
                    String.format("A função '%s' não declarou nenhum retorno, porém há %s comandos return dentro dela",
                            currentFunctionName, valuesActuallyBeingReturned.size()),
                    ctx);
        }

        if (declaredFunctionReturns.size() > 0) {
            if (valuesActuallyBeingReturned.size() == 0) {
                String errorMessage = String.format(
                        "Foram especificados %s valores de retorno na declaração da função, mas %s estão sendo retornados",
                        declaredFunctionReturns.size(), 0);
                error(46, errorMessage, ctx);
            }

            for (ArrayList<Type> returnStatement : valuesActuallyBeingReturned) {
                if (returnStatement.size() != declaredFunctionReturns.size()) {
                    String errorMessage = String.format(
                            "Foram especificados %s valores de retorno na declaração da função, mas %s estão sendo retornados",
                            declaredFunctionReturns.size(), returnStatement.size());
                    error(46, errorMessage, ctx);
                }

                for (int i = 0; i < declaredFunctionReturns.size(); i++) {
                    if (!returnStatement.get(i).match(declaredFunctionReturns.get(i))) {
                        String errorMessage = String.format(
                                "O %sº retorno da função '%s' é do tipo %s, mas foi declarado como %s", i + 1,
                                currentFunctionName, returnStatement.get(i).typeName(),
                                declaredFunctionReturns.get(i).typeName());
                        error(47, errorMessage, ctx);
                    }
                }
            }
        }

        functionEnvironments.pop();

        return null;
    }

    @Override
    public ArrayList<Type> visitParams(LangParser.ParamsContext ctx) {
        ArrayList<Type> functionParameters = new ArrayList<>();

        for (LangParser.TypeContext type : ctx.type()) {
            functionParameters.add(visitType(type));
        }

        return functionParameters;
    }

    @Override
    public Type visitType(LangParser.TypeContext ctx) {
        if (ctx.btype() != null) {
            return visitBtype(ctx.btype());
        } else {
            return new Array(visitType(ctx.type()));
        }
    }

    @Override
    public Type visitBtype(LangParser.BtypeContext ctx) {
        String typeName = ctx.getText();

        if (typeName.equals("Int")) {
            return new Int();
        }

        if (typeName.equals("Float")) {
            return new Float();
        }

        if (typeName.equals("Bool")) {
            return new Bool();
        }

        if (typeName.equals("Char")) {
            return new Char();
        }

        if (!abstractDataTypes.containsKey(typeName)) {
            error(12, String.format("Tipo '%s' ainda não definido no programa", typeName), ctx);
            return null;
        }

        return new Data(typeName);
    }

    @Override
    public Object visitCmd(LangParser.CmdContext ctx) {
        FunctionEnvironment currentFunctionEnvironment = functionEnvironments.peek();

        if (ctx.ATTRIBUTION() != null) {
            Object variable = visitLvalue(ctx.lvalue(0));
            Type expressionType = visitExp(ctx.exp(0));

            if (!(variable instanceof Type)) {
                currentFunctionEnvironment.declareVariable((String) variable, expressionType);
                return null;
            }

            Type variableType = (Type) variable;

            if (!variableType.match(expressionType)) {
                error(11, String.format("Variável já declarada com tipo %s", variableType.typeName()), ctx);
            }
        }

        if (ctx.PRINT() != null) {
            visitExp(ctx.exp(0));
        }

        if (ctx.READ() != null) {
            Object variable = visitLvalue(ctx.lvalue(0));

            if (variable instanceof String) {
                currentFunctionEnvironment.declareVariable((String) variable, new UnreadValue());
                return null;
            }

            error(54, String.format("Tentativa de usar o comando read em uma variável já declarada com tipo %s",
                    ((Type) variable).typeName()), ctx);
            return null;
        }

        if (ctx.IF() != null) {
            Type expresionResult = visitExp(ctx.exp(0));

            if (!expresionResult.match(new Bool())) {
                error(50, String.format("Teste do comando 'if' não é uma expressão booleana, %s encontrado",
                        expresionResult.typeName()), ctx);
                return null;
            }

            for (LangParser.CmdContext command : ctx.cmd()) {
                visitCmd(command);
            }
        }

        if (ctx.ITERATE() != null) {
            Type expresionResult = visitExp(ctx.exp(0));

            if (!expresionResult.match(new Int())) {
                error(51, String.format("Teste do comando 'iterate' não é uma expressão booleana, %s encontrado",
                        expresionResult.typeName()), ctx);
                return null;
            }

            visitCmd(ctx.cmd(0));
        }

        if (ctx.RETURN() != null) {
            ArrayList<Type> variablesBeingReturned = new ArrayList<>();

            for (LangParser.ExpContext expContext : ctx.exp()) {
                variablesBeingReturned.add(visitExp(expContext));
            }

            currentFunctionEnvironment.addReturnStatementVariables(variablesBeingReturned);
        }

        if (ctx.OPEN_CURLY_BRACER() != null) {
            for (LangParser.CmdContext command : ctx.cmd()) {
                visitCmd(command);
            }
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().getText();

            testFunctionCall(functionName, ctx.exps(), ctx);

            ArrayList<Type> functionReturns = programEnvironment.get(functionName).getFunctionReturnTypes();

            if (ctx.lvalue().size() != functionReturns.size()) {
                error(44, String.format("A função '%s' retorna %s valores, %s recebidos", functionName,
                        functionReturns.size(), ctx.lvalue().size()), ctx);
                return null;
            }

            for (int i = 0; i < functionReturns.size(); i++) {
                Object receivingVariable = visitLvalue(ctx.lvalue(i));

                if (receivingVariable instanceof Type) {
                    if (!((Type) receivingVariable).match(functionReturns.get(i))) {
                        String errorMessage = String.format(
                                "O %s° retorno da função '%s' é do tipo %s, mas uma variável do tipo %s está recebendo",
                                i + 1, functionName, functionReturns.get(i).typeName(),
                                ((Type) receivingVariable).typeName());

                        error(45, errorMessage, ctx);

                        return null;
                    }
                }

                if (receivingVariable instanceof String) {
                    currentFunctionEnvironment.declareVariable((String) receivingVariable, functionReturns.get(i));
                }
            }

            FunctionEnvironment functionBeingVisited = programEnvironment.get(functionName);

            if (!functionBeingVisited.hasBeenVisited()) {
                visitFunc(functions.get(functionName));
            }
        }

        return null;
    }

    private void testFunctionCall(String functionName, LangParser.ExpsContext expsContext, ParserRuleContext ctx) {
        FunctionEnvironment functionBeingCalled = programEnvironment.get(functionName);
        LangParser.FuncContext funcContext = functions.get(functionName);

        if (functionBeingCalled == null) {
            error(41, String.format("A função '%s' sendo chamada não foi declarada no programa", functionName), ctx);
        }

        ArrayList<Type> parametersBeingPassed = visitExps(expsContext);
        ArrayList<Type> expectedParameters = functionBeingCalled.getFunctionParameterTypes();

        if (expectedParameters.size() != parametersBeingPassed.size()) {
            error(42, String.format("A função '%s' espera %s parâmetros, %s passados", functionName,
                    expectedParameters.size(), parametersBeingPassed.size()), ctx);
        }

        LangParser.ParamsContext paramsContext = funcContext.params();

        for (int i = 0; i < expectedParameters.size(); i++) {
            Type parameterBeingPassed = parametersBeingPassed.get(i);
            Type expectedParameter = expectedParameters.get(i);

            if (!parameterBeingPassed.match(expectedParameter)) {
                error(43, String.format("O %sº parâmetro esperado da função '%s' é do tipo %s, mas %s passado", i + 1,
                        functionName, expectedParameter.typeName(), parameterBeingPassed.typeName()), ctx);
            }

            functionBeingCalled.declareVariable(paramsContext.ID(i).getText(), expectedParameter);
        }
    }

    @Override
    public Type visitExp(LangParser.ExpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitRexp(ctx.rexp());
        }

        Type leftSide = visitExp(ctx.exp(0));
        Type rightSide = visitExp(ctx.exp(1));

        if (!leftSide.match(new Bool())) {
            error(31, "Operação 'AND' com o o lado esquerdo não booleano", ctx);
        }

        if (!rightSide.match(new Bool())) {
            error(31, "Operação 'AND' com o o lado direito não booleano", ctx);
        }

        return new Bool();
    }

    @Override
    public Type visitRexp(LangParser.RexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitAexp(ctx.aexp());
        }

        Type leftSide = visitRexp(ctx.rexp());
        Type rightSide = visitAexp(ctx.aexp());

        if (!leftSide.match(rightSide)) {
            error(33, "Operação booleana com dois tipos diferentes", ctx);
        }

        return new Bool();
    }

    @Override
    public Type visitAexp(LangParser.AexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitMexp(ctx.mexp());
        }

        Type leftSide = visitAexp(ctx.aexp());
        Type rightSide = visitMexp(ctx.mexp());

        if (!(leftSide.match(new Int()) || leftSide.match(new Float()))) {
            error(21, String.format(
                    "A operação matemática deve ser feita com tipos Int ou Float, mas tipo %s encontrado no lado esquerdo",
                    leftSide.typeName()), ctx);
        }

        if (!(rightSide.match(new Int()) || rightSide.match(new Float()))) {
            error(21, String.format(
                    "A operação matemática deve ser feita com tipos Int ou Float, mas tipo %s encontrado no lado direito",
                    rightSide.typeName()), ctx);
        }

        if (!leftSide.match(rightSide)) {
            error(22, "Operação matemática com dois tipos diferentes", ctx);
        }

        return leftSide;
    }

    @Override
    public Type visitMexp(LangParser.MexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitSexp(ctx.sexp());
        }

        Type leftSide = visitMexp(ctx.mexp());
        Type rightSide = visitSexp(ctx.sexp());

        if (!(leftSide.match(new Int()) || leftSide.match(new Float()))) {
            error(21, String.format(
                    "A operação matemática deve ser feita com tipos Int ou Float, mas tipo %s encontrado no lado esquerdo",
                    leftSide.typeName()), ctx);
        }

        if (!(rightSide.match(new Int()) || rightSide.match(new Float()))) {
            error(21, String.format(
                    "A operação matemática deve ser feita com tipos Int ou Float, mas tipo %s encontrado no lado direito",
                    rightSide.typeName()), ctx);
        }

        if (!leftSide.match(rightSide)) {
            error(22, "Operação matemática com dois tipos diferentes", ctx);
        }

        return leftSide;
    }

    @Override
    public Type visitSexp(LangParser.SexpContext ctx) {
        if (ctx.NOT() != null) {
            Type type = visitSexp(ctx.sexp());

            if (!(type.match(new Bool()))) {
                error(32, String.format("Operação 'NOT' requer um tipo booleano, %s encontrado", type.typeName()), ctx);
            }

            return new Bool();
        }

        if (ctx.MINUS() != null) {
            Type type = visitSexp(ctx.sexp());

            if (!(type.match(new Int()) || type.match(new Float()))) {
                error(23, String.format("Operação de inversão de sinal requer um tipo Int ou Float, %s encontrado",
                        type.typeName()), ctx);
            }

            return type;
        }

        if (ctx.INT() != null) {
            return new Int();
        }

        if (ctx.FLOAT() != null) {
            return new Float();
        }

        if (ctx.LITERAL() != null) {
            return new Char();
        }

        if (ctx.TRUE() != null) {
            return new Bool();
        }

        if (ctx.FALSE() != null) {
            return new Bool();
        }

        if (ctx.NULL() != null) {
            return null;
        }

        if (ctx.pexp() != null) {
            return visitPexp(ctx.pexp());
        }

        return null;
    }

    @Override
    public Type visitPexp(LangParser.PexpContext ctx) {
        FunctionEnvironment currentFunctionEnvironment = functionEnvironments.peek();

        if (ctx.lvalue() != null) {
            Object variable = visitLvalue(ctx.lvalue());

            if (!(variable instanceof Type)) {
                error(15, String.format("Variável '%s' ainda não foi declarada", variable), ctx);
                return null;
            }

            return (Type) variable;
        }

        if (ctx.NEW() != null) {
            Type type = visitType(ctx.type());

            if (ctx.OPEN_BRACKET() != null) {
                Type arraySize = visitExp(ctx.exp());

                if (!arraySize.match(new Int())) {
                    error(18, String.format("O tamanho de um array deve ser do tipo Int, %s encontrado",
                            arraySize.typeName()), ctx);
                }

                type = new Array(type);
            }

            return type;
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().getText();
            FunctionEnvironment functionEnvironment = programEnvironment.get(functionName);

            testFunctionCall(functionName, ctx.exps(), ctx);

            if (!functionEnvironment.hasBeenVisited()) {
                visitFunc(functions.get(functionName));
            }

            ArrayList<Type> functionReturnTypes = functionEnvironment.getFunctionReturnTypes();

            Integer parameterIndex = 0;
            int numberOfReturns = functionReturnTypes.size();

            if (numberOfReturns == 0) {
                error(49, String.format(
                        "A função '%s' não possui nenhum retorno, porém houve uma tentativa de usá-lo em uma expressão",
                        functionName), ctx);
            }

            if (numberOfReturns > 1) {
                if (ctx.OPEN_BRACKET() == null) {
                    error(52, String.format(
                            "A função '%s' retorna %s valores, por isso, é necessário passar o índice do parâmetro que será usado entre colchetes",
                            functionName, numberOfReturns), ctx);
                }

                Type type = visitExp(ctx.exp());
                if (!(type.match(new Int()))) {
                    error(53, String.format("O valor passado entre colchetes deve ser do tipo Int, %s passado",
                            type.typeName()), ctx);
                }

                parameterIndex = Integer.parseInt(ctx.exp().getText());
            }

            return functionReturnTypes.get(parameterIndex);
        }

        if (ctx.OPEN_PARENTESIS() != null) {
            return visitExp(ctx.exp());
        }

        return null;
    }

    @Override
    public Object visitLvalue(LangParser.LvalueContext ctx) {
        FunctionEnvironment currentFunctionEnvironment = functionEnvironments.peek();

        if (ctx.getChildCount() == 1) {
            String variableName = ctx.ID().getText();

            if (!currentFunctionEnvironment.isVariableDeclared(variableName)) {
                return variableName;
            }

            return currentFunctionEnvironment.getVariableType(variableName);
        }

        if (ctx.OPEN_BRACKET() != null) {
            Object variable = visitLvalue(ctx.lvalue());

            if (!(variable instanceof Type)) {
                error(16, String.format("Tentativa de acessar uma posição de uma variável ainda não inicializada"),
                        ctx);
                return null;
            }

            Type variableType = (Type) variable;

            if (variable instanceof Array) {
                return ((Array) variable).getType();
            } else {
                error(17, String.format("Tentativa de acessar uma variável do tipo %s como um array",
                        variableType.typeName()), ctx);
            }
        }

        if (ctx.ACCESSOR() != null) {
            Object variable = visitLvalue(ctx.lvalue());
            String propertyName = ctx.ID().getText();

            if (!(variable instanceof Type)) {
                error(13,
                        String.format("Tentativa de acessar a propriedade '%s' de uma variável ainda não inicializada",
                                propertyName),
                        ctx);
                return null;
            }

            Type variableType = (Type) variable;

            if (!(variableType instanceof Data)) {
                String typeName = variableType.typeName();

                error(14, String.format("Tentativa de acessar a propriedade '%s' de uma variável do tipo básico %s",
                        propertyName, typeName), ctx);

                return null;
            }

            Data abstractType = abstractDataTypes.get(variableType.typeName());

            if (!abstractType.hasProperty(propertyName)) {
                error(15, String.format("Tipo abstrato '%s' não possui a propriedade '%s'", abstractType.typeName(),
                        propertyName), ctx);
                return null;
            }

            return abstractType.getPropertyType(propertyName);
        }

        return null;
    }

    @Override
    public ArrayList<Type> visitExps(LangParser.ExpsContext ctx) {
        ArrayList<Type> types = new ArrayList<>();

        if (ctx != null) {
            for (LangParser.ExpContext expression : ctx.exp()) {
                types.add(visitExp(expression));
            }
        }

        return types;
    }

    public void error(Integer code, String message, ParserRuleContext context) {
        Integer line = context.getStart().getLine();
        Integer column = context.getStart().getCharPositionInLine();

        String errorMessage = String.format("%s. Linha %s | Coluna %s | Arquivo %s", message, line, column, filename);

        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");

        System.exit(code);
    }
}
