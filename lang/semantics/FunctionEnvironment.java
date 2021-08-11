// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics;

import lang.parser.grammar.antlr.LangParser;
import lang.semantics.types.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionEnvironment {
    private final String functionName;
    private final LangParser.FuncContext funcContext;
    private Boolean visited;

    private final ArrayList<Type> functionReturnTypes;
    private final ArrayList<Type> functionParameterTypes;
    private final ArrayList<ArrayList<Type>> variablesActuallyBeingReturned = new ArrayList<>();

    private final HashMap<String, Type> symbolTable;

    public FunctionEnvironment(String functionName, LangParser.FuncContext funcContext,
            ArrayList<Type> functionParameters, ArrayList<Type> functionReturns) {
        this.functionName = functionName;
        this.funcContext = funcContext;

        this.functionParameterTypes = functionParameters;
        this.functionReturnTypes = functionReturns;

        this.visited = false;

        this.symbolTable = new HashMap<>();
    }

    public LangParser.FuncContext getFunctionContext() {
        return funcContext;
    }

    public void declareVariable(String name, Type type) {
        symbolTable.put(name, type);
    }

    public String getFunctionName() {
        return functionName;
    }

    public Boolean hasBeenVisited() {
        return visited;
    }

    public void markAsVisited() {
        visited = true;
    }

    public Type getVariableType(String name) {
        return symbolTable.get(name);
    }

    public ArrayList<Type> getFunctionParameterTypes() {
        return functionParameterTypes;
    }

    public ArrayList<Type> getFunctionReturnTypes() {
        return functionReturnTypes;
    }

    public void addReturnStatementVariables(ArrayList<Type> returnedVariables) {
        variablesActuallyBeingReturned.add(returnedVariables);
    }

    public ArrayList<ArrayList<Type>> getVariablesActuallyBeingReturned() {
        return variablesActuallyBeingReturned;
    }

    public Boolean isVariableDeclared(String name) {
        return symbolTable.get(name) != null;
    }
}
