import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import java.io.IOException;

public class Main {

    /**
     * Creates lexer with input C++ code file.
     * Writes generated HTML code in another file.
     * */
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("testdata\\input\\codeSample1.cpp");
        lexer.parse();
        lexer.writeParsedTokensToHTML("testdata\\output\\tokens.html");
    }
}
