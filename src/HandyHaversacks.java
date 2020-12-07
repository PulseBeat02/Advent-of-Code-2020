import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HandyHaversacks {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("handyhaversacks.txt"));
        Set<Bag> bags = new HashSet<>(); // avoid duplicates
        Set<String> added = new HashSet<>();
        String line = br.readLine();
        Set<Character> digits = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        while (line != null) {
            String[] fragments = line.split(" ");
            String name = fragments[0] + " " + fragments[1];
            List<Bag> children = getChildrenBags(fragments, digits);
            if (added.contains(name)) {
                Bag b = removeBag(bags, name);
                b.children.addAll(children);
                bags.add(b);
            } else {
                Bag parent = new Bag(name, children);
                added.add(name);
                bags.add(parent);
            }
            line = br.readLine();
        }
        int count = 0;
        Queue<Bag> recurse = new PriorityQueue<>(bags);
        while (bags.size() > 0) {
            Bag b = recurse.poll();
            if (b != null && b.children.size() != 0) {
                count += checkGoldValidity(b) ? 1 : 0;
                for (Bag child : b.children) {
                    recurse.add(child);
                }
            }
            recurse.forEach(bag -> {
                System.out.println("============================");
                System.out.println("Name: " + bag.name);
                System.out.print("Children: ");
                bag.children.forEach(child -> {
                    System.out.print(child.name + ", ");
                });
                System.out.println();
                System.out.println();
            });
        }
        System.out.println(count);
    }

    private static boolean checkGoldValidity(Bag b) {
        if (b == null) {
            return false;
        }
        for (Bag child : b.children) {
            if (child.name.equals("shiny gold")) {
                return true;
            }
        }
        return false;
    }

    private static Bag removeBag(Set<Bag> bags, String str) {
        for (Bag b: bags) {
            if (b.name.equals(str)) {
                bags.remove(b);
                return b;
            }
        }
        return null;
    }

    private static List<Bag> getChildrenBags(String[] fragments, Set<Character> digits) {
        List<Bag> childrenBags = new ArrayList<>();
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
                childrenBags.add(new Bag(childrenName, new ArrayList<>()));
            }
        }
        return childrenBags;
    }

    public static class Bag implements Comparable<Bag> {
        public final String name;
        public List<Bag> children;
        public Bag(String name, List<Bag> children) {
            this.name = name;
            this.children = children == null ? new ArrayList<>() : new ArrayList<>(children);
        }
        @Override
        public int compareTo(Bag o) {
            return this.name.compareTo(o.name);
        }
    }
}
