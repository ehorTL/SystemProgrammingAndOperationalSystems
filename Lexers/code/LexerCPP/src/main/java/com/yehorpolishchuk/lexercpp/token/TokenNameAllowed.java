package com.yehorpolishchuk.lexercpp.token;

public enum TokenNameAllowed {
    KEYWORD,
    OPERATOR,
    PUNCTUATOR, // separator ()[]{} , ;
    COMMENT,
    IDENTIFIER,
    LITERAL_STRING,
    LITERAL_CHAR,
    LITERAL_NUMBER,
    PREPROCESSOR_DIR,
    TRIGRAPH,
    ERROR
}
