.DEFAULT_GOAL := compile

.PHONY: help
help:
	@echo ""
	@echo "help:        Show this help"
	@echo "compile:     Compile the source code"
	@echo "tests:       Build the source code and run tests."
	@echo "build:       Run a clean build with mvn install -DskipTests"
	@echo "clean:       Clean build artifacts"
	@echo ""

.PHONY: deps
deps:
	command -v java
	command -v mvn
	@echo "Check Java JDK version 21"
	@java -version 2>&1 | grep "version \"21\..*\""

.PHONY: compile
compile: deps
	mvn compile

.PHONY: tests
tests:
	mvn test

.PHONY: build
build: clean
	mvn install -DskipTests

.PHONY: clean
clean:
	mvn clean
