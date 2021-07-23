// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.parser;

import lang.ast.Node;
import lang.ast.SuperNode;
import lang.parser.grammar.antlr.LangLexer;
import lang.parser.grammar.antlr.LangParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

import static org.antlr.v4.runtime.CharStreams.fromFileName;

public class SyntaxParser implements ParseAdaptor {
    @Override
    public SuperNode parseFile(String path) {
        try {
            CharStream inputLanguage = fromFileName(path);
            LangLexer langLexer = new LangLexer(inputLanguage);

            CommonTokenStream tokenStream = new CommonTokenStream(langLexer);
            LangParser langParser = new LangParser(tokenStream);

            ParseTree cst = langParser.prog();

            if (langParser.getNumberOfSyntaxErrors() > 0) {
                return null;
            }

            return new Node(cst, langParser.getVocabulary());
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado no caminho " + path);
            return null;
        }
    }
}
