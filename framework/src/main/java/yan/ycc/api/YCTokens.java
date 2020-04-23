package yan.ycc.api;

import java.util.List;
import java.util.Map;

public interface YCTokens {
    int UNKNOWN = 0;

    int IDENTIFIER = 1;

    int INT_LIT = 2;

    int FLOAT_LIT = 3;

    int ADD = 4;

    int MINUS = 5;

    int MULTI = 6;

    int DIV = 7;

    int GT = 8;

    int GTE = 9;

    int LT = 10;

    int LTE = 11;

    int EQ = 12;

    int NEQ = 13;

    int ASSIGN = 14;

    int KW_INT = 15;

    int KW_FLOAT = 16;

    int KW_VOID = 17;

    int KW_IF = 18;

    int KW_ELSE = 19;

    int KW_WHILE = 20;

    int KW_CONTINUE = 21;

    int KW_BREAK = 22;

    int KW_RETURN = 23;

    int LPAREN = 24;

    int RPAREN = 25;

    int LBRACE = 26;

    int RBRACE = 27;

    int SEMICOLON = 28;

    int COMMA = 29;


    List<String> tokenNames = List.of("UNKNOWN", "IDENTIFIER", "INT_LIT", "FLOAT_LIT", "ADD", "MINUS", "MULTI", "DIV", "GT", "GTE", "LT", "LTE", "EQ", "NEQ", "ASSIGN", "KW_INT", "KW_FLOAT", "KW_VOID", "KW_IF", "KW_ELSE", "KW_WHILE", "KW_CONTINUE", "KW_BREAK", "KW_RETURN", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "SEMICOLON", "COMMA");

    List<String> tokenSymbolNames = List.of("UNKNOWN", "IDENTIFIER", "INT_LIT", "FLOAT_LIT", "ADD", "MINUS", "MULTI", "DIV", "GT", "GTE", "LT", "LTE", "EQ", "NEQ", "ASSIGN", "int", "float", "void", "if", "else", "while", "continue", "break", "return", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "SEMICOLON", "COMMA");

    Map<String, Integer> keyword = Map.ofEntries(Map.entry("int", 15), Map.entry("float", 16), Map.entry("void", 17), Map.entry("if", 18), Map.entry("else", 19), Map.entry("while", 20), Map.entry("continue", 21), Map.entry("break", 22), Map.entry("return", 23));


}