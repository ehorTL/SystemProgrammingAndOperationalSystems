package com.yehorpolishchuk.lexercpp.lexeme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Lexeme {

    //length available 2,4,5,6
    private static HashSet<String> ppDirectives = new HashSet<String>(
            new ArrayList<>(Arrays.asList(new String [] {"if", "ifdef", "ifndef", "elif", "else", "endif",
                    "include", "define", "undef", "line",
                    "error", "pragma"}))
    );

    //size 73
    private static HashSet<String> keywords = new HashSet<String>(
            new ArrayList<>(Arrays.asList(new String [] {
                    "alignas", "continue", "friend", "register", "true",
                    "alignof", "decltype ", "goto", "reinterpret_cast", "try",
                    "asm", "default", "if", "return", "typedef",
                    "auto", "delete", "inline", "short", "typeid",
                    "bool", "do", "int", "signed", "typename",
                    "break", "double", "long", "sizeof", "union",
                    "case", "dynamic_cast", "mutable", "static", "unsigned",
                    "catch", "else", "namespace", "static_assert", "using",
                    "char", "enum", "new", "static_cast", "virtual",
                    "char16_t", "explicit", "noexcept", "struct", "void",
                    "char32_t", "export", "nullptr", "switch", "volatile",
                    "class", "extern", "operator", "template", "wchar_t",
                    "const", "false", "private", "this", "while",
                    "constexpr", "float", "protected", "thread_local",
                    "const_cast", "for", "public", "throw"}))
    );

    public static boolean isKeyword(String s){
        return keywords.contains((String) s);
    }

    /**
     * @return c is an alphabet character or digit or underscore
     * */
    public static boolean isIdentifierChar(char c) {
        return isLetterOrUnderscore(c) || isDigit(c);
    }

    /**
     * @return c is digit [0,...,9]
     * */
    public static boolean isDigit(char c){
        return Character.isDigit(c);
    }

    public static boolean isLetter(char c){
        return  (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isLetterOrUnderscore(char c){
        return isLetter(c) || c == '_';
    }

    public static boolean isPreprocessorKeyword(String s){
        return ppDirectives.contains((String) s);
    }

    /**
     * Symbols from basic source character set.
     * Set power is 96 = 96 = 26*2 alphabet chars + 10 numbers + 29 other symbols +
     * + whiltespaces (space, formfeed, linefeed, horizntal and vertical tab)
     *
     * \r code is 13 (carriage return)
     * \n code is 10 (linefeed)
     * \f code is 12 (formfeed)
     * */
    public static boolean isAllowedChar(char c){
        return isLetterOrUnderscore(c) || isDigit(c) || isAllowedNonIdChar(c) ||
                isWhitespace(c);
    }

    /**
     * Space, horizontal tab,
     * vertical tab == '\013' == 11,
     * formfeed  == '\014' == 12,
     * newline == linefeed == '\n' == 10
     */
    public static boolean isWhitespace(char c){
        return c == ' ' || c == '\t' || c == '\013' || c == '\014' || c == '\n';
    }

    public static boolean isWhitespaceWithoutNewLine(char c){
        return  (c != '\n') && isWhitespace(c);
    }

    /**
     * utily method
     * Size of char sequence is 28
     * */
    private static boolean isAllowedNonIdChar(char c){
        return  /*c == '_' || */ c == '{' || c == '}'  || c == '['  || c == ']' || c == '#' ||
                c == '(' || c == ')' || c == '<'  || c == '>'  || c == '%' || c == ':' ||
                c == ';' || c == '.' || c == '?'  || c == '*'  || c == '+' || c == '-' ||
                c == '/' || c == '^' || c == '&'  || c == '|'  || c == '~' || c == '!' ||
                c == '=' || c == ',' || c == '\\' || c == '\"' || c == '\'';
    }

    public static boolean isSeparatorAfterError(char c){
        return isWhitespace(c) || (isAllowedNonIdChar(c) &&
                c != '\\' && c != '\'' && c != '\"' && c != '#');
    }

    public static boolean isSeparatorAfterDecimalNumber(char c){
        return isWhitespace(c) || (isAllowedNonIdChar(c) &&
                c != '_' && c != '#' && c != '.' && c != '~' && c != '\\' && c != '\"' && c != '\'');
    }

    public static boolean isBinaryDigit(char c){
        return c == '0' || c == '1';
    }

    public static boolean isHexDigit(char c){
        return Character.isDigit(c) ||
                ((byte)'a' <= (byte)c && (byte)'e' >= (byte)c) ||
                ((byte)'A' <= (byte)c && (byte)'E' >= (byte)c);
    }

    public static boolean isOctalDigit(char c){
        return Character.isDigit(c) && (c < (byte)'8');
    }
}