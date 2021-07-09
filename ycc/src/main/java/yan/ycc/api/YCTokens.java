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

    int ADD_ASSIGN = 16;

    int MINUS_ASSIGN = 17;

    int MULTI_ASSIGN = 18;

    int DIV_ASSIGN = 19;

    int REM_ASSIGN = 20;

    int LOR = 21;

    int LAND = 22;

    int LNOT = 23;

    int KW_INT = 24;

    int KW_FLOAT = 25;

    int KW_VOID = 26;

    int KW_IF = 27;

    int KW_ELSE = 28;

    int KW_WHILE = 29;

    int KW_CONTINUE = 30;

    int KW_BREAK = 31;

    int KW_RETURN = 32;

    int KW_FOR = 33;

    int LPAREN = 34;

    int RPAREN = 35;

    int LBRACE = 36;

    int RBRACE = 37;

    int SEMICOLON = 38;

    int COMMA = 39;


    List<String> tokenNames = List.of("unknown", "identifier", "integer literal", "float literal", "add", "minus", "multiply", "divide", "remainder", "greater than", "greater than or equal", "less than", "less than or equal", "equal", "not equal", "assign", "add assign", "minus assign", "multi assign", "div assign", "rem assign", "logical or", "logical and", "logical not", "kw int", "kw float", "kw void", "kw if", "kw else", "kw while", "kw continue", "kw break", "kw return", "kw for", "left parenthesis", "right parenthesis", "left brace", "right brace", "semicolon", "comma");

    List<String> tokenSymbolNames = List.of("unknown", "identifier", "integer literal", "float literal", "+", "-", "*", "/", "%", ">", ">=", "<", "<=", "==", "!=", "=", "+=", "-=", "*=", "/=", "%=", "||", "&&", "!", "int", "float", "void", "if", "else", "while", "continue", "break", "return", "for", "(", ")", "{", "}", ";", ",");

    Map<String, Integer> keyword = Map.ofEntries(Map.entry("int", 24), Map.entry("float", 25), Map.entry("void", 26), Map.entry("if", 27), Map.entry("else", 28), Map.entry("while", 29), Map.entry("continue", 30), Map.entry("break", 31), Map.entry("return", 32), Map.entry("for", 33));

}