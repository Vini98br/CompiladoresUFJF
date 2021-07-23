// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.interpreter;

import lang.ast.Node;
import lang.parser.ParseAdaptor;
import lang.semantics.TypeAnalyzer;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestInterpreter {
    public static void runInterpreterTests(ParseAdaptor langParser) {
        String filename = "testes/sintaxe/certo/attrADD.lan";
        Node node = (Node) langParser.parseFile(filename);

        ParseTree parseTree = node.getParseTree();

        if (parseTree == null) {
            return;
        }

        TypeAnalyzer typeAnalyzer = new TypeAnalyzer(parseTree, filename);

        Interpreter interpreter = new Interpreter(parseTree, node.getVocabulary());
    }
}
