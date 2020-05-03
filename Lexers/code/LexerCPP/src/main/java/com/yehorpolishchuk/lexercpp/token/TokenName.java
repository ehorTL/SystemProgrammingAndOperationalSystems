package com.yehorpolishchuk.lexercpp.token;

public class TokenName {
    private TokenNameAllowed name;

    public TokenNameAllowed getTokenName(){
        return this.name;
    }

    public TokenName(TokenNameAllowed tokenNameAllowed) {
        this.name = tokenNameAllowed;
    }
}
