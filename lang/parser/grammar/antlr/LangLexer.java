// Generated from lang/parser/grammar/Lang.g4 by ANTLR 4.8
package lang.parser.grammar.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LangLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DATA=1, PRINT=2, IF=3, ELSE=4, ITERATE=5, TRUE=6, FALSE=7, NEW=8, READ=9, 
		RETURN=10, PLUS=11, MINUS=12, MULT=13, DIV=14, MOD=15, AND=16, OR=17, 
		NOT=18, LESS_THAN=19, MORE_THAN=20, EQUAL=21, NOT_EQUAL=22, BTYPE=23, 
		Int=24, Float=25, Char=26, Bool=27, OPEN_PARENTESIS=28, CLOSE_PARENTESIS=29, 
		OPEN_BRACKET=30, CLOSE_BRACKET=31, OPEN_CURLY_BRACER=32, CLOSE_CURLY_BRACER=33, 
		ACCESSOR=34, COLON=35, SEMICOLON=36, DOUBLE_COLON=37, COMMA=38, ATTRIBUTION=39, 
		COMMENT=40, MULTILINE_COMMENT=41, NEWLINE=42, WS=43, TYPE=44, BOOLEAN=45, 
		NULL=46, ID=47, FLOAT=48, INT=49, LITERAL=50, CHAR=51, NUMBER=52, UPPERCASE=53, 
		LOWERCASE=54, UNICODE=55;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"DATA", "PRINT", "IF", "ELSE", "ITERATE", "TRUE", "FALSE", "NEW", "READ", 
			"RETURN", "PLUS", "MINUS", "MULT", "DIV", "MOD", "AND", "OR", "NOT", 
			"LESS_THAN", "MORE_THAN", "EQUAL", "NOT_EQUAL", "BTYPE", "Int", "Float", 
			"Char", "Bool", "OPEN_PARENTESIS", "CLOSE_PARENTESIS", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "OPEN_CURLY_BRACER", "CLOSE_CURLY_BRACER", "ACCESSOR", 
			"COLON", "SEMICOLON", "DOUBLE_COLON", "COMMA", "ATTRIBUTION", "COMMENT", 
			"MULTILINE_COMMENT", "NEWLINE", "WS", "TYPE", "BOOLEAN", "NULL", "ID", 
			"FLOAT", "INT", "LITERAL", "CHAR", "NUMBER", "UPPERCASE", "LOWERCASE", 
			"UNICODE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'data'", "'print'", "'if'", "'else'", "'iterate'", "'true'", "'false'", 
			"'new'", "'read'", "'return'", "'+'", "'-'", "'*'", "'/'", "'%'", "'&&'", 
			"'||'", "'!'", "'<'", "'>'", "'=='", "'!='", null, "'Int'", "'Float'", 
			"'Char'", "'Bool'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'.'", 
			"':'", "';'", "'::'", "','", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DATA", "PRINT", "IF", "ELSE", "ITERATE", "TRUE", "FALSE", "NEW", 
			"READ", "RETURN", "PLUS", "MINUS", "MULT", "DIV", "MOD", "AND", "OR", 
			"NOT", "LESS_THAN", "MORE_THAN", "EQUAL", "NOT_EQUAL", "BTYPE", "Int", 
			"Float", "Char", "Bool", "OPEN_PARENTESIS", "CLOSE_PARENTESIS", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "OPEN_CURLY_BRACER", "CLOSE_CURLY_BRACER", "ACCESSOR", 
			"COLON", "SEMICOLON", "DOUBLE_COLON", "COMMA", "ATTRIBUTION", "COMMENT", 
			"MULTILINE_COMMENT", "NEWLINE", "WS", "TYPE", "BOOLEAN", "NULL", "ID", 
			"FLOAT", "INT", "LITERAL", "CHAR", "NUMBER", "UPPERCASE", "LOWERCASE", 
			"UNICODE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LangLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Lang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\29\u0173\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r"+
		"\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\5\30\u00c8\n\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3(\3)"+
		"\3)\3)\3)\7)\u00fb\n)\f)\16)\u00fe\13)\3)\7)\u0101\n)\f)\16)\u0104\13"+
		")\3)\3)\3*\3*\3*\3*\7*\u010c\n*\f*\16*\u010f\13*\3*\3*\3*\3*\3*\3+\6+"+
		"\u0117\n+\r+\16+\u0118\3+\6+\u011c\n+\r+\16+\u011d\5+\u0120\n+\3+\3+\3"+
		",\6,\u0125\n,\r,\16,\u0126\3,\3,\3-\3-\7-\u012d\n-\f-\16-\u0130\13-\3"+
		".\3.\5.\u0134\n.\3/\3/\3/\3/\3/\3/\3/\3/\3/\5/\u013f\n/\3\60\3\60\3\60"+
		"\3\60\7\60\u0145\n\60\f\60\16\60\u0148\13\60\3\61\7\61\u014b\n\61\f\61"+
		"\16\61\u014e\13\61\3\61\3\61\6\61\u0152\n\61\r\61\16\61\u0153\3\62\6\62"+
		"\u0157\n\62\r\62\16\62\u0158\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3"+
		"\63\5\63\u0164\n\63\3\63\3\63\3\64\3\64\5\64\u016a\n\64\3\65\3\65\3\66"+
		"\3\66\3\67\3\67\38\38\4\u00fc\u010d\29\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\66k\67m8o9\3\2\b\4\2\f\f\17\17\3\2\f\f\4\2"+
		"\13\13\"\"\3\2\62;\3\2C\\\3\2c|\2\u018a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\3q\3\2\2\2\5v\3"+
		"\2\2\2\7|\3\2\2\2\t\177\3\2\2\2\13\u0084\3\2\2\2\r\u008c\3\2\2\2\17\u0091"+
		"\3\2\2\2\21\u0097\3\2\2\2\23\u009b\3\2\2\2\25\u00a0\3\2\2\2\27\u00a7\3"+
		"\2\2\2\31\u00a9\3\2\2\2\33\u00ab\3\2\2\2\35\u00ad\3\2\2\2\37\u00af\3\2"+
		"\2\2!\u00b1\3\2\2\2#\u00b4\3\2\2\2%\u00b7\3\2\2\2\'\u00b9\3\2\2\2)\u00bb"+
		"\3\2\2\2+\u00bd\3\2\2\2-\u00c0\3\2\2\2/\u00c7\3\2\2\2\61\u00c9\3\2\2\2"+
		"\63\u00cd\3\2\2\2\65\u00d3\3\2\2\2\67\u00d8\3\2\2\29\u00dd\3\2\2\2;\u00df"+
		"\3\2\2\2=\u00e1\3\2\2\2?\u00e3\3\2\2\2A\u00e5\3\2\2\2C\u00e7\3\2\2\2E"+
		"\u00e9\3\2\2\2G\u00eb\3\2\2\2I\u00ed\3\2\2\2K\u00ef\3\2\2\2M\u00f2\3\2"+
		"\2\2O\u00f4\3\2\2\2Q\u00f6\3\2\2\2S\u0107\3\2\2\2U\u011f\3\2\2\2W\u0124"+
		"\3\2\2\2Y\u012a\3\2\2\2[\u0133\3\2\2\2]\u013e\3\2\2\2_\u0140\3\2\2\2a"+
		"\u014c\3\2\2\2c\u0156\3\2\2\2e\u015a\3\2\2\2g\u0169\3\2\2\2i\u016b\3\2"+
		"\2\2k\u016d\3\2\2\2m\u016f\3\2\2\2o\u0171\3\2\2\2qr\7f\2\2rs\7c\2\2st"+
		"\7v\2\2tu\7c\2\2u\4\3\2\2\2vw\7r\2\2wx\7t\2\2xy\7k\2\2yz\7p\2\2z{\7v\2"+
		"\2{\6\3\2\2\2|}\7k\2\2}~\7h\2\2~\b\3\2\2\2\177\u0080\7g\2\2\u0080\u0081"+
		"\7n\2\2\u0081\u0082\7u\2\2\u0082\u0083\7g\2\2\u0083\n\3\2\2\2\u0084\u0085"+
		"\7k\2\2\u0085\u0086\7v\2\2\u0086\u0087\7g\2\2\u0087\u0088\7t\2\2\u0088"+
		"\u0089\7c\2\2\u0089\u008a\7v\2\2\u008a\u008b\7g\2\2\u008b\f\3\2\2\2\u008c"+
		"\u008d\7v\2\2\u008d\u008e\7t\2\2\u008e\u008f\7w\2\2\u008f\u0090\7g\2\2"+
		"\u0090\16\3\2\2\2\u0091\u0092\7h\2\2\u0092\u0093\7c\2\2\u0093\u0094\7"+
		"n\2\2\u0094\u0095\7u\2\2\u0095\u0096\7g\2\2\u0096\20\3\2\2\2\u0097\u0098"+
		"\7p\2\2\u0098\u0099\7g\2\2\u0099\u009a\7y\2\2\u009a\22\3\2\2\2\u009b\u009c"+
		"\7t\2\2\u009c\u009d\7g\2\2\u009d\u009e\7c\2\2\u009e\u009f\7f\2\2\u009f"+
		"\24\3\2\2\2\u00a0\u00a1\7t\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7v\2\2\u00a3"+
		"\u00a4\7w\2\2\u00a4\u00a5\7t\2\2\u00a5\u00a6\7p\2\2\u00a6\26\3\2\2\2\u00a7"+
		"\u00a8\7-\2\2\u00a8\30\3\2\2\2\u00a9\u00aa\7/\2\2\u00aa\32\3\2\2\2\u00ab"+
		"\u00ac\7,\2\2\u00ac\34\3\2\2\2\u00ad\u00ae\7\61\2\2\u00ae\36\3\2\2\2\u00af"+
		"\u00b0\7\'\2\2\u00b0 \3\2\2\2\u00b1\u00b2\7(\2\2\u00b2\u00b3\7(\2\2\u00b3"+
		"\"\3\2\2\2\u00b4\u00b5\7~\2\2\u00b5\u00b6\7~\2\2\u00b6$\3\2\2\2\u00b7"+
		"\u00b8\7#\2\2\u00b8&\3\2\2\2\u00b9\u00ba\7>\2\2\u00ba(\3\2\2\2\u00bb\u00bc"+
		"\7@\2\2\u00bc*\3\2\2\2\u00bd\u00be\7?\2\2\u00be\u00bf\7?\2\2\u00bf,\3"+
		"\2\2\2\u00c0\u00c1\7#\2\2\u00c1\u00c2\7?\2\2\u00c2.\3\2\2\2\u00c3\u00c8"+
		"\5\61\31\2\u00c4\u00c8\5\63\32\2\u00c5\u00c8\5\65\33\2\u00c6\u00c8\5\67"+
		"\34\2\u00c7\u00c3\3\2\2\2\u00c7\u00c4\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7"+
		"\u00c6\3\2\2\2\u00c8\60\3\2\2\2\u00c9\u00ca\7K\2\2\u00ca\u00cb\7p\2\2"+
		"\u00cb\u00cc\7v\2\2\u00cc\62\3\2\2\2\u00cd\u00ce\7H\2\2\u00ce\u00cf\7"+
		"n\2\2\u00cf\u00d0\7q\2\2\u00d0\u00d1\7c\2\2\u00d1\u00d2\7v\2\2\u00d2\64"+
		"\3\2\2\2\u00d3\u00d4\7E\2\2\u00d4\u00d5\7j\2\2\u00d5\u00d6\7c\2\2\u00d6"+
		"\u00d7\7t\2\2\u00d7\66\3\2\2\2\u00d8\u00d9\7D\2\2\u00d9\u00da\7q\2\2\u00da"+
		"\u00db\7q\2\2\u00db\u00dc\7n\2\2\u00dc8\3\2\2\2\u00dd\u00de\7*\2\2\u00de"+
		":\3\2\2\2\u00df\u00e0\7+\2\2\u00e0<\3\2\2\2\u00e1\u00e2\7]\2\2\u00e2>"+
		"\3\2\2\2\u00e3\u00e4\7_\2\2\u00e4@\3\2\2\2\u00e5\u00e6\7}\2\2\u00e6B\3"+
		"\2\2\2\u00e7\u00e8\7\177\2\2\u00e8D\3\2\2\2\u00e9\u00ea\7\60\2\2\u00ea"+
		"F\3\2\2\2\u00eb\u00ec\7<\2\2\u00ecH\3\2\2\2\u00ed\u00ee\7=\2\2\u00eeJ"+
		"\3\2\2\2\u00ef\u00f0\7<\2\2\u00f0\u00f1\7<\2\2\u00f1L\3\2\2\2\u00f2\u00f3"+
		"\7.\2\2\u00f3N\3\2\2\2\u00f4\u00f5\7?\2\2\u00f5P\3\2\2\2\u00f6\u00f7\7"+
		"/\2\2\u00f7\u00f8\7/\2\2\u00f8\u00fc\3\2\2\2\u00f9\u00fb\13\2\2\2\u00fa"+
		"\u00f9\3\2\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fc\u00fa\3\2"+
		"\2\2\u00fd\u0102\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0101\n\2\2\2\u0100"+
		"\u00ff\3\2\2\2\u0101\u0104\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2"+
		"\2\2\u0103\u0105\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u0106\b)\2\2\u0106"+
		"R\3\2\2\2\u0107\u0108\7}\2\2\u0108\u0109\7/\2\2\u0109\u010d\3\2\2\2\u010a"+
		"\u010c\13\2\2\2\u010b\u010a\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010e\3"+
		"\2\2\2\u010d\u010b\3\2\2\2\u010e\u0110\3\2\2\2\u010f\u010d\3\2\2\2\u0110"+
		"\u0111\7/\2\2\u0111\u0112\7\177\2\2\u0112\u0113\3\2\2\2\u0113\u0114\b"+
		"*\2\2\u0114T\3\2\2\2\u0115\u0117\t\2\2\2\u0116\u0115\3\2\2\2\u0117\u0118"+
		"\3\2\2\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u0120\3\2\2\2\u011a"+
		"\u011c\t\3\2\2\u011b\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u011b\3\2"+
		"\2\2\u011d\u011e\3\2\2\2\u011e\u0120\3\2\2\2\u011f\u0116\3\2\2\2\u011f"+
		"\u011b\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0122\b+\2\2\u0122V\3\2\2\2\u0123"+
		"\u0125\t\4\2\2\u0124\u0123\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0124\3\2"+
		"\2\2\u0126\u0127\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\b,\2\2\u0129"+
		"X\3\2\2\2\u012a\u012e\5k\66\2\u012b\u012d\5_\60\2\u012c\u012b\3\2\2\2"+
		"\u012d\u0130\3\2\2\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012fZ\3"+
		"\2\2\2\u0130\u012e\3\2\2\2\u0131\u0134\5\r\7\2\u0132\u0134\5\17\b\2\u0133"+
		"\u0131\3\2\2\2\u0133\u0132\3\2\2\2\u0134\\\3\2\2\2\u0135\u0136\7)\2\2"+
		"\u0136\u0137\7^\2\2\u0137\u0138\7\62\2\2\u0138\u0139\3\2\2\2\u0139\u013f"+
		"\7)\2\2\u013a\u013b\7p\2\2\u013b\u013c\7w\2\2\u013c\u013d\7n\2\2\u013d"+
		"\u013f\7n\2\2\u013e\u0135\3\2\2\2\u013e\u013a\3\2\2\2\u013f^\3\2\2\2\u0140"+
		"\u0146\5g\64\2\u0141\u0145\5g\64\2\u0142\u0145\5i\65\2\u0143\u0145\7a"+
		"\2\2\u0144\u0141\3\2\2\2\u0144\u0142\3\2\2\2\u0144\u0143\3\2\2\2\u0145"+
		"\u0148\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147`\3\2\2\2"+
		"\u0148\u0146\3\2\2\2\u0149\u014b\5i\65\2\u014a\u0149\3\2\2\2\u014b\u014e"+
		"\3\2\2\2\u014c\u014a\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014f\3\2\2\2\u014e"+
		"\u014c\3\2\2\2\u014f\u0151\7\60\2\2\u0150\u0152\5i\65\2\u0151\u0150\3"+
		"\2\2\2\u0152\u0153\3\2\2\2\u0153\u0151\3\2\2\2\u0153\u0154\3\2\2\2\u0154"+
		"b\3\2\2\2\u0155\u0157\5i\65\2\u0156\u0155\3\2\2\2\u0157\u0158\3\2\2\2"+
		"\u0158\u0156\3\2\2\2\u0158\u0159\3\2\2\2\u0159d\3\2\2\2\u015a\u0163\7"+
		")\2\2\u015b\u0164\5g\64\2\u015c\u015d\7^\2\2\u015d\u0164\7p\2\2\u015e"+
		"\u015f\7^\2\2\u015f\u0164\7v\2\2\u0160\u0161\7^\2\2\u0161\u0164\7^\2\2"+
		"\u0162\u0164\5o8\2\u0163\u015b\3\2\2\2\u0163\u015c\3\2\2\2\u0163\u015e"+
		"\3\2\2\2\u0163\u0160\3\2\2\2\u0163\u0162\3\2\2\2\u0164\u0165\3\2\2\2\u0165"+
		"\u0166\7)\2\2\u0166f\3\2\2\2\u0167\u016a\5m\67\2\u0168\u016a\5k\66\2\u0169"+
		"\u0167\3\2\2\2\u0169\u0168\3\2\2\2\u016ah\3\2\2\2\u016b\u016c\t\5\2\2"+
		"\u016cj\3\2\2\2\u016d\u016e\t\6\2\2\u016el\3\2\2\2\u016f\u0170\t\7\2\2"+
		"\u0170n\3\2\2\2\u0171\u0172\4\2\u0101\2\u0172p\3\2\2\2\25\2\u00c7\u00fc"+
		"\u0102\u010d\u0118\u011d\u011f\u0126\u012e\u0133\u013e\u0144\u0146\u014c"+
		"\u0153\u0158\u0163\u0169\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}