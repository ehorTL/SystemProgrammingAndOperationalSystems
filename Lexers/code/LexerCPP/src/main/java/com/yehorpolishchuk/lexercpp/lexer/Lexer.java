package com.yehorpolishchuk.lexercpp.lexer;

import com.yehorpolishchuk.lexercpp.lexeme.Lexeme;
import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import static com.yehorpolishchuk.lexercpp.token.TokenNameAllowed.*;

public class Lexer {
    private int state;
    private int indexPointerRawCodeStream;
    private String rawCode;
    private StringBuilder rawCodeStream;
    private ArrayList<Token> tokens;
    private String rawCodeFilename;
    private StringBuilder buffer;

    /**
     * Reads a file with filename and save it content to this.rawCode string.
     * All new line symbols are replaced with only '\n' character.
     */
    public Lexer(String rawCodeFilename) throws FileNotFoundException {
        this.rawCodeFilename = rawCodeFilename;

        File file = new File(rawCodeFilename);
        Scanner scanner = new Scanner(file);
        StringBuilder sb = new StringBuilder("");
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            sb.append("\n");
        }
        this.rawCodeStream = sb;
        this.rawCode = sb.toString();

        this.state = 0;
        this.indexPointerRawCodeStream = 0;
        this.tokens = new ArrayList<>();
        this.buffer = new StringBuilder();
    }

    public String generateHTMLFromTokens() {
        String htmlHeader = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Lexer</title>" +
                "<style>" +
                "html{" +
                "white-space: pre-wrap;" +
                "background-color: #e5e0e0;" +
                "font-weight: 900;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>";

        int currentIndex = 0;
        while (Lexeme.isWhitespace(this.rawCodeStream.charAt(currentIndex))) {
            currentIndex++;
        }

        StringBuilder returnHTML = new StringBuilder(htmlHeader);
        int currentTokenIndex = 0;
        while (currentIndex < this.rawCodeStream.length() && currentTokenIndex < tokens.size()) {
            returnHTML.append(generateHTMLTagForToken(tokens.get(currentTokenIndex)));
            currentIndex += tokens.get(currentTokenIndex).getValue().getValue().length();

            while ( currentIndex < this.rawCodeStream.length() &&
                    Lexeme.isWhitespace(this.rawCodeStream.charAt(currentIndex))) {
                returnHTML.append(this.rawCodeStream.charAt(currentIndex));
                currentIndex++;
            }

            currentTokenIndex++;
        }

        returnHTML.append("</body></html>"); //close body
        return returnHTML.toString();
    }

    private String escapeHTMLSpecialChars(String s){
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c){
                case '<': returnString.append("&lt;"); break;
                case '>': returnString.append("&gt;"); break;
                case '\'': returnString.append("&#39"); break;
                case '"': returnString.append("&quot"); break;
                case '&': returnString.append("&amp"); break;
                default:
                    returnString.append(c);
            }
        }

        return returnString.toString();
    }

    /**
     * Creates token array from rawCodeStream.
     * */
    public void parse(){
        //add \n to the beginning of the raw code and to the end of code
        this.rawCodeStream = new StringBuilder("\n" + this.rawCode + "\n");
        this.indexPointerRawCodeStream = 0;

        this.tokens = new ArrayList<>();

        //state handling loop
        while (this.indexPointerRawCodeStream < this.rawCodeStream.length()){
            char c = rawCodeStream.charAt(this.indexPointerRawCodeStream);

            //logging transitions of DFA
            System.out.print("MUMBER : " + indexPointerRawCodeStream + " code " + (byte)c + " char " +
                    ((c == '\n') ? "\\n" : c) + " STATE FROM: " + state);

            switch (this.state){
                case 0 : startState(c); break;
                case 1 : notAllowedSymbolsInBuffer1(c); break; //error state
                case 2: maybePPDirective2(c); break;
                case 3: maybePPDirective3(c); break;
                case 4: maybePPDirective4(c); break;
                case 5: maybePPDirective5Ends(c); break;
                case 6: maybeStringLiteral6(c); break;
                case 7: maybeStringLiteral7(c); break;
                case 8: maybeStringLiteral8(c); break;
                case 9: maybeStringLiteral9(c); break;
                case 10: maybeIdentifier10(c); break;
                case 11: lessThanOp(c); break;
                case 12: bitLeftShiftOp(c); break;
                case 13: greaterThatOp(c); break;
                case 14: bitRightShiftOp(c); break;
                case 15: plusOp15(c); break;
                case 16: minusOp16(c); break;
                case 17: pointerOp17(c); break;
                case 18: notXorMultOp18(c); break;
                case 19: bitAndOp19(c); break;
                case 20: bitOrOp20(c); break;
                case 21: modOp21(c); break;
                case 22: ppNumberSignAlt22(c); break;
                case 23: ppNumberSignDoubleAlt23(c); break;
                case 24: assignOrEqOp(c); break;
                case 25: dotOp25(c); break;
                case 26: maybeThreeDots26(c); break;
                case 27: colon27(c); break;
//                case 28: no 28 state
                case 29: maybeComment29(c); break;
                case 30: inSingleLineComment30(c); break;
                case 31: multilineComment31(c); break;
                case 32: maybeMultilineCommentCloses32(c); break;
                case 33: questionMarkOp33(c); break;
                case 34: maybeTrigraph34(c); break;
                case 35: floatStart35(c); break;
                case 36: floatExp36(c); break;
                case 37: floatExpSign37(c); break;
                case 38: floatExpWithoutSign38(c); break;
                case 39: floatExpBigD39(c); break;
                case 40: floatExpBigDBigL40(c); break;
                case 41: floatExpSmallD41(c); break;
//                case 42: no 42 state
                case 43: binOctHexStart43(c); break;
                case 44: hexStartX44(c); break;
                case 45: binStartB45(c); break;
                case 46: octStartNum0746(c); break;
                case 47: numberErrorAccumulatingState47(c); break;
                case 48: decimalStartNum48(c); break;
                case 49: unsignedUu49(c); break;
                case 50: unsignedSmallL50(c); break;
                case 51: unsignedBigL51(c); break;
                case 52: unsignedll52(c); break;
//                case 53: no 53 state
                case 54: hex54(c); break;
                case 55: bin55(c); break;
                case 56: charLiteralStart56(c); break;
                case 57: anyCharInCharLiteral57(c); break;
                case 58: escapeSymbolInCharLiteral58(c); break;
                case 59: whitespaceAfterEscapeSymbolInCharLiteral59(c); break;
                case 60: linefeedSymbolInCharLiteral60(c); break;
                case 61: floatEndingWithPoint61(c); break;
                case -1: {
                    state = 0;
                    indexPointerRawCodeStream--;
                    break;
                }
                default:{
                    break;
                }
            }

            //logging transitions of DFA
            System.out.println(" ---> " + state);

            this.indexPointerRawCodeStream++;
        }

        //flush not empty buffer as an ERROR
        if (this.buffer.length() != 0){
            addTokenAndClearBuffer(ERROR, buffer.toString());
        }

        //replace some identifiers with keywords
        for (Token t : tokens){
            if (t.getName().getTokenName() == IDENTIFIER){
                if (Lexeme.isKeyword(t.getValue().getValue())){
                    t.setName(KEYWORD);
                }
            }
        }

        //common table enhancement can be written here in loop
        //the simple way to clarify token is by using regex here in loop
    }

    /**
     * Add transitionSymbol to the buffer and set new state;
     * Transition function analogue
     */
    private void moveAndAddToBuffer(char transitionSymbol, int newState){
        this.state = newState;
        this.buffer.append(transitionSymbol);
    }

    private void moveAndAddToBuffer(String transitionSymbol, int newState){
        this.state = newState;
        this.buffer.append(transitionSymbol);
    }

    private void addToken(TokenNameAllowed tokenName, String value){
        tokens.add(new Token(tokenName, value));
    }

    private void addToken(TokenNameAllowed tokenName, char value){
        tokens.add(new Token(tokenName, Character.toString(value)));
    }

    /**
     * Creates and adds a token of < tokenName, value > to tokens array.
     * */
    private void addTokenAndClearBuffer(TokenNameAllowed tokenName, String value){
        tokens.add(new Token(tokenName, value));
        buffer = new StringBuilder("");
    }

    /**
     * Checks if input character is one from , ; { [ ( ) ] } ~
     * as they always makes token
     * */
    private boolean isAlwaysOneCharToken(char c){
        return c == ',' || c == ';' || c == '{' || c == '[' || c == '('
                || c == ')' || c == ']' || c == '}' || c == '~';
    }

    private void startState(char c){
        if (c == '#'){
            addToken(ERROR, "#"); // the same state
        } else if (c == '\n'){
            state = 2;
        } else if (c == '\"'){
            moveAndAddToBuffer('\"', 6);
        } else if (Lexeme.isLetterOrUnderscore(c)){
            moveAndAddToBuffer(c,10);
        } else if (isAlwaysOneCharToken(c)){
            if (c == '~'){
                addToken(OPERATOR, Character.toString(c));
            } else {
                addToken(PUNCTUATOR, Character.toString(c));
            }
        } else if (c == '|'){
            moveAndAddToBuffer(c, 20);
        } else if (c == '&'){
            moveAndAddToBuffer(c, 19);
        } else if (c == '!' || c == '^' || c == '*'){
            moveAndAddToBuffer(c, 18);
        } else if (c == '-'){
            moveAndAddToBuffer(c, 16);
        } else if (c == '+'){
            moveAndAddToBuffer(c, 15);
        } else if (c == '>'){
            moveAndAddToBuffer(c, 13);
        } else if (c == '<'){
            moveAndAddToBuffer(c, 11);
        } else if (c == '%'){
            moveAndAddToBuffer(c, 21);
        } else if (c == '='){
            moveAndAddToBuffer(c, 24);
        } else if (c == '\\'){
            addToken(ERROR, c);
        } else if (c == ':'){
            moveAndAddToBuffer(c, 27);
        } else if (c == '.'){
            moveAndAddToBuffer(c, 25);
        } else if (c == '/'){
            moveAndAddToBuffer(c, 29);
        } else if (c == '?'){
            moveAndAddToBuffer(c, 33);
        } else if (Lexeme.isDigit(c) && c != '0'){
            moveAndAddToBuffer(c, 48);
        } else if (c == '0'){
            moveAndAddToBuffer(c, 43);
        } else if (c == '\''){
            moveAndAddToBuffer(c, 56);
        } else if (!Lexeme.isAllowedChar(c)){
            moveAndAddToBuffer(c, 1);
        }
    }

    private void lessThanOp(char c){
        if (c == '<'){
            moveAndAddToBuffer(c, 12);
        } else if (c == '='){
            addTokenAndClearBuffer(OPERATOR, "<=");
            state = 0;
        } else if (c == ':' || c == '%'){
            addTokenAndClearBuffer(PUNCTUATOR, buffer.toString() + c);
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, "<");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void bitLeftShiftOp(char c){
        if (c == '='){
            addTokenAndClearBuffer(OPERATOR,"<<=");
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR,"<<");
            state = 0;
            indexPointerRawCodeStream--;
        }
    }

    private void greaterThatOp(char c){
        if (c == '>'){
            moveAndAddToBuffer(c, 14);
        } else if (c == '='){
            addTokenAndClearBuffer(OPERATOR, ">=");
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, ">");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void bitRightShiftOp(char c){
        if (c == '='){
            addTokenAndClearBuffer(OPERATOR, ">>=");
        } else {
            addTokenAndClearBuffer(OPERATOR, ">>");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void plusOp15(char c){
        if (c == '+' || c == '='){
            addTokenAndClearBuffer(OPERATOR,buffer.toString() + c);
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, "+");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void minusOp16(char c){
        if (c == '>'){
            moveAndAddToBuffer(c, 17);
        } else if (c == '-' || c == '='){
            addTokenAndClearBuffer(OPERATOR, buffer.toString() + c);
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, "-");
            state = 0;
            indexPointerRawCodeStream--;
        }
    }

    private void pointerOp17(char c){
        if (c == '*'){
            addTokenAndClearBuffer(OPERATOR, "->*");
        } else {
            addTokenAndClearBuffer(OPERATOR, "->");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void notXorMultOp18(char c){
        if (c == '='){
            addTokenAndClearBuffer(OPERATOR, buffer.toString() + c);
        } else {
            addTokenAndClearBuffer(OPERATOR, buffer.toString());
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void bitAndOp19(char c){
        if (c == '&' || c == '='){
            addTokenAndClearBuffer(OPERATOR, buffer.toString() + c);
        } else {
            addTokenAndClearBuffer(OPERATOR,"&");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void bitOrOp20(char c){
        if (c == '|' || c == '='){
            addTokenAndClearBuffer(OPERATOR, buffer.toString() + c);
        } else {
            addTokenAndClearBuffer(OPERATOR,"|");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void modOp21(char c){
        if (c == ':'){
            moveAndAddToBuffer(c, 22);
        } else if (c == '=' || c == '>'){
            addTokenAndClearBuffer(OPERATOR, buffer.toString() + c);
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, "%");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    //cannot be reached from 0 state, must be rewritten with reaching from 2 state
    private void ppNumberSignAlt22(char c){
        if (c == '%'){
            moveAndAddToBuffer(c, 23);
        } else {
            addTokenAndClearBuffer(OPERATOR, "%:");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    //generates error if %:% sequence in buffer as there cannot exist %: operator and then % operator
    private void ppNumberSignDoubleAlt23(char c){
        if (c == ':'){
            addTokenAndClearBuffer(OPERATOR, "%:%:");
        } else {
            addTokenAndClearBuffer(ERROR, "%:%");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void assignOrEqOp(char c){
        if (c == '='){
            addTokenAndClearBuffer(OPERATOR, "==");
        } else {
            addTokenAndClearBuffer(OPERATOR, "=");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void dotOp25(char c){
        if (c == '.'){
            moveAndAddToBuffer(c, 26);
        } else if (c == '*'){
            addTokenAndClearBuffer(OPERATOR, ".*");
            state = 0;
        } else if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 35);
        } else {
            addTokenAndClearBuffer(OPERATOR, ".");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void maybeThreeDots26(char c){
        if (c == '.'){
            addTokenAndClearBuffer(OPERATOR, "...");
            state = 0;
        } else {
            addTokenAndClearBuffer(ERROR, "..");
            state = -1;
            indexPointerRawCodeStream--;
        }
    }

    private void colon27(char c){
        if (c == ':'){
            addTokenAndClearBuffer(OPERATOR,"::");
        } else if (c == '>'){
            addTokenAndClearBuffer(PUNCTUATOR, ":>");
        } else {
            addTokenAndClearBuffer(OPERATOR, ":");
            indexPointerRawCodeStream--;
        }
        state = 0;
    }

    private void maybeComment29(char c){
        if (c == '/') {
            moveAndAddToBuffer(c, 30);
        } else if (c == '*'){
            moveAndAddToBuffer(c, 31);
        } else if (c == '='){
            addTokenAndClearBuffer(OPERATOR, "/=");
            state = 0;
        } else {
            addTokenAndClearBuffer(OPERATOR, "/");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void inSingleLineComment30(char c){
        if (c =='\n'){
            addTokenAndClearBuffer(COMMENT, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 30);
        }
    }

    private void multilineComment31(char c){
        if (c == '*'){
            moveAndAddToBuffer(c, 32);
        } else {
            moveAndAddToBuffer(c, 31);
        }
    }

    private void maybeMultilineCommentCloses32(char c){
        if (c == '/'){
            addTokenAndClearBuffer(COMMENT, buffer.toString() + '/');
            state = 0;
        } else {
            moveAndAddToBuffer(c, 31);
        }
    }

    private void questionMarkOp33(char c){
        if (c == '?'){
            moveAndAddToBuffer(c, 34);
        } else {
            addTokenAndClearBuffer(OPERATOR, "?");
            indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void maybeTrigraph34(char c){
        if (c == '=' || c == '/' || c == '\'' || c == '(' || c == ')' || c == '!'
                || c == '<' || c == '>' || c == '-'){
            addTokenAndClearBuffer(TRIGRAPH, buffer.toString() + c);
            state = 0;
        } else {
            addTokenAndClearBuffer(ERROR, "??");
            state = -1;
            indexPointerRawCodeStream--;
        }
    }

    private void floatStart35(char c){
        if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 35);
        } else if (c == 'e' || c == 'E'){
            moveAndAddToBuffer(c, 36);
        } else if (c == 'd'){
            moveAndAddToBuffer(c, 41);
        } else if (c == 'D'){
            moveAndAddToBuffer(c, 39);
        }

        else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void floatExp36(char c){
        if (c == '+' || c == '-'){
            moveAndAddToBuffer(c, 37);
        } else if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 38);
        } else {
            moveAndAddToBuffer(c, 47); //error accumulation
        }
    }

    private void floatExpSign37(char c){
        if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 38);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void floatExpWithoutSign38(char c){
        if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 38);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else if (c == 'd') {
            moveAndAddToBuffer(c, 41);
        } else if (c == 'D'){
            moveAndAddToBuffer(c, 39);
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void floatExpBigD39(char c){
        if (c == 'L') {
            moveAndAddToBuffer(c, 40);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void floatExpBigDBigL40(char c){
        if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void floatExpSmallD41(char c){
        if (c == 'l') {
            moveAndAddToBuffer(c, 40);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void binOctHexStart43(char c){
        if (c == '.'){
            moveAndAddToBuffer(c, 61);
        } else if (Lexeme.isOctalDigit(c)){
            moveAndAddToBuffer(c, 46);
        } else if (c == 'b' || c == 'B'){
            moveAndAddToBuffer(c, 45);
        } else if (c == 'x' || c == 'X'){
            moveAndAddToBuffer(c, 44);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void hexStartX44(char c){
       if (Lexeme.isHexDigit(c)){
           moveAndAddToBuffer(c, 54);
       } else {
           moveAndAddToBuffer(c, 47);
       }
    }

    private void binStartB45(char c){
        if (Lexeme.isBinaryDigit(c)){
            moveAndAddToBuffer(c, 55);
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void octStartNum0746(char c){
        if (Lexeme.isOctalDigit(c)){
            moveAndAddToBuffer(c, 46);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void numberErrorAccumulatingState47(char c){
        if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(ERROR, buffer.toString());
            indexPointerRawCodeStream--;
            state = -1;
        } else{
            moveAndAddToBuffer(c, 47);
        }
    }

    private void decimalStartNum48(char c){
        if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 48);
        } else if (c == 'u' || c== 'U'){
            moveAndAddToBuffer(c, 49);
        } else if (c == 'l'){
            moveAndAddToBuffer(c, 50);
        } else if (c == 'L'){
            moveAndAddToBuffer(c, 51);
        } else if (c == '.'){
            moveAndAddToBuffer(c, 61);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void unsignedUu49(char c){
        if (c == 'l'){
            moveAndAddToBuffer(c, 50);
        } else if (c == 'L'){
            moveAndAddToBuffer(c, 51);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void unsignedSmallL50(char c){
        if (c == 'l'){
            moveAndAddToBuffer(c, 52);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void unsignedBigL51(char c){
        if (c == 'L'){
            moveAndAddToBuffer(c, 52);
        } else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void unsignedll52(char c){
         if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void hex54(char c){
        if (Lexeme.isHexDigit(c)){
            moveAndAddToBuffer(c, 54);
        } else if (c == 'u' || c== 'U'){
            moveAndAddToBuffer(c, 49);
        } else if (c == 'l'){
            moveAndAddToBuffer(c, 50);
        } else if (c == 'L'){
            moveAndAddToBuffer(c, 51);
        }
        else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void bin55(char c){
        if (Lexeme.isBinaryDigit(c)){
            moveAndAddToBuffer(c, 54);
        } else if (c == 'u' || c== 'U'){
            moveAndAddToBuffer(c, 49);
        } else if (c == 'l'){
            moveAndAddToBuffer(c, 50);
        } else if (c == 'L'){
            moveAndAddToBuffer(c, 51);
        }
        else if (Lexeme.isSeparatorAfterDecimalNumber(c)){
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            indexPointerRawCodeStream--;
            state = 0;
        } else {
            moveAndAddToBuffer(c, 47);
        }
    }

    private void charLiteralStart56(char c){
        if (c == '\\'){
            moveAndAddToBuffer(c, 58);
        } else if (c == '\n'){
            addTokenAndClearBuffer(ERROR, "\'");
            indexPointerRawCodeStream--;
            state = -1;
        } else {
            moveAndAddToBuffer(c, 57);
        }
    }

    private void anyCharInCharLiteral57(char c){
        if (c == '\n'){
            addTokenAndClearBuffer(ERROR, buffer.toString());
            indexPointerRawCodeStream--;
            state = -1;
        } else if (c == '\''){
            addTokenAndClearBuffer(LITERAL_CHAR, buffer.toString()  + "\'");
            state = 0;
        } else if (c == '\\') {
            moveAndAddToBuffer(c, 58);
        } else {
            moveAndAddToBuffer(c, 57);
        }
    }

    private void escapeSymbolInCharLiteral58(char c){
        if (Lexeme.isWhitespaceWithoutNewLine(c)){
            moveAndAddToBuffer(c, 59);
        } else if (c == '\n'){
            moveAndAddToBuffer(c, 56);
        } else {
            moveAndAddToBuffer(c, 57);
        }
    }

    private void whitespaceAfterEscapeSymbolInCharLiteral59(char c){
        if (Lexeme.isWhitespaceWithoutNewLine(c)){
            moveAndAddToBuffer(c, 59);
        } else if (c == '\n'){
            moveAndAddToBuffer(c, 60);
        } else if (c == '\\'){
            moveAndAddToBuffer(c, 58);
        } else if (c == '\''){
            addTokenAndClearBuffer(LITERAL_CHAR, buffer.toString() + "\'");
            state = 0;
        } else {
            moveAndAddToBuffer(c, 57);
        }
    }

    private void linefeedSymbolInCharLiteral60(char c){
        if (Lexeme.isWhitespaceWithoutNewLine(c)){
            moveAndAddToBuffer(c, 59);
        } else if (c == '\\'){
            moveAndAddToBuffer(c, 58);
        } else if (c == '\''){
            addTokenAndClearBuffer(LITERAL_CHAR, buffer.toString() + "\'");
            state = 0;
        } else {
            moveAndAddToBuffer(c, 57); //or to 60
        }
    }

    private void floatEndingWithPoint61(char c){
        if (Lexeme.isDigit(c)){
            moveAndAddToBuffer(c, 35);
        } else if (c == 'e' || c == 'E'){
            moveAndAddToBuffer(c, 36);
        } else if (c == 'd'){
            moveAndAddToBuffer(c, 41);
        } else if (c == 'D'){
            moveAndAddToBuffer(c, 39);
        } else{
            addTokenAndClearBuffer(LITERAL_NUMBER, buffer.toString());
            state = 0;
            indexPointerRawCodeStream--;
        }
    }

    /**
     * Work with string literals.
    * */
    private void maybeStringLiteral6(char c){
        if (c == '\"'){
            addTokenAndClearBuffer(LITERAL_STRING, buffer.toString() + "\"");
            state = 0;
        } else if (c == '\n'){
            addTokenAndClearBuffer(ERROR, "\"");
            indexPointerRawCodeStream--;
            state = -1;
        } else if (c == '\\'){
            moveAndAddToBuffer(c, 7);
        } else if (c == '?'){
            moveAndAddToBuffer(c,8);
        } else {
            moveAndAddToBuffer(c, 6);  //the same state
        }
    }

    private void maybeStringLiteral7(char c){
        if(Lexeme.isWhitespaceWithoutNewLine(c)){
            moveAndAddToBuffer(c, 7);
        } else { // (c == '\n') and other
            moveAndAddToBuffer(c, 6);
        }
    }

    private void maybeStringLiteral8(char c){
        if (c == '?'){
          moveAndAddToBuffer(c,9);
        } else if (c == '\n'){
            addTokenAndClearBuffer(ERROR, buffer.toString());
            state = -1;
            indexPointerRawCodeStream--;
        } else {
            moveAndAddToBuffer(c, 6);
        }
    }

    private void maybeStringLiteral9(char c){
        if (c == '\n'){
            addTokenAndClearBuffer(ERROR, buffer.toString());
            state = -1;
            indexPointerRawCodeStream--;
        } else if (c == '/'){
            moveAndAddToBuffer(c, 7);
        } else {
            moveAndAddToBuffer(c, 6);
        }
    }

    private void maybeIdentifier10(char c){
        if (Lexeme.isIdentifierChar(c)){
            moveAndAddToBuffer(c,10); //retain the same
        } else if (!Lexeme.isAllowedChar(c)){
            moveAndAddToBuffer(c, 1);
        } else {
            addTokenAndClearBuffer(IDENTIFIER, buffer.toString());
            state = 0;
            indexPointerRawCodeStream--;
        }
    }

    private void maybePPDirective2(char c){
        if (Lexeme.isWhitespace(c)){
            state = 2; //retain the same state
        } else if (c == '#'){
            moveAndAddToBuffer('#', 3);
        } else {
            state = 0;
            indexPointerRawCodeStream--;
        }
    }

    private void notAllowedSymbolsInBuffer1(char c){
        if (Lexeme.isSeparatorAfterError(c)){
            addTokenAndClearBuffer(ERROR, buffer.toString());
            state = 0;
            indexPointerRawCodeStream--;
        } else{
            moveAndAddToBuffer(c, 1);
        }
    }

    private void maybePPDirective3(char c){
        if (Lexeme.isWhitespaceWithoutNewLine(c)){
            moveAndAddToBuffer(c,3); //retain the same
        } else {
            StringBuilder keywordSearchingFor = new StringBuilder("");
            if (isPreprocessorKeywordNext(this.indexPointerRawCodeStream, keywordSearchingFor)){
                moveAndAddToBuffer(keywordSearchingFor.toString(), 4);
                this.indexPointerRawCodeStream += keywordSearchingFor.length() - 1;
            } else { //c == '\n' or another
                addTokenAndClearBuffer(ERROR, "#");
                indexPointerRawCodeStream--;
                state = 0;
            }
        }
    }

    private void maybePPDirective4(char c){
        if (c == '/'){
            moveAndAddToBuffer('/', 5);
        } else if (Lexeme.isAllowedChar(c) && (c != '\n')){
            moveAndAddToBuffer(c, 4); //retain the same
        } else { //if newline symbol
            addTokenAndClearBuffer(PREPROCESSOR_DIR, this.buffer.toString());
            this.indexPointerRawCodeStream--;
            state = 0;
        }
    }

    private void maybePPDirective5Ends(char c){
        if (c == '/'){
            //delete last symbol to be '/' from buffer
            //next token is going to be a single line comment
            addTokenAndClearBuffer(PREPROCESSOR_DIR, buffer.deleteCharAt(buffer.length() - 1).toString());
            indexPointerRawCodeStream -= 2;
            state = 0;
        } else if (c == '\n'){
            addTokenAndClearBuffer(PREPROCESSOR_DIR, this.buffer.toString());
            this.indexPointerRawCodeStream--;
            state = 0;
        } else if (Lexeme.isAllowedChar(c) && (c != '\n')){
            moveAndAddToBuffer(c, 4); //retain the same
        } else {
            addTokenAndClearBuffer(ERROR, buffer.toString()); //unallowed symbol
            state = -1; //error state
        }
    }

    /**
     * Checks if the word starting from fromIndexInRawCodeStream index in rawCodeStream
     * is a preprocessor keyword.
     *
     * If true, the word found is written to returnWhatKeyword StringBuilder object.
     *
     * @param fromIndexInRawCodeStream index the string should be starting from
     * @param returnWhatKeyword the word found will be written in
     *
     * @return if code stream contains any preprocessor keyword starting from fromIndexInRawCodeStream
     * */
    private boolean isPreprocessorKeywordNext(int fromIndexInRawCodeStream, StringBuilder returnWhatKeyword){
        //length available 2,4,5,6
        int[] sizesAvailable = new int[]{2,4,5,6,7};
        String pattern = "";
        for (int i = 0; i < sizesAvailable.length; i++){
            if (fromIndexInRawCodeStream + sizesAvailable[i] <= this.rawCodeStream.length()){
                pattern = this.rawCodeStream.substring(fromIndexInRawCodeStream, fromIndexInRawCodeStream + sizesAvailable[i]);
                if (Lexeme.isPreprocessorKeyword(pattern)){
                    returnWhatKeyword.append(pattern);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns string representing HTML tag describing token in the manner it should be shown in a code editor.
     * */
    private String generateHTMLTagForToken(Token t) {
        StringBuilder returnTag = new StringBuilder("<span style=\"color: ");
        switch (t.getName().getTokenName()) {
            case KEYWORD:
                returnTag.append(TextTokenColor.KEYWORD.getColorCode());
                break;
            case OPERATOR:
                returnTag.append(TextTokenColor.OPERATOR.getColorCode());
                break;
            case COMMENT:
                returnTag.append(TextTokenColor.COMMENT.getColorCode());
                break;
            case PUNCTUATOR:
                returnTag.append(TextTokenColor.PUNCTUATOR.getColorCode());
                break;
            case IDENTIFIER:
                returnTag.append(TextTokenColor.IDENTIFIER.getColorCode());
                break;
            case LITERAL_STRING:
                returnTag.append(TextTokenColor.LITERAL_STRING.getColorCode());
                break;
            case LITERAL_CHAR:
                returnTag.append(TextTokenColor.LITERAL_CHAR.getColorCode());
                break;
            case LITERAL_NUMBER:
                returnTag.append(TextTokenColor.LITERAL_NUMBER.getColorCode());
                break;
            case PREPROCESSOR_DIR:
                returnTag.append(TextTokenColor.PREPROC_DIR.getColorCode());
                break;
            case ERROR:
                returnTag.append(TextTokenColor.ERROR.getColorCode());
                break;
            case TRIGRAPH:
                returnTag.append(TextTokenColor.TRIGRAPH.getColorCode());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t.getName().getTokenName());
        }

        return returnTag.append("\" title=\"" + t.getName().getTokenName().name() + "\">" +
                escapeHTMLSpecialChars(t.getValue().getValue()) +
                "</span>").toString();
    }

    public void writeParsedTokensToHTML(String fullpath) throws IOException {
        FileWriter fileWriter = new FileWriter(fullpath);
        fileWriter.write(generateHTMLFromTokens());
        fileWriter.close();
    }


    /**
     * Represents colors tokens will be printed in as HTML code
     */
    private enum TextTokenColor {
        COMMENT("#4B515D"), //grey
        OPERATOR("#CC0000"), //red
        PUNCTUATOR("#b71c1c"), //SEPARATOR, darken red
        IDENTIFIER("#000000"), //black
        KEYWORD("#1a237e"), //indigo darken
        PREPROC_DIR("#00C851"), //green
        ERROR("#880e4f"), //pink darken
        LITERAL_STRING("#304ffe"), //indigo accent-4
        LITERAL_CHAR("#e65100"), //orange darken-4
        LITERAL_NUMBER("#f48fb1"), // pink lighten-3
        TRIGRAPH("#4e342e"); //brown darken 3

        private final String colorCode;

        TextTokenColor(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return this.colorCode;
        }
    }

}
