package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class OperationOrder {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("operationorder.txt"));
        String line = br.readLine();
        long partOneSum = 0;
        long partTwoSum = 0;
        while (line != null) {
            partOneSum += evaluateWhole(noOperatorPrecedence(line));
            partTwoSum += evaluateWhole(plusOperationPrecedence(line));
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOneSum);
        System.out.println("Part Two: " + partTwoSum);
    }

    private static long evaluateWhole(String line) {
        Stack<String> stack = new Stack<>();
        for (char c : line.toCharArray()) {
            if (c == '+' || c == '*') {
                long a = Long.parseLong(stack.pop());
                long b = Long.parseLong(stack.pop());
                if (c == '+') {
                    stack.push(String.valueOf(a + b));
                } else {
                    stack.push(String.valueOf(a * b));
                }
            } else {
                stack.push(String.valueOf(c));
            }
        }
        return Long.parseLong(stack.pop());
    }

    private static String noOperatorPrecedence(String line) {
        StringBuilder post = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (char c : line.toCharArray()) {
            switch (c) {
                case '+':
                case '*':
                    if (stack.isEmpty() || stack.peek() == '(') {
                        stack.push(c);
                    } else if (stack.peek() == '+' || stack.peek() == '*') {
                        post.append(stack.pop());
                        stack.push(c);
                    } else {
                        stack.push(c);
                    }
                    break;
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    while (stack.peek() != '(') {
                        post.append(stack.pop());
                    }
                    stack.pop();
                    break;
                default:
                    if (c != ' ') {
                        post.append(c);
                    }
                    break;
            }
        }
        while (!stack.isEmpty()) {
            post.append(stack.pop());
        }
        return post.toString();
    }

    private static String plusOperationPrecedence(String line) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (char c : line.toCharArray()) {
            switch (c) {
                case '+':
                case '*':
                    if (stack.isEmpty() || stack.peek() == '(') {
                        stack.push(c);
                    } else if (order(stack.peek()) >= order(c)) {
                        postfix.append(stack.pop());
                        stack.push(c);
                    } else {
                        stack.push(c);
                    }
                    break;
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    while (stack.peek() != '(') {
                        postfix.append(stack.pop());
                    }
                    stack.pop();
                    break;
                default:
                    if (c != ' ') {
                        postfix.append(c);
                    }
                    break;
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private static int order(char c) {
        return c == '+' ? 1 : -1;
    }

}
