/* Arthur Dornelas: 201735004, Vinicius Soranço: 201735003 */

package lang;

import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import lang.lexer.grammar.antlr.LangLexer;

@SuppressWarnings("deprecation")
public class LangCompiler {
  public static void main(String[] args) throws IOException {

    FileInputStream fis = new FileInputStream("roda.lan");
    ANTLRInputStream input = new ANTLRInputStream(fis);
    Lexer lexer = new LangLexer(input);

    Token token = lexer.nextToken();
    while (token.getType() != LangLexer.EOF) {
      System.out.println("\t" + lexer.getRuleNames()[token.getType() - 1] + ": " + token.getText());
      token = lexer.nextToken();
    }
  }
}
