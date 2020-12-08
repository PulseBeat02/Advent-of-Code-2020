import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HandyHaversacks {

    public static void main(String[] args) throws IOException {
        System.out.println("Part One: " + partOne());
        System.out.println("Part Two: " + partTwo());
    }

    public static int partOne() throws IOException {
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
        br.close();
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
        return answerSet.size();
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

    //////////////////////////////////////
    //                                  //
    //             Part Two             //
    //                                  //
    //////////////////////////////////////

    public static int partTwo() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("handyhaversacks.txt"));
        Map<String, List<String>> bags = new HashMap<>();
        String line = br.readLine();
        Set<Character> digits = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        while (line != null) {
            String[] fragments = line.split(" ");
            String name = fragments[0] + " " + fragments[1];
            bags.put(name, getChildrenBags(fragments, name, digits));
            line = br.readLine();
        }
        br.close();
        int count = 0;
        Queue<String> queue = new PriorityQueue<>(bags.get("shiny gold"));
        while (queue.size() > 0) {
            String p = queue.poll();
            for (int i = 0; i < bags.get(p).size(); i++) {
                queue.add(bags.get(p).get(i));
            }
            count++;
        }
        return count;
    }

    private static List<String> getChildrenBags(String[] fragments, String parent, Set<Character> digits) {
        List<String> childrenBags = new ArrayList<>();
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
                for (int j = 0; j < count; j++) {
                    childrenBags.add(childrenName);
                }
            }
        }
        return childrenBags;
    }

}
