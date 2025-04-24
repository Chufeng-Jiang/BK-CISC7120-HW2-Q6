import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Interpreter {
    private final Map<String, Integer> variables = new HashMap<>();

    public void interpret(String program) {
        try {
            Tokenizer tokenizer = new Tokenizer();
            List<Token> tokens = tokenizer.lex(program);

            Parser parser = new Parser(tokens);
            Node result = parser.Program();

            execute(result);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private int evaluate(Node node) {
        if (node.type == TokenType.LITERAL) {
            return Integer.parseInt(node.value);
        } else if (node.type == TokenType.IDENTIFIER) {
            if (variables.containsKey(node.value)) {
                return variables.get(node.value);
            } else {
                throw new IllegalArgumentException("Variable '" + node.value + "' is uninitialized");
            }
        } else if (node.type == TokenType.OPERATOR) {
            if (node.value.equals("+")) {
                return evaluate(node.children.get(0)) + evaluate(node.children.get(1));
            } else if (node.value.equals("-")) {
                if (node.children.size() == 1) {
                    return -evaluate(node.children.get(0));  // Unary minus
                } else {
                    return evaluate(node.children.get(0)) - evaluate(node.children.get(1));  // Binary minus
                }
            } else if (node.value.equals("*")) {
                return evaluate(node.children.get(0)) * evaluate(node.children.get(1));
            } else {
                throw new IllegalArgumentException("Invalid operator: " + node.value);
            }
        } else {
            throw new IllegalArgumentException("Invalid node type");
        }
    }

    private void execute(Node program) {
        for (Node assignment : program.children) {
            String identifier = assignment.children.get(0).value;
            int value = evaluate(assignment.children.get(1));
            variables.put(identifier, value);
        }

        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
