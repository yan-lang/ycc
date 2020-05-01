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

    int REL_OR = 15;

    int REL_AND = 16;

    int REL_NOT = 17;

    int KW_INT = 18;

    int KW_FLOAT = 19;

    int KW_VOID = 20;

    int KW_IF = 21;

    int KW_ELSE = 22;

    int KW_WHILE = 23;

    int KW_CONTINUE = 24;

    int KW_BREAK = 25;

    int KW_RETURN = 26;

    int LPAREN = 27;

    int RPAREN = 28;

    int LBRACE = 29;

    int RBRACE = 30;

    int SEMICOLON = 31;

    int COMMA = 32;


    List<String> tokenNames = List.of("unknown", "identifier", "integer literal", "float literal", "add", "minus", "multiply", "divide", "greater than", "greater than or equal", "less than", "less than or equal", "equal", "not equal", "assign", "relational or", "relational and", "relational not", "kw int", "kw float", "kw void", "kw if", "kw else", "kw while", "kw continue", "kw break", "kw return", "left parenthesis", "right parenthesis", "left brace", "right brace", "semicolon", "comma");

    List<String> tokenSymbolNames = List.of("unknown", "identifier", "integer literal", "float literal", "+", "-", "*", "/", ">", ">=", "<", "<=", "==", "!=", "=", "||", "&&", "!", "int", "float", "void", "if", "else", "while", "continue", "break", "return", "(", ")", "{", "}", ";", ",");

    Map<String, Integer> keyword = Map.ofEntries(Map.entry("int", 18), Map.entry("float", 19), Map.entry("void", 20), Map.entry("if", 21), Map.entry("else", 22), Map.entry("while", 23), Map.entry("continue", 24), Map.entry("break", 25), Map.entry("return", 26));



}