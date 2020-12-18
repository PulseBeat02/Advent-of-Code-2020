package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandheldHalting {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("handheldhalting.txt"));
        List<Pair<String, Integer>> boot = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            int value;
            if (line.charAt(4) == '+') {
                value = Integer.parseInt(line.substring(5));
            } else {
                value = Integer.parseInt(line.substring(4));
            }
            boot.add(new Pair<>(line.substring(0, 3), value));
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(boot));
        System.out.println("Part Two: " + partTwo(boot));
    }

    private static int partOne(List<Pair<String, Integer>> boot) {
        Set<Integer> visited = new HashSet<>();
        int count = 0;
        int recurse = 0;
        while (visited.add(recurse)) {
            String operation = boot.get(recurse).key;
            int number = boot.get(recurse).value;
            switch (operation) {
                case "acc":
                    count += number;
                    recurse++;
                    break;
                case "jmp":
                    recurse += number;
                    break;
                case "nop":
                    recurse++;
                    break;
            }
        }
        return count;
    }

    private static int partTwo(List<Pair<String, Integer>> boot) {
        for (int i = 0; i < boot.size(); i++) {
            Pair<String, Integer> instruction = boot.get(i);
            String type = instruction.key;
            int value = instruction.value;
            if (type.equals("jmp") || type.equals("nop")) {
                List<Pair<String, Integer>> modified = new ArrayList<>(boot);
                type = type.equals("jmp") ? "nop" : "jmp";
                modified.remove(instruction);
                modified.add(i, new Pair<>(type, value));
                int result = testValidity(modified);
                if (result != Integer.MAX_VALUE) {
                    return result;
                }
            }
        }
        return -1;
    }

    private static int testValidity(List<Pair<String, Integer>> boot) {
        int accumulator = 0;
        List<Integer> ran = new ArrayList<>();
        for (int i = 0; i < boot.size(); i++) {
            if (ran.contains(i)) {
                return Integer.MAX_VALUE;
            }
            ran.add(i);
            Pair<String, Integer> instruction = boot.get(i);
            String type = instruction.key;
            int value = instruction.value;
            switch (type) {
                case "nop":
                    break;
                case "acc":
                    accumulator += value;
                    break;
                case "jmp":
                    i += value - 1;
                    break;
            }
        }
        return accumulator;
    }

    private static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}