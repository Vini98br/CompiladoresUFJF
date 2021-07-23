// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.interpreter;

import java.io.*;
import lang.ast.Node;
import lang.parser.ParseAdaptor;
import lang.semantics.TypeAnalyzer;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestInterpreter {
    private ParseAdaptor adp;
    private String okSrcs = "testes/sintaxe/certo/";
    private String notOkSrcs = "testes/sintaxe/errado/";
    private File f;
    private File f2;

    public TestInterpreter(ParseAdaptor adp) {
        this.adp = adp;
        f = new File(okSrcs);
        runOkTests();
    }

    private String filler(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += " ";
        }
        return s;
    }

    public void runOkTests() {
        File inst[];
        int flips, flops;
        flips = 0;
        flops = 0;

        try {
            if (f.isDirectory()) {
                String pth;
                inst = f.listFiles();
                for (File s : inst) {
                    try {
                        pth = s.getPath();
                        System.out.println("Testando " + pth + filler(50 - pth.length()));

                        Node node = (Node) this.adp.parseFile(pth);

                        ParseTree parseTree = node.getParseTree();

                        if (parseTree == null) {
                            return;
                        }

                        TypeAnalyzer typeAnalyzer = new TypeAnalyzer(parseTree, pth);
                        Interpreter interpreter = new Interpreter(parseTree, node.getVocabulary());
                        flips++;
                    } catch (Exception e) {
                        flops++;
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println("Total de acertos: " + flips);
                System.out.println("Total de erros: " + flops);
            } else {
                System.out.println("O caminho " + f.getPath() + " não é um diretório ou não existe.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
