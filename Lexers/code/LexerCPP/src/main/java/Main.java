import com.yehorpolishchuk.lexercpp.lexeme.Lexeme;
import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.TokenName;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Lexer lexer = new Lexer("codeSample.cpp");
        System.out.println(Lexeme.isPreprocessorKeyword("testword"));

        Lexer lexer = new Lexer("codeSample2.cpp");
        lexer.parse();
        lexer.writeParsedTokensToHTML("C:\\Users\\user\\Desktop\\testhtml\\tokens.html");
    }
}
