import java.io.File;
import java.util.*;

import java.nio.file.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main <file-path>");
            System.exit(1);
        }
        String filePath = args[0];

        try {
            String program = new String(Files.readAllBytes(Paths.get(filePath)));

            Interpreter interpreter = new Interpreter();
            interpreter.interpret(program);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
