package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AllergenAssessment {

    private static final Map<String, String> translations = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("allergenassessment.txt"));
        Set<String> allergens = new HashSet<>();
        Set<String> ingredients = new HashSet<>();
        String in = br.readLine();
        List<String> inputs = new ArrayList<>();
        while (in != null) {
            inputs.add(in);
            String[] fragments = in.split("\\(");
            String[] ingredientFragments = fragments[0].trim().split(" ");
            ingredients.addAll(Arrays.asList(ingredientFragments));
            String[] allergensFraments = fragments[1].substring(9, fragments[1].length() - 1).replaceAll(",", "").split(" ");
            allergens.addAll(Arrays.asList(allergensFraments));
            in = br.readLine();
        }
        System.out.println("Part One: " + partOne(generateMatches(inputs, allergens, ingredients), inputs));
        System.out.println("Part Two: " + partTwo());
    }

    private static Map<String, List<String>> generateMatches(List<String> inputs, Set<String> allergens, Set<String> ingredients) {
        Map<String, List<String>> matches = new HashMap<>();
        for (String allergen : allergens) {
            List<String> temp = new ArrayList<>(ingredients);
            for (String line : inputs) {
                if (line.contains(" " + allergen)) {
                    temp.retainAll(new ArrayList<>(Arrays.asList(line.substring(0, line.indexOf("(") - 1).split(" "))));
                }
            }
            matches.put(allergen, temp);
        }
        return matches;
    }

    private static int partOne(Map<String, List<String>> possibleMatches, List<String> input) {
        while (!possibleMatches.isEmpty()) {
            Iterator<Map.Entry<String, List<String>>> itr = possibleMatches.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, List<String>> entry = itr.next();
                List<String> ingredients = entry.getValue();
                if (ingredients.size() == 1) {
                    String allergen = entry.getKey();
                    String ingredient = ingredients.get(0);
                    translations.put(allergen, ingredient);
                    itr.remove();
                    for (List<String> list : possibleMatches.values()) {
                        list.remove(ingredient);
                    }
                }
            }
        }
        int count = 0;
        for (String line : input) {
            String[] ingredients = line.substring(0, line.indexOf("(") - 1).split(" ");
            for (String ingredient : ingredients) {
                if (!translations.containsValue(ingredient)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static String partTwo() {
        Set<String> set = new TreeSet<>();
        for (String key : translations.keySet()) {
            set.add(key + "/" + translations.get(key));
        }
        StringBuilder dangerous = new StringBuilder();
        for (String str : set) {
            dangerous.append(str.substring(str.indexOf("/") + 1));
            dangerous.append(',');
        }
        return dangerous.substring(0, dangerous.length() - 1);
    }

}
