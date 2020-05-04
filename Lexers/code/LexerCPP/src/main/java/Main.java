import com.yehorpolishchuk.lexercpp.lexeme.Lexeme;
import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.TokenName;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Lexer lexer = new Lexer("codeSample.cpp");
        System.out.println(Lexeme.isPreprocessorKeyword("testword"));

        Lexer lexer = new Lexer("codeSample3.cpp");
        lexer.parse();
        lexer.writeParsedTokensToHTML("C:\\Users\\user\\Desktop\\testhtml\\tokens.html");


        StringBuilder s = new StringBuilder("cheburek");
        changestring(s);
        System.out.println(s.toString());

    }

    public static boolean changestring(StringBuilder s){
        s.delete(0, s.length());
        return true;
    }
}
