// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics;

import lang.parser.grammar.antlr.LangParser;
import lang.semantics.types.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionEnvironment {
    // Nome da função
    private final String functionName;
    // Contexto da função
    private final LangParser.FuncContext funcContext;
    private Boolean visited;

    // Tipos de retorno da função
    private final ArrayList<Type> functionReturnTypes;
    // Parâmetros da função
    private final ArrayList<Type> functionParameterTypes;
    // Variáveis que estão sendo retornadas
    private final ArrayList<ArrayList<Type>> variablesActuallyBeingReturned = new ArrayList<>();

    // Hash map responsável por mapear nome da variável e a variável de fato
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

    // Realiza a declaração da variável
    // A declaração da variável está dentro FunctionEnvironment para mantermos o
    // escopo da função em questão
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

    // Pega uma variável pelo seu nome
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

    // Verifica se a variável existe no hash map
    public Boolean isVariableDeclared(String name) {
        return symbolTable.get(name) != null;
    }
}
