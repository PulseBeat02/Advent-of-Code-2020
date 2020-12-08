import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HandyHaversacksPartOne {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("handyhaversacks.txt"));
        Map<Bag, Set<Bag>> bags = new HashMap<>();
        String line = br.readLine();
        Set<Character> digits = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        while (line != null) {
            String[] fragments = line.split(" ");
            String name = fragments[0] + " " + fragments[1];
            Bag parent = new Bag(null, name);
            bags.put(new Bag(parent, name), getChildrenBags(fragments, parent, digits));
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
        Set<Bag> answerSet = new HashSet<>();
        Queue<Bag> searchSet = new PriorityQueue<>();
        Bag gold = new Bag(null, "shiny gold");
        searchSet.add(gold);
        while (searchSet.size() > 0) {
            Bag bag = searchSet.poll();
            if (!bag.marked) {
                for (Bag parent : bags.keySet()) {
                    for (Bag child : bags.get(parent)) {
                        if (child.equals(bag)) {
                            answerSet.add(child.parent);
                            searchSet.add(child.parent);
                            break;
                        }
                    }
                }
            }
            bag.marked = true;
        }

        System.out.println("Part One: " + answerSet.size());

    }

    private static Set<Bag> getChildrenBags(String[] fragments, Bag parent, Set<Character> digits) {
        Set<Bag> childrenBags = new HashSet<>();
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
                childrenBags.add(new Bag(parent, childrenName));
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
