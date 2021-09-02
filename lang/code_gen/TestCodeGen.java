// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.code_gen;

import lang.ast.Node;
import lang.parser.ParseAdaptor;
import lang.semantics.TypeAnalyzer;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;

public class TestCodeGen {
    public static void generatePythonCode(ParseAdaptor langParser, String dirPath) {
        File directory = new File(dirPath);

        if (!directory.isDirectory()) {
            System.out.println("O caminho " + directory.getPath() + " não é um diretório ou não existe.");
            System.exit(1);
        }

        for (File file : directory.listFiles()) {
            String path = file.getPath();
            String filename = file.getName();

            Node node = (Node) langParser.parseFile(path);

            ParseTree parseTree = node.getParseTree();

            if (parseTree == null) {
                return;
            }

            TypeAnalyzer typeAnalyzer = new TypeAnalyzer(parseTree, filename);

            LangToPython langToPython = new LangToPython(parseTree, typeAnalyzer, filename);
        }
    }
}
