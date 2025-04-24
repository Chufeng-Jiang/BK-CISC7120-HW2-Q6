import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Tokenizer {
    public List<Token> lex(String input) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        while (index < input.length()) {
            char c = input.charAt(index);

            if (Character.isWhitespace(c)) {
                index++;
            } else if (Character.isAlphabetic(c) || c == '_') {
                StringBuilder identifier = new StringBuilder();
                identifier.append(c);
                index++;
                while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
                    identifier.append(input.charAt(index));
                    index++;
                }
                tokens.add(new Token(TokenType.IDENTIFIER, identifier.toString()));
            } else if (Character.isDigit(c)) {
                StringBuilder literal = new StringBuilder();
                literal.append(c);
                index++;
                while (index < input.length() && Character.isDigit(input.charAt(index))) {
                    literal.append(input.charAt(index));
                    index++;
                }

                String value = literal.toString();
               // System.out.println("Found literal: " + value);
                if (value.startsWith("0") && value.length() > 1) {
                //    System.out.println("Invalid literal detected!");
                    throw new IllegalArgumentException("Invalid literal: " + value + ". Leading zeros are not allowed.");
                }

                tokens.add(new Token(TokenType.LITERAL, literal.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '=') {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                index++;
            } else if (c == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                index++;
            } else if (c == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                index++;
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                index++;
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }
        tokens.add(new Token(TokenType.END, ""));
        return tokens;
    }
}