package com.yehorpolishchuk.lexercpp.lexer;

import com.yehorpolishchuk.lexercpp.lexeme.Lexeme;
import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenName;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import com.yehorpolishchuk.lexercpp.token.TokenValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
     * All new line symbols are replaces with only '\n' character.
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
     * for now it's test data here hardcoded
     * */
    public void parse(){
        //add \n to the beginning of the raw code
        this.rawCodeStream = new StringBuilder("\n" + this.rawCode);
        this.indexPointerRawCodeStream = 0;

        this.tokens = new ArrayList<>();

        //state handling loop
        while (this.indexPointerRawCodeStream < this.rawCodeStream.length()){
            char c = rawCodeStream.charAt(this.indexPointerRawCodeStream);

            System.out.print("MUMBER : " + indexPointerRawCodeStream + " code " + (byte)c + " char " +
                    ((c == '\n') ? "\\n" : c) + " STATE FROM: " + state);

            switch (this.state){
                case 0 : startState(c); break;
//                case 1 :  no 1 state
                case 2: maybePPDirective2(c); break;
                case 3: maybePPDirective3(c); break;
                case 4: maybePPDirective4(c); break;
                case 5: maybePPDirective5Ends(c); break;
                case 6: maybeStringLiteral6(c); break;
                case 7: maybeStringLiteral7(c); break;
                case 8: maybeStringLiteral8(c); break;
                case 9: maybeStringLiteral9(c); break;
                case 10: maybeIdentifier10(c); break;
                case -1: {
                    state = 0;
                    indexPointerRawCodeStream--;
                    break;
                }
                default:{
                    break; //change code, dummy
                }
            }

            System.out.println(" ---> " + state);

            this.indexPointerRawCodeStream++;
        }

//        this.tokens = new ArrayList<>();
//        tokens.add(new Token(PREPROCESSOR_DIR, "#include <iostream>"));
//        tokens.add(new Token(KEYWORD, "int"));
//        tokens.add(new Token(IDENTIFIER, "main"));
//        tokens.add(new Token(PUNCTUATOR, "("));
//        tokens.add(new Token(PUNCTUATOR, ")"));
//        tokens.add(new Token(PUNCTUATOR, "{"));
//        tokens.add(new Token(KEYWORD, "return"));
//        tokens.add(new Token(LITERAL_NUMBER, "0"));
//        tokens.add(new Token(PUNCTUATOR, ";"));
//        tokens.add(new Token(PUNCTUATOR, "}"));
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

    private void addTokenAndClearBuffer(TokenNameAllowed tokenName, String value){
        tokens.add(new Token(tokenName, value));
        buffer = new StringBuilder("");
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
        }
        //else do nothing
    }

    /**
     * owrk with string literals
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
        LITERAL_CHAR("#fdd835"), //yellow darken-1
        LITERAL_NUMBER("#f48fb1"); // pink lighten-3

        private final String colorCode;

        TextTokenColor(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return this.colorCode;
        }
    }

}
