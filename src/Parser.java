import java.util.List;

class Parser {
    private List<Token> tokens;
    private int index = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node Program() {
        Node program = new Node(TokenType.END, "");
        while (tokens.get(index).type != TokenType.END) {
            program.children.add(Assignment());
        }
        return program;
    }

    private Node Assignment() {
        // Assignment: Identifier = Exp;
        Node assignment = new Node(TokenType.OPERATOR, "=");
        assignment.children.add(Identifier());

        // Ensure '=' operator exists for assignment
        if (tokens.get(index).type == TokenType.OPERATOR && tokens.get(index).value.equals("=")) {
            index++;  // Remove '='
        } else {
            throw new IllegalArgumentException("Expected assignment operator");
        }

        assignment.children.add(Exp());

        // Ensure the semicolon exists at the end of an assignment
        if (tokens.get(index).type == TokenType.SEMICOLON) {
            index++;  // Remove ';'
        } else {
            throw new IllegalArgumentException("Expected semicolon");
        }

        return assignment;
    }

    private Node Exp() {
        // Exp -> Exp + Term | Exp - Term | Term
        Node exp = Term();

        while (tokens.get(index).type == TokenType.OPERATOR &&
                (tokens.get(index).value.equals("+") || tokens.get(index).value.equals("-"))) {
            // Create a node for operator
            Node opNode = new Node(tokens.get(index).type, tokens.get(index).value);
            index++;  // Consume the operator

            // Parse the right side term
            Node term = Term();
            opNode.children.add(exp);
            opNode.children.add(term);

            exp = opNode;  // Update the expression
        }

        return exp;
    }

    private Node Term() {
        // Term -> Term * Fact | Fact
        Node term = Fact();

        while (tokens.get(index).type == TokenType.OPERATOR && tokens.get(index).value.equals("*")) {
            // Create a node for operator
            Node opNode = new Node(tokens.get(index).type, tokens.get(index).value);
            index++;  // Consume the '*'

            // Parse the right side fact
            Node fact = Fact();
            opNode.children.add(term);
            opNode.children.add(fact);

            term = opNode;  // Update the term
        }

        return term;
    }

    private Node Fact() {
        // Fact -> ( Exp ) | - Fact | + Fact | Literal | Identifier
        if (tokens.get(index).type == TokenType.LPAREN) {
            index++;  // Remove '('
            Node exp = Exp();
            if (tokens.get(index).type != TokenType.RPAREN) {
                throw new IllegalArgumentException("Expected closing parenthesis");
            }
            index++;  // Remove ')'
            return exp;
        } else if (tokens.get(index).type == TokenType.OPERATOR &&
                (tokens.get(index).value.equals("+") || tokens.get(index).value.equals("-"))) {
            // Handle unary plus or minus
            String unaryOperator = tokens.get(index).value;
            index++;  // Consume the operator

            Node fact = Fact();
            Node opNode = new Node(TokenType.OPERATOR, unaryOperator);
            opNode.children.add(fact);
            return opNode;
        } else if (tokens.get(index).type == TokenType.LITERAL) {
            // Literal -> 0 | NonZeroDigit Digit*
            Node literal = Literal();
            return literal;
        } else if (tokens.get(index).type == TokenType.IDENTIFIER) {
            return Identifier();
        } else {
            throw new IllegalArgumentException("Invalid token in fact");
        }
    }

    private Node Literal() {
        // Literal -> 0 | NonZeroDigit Digit*
        String value = tokens.get(index).value;

        // Reject literals that start with 0 and have more digits (e.g., 001)
        if (value.startsWith("0") && value.length() > 1) {
            throw new IllegalArgumentException("Invalid literal: " + value + ". Leading zeros are not allowed.");
        }

        // Check for valid literal: 0 or NonZeroDigit Digit*
        if (value.equals("0")) {
            Node literal = new Node(TokenType.LITERAL, value);
            index++;  // Consume the literal
            return literal;
        } else if (isNonZeroDigit(value)) {
            Node literal = new Node(TokenType.LITERAL, value);
            index++;  // Consume the literal
            return literal;
        } else {
            throw new IllegalArgumentException("Invalid literal: " + value);
        }
    }

