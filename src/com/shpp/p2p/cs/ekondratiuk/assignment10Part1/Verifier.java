package com.shpp.p2p.cs.ekondratiuk.assignment10Part1;

import java.rmi.UnexpectedException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;

/**
 * Contains the auxiliary methods for verifying of required conditions
 */
public class Verifier {

    /**
     * Checks if the input arguments' set of the program is empty.
     *
     * @param args                  Input program arguments' array
     * @throws UnexpectedException  Throws the exception if the array is empty
     */
    public static void isArgsArrayEmpty(String[] args) throws UnexpectedException {
        if (args.length == 0) {
            throw new UnexpectedException("There is no arguments passed to program");
        }
    }

    /**
     * Checks if the String expression matches the syntax rules
     * Throws a corresponding exception if some of the rules is not satisfied.
     *
     * @param expression String of expression for checking
     */
    static void checkExpressionSyntax(String expression) {
        /* Check if the expression has zero dividing operation */
        if (expression.matches(".+/0.*")) {
            throw new IllegalArgumentException("Incorrect operation: Zero dividing");
        }
        if (
            /* several operators in succession exclude braces */
            expression.matches(".*[*/+^]{2,}.*") ||
            /* one or more than one operators *,/,^ at the beginning of the expression */
            expression.matches("^[*/+^]+.+$") ||
            /* one or more than one operators *,/,^ at the end of the expression */
            expression.matches("^.+[*/+^]+$") ||
            /* subsequence of a var or constant and left brace without operator between them */
            expression.matches(".*[a-zA-Z|0-9]+\\(.*") ||
            /* subsequence of left brace and var or constant without operator between them */
            expression.matches(".*\\)[a-zA-Z|0-9]+.*") ||
            /* subsequence of two right braces and one or more than one operator(+*^/-) between them */
            expression.matches(".*\\)[+*^/-]+\\).*") ||
            /* subsequence of two left braces and one or more than one operator(+*^/-) between them */
            expression.matches(".*\\([+*^/-]+\\(.*") ||
            /* subsequence of one or more than one operator after operator "-" */
            expression.matches(".*-[*/+^-]+.*") ||
            /* subsequence of "+-" operators */
            expression.matches(".*\\+-.*")
        ) {
            throw new IllegalArgumentException("Syntax error in the expression");
        }
    }

    /**
     * Checks if the given lexemes' list has correct braces' syntax
     * Left brace's number must match right brace's number
     * Each open brace must have its own corresponding close brace
     *
     * @param lexemes Lexemes list for checking
     */
    static void checkBracesSyntax(List<String> lexemes) {
        Stack<String> stack = new Stack<>();
        for (String lexeme : lexemes) {

            /* Open brace as current lexeme processing */
            if (lexeme.equals("(")) {
                if (stack.empty() || !stack.empty() && stack.peek().equals("(")) {
                    stack.push(lexeme);
                }
            }

            /* Close brace as current lexeme processing */
            else if (lexeme.equals(")")) {
                if (stack.empty()) {
                    throw new InputMismatchException("Something wrong with braces");
                }
                if (stack.peek().equals("(")) {
                    stack.pop();
                }
            }
        }
        if (!stack.empty()) {
            throw new InputMismatchException("Something wrong with braces");
        }
    }

    /**
     * Checks if the expression vars number is equal to passed arguments' number
     */
    static boolean expVarsNumberEqualsArgsNumber(List<String> varsInExpression, List<String> arguments ) {
        return varsInExpression.size() == arguments.size();
    }

    /**
     * Checks if the input variable's name matches the expression
     * It must be syntactically correct and correspond with the expression
     *
     * @param argument          Argument string(for example "a=2")
     * @param varsInExpression  List of allowed variable names in the expression
     * @return                  True if argument's name is correct and matches the expression
     */
    static boolean isArgumentNameCorrect(String argument, List<String> varsInExpression) {
        if (!isArgumentStringCorrect(argument)) {
            return false;
        }
        return expressionHasArgument(Parser.getVarNameFromArgument(argument), varsInExpression);
    }

    /**
     * Checks the input argument's notation format
     * Correct format is: "(any name of english letters)=(any double value)"
     * For example: "a=2" or "var = 2.0"
     *
     * @param s  String of argument
     * @return   True if the argument is correct
     */
    private static boolean isArgumentStringCorrect(String s) {
        return s.matches("[a-zA-Z]+=-*[0-9.]+");
    }

    /**
     * Checks if the expression has a variable with a name identical to passed
     *
     * @param varName           Variable's name for checking
     * @param varsInExpression  List of variables in the expression
     * @return                  True if the passed variables name is in the expression
     */
    private static boolean expressionHasArgument(String varName, List<String> varsInExpression) {
        return varsInExpression.contains(varName);
    }

    /**
     * Checks if the passed string is operator
     * Returns true if passed argument is one of "+-/*^()"
     */
    static boolean isOperator(String string) {
        return "_+-/*^()".contains(string);
    }

    /**
     * Checks if a passed position in the lexemes list is the unary minus
     *
     * @param operatorPosition The number of a lexeme in the lexemes' list
     * @param lexemes          The lexemes list
     * @return                 True if the specified lexeme is the unary minus
     */
    static boolean isUnaryMinus(int operatorPosition, List<String> lexemes) {
        return operatorPosition == 0 || "^*/(".contains(lexemes.get(operatorPosition - 1));
    }

    /**
     * Checks if the passed string is unary operator
     * Returns true if passed argument is one of "_"
     */
    static boolean isUnaryOperator(String string) {
        return "_".contains(string);
    }

    /**
     * Checks if the passed element is variable
     * The variable can contains english letters in lower and upper case
     */
    static boolean isVariable(String line) {
        return line.matches("[a-zA-Z]+");
    }
}
