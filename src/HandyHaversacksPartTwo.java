import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HandyHaversacksPartTwo {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("handyhaversacks.txt"));
        Map<Bag, Map<Bag, Integer>> bags = new HashMap<>();
        String line = br.readLine();
        Set<Character> digits = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        Bag gold = null;
        while (line != null) {
            String[] fragments = line.split(" ");
            String name = fragments[0] + " " + fragments[1];
            Bag parent = new Bag(null, name);
            bags.put(parent, getChildrenBags(fragments, parent, digits));
            if (name.equals("shiny gold")) {
                gold = parent;
            }
            line = br.readLine();
        }
//        for (Bag key : bags.keySet()) {
//        	System.out.println("Key: " + key.name);
//        	System.out.println("Children: ");
//        	for (Bag value : bags.get(key)) {
//        		System.out.println(value.name + " ");
//        	}
//        	System.out.println();
//        }
        System.out.println("Part Two: " + calculate(bags, gold));
    }

    private static int calculate(Map<Bag, Map<Bag, Integer>> bags, Bag current) {
        int count = 0;
        for (Bag child : bags.get(current).keySet()) {
            count += bags.get(current).get(child) * calculate(bags, child);
        }
        return count;
    }

    private static Map<Bag, Integer> getChildrenBags(String[] fragments, Bag parent, Set<Character> digits) {
        Map<Bag, Integer> childrenBags = new HashMap<>();
        for (String str : fragments) {
            if (str.equals("no")) {
                return childrenBags;
            }
        }
        for (int i = 2; i < fragments.length; i++) {
            if (digits.contains(fragments[i].charAt(0))) {
                String last = fragments[i + 2];
                if (last.substring(0, last.length() - 1).equals(",")) {
                    last = last.substring(0, last.length() - 1);
                }
                String childrenName = fragments[i + 1] + " " + last;
                int count = Integer.parseInt(fragments[i]);
                childrenBags.put(new Bag(parent, childrenName), count);
            }
        }
        return childrenBags;
    }

    public static class Bag implements Comparable<Bag> {
        public final Bag parent;
        public final String name;
        public boolean marked;
        public Bag(Bag parent, String name) {
            this.parent = parent;
            this.name = name;
        }
        @Override
        public int compareTo(Bag o) {
            return this.name.compareTo(o.name);
        }
        @Override
        public boolean equals(Object other) {
            if (other instanceof Bag) {
                return name.equals(((Bag) other).name);
            } else {
                return false;
            }
        }
    }
}
