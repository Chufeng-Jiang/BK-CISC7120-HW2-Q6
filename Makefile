SRC_DIR := src
BIN_DIR := bin/src

SOURCES := $(wildcard $(SRC_DIR)/*.java)

CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)


DEPENDENCIES := $(BIN_DIR)/TokenType.class $(BIN_DIR)/Token.class $(BIN_DIR)/Tokenizer.class \
                $(BIN_DIR)/Node.class $(BIN_DIR)/Parser.class $(BIN_DIR)/Interpreter.class 


all: $(CLASSES) $(BIN_DIR)/Main.class


$(BIN_DIR)/TokenType.class: $(SRC_DIR)/TokenType.java
	@mkdir -p $(BIN_DIR)  
	javac -d $(BIN_DIR) $(SRC_DIR)/TokenType.java  


$(BIN_DIR)/Token.class: $(BIN_DIR)/TokenType.class $(SRC_DIR)/Token.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Token.java 


$(BIN_DIR)/Tokenizer.class: $(BIN_DIR)/Token.class $(BIN_DIR)/TokenType.class $(SRC_DIR)/Tokenizer.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Tokenizer.java  


$(BIN_DIR)/Node.class: $(SRC_DIR)/Node.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Node.java  


$(BIN_DIR)/Parser.class: $(BIN_DIR)/Tokenizer.class $(BIN_DIR)/TokenType.class $(SRC_DIR)/Parser.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Parser.java  


$(BIN_DIR)/Interpreter.class: $(BIN_DIR)/Node.class $(BIN_DIR)/Parser.class $(BIN_DIR)/Tokenizer.class $(SRC_DIR)/Interpreter.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Interpreter.java  


$(BIN_DIR)/Main.class: $(DEPENDENCIES) $(SRC_DIR)/Main.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(BIN_DIR) -d $(BIN_DIR) $(SRC_DIR)/Main.java  


clean:
	rm -rf $(BIN_DIR)


