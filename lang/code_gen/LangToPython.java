// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003
package lang.code_gen;

import lang.parser.grammar.antlr.LangBaseVisitor;
import lang.parser.grammar.antlr.LangParser;
import lang.semantics.FunctionEnvironment;
import lang.semantics.TypeAnalyzer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LangToPython extends LangBaseVisitor<Object> {
    private final HashMap<String, FunctionEnvironment> programEnvironment;
    private final STGroup pythonTemplates;
    private final String filename;

    public LangToPython(ParseTree parseTree, TypeAnalyzer typeAnalyzer, String filename) {
        programEnvironment = typeAnalyzer.getProgramEnvironment();
        pythonTemplates = new STGroupDir("lang/code_gen/python");

        this.filename = filename.split("\\.")[0];

        this.visit(parseTree);
    }

    @Override
    public Object visitProg(LangParser.ProgContext ctx) {
        ST prog = pythonTemplates.getInstanceOf("prog");

        for (Map.Entry<String, FunctionEnvironment> function : programEnvironment.entrySet()) {
            FunctionEnvironment functionEnvironment = function.getValue();

            ST func = visitFunc(functionEnvironment);

            prog.add("funcs", func);
        }

        for (LangParser.DataContext dataDeclaration : ctx.data()) {
            ST data = visitData(dataDeclaration);

            prog.add("data", data);
        }

        super.visitProg(ctx);

        try {
            PrintStream ps = new PrintStream("lang/code_gen/out/" + filename + ".py");
            ps.println(prog.render());

            System.out.println("Saída escrita em lang/code_gen/out/" + filename + ".py");
        } catch (Exception e) {
            System.out.println("Algum erro ocorreu ao escrever a saída no arquivo:");
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public ST visitData(LangParser.DataContext ctx) {
        ST data = pythonTemplates.getInstanceOf("data");

        String dataName = ctx.TYPE().getText();

        if (getPythonReservedWords().contains(dataName)) {
            dataName += "_";
        }

        data.add("name", dataName);

        for (LangParser.DeclContext declContext : ctx.decl()) {
            ST decl = visitDecl(declContext);

            data.add("decl", decl);
        }

        return data;
    }

    @Override
    public ST visitDecl(LangParser.DeclContext ctx) {
        ST decl = pythonTemplates.getInstanceOf("decl");

        String propertyName = ctx.ID().getText();

        if (getPythonReservedWords().contains(propertyName)) {
            propertyName += "_";
        }

        decl.add("id", propertyName);

        return decl;
    }

    public ST visitFunc(FunctionEnvironment functionEnvironment) {
        LangParser.FuncContext ctx = functionEnvironment.getFunctionContext();

        ST func = pythonTemplates.getInstanceOf("func");

        String functionName = functionEnvironment.getFunctionName();

        if (getPythonReservedWords().contains(functionName)) {
            functionName += "_";
        }

        func.add("name", functionName);

        if (functionEnvironment.getFunctionParameterTypes().size() > 0) {
            ST params = visitParams(ctx.params());
            func.add("params", params);
        }

        for (LangParser.CmdContext command : ctx.cmd()) {
            ST cmd = visitCmd(command);
            func.add("cmds", cmd);
        }

        return func;
    }

    @Override
    public ST visitParams(LangParser.ParamsContext ctx) {
        ST params = pythonTemplates.getInstanceOf("csv");

        for (TerminalNode variable : ctx.ID()) {
            String variableName = variable.getText();

            if (getPythonReservedWords().contains(variableName)) {
                variableName = variableName + "_";
            }

            params.add("value", variableName);
        }

        return params;
    }

    @Override
    public ST visitType(LangParser.TypeContext ctx) {
        if (ctx.type() != null) {
            return visitType(ctx.type());
        }

        if (ctx.btype() != null) {
            return visitBtype(ctx.btype());
        }

        return null;
    }

    @Override
    public ST visitBtype(LangParser.BtypeContext ctx) {
        if (ctx.TYPE() != null) {
            String typeName = ctx.getText();

            if (getPythonReservedWords().contains(typeName)) {
                typeName += "_";
            }

            return new ST(typeName + ".copy()");
        }

        return new ST("None");
    }

    @Override
    public ST visitCmd(LangParser.CmdContext ctx) {
        ST cmd = pythonTemplates.getInstanceOf("cmd");

        if (ctx.PRINT() != null) {
            ST print = pythonTemplates.getInstanceOf("print");

            ST exp = visitExp(ctx.exp(0));
            print.add("exp", exp);

            cmd.add("line", print);
        }

        if (ctx.READ() != null) {
            ST attribution = pythonTemplates.getInstanceOf("attribution");

            ST variable = visitLvalue(ctx.lvalue(0));

            attribution.add("variable", variable);
            attribution.add("exp", "raw_input()");

            cmd.add("line", attribution);
        }

        if (ctx.ITERATE() != null) {
            ST iterate = pythonTemplates.getInstanceOf("iterate");

            iterate.add("numberOfLoops", visitExp(ctx.exp(0)));
            iterate.add("cmds", visitCmd(ctx.cmd(0)));

            cmd.add("line", iterate);
        }

        if (ctx.ATTRIBUTION() != null) {
            ST attribution = pythonTemplates.getInstanceOf("attribution");

            ST variable = visitLvalue(ctx.lvalue(0));
            attribution.add("variable", variable);

            ST exp = visitExp(ctx.exp(0));
            attribution.add("exp", exp);

            cmd.add("line", attribution);
        }

        if (ctx.IF() != null) {
            ST ifCommand = pythonTemplates.getInstanceOf("if");

            ST test = visitExp(ctx.exp(0));
            ifCommand.add("test", test);

            ST insideIfCmd = visitCmd(ctx.cmd(0));
            ifCommand.add("cmds", insideIfCmd);

            if (ctx.ELSE() != null) {
                ST insideElseCmd = visitCmd(ctx.cmd(1));
                ifCommand.add("elseCmds", insideElseCmd);
            }

            cmd.add("line", ifCommand);
        }

        if (ctx.RETURN() != null) {
            ST returnValues = pythonTemplates.getInstanceOf("csv");

            for (LangParser.ExpContext expression : ctx.exp()) {
                returnValues.add("value", visitExp(expression));
            }

            cmd.add("line", "return " + returnValues.render());
        }

        if (ctx.OPEN_CURLY_BRACER() != null) {
            for (LangParser.CmdContext command : ctx.cmd()) {
                ST innerCmd = visitCmd(command);

                cmd.add("line", innerCmd);
            }
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().getText();

            if (getPythonReservedWords().contains(functionName)) {
                functionName += "_";
            }

            ST functionCall = pythonTemplates.getInstanceOf("functionCall");

            functionCall.add("functionName", functionName);

            if (ctx.exps() != null) {
                ST params = visitExps(ctx.exps());

                functionCall.add("params", params);
            }

            if (ctx.LESS_THAN() != null) {
                ST returns = pythonTemplates.getInstanceOf("csv");

                for (LangParser.LvalueContext lvalueContext : ctx.lvalue()) {
                    returns.add("value", visitLvalue(lvalueContext));
                }

                functionCall.add("return", returns);
            }

            cmd.add("line", functionCall);
        }

        return cmd;
    }

    @Override
    public ST visitExp(LangParser.ExpContext ctx) {
        ST exp = pythonTemplates.getInstanceOf("exp");

        if (ctx.AND() != null) {
            ST leftSide = visitExp(ctx.exp(0));
            ST rightSide = visitExp(ctx.exp(1));

            exp.add("leftSide", leftSide);
            exp.add("operator", "and");
            exp.add("rightSide", rightSide);

            return exp;
        }

        return visitRexp(ctx.rexp());
    }

    @Override
    public ST visitRexp(LangParser.RexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitAexp(ctx.aexp());
        }

        ST rexp = pythonTemplates.getInstanceOf("exp");

        ST lefSide = visitRexp(ctx.rexp());
        String operator = ctx.getChild(1).getText();
        ST rightSide = visitAexp(ctx.aexp());

        rexp.add("leftSide", lefSide);
        rexp.add("operator", operator);
        rexp.add("rightSide", rightSide);

        return rexp;
    }

    @Override
    public ST visitAexp(LangParser.AexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitMexp(ctx.mexp());
        }

        ST aexp = pythonTemplates.getInstanceOf("exp");

        ST lefSide = visitAexp(ctx.aexp());
        String operator = ctx.getChild(1).getText();
        ST rightSide = visitMexp(ctx.mexp());

        aexp.add("leftSide", lefSide);
        aexp.add("operator", operator);
        aexp.add("rightSide", rightSide);

        return aexp;
    }

    @Override
    public ST visitMexp(LangParser.MexpContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitSexp(ctx.sexp());
        }

        ST mexp = pythonTemplates.getInstanceOf("exp");

        ST lefSide = visitMexp(ctx.mexp());
        String operator = ctx.getChild(1).getText();
        ST rightSide = visitSexp(ctx.sexp());

        mexp.add("leftSide", lefSide);
        mexp.add("operator", operator);
        mexp.add("rightSide", rightSide);

        return mexp;
    }

    @Override
    public ST visitSexp(LangParser.SexpContext ctx) {
        if (ctx.pexp() != null) {
            return visitPexp(ctx.pexp());
        }

        if (ctx.TRUE() != null) {
            return new ST("True");
        }

        if (ctx.FALSE() != null) {
            return new ST("False");
        }

        if (ctx.NOT() != null) {
            ST exp = visitSexp(ctx.sexp());

            return new ST(String.format("not %s", exp.render()));
        }

        if (ctx.MINUS() != null) {
            ST exp = visitSexp(ctx.sexp());

            return new ST(String.format("- %s", exp.render()));
        }

        return new ST(ctx.getChild(0).getText());
    }

    @Override
    public ST visitPexp(LangParser.PexpContext ctx) {
        if (ctx.lvalue() != null) {
            return visitLvalue(ctx.lvalue());
        }

        if (ctx.ID() != null) {
            String functionName = ctx.ID().getText();

            if (getPythonReservedWords().contains(functionName)) {
                functionName += "_";
            }

            FunctionEnvironment functionEnvironment = programEnvironment.get(functionName);

            ST functionCall = pythonTemplates.getInstanceOf("functionCall");

            functionCall.add("functionName", functionName);

            if (ctx.exps() != null) {
                ST params = visitExps(ctx.exps());

                functionCall.add("params", params);
            }

            if (ctx.OPEN_BRACKET() != null && functionEnvironment.getFunctionReturnTypes().size() > 1) {
                ST index = visitExp(ctx.exp());

                functionCall.add("index", index);
            }

            return functionCall;
        }

        if (ctx.NEW() != null) {
            ST type = visitType(ctx.type());

            if (ctx.OPEN_BRACKET() != null) {
                ST array = pythonTemplates.getInstanceOf("array");

                ST size = visitExp(ctx.exp());

                array.add("type", type);
                array.add("size", size);

                return array;
            }

            return type;
        }

        if (ctx.OPEN_PARENTESIS() != null) {
            ST exp = visitExp(ctx.exp());

            return new ST("(" + exp.render() + ")");
        }

        return null;
    }

    @Override
    public ST visitLvalue(LangParser.LvalueContext ctx) {
        if (ctx.getChild(0) == ctx.ID()) {
            String variableName = ctx.ID().getText();

            if (getPythonReservedWords().contains(variableName)) {
                variableName = variableName + "_";
            }

            return new ST(variableName);
        }

        if (ctx.ACCESSOR() != null) {
            ST lvalue = visitLvalue(ctx.lvalue());

            String propertyName = ctx.ID().getText();

            if (getPythonReservedWords().contains(propertyName)) {
                propertyName += "_";
            }

            return new ST(String.format("%s['%s']", lvalue.render(), propertyName));
        }

        if (ctx.OPEN_BRACKET() != null) {
            ST lvalue = visitLvalue(ctx.lvalue());
            ST exp = visitExp(ctx.exp());

            return new ST(String.format("%s[%s]", lvalue.render(), exp.render()));
        }

        return null;
    }

    @Override
    public ST visitExps(LangParser.ExpsContext ctx) {
        ST params = pythonTemplates.getInstanceOf("csv");

        for (LangParser.ExpContext expression : ctx.exp()) {
            params.add("value", visitExp(expression));
        }

        return params;
    }

    public ArrayList<String> getPythonReservedWords() {
        ArrayList<String> reservedWords = new ArrayList<>();

        reservedWords.add("True");
        reservedWords.add("False");
        reservedWords.add("None");
        reservedWords.add("while");
        reservedWords.add("def");
        reservedWords.add("and");
        reservedWords.add("except");
        reservedWords.add("lambda");
        reservedWords.add("with");
        reservedWords.add("as");
        reservedWords.add("finally");
        reservedWords.add("nonlocal");
        reservedWords.add("yield");
        reservedWords.add("break");
        reservedWords.add("for");
        reservedWords.add("not");
        reservedWords.add("class");
        reservedWords.add("from");
        reservedWords.add("or");
        reservedWords.add("raise");
        reservedWords.add("del");
        reservedWords.add("import");
        reservedWords.add("elif");
        reservedWords.add("in");
        reservedWords.add("is");
        reservedWords.add("try");
        reservedWords.add("range");

        return reservedWords;
    }
}
