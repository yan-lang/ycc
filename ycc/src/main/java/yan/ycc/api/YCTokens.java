package yan.ycc.api;

import java.util.List;
import java.util.Map;

/**
 * YCTokens包含YC语言所有可能出现的单词类型定义。
 */
public interface YCTokens {
    int UNKNOWN = 0;

    int IDENTIFIER = 1;

    int INT_LIT = 2;

    int FLOAT_LIT = 3;

    int ADD = 4;

    int MINUS = 5;

    int MULTI = 6;

    int DIV = 7;

    int REM = 8;

    int GT = 9;

    int GTE = 10;

    int LT = 11;

    int LTE = 12;

    int EQ = 13;

    int NEQ = 14;

    int ASSIGN = 15;

    int LOR = 16;

    int LAND = 17;

    int LNOT = 18;

    int KW_INT = 19;

    int KW_FLOAT = 20;

    int KW_VOID = 21;

    int KW_IF = 22;

    int KW_ELSE = 23;

    int KW_WHILE = 24;

    int KW_CONTINUE = 25;

    int KW_BREAK = 26;

    int KW_RETURN = 27;

    int LPAREN = 28;

    int RPAREN = 29;

    int LBRACE = 30;

    int RBRACE = 31;

    int SEMICOLON = 32;

    int COMMA = 33;


    List<String> tokenNames = List.of("unknown", "identifier", "integer literal", "float literal", "add", "minus", "multiply", "divide", "remainder", "greater than", "greater than or equal", "less than", "less than or equal", "equal", "not equal", "assign", "logical or", "logical and", "logical not", "kw int", "kw float", "kw void", "kw if", "kw else", "kw while", "kw continue", "kw break", "kw return", "left parenthesis", "right parenthesis", "left brace", "right brace", "semicolon", "comma");

    List<String> tokenSymbolNames = List.of("unknown", "identifier", "integer literal", "float literal", "+", "-", "*", "/", "%", ">", ">=", "<", "<=", "==", "!=", "=", "||", "&&", "!", "int", "float", "void", "if", "else", "while", "continue", "break", "return", "(", ")", "{", "}", ";", ",");

    Map<String, Integer> keyword = Map.ofEntries(Map.entry("int", 19), Map.entry("float", 20), Map.entry("void", 21), Map.entry("if", 22), Map.entry("else", 23), Map.entry("while", 24), Map.entry("continue", 25), Map.entry("break", 26), Map.entry("return", 27));


}