package com.shpp.p2p.cs.ekondratiuk.assignment10Part1;

import java.util.*;

/**
 * Evaluates arithmetical expressions is string format(with spaces or without them)
 * The expression may have variables consist of english letters in lower and upper case
 * The expression may have constants of integer or double(num.num) numbers
 * The evaluator supports the format with braces and without them as well
 * The evaluator supports the following operations: "+","-","*","/","^"
 * "*","/" are operators of equal priority. Left-associative
 * "+","-" are operators of equal priority, but of lower range than previous pair. Left-associative
 * "^" has the highest priority from the used binary operators("+","-","*","/")
 * The expression cannot have any operators at the end of the string
 * The expression can have only one operator of unary minus at the beginning of the string
 * The expression cannot have more than one of the operators in succession, exclude: "*-","/-","^-"
 */
public class Main {
    /**
     * Gets an expression(and its arguments if they are needed) and calculate it.
     * After launch of the program it parses the passed expression and arguments
     * through IDEA parameters or command line.
     * The program can calculate the same already parsed expression using new arguments
     * passed through the console.
     *
     * @param args Input arguments' list of the program
     */
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        List<String> arguments = new ArrayList<>();

        try {
            /* Check if any args were passed in program */
            Verifier.isArgsArrayEmpty(args);

            String expression = Parser.prepareExpression(args[0]);
            List<String> lexemes = Parser.getLexemesList(expression);
            Verifier.checkBracesSyntax(lexemes);
            List<String> varsInExpression = Parser.getVarsFromExpression(lexemes);
            List<String> postfixNotation = Parser.getPostfixNotation(Parser.getLexemesList(expression));

            /* Primary arguments from command line processing */
            for (int i = 1; i < args.length; i++) {
                String argument = Parser.trimSpaces(args[i]);
                if (Verifier.isArgumentNameCorrect(argument, varsInExpression)) {
                    arguments.add(argument);
                }
            }
            /* Try to evaluate the expression with primary arguments */
            if (Verifier.expVarsNumberEqualsArgsNumber(varsInExpression, arguments)) {
                String calculateResult = Calculator.calculateExpression(arguments, postfixNotation);
                printResults(expression, arguments, calculateResult);
                arguments.clear();
            } else {
                System.out.println("Input expression: " + expression +
                        "\nSomething wrong with arguments.");
            }

            /* Evaluate processed expression for cyclically got arguments from the console if they are */
            while (varsInExpression.size() > 0) {
                System.out.println("\nInitialize the variables with new values " +
                        "in the following format: varName=varValue\n" +
                        "* varName may contain english letters in lower and upper case\n" +
                        "* varValue may be any constant of integer or double(number.number) type\n" +
                        "After type an argument string press Enter.\n");
                /* The cycle iterations number is the same as variables number in the input expression */
                for (String var : varsInExpression) {
                    while (true) {
                        /* Check every argument from input till it is allowed and syntactically correct */
                        System.out.print("argument " + var + ": ");
                        String argument = scan.nextLine();
                        if (argument.equals("")) {
                            System.out.println("Argument is not passed. Try again");
                            continue;
                        }
                        argument = Parser.trimSpaces(argument);
                        if (Verifier.isArgumentNameCorrect(argument, varsInExpression)) {
                            if (Parser.getVarNameFromArgument(argument).equals(var)) {
                                arguments.add(argument);
                                break;
                            } else {
                                System.out.println("The given argument does not match the prompted variable. " +
                                        "Try again");
                            }
                        } else {
                            System.out.println("Something wrong with passed arguments.");
                        }
                    }
                }
                /* Try to evaluate the expression with arguments from the console */
                String calculateResult = Calculator.calculateExpression(arguments, postfixNotation);
                printResults(expression, arguments, calculateResult);
                arguments.clear();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /** Prints the input expression, the passed arguments if they are and the calculation's result */
    private static void printResults(String expression, List<String> arguments, String result) {
        System.out.println("Input expression: " + expression);
        if (arguments.size() > 0) {
            System.out.print("Arguments: ");
            for (String argument : arguments) {
                System.out.print(argument + " ");
            }
            System.out.println();
        }
        System.out.println("Result: " + result);
    }
}