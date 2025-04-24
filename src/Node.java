import java.util.ArrayList;
import java.util.List;

class Node {
    TokenType type;
    String value;
    List<Node> children;

    Node(TokenType type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }
}