// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.ast;

import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;

public class Node extends SuperNode {
    private Integer line;
    private Integer column;
    private final ParseTree parseTree;
    private final Vocabulary vocabulary;

    public Node(ParseTree parseTree, Vocabulary vocabulary) {
        this.parseTree = parseTree;
        this.vocabulary = vocabulary;
    }

    public ParseTree getParseTree() {
        return this.parseTree;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public int getColumn() {
        return this.column;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }
}
