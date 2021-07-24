# Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

# rm ./lang/**/*.class
# rm ./lang/*.class

java -jar lang/parser/grammar/antlr-4.8-complete.jar -o lang/parser/grammar/antlr -package lang.parser.grammar.antlr -visitor lang/parser/grammar/Lang.g4 -Xexact-output-dir

javac -cp .:lang/parser/grammar/antlr-4.8-complete.jar lang/LangCompiler.java

java -cp .:lang/parser/grammar/antlr-4.8-complete.jar lang/LangCompiler -bsm