    // Helper method to check if the value is a valid NonZeroDigit (1-9)
    private boolean isNonZeroDigit(String value) {
        return value.matches("[1-9][0-9]*");  // Non-zero digit followed by optional digits
    }

    private Node Identifier() {
        // Identifier -> Letter [Letter | Digit]*
        if (tokens.get(index).type == TokenType.IDENTIFIER) {
            Node identifier = new Node(tokens.get(index).type, tokens.get(index).value);
            index++;  // Consume the identifier
            return identifier;
        } else {
            throw new IllegalArgumentException("Expected identifier");
        }
    }
}


//class Parser {
//    private List<Token> tokens;
//    private int index = 0;
//
//    public Parser(List<Token> tokens) {
//        this.tokens = tokens;
//    }
//
//    public Node parseProgram() {
//        Node program = new Node(TokenType.END, "");
//        while (tokens.get(index).type != TokenType.END) {
//            program.children.add(parseAssignment());
//        }
//        return program;
//    }
//
//    private Node parseAssignment() {
//        Node assignment = new Node(TokenType.OPERATOR, "=");
//        assignment.children.add(parseIdentifier());
//
//        if (tokens.get(index).type == TokenType.OPERATOR && tokens.get(index).value.equals("=")) {
//            index++;  // Remove '='
//        } else {
//            throw new IllegalArgumentException("Expected assignment operator");
//        }
//
//        assignment.children.add(parseExp());
//
//        if (tokens.get(index).type == TokenType.SEMICOLON) {
//            index++;  // Remove ';'
//        } else {
//            throw new IllegalArgumentException("Expected semicolon");
//        }
//
//        return assignment;
//    }
//
//    private Node parseExp() {
//        Node exp = parseTerm();
//
//        while (tokens.get(index).type == TokenType.OPERATOR && (tokens.get(index).value.equals("+") || tokens.get(index).value.equals("-"))) {
//            Node opNode = new Node(tokens.get(index).type, tokens.get(index).value);
//            index++;
//            Node term = parseTerm();
//            opNode.children.add(exp);
//            opNode.children.add(term);
//            exp = opNode;
//        }
//
//        return exp;
//    }
//
//    private Node parseTerm() {
//        Node term = parseFact();
//
//        while (tokens.get(index).type == TokenType.OPERATOR && tokens.get(index).value.equals("*")) {
//            Node opNode = new Node(tokens.get(index).type, tokens.get(index).value);
//            index++;
//            Node fact = parseFact();
//            opNode.children.add(term);
//            opNode.children.add(fact);
//            term = opNode;
//        }
//
//        return term;
//    }
//
//    private Node parseFact() {
//        if (tokens.get(index).type == TokenType.LPAREN) {
//            index++;  // Remove '('
//            Node exp = parseExp();
//            if (tokens.get(index).type != TokenType.RPAREN) {
//                throw new IllegalArgumentException("Expected closing parenthesis");
//            }
//            index++;  // Remove ')'
//            return exp;
//        } else if (tokens.get(index).type == TokenType.OPERATOR && (tokens.get(index).value.equals("+") || tokens.get(index).value.equals("-"))) {
//            String unaryOperator = tokens.get(index).value;
//            index++;
//            Node fact = parseFact();
//            Node opNode = new Node(TokenType.OPERATOR, unaryOperator);
//            opNode.children.add(fact);
//            return opNode;
//        } else if (tokens.get(index).type == TokenType.LITERAL) {
//            Node literal = new Node(tokens.get(index).type, tokens.get(index).value);
//            index++;
//            return literal;
//        } else if (tokens.get(index).type == TokenType.IDENTIFIER) {
//            return parseIdentifier();
//        } else {
//            throw new IllegalArgumentException("Invalid token in fact");
//        }
//    }
//
//    private Node parseIdentifier() {
//        if (tokens.get(index).type == TokenType.IDENTIFIER) {
//            Node identifier = new Node(tokens.get(index).type, tokens.get(index).value);
//            index++;
//            return identifier;
//        } else {
//            throw new IllegalArgumentException("Expected identifier");
//        }
//    }
//}
