package com.yehorpolishchuk.lexercpp.lexeme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Lexeme {

    private static HashSet<String> ppDirectives = new HashSet<String>(
            new ArrayList<>(Arrays.asList(new String [] {"if", "ifdef", "ifndef", "elif", "else", "endif",
                    "include", "define", "unedf", "line",
                    "error", "pragma"}))
    );

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
        return Character.isLetter(c);
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
     * Size of char sequence is 29
     * */
    private static boolean isAllowedNonIdChar(char c){
        return  c == '_' || c == '{' || c == '}'  || c == '['  || c == ']' || c == '#' ||
                c == '(' || c == ')' || c == '<'  || c == '>'  || c == '%' || c == ':' ||
                c == ';' || c == '.' || c == '?'  || c == '*'  || c == '+' || c == '-' ||
                c == '/' || c == '^' || c == '&'  || c == '|'  || c == '~' || c == '!' ||
                c == '=' || c == ',' || c == '\\' || c == '\"' || c == '\'';
    }
}