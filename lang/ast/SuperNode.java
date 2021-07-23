// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.ast;

import lang.parser.grammar.antlr.LangBaseVisitor;

public abstract class SuperNode extends LangBaseVisitor<Object> {
    // The line and column of the node in the input text

    public abstract int getLine();

    public abstract int getColumn();
}
