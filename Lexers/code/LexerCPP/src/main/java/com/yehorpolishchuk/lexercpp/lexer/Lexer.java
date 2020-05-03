package com.yehorpolishchuk.lexercpp.lexer;

import com.yehorpolishchuk.lexercpp.lexeme.Lexeme;
import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenName;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.yehorpolishchuk.lexercpp.token.TokenNameAllowed.*;

public class Lexer {
    private int state;
    private int indexPointer;
    private String rawCode;
    private StringBuilder rawCodeStream;
    private ArrayList<Token> tokens;
    private String rawCodeFilename;

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
        this.indexPointer = 0;
        this.tokens = new ArrayList<>();
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
        this.tokens = new ArrayList<>();
        tokens.add(new Token(PREPROCESSOR_DIR, "#include <iostream>"));
        tokens.add(new Token(KEYWORD, "int"));
        tokens.add(new Token(IDENTIFIER, "main"));
        tokens.add(new Token(PUNCTUATOR, "("));
        tokens.add(new Token(PUNCTUATOR, ")"));
        tokens.add(new Token(PUNCTUATOR, "{"));
        tokens.add(new Token(KEYWORD, "return"));
        tokens.add(new Token(LITERAL_NUMBER, "0"));
        tokens.add(new Token(PUNCTUATOR, ";"));
        tokens.add(new Token(PUNCTUATOR, "}"));
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
