package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MonsterMessages {

    private static Queue<Rule> queue;
    private static Set<List<Rule>> unique;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("monstermessages.txt"));
        Map<Integer, Rule> rules = new HashMap<>();
        String currentLine = br.readLine();
        while (!currentLine.isEmpty()) {
            String[] bits = currentLine.split(":");
            int id = Integer.parseInt(bits[0]);
            if (bits[1].charAt(1) == '"') {
                rules.put(id, new MatchingRule(id, bits[1].charAt(2)));
            } else {
                if (bits[1].contains("|")) {
                    String[] pipes = bits[1].split("\\|");
                    List<List<Integer>> rulesSet = new ArrayList<>();
                    for (int j = 0; j < pipes.length; j++) {
                        List<Integer> currentRules = new ArrayList<>();
                        for (String child : pipes[j].trim().split(" ")) {
                            currentRules.add(Integer.parseInt(child));
                        }
                        rulesSet.add(currentRules);
                    }
                    rules.put(id, new SubRuleOr(id, rulesSet));
                } else {
                    String[] first = bits[1].trim().split(" ");
                    List<Integer> children = new ArrayList<>();
                    for (String str : first) {
                        children.add(Integer.parseInt(str));
                    }
                    rules.put(id, new SubRule(id, children));
                }
            }
            currentLine = br.readLine();
        }
        currentLine = br.readLine();
        List<String> queries = new ArrayList<>();
        while (currentLine != null) {
            queries.add(currentLine);
            currentLine = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + calculateCount(rules, queries));
        rules.replace(8, new SubRuleOr(8, Arrays.asList(Collections.singletonList(42), Arrays.asList(42, 8))));
        rules.replace(11, new SubRuleOr(11, Arrays.asList(Arrays.asList(42, 31), Arrays.asList(42, 11, 31))));
        System.out.println("Part Two: " + calculateCount(rules, queries));
    }

    private static int calculateCount(Map<Integer, Rule> rules, List<String> queries) {
        int i = 0;
        int count = 0;
        while (i < queries.size()) {
            String line = queries.get(i);
            char[] chars = line.toCharArray();
            List<Character> list = new ArrayList<>();
            for (char c : chars) {
                list.add(c);
            }
            queue = new ArrayDeque<>();
            unique = new HashSet<>();
            if (matchesRules(rules, rules.get(0), list) && list.size() == 0) {
                count++;
            }
            if (unique.size() == 1 && line.equals(concatenate(list))) {
                count++;
            }
            i++;
        }
        return count;
    }

    private static String concatenate(List<Character> chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static boolean matchesRules(Map<Integer, Rule> rules, Rule rule, List<Character> query) {
        if (rule instanceof MatchingRule) {
            MatchingRule matchingRule = (MatchingRule) rule;
            if (concatenate(query).startsWith(String.valueOf(matchingRule.character))) {
                query.remove(0);
                return true;
            }
        } else {
            List<List<Integer>> allRules = new ArrayList<>();
            if (rule instanceof SubRule) {
                allRules.add(((SubRule)rule).children);
            } else if (rule instanceof SubRuleOr) {
                allRules = new ArrayList<>(((SubRuleOr)rule).children);
            }
            for (List<Integer> allRule : allRules) {
                boolean matches = true;
                List<Character> temp = new ArrayList<>(query);
                for (int child : allRule) {
                    queue.add(rule);
                    Rule childRule = rules.get(child);
                    if (!matchesRules(rules, childRule, temp)) {
                        matches = false;
                        queue.remove(childRule);
                        break;
                    }
                }
                if (matches) {
                    int size = query.size();
                    while (size > temp.size()) {
                        query.remove(0);
                        size--;
                    }
                    if (size == 0) {
                        unique.add(new ArrayList<Rule>(queue));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static class Rule {
        private final int id;
        public Rule(int id) {
            this.id = id;
        }
    }

    private static class MatchingRule extends Rule {
        private final char character;
        public MatchingRule(int id, char c) {
            super(id);
            this.character = c;
        }
    }

    private static class SubRule extends Rule {
        private final List<Integer> children;
        public SubRule(int id, List<Integer> children) {
            super(id);
            this.children = children;
        }
    }

    private static class SubRuleOr extends Rule {
        private final List<List<Integer>> children;
        public SubRuleOr(int id, List<List<Integer>> children) {
            super(id);
            this.children = children;
        }
    }


}
