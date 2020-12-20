package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonsterMessages {

    private static Map<Integer, List<List<Integer>>> ruleToOtherRules = new HashMap<>();
    private static Map<Integer, String> endRules = new HashMap<>();

    private static List<Integer> stack = new ArrayList<>();
    private static Set<List<Integer>> uniqStacks = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("monstermessages.txt"));
        List<String> lines = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        System.out.println("Part One: " + partOne(lines));
    }

    private static int partOne(List<String> lines) {
        List<String> ruleLines = new ArrayList<>();
        int i = 0;
        String line = lines.get(i);
        while (!line.isEmpty()) {
            ruleLines.add(line);
            line = lines.get(i);
            i++;
        }
        parseRules(ruleLines);
        List<String> messages = new ArrayList<>();
        i++;
        for (; i < lines.size(); i++) {
            line = lines.get(i);
            messages.add(line);
        }
        int count = 0;
        for (String message : messages) {
            String[] a = message.split("");
            List<String> messageChars = new ArrayList<>(Arrays.asList(a));
            uniqStacks = new HashSet<>();
            stack = new ArrayList<>();
            boolean solution = checkIfRulesMatchesMessage(0, messageChars);
            if (solution && messageChars.size() == 0) {
                count++;
            }
            if (uniqStacks.size() == 1 && message.equals(String.join("", messageChars))) {
                count++;
            }
        }
        return count;
    }

    private static void parseRules(List<String> lines) {
        for (String line : lines) {
            String[] lineParts = line.split(": ");
            Integer id = Integer.valueOf(lineParts[0]);
            List<List<Integer>> rule = ruleToOtherRules.getOrDefault(id, new ArrayList<>());
            ruleToOtherRules.put(id, rule);
            String ruleSetString = lineParts[1];

            if (ruleSetString.startsWith("\"")) {
                endRules.put(id, ruleSetString.substring(1, ruleSetString.length() - 1));
            } else {
                String[] rulesString = ruleSetString.split(" ");
                List<Integer> subRules = new ArrayList<>();
                for (String ruleString : rulesString) {
                    if (ruleString.equals("|")) {
                        rule.add(subRules);
                        subRules = new ArrayList<>();
                    } else {
                        Integer subRule = Integer.valueOf(ruleString);
                        List<List<Integer>> otherSubRUle = ruleToOtherRules.getOrDefault(subRule, new ArrayList<>());
                        ruleToOtherRules.put(subRule, otherSubRUle);
                        subRules.add(subRule);
                    }
                }
                rule.add(subRules);
            }
        }
    }

    private static boolean checkIfRulesMatchesMessage(Integer rule, List<String> message) {
        if (endRules.containsKey(rule)) {
            if (matches(message, endRules.get(rule))) {
                message.remove(0);
                return true;
            }
        }
        List<List<Integer>> subRuleLists = ruleToOtherRules.get(rule);
        for (List<Integer> subRuleList : subRuleLists) {
            boolean matches = true;
            List<String> copyMessage = new ArrayList<>(message);
            for (int subRule : subRuleList) {
                stack.add(rule);
                if (!checkIfRulesMatchesMessage(subRule, copyMessage)) {
                    matches = false;
                    stack.remove(rule);
                    break;
                }
            }
            if (matches) {
                while (message.size() > copyMessage.size()) {
                    message.remove(0);
                }
                if (message.size() == 0) {
                    uniqStacks.add(new ArrayList<>(stack));
                }
                return true;
            }
        }
        return false;
    }

    private static boolean matches(List<String> message, String ruleMatchString) {
        return String.join("", message).startsWith(ruleMatchString);
    }

}
