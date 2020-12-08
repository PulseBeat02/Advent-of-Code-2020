import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HandyHaversacksPartTwo {

    public static void main(String[] args) throws IOException {
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
        System.out.println("Part Two: " + count);
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
