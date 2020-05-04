package com.yehorpolishchuk.lexercpp.token;

public class Token {
    private TokenName name;
    private TokenValue value;

    public Token(TokenNameAllowed tokenName, String value){
        this.name = new TokenName(tokenName);
        this.value = new TokenValue(value);
    }

    public void setName(TokenNameAllowed tokenName) {
        this.name = new TokenName(tokenName);
    }

    public TokenName getName() {
        return name;
    }

    public TokenValue getValue() {
        return value;
    }
}
