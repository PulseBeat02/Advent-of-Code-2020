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

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("monstermessages.txt"));
        Map<Integer, Rule> rules = new HashMap<>();
        String line = br.readLine();
        while (!line.isEmpty()) {
            String[] bits = line.split(":");
            int id = Integer.parseInt(bits[0]);
            if (bits[1].charAt(1) == '"') {
                rules.put(id, new MatchingRule(id, bits[1].charAt(2)));
            } else {
                if (bits[1].contains("|")) {
                    String[] pipes = bits[1].split("\\|");
                    String[] first = pipes[0].trim().split(" ");
                    String[] second = pipes[1].trim().split(" ");
                    List<List<Rule>> rulesSet = new ArrayList<>();
                    rulesSet.add(Arrays.asList(new Rule(Integer.parseInt(first[0])), new Rule(Integer.parseInt(first[1]))));
                    rulesSet.add(Arrays.asList(new Rule(Integer.parseInt(second[0])), new Rule(Integer.parseInt(second[1]))));
                    rules.put(id, new SubRuleOr(id, rulesSet));
                } else {
                    String[] first = bits[1].split(" ");
                    rules.put(id, new SubRule(id, Arrays.asList(new Rule(Integer.parseInt(first[0])), new Rule(Integer.parseInt(first[1])))));
                }
            }
            line = br.readLine();
        }
        line = br.readLine();
        int count = 0;
        while (line != null) {
            if (matchesRules()) // to be continued
            line = br.readLine();
        }

    }

    private static int matchesRules(Rule rule, String query, int currentIndex) {
        if (rule instanceof MatchingRule) {
            MatchingRule matchingRule = (MatchingRule) rule;
            for (int i = currentIndex; i < query.length(); i++) {
                if (query.charAt(i) == matchingRule.character) {
                    return i;
                }
            }
        } else if (rule instanceof SubRule) {
            SubRule subRule = (SubRule) rule;
            List<Rule> children = subRule.children;
            for (int index = currentIndex; index < children.size(); index++) {
                int result = matchesRules(children.get(index), query, index);
                if (result > 0) {
                    currentIndex = result;
                } else {
                    return -1;
                }
            }
            return currentIndex;
        } else if (rule instanceof SubRuleOr) {
            SubRuleOr subRuleOr = (SubRuleOr) rule;
            List<List<Rule>> allRules = subRuleOr.children;
            outer: for (List<Rule> allRule : allRules) {
                for (int index = currentIndex; index < allRule.size(); index++) {
                    int result = matchesRules(allRule.get(index), query, index);
                    if (result > 0) {
                        currentIndex = result;
                    } else {
                        continue outer;
                    }
                }
                return currentIndex;
            }
        }
        return -1;
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
        private final List<Rule> children;

        public SubRule(int id, List<Rule> children) {
            super(id);
            this.children = children;
        }
    }

    private static class SubRuleOr extends Rule {
        private final List<List<Rule>> children;

        public SubRuleOr(int id, List<List<Rule>> children) {
            super(id);
            this.children = children;
        }
    }

}
