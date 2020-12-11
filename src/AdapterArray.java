import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdapterArray {

    public static Map<Integer, Long> counts = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("adapterarray.txt"));
        List<Integer> list = new ArrayList<>();
        list.add(0);
        String line = br.readLine();
        while (line != null) {
            list.add(Integer.parseInt(line));
            line = br.readLine();
        }
        Collections.sort(list);
        System.out.println("Part One: " + partOne(list));
        System.out.println("Part Two: " + partTwo(list, 0));
    }

    public static int partOne(List<Integer> list) {
        int one = 0;
        int three = 1;
        for (int i = 0; i < list.size() - 1; i++) {
            int dist = list.get(i + 1) - list.get(i);
            if (dist == 1) {
                one++;
            } else if (dist == 3) {
                three++;
            }
        }
        return one * three;
    }

    public static long partTwo(List<Integer> list, int i) {
        if (i == list.size() - 1) {
            return 1;
        }
        if (counts.containsKey(i)) {
            return counts.get(i);
        }
        long count = 0;
        for (int j = i + 1; j < list.size(); j++) {
            if (list.get(j) - list.get(i) > 3) {
                break; // invalid order
            }
            count += partTwo(list, j); // add to count
        }
        counts.put(i, count); // add the count for each of the integers in the list
        return count;
    }

}
