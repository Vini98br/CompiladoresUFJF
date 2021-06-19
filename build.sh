java -jar ./lang/lexer/grammar/antlr-4.8-complete.jar -o lang/lexer/grammar/antlr -package lang.lexer.grammar.antlr -visitor ./lang/lexer/grammar/Lang.g4 -Xexact-output-dir

javac -cp .:lang/lexer/grammar/antlr-4.8-complete.jar lang/LangCompiler.java

java -cp .:lang/lexer/grammar/antlr-4.8-complete.jar lang/LangCompiler -bs