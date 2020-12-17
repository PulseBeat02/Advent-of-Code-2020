import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketTranslation {

    public static List<List<Integer>> nearbyTickets;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("tickettranslation.txt"));
        Map<String, List<Range>> props = new HashMap<>();
        String line = br.readLine();
        while (!line.isEmpty()) {
            int colonLoc = line.indexOf(':');
            int firstDashLoc = line.indexOf('-');
            int secondDashLoc = line.lastIndexOf('-');
            int orLoc = line.lastIndexOf("or");
            String propertyName = line.substring(0, colonLoc);
            Range first = new Range(Integer.parseInt(line.substring(colonLoc + 2, firstDashLoc)),
                    Integer.parseInt(line.substring(firstDashLoc + 1, orLoc - 1)));
            Range second = new Range(Integer.parseInt(line.substring(orLoc + 3, secondDashLoc)),
                    Integer.parseInt(line.substring(secondDashLoc + 1)));
            props.put(propertyName, new ArrayList<>(Arrays.asList(first, second)));
            line = br.readLine();
        }
        br.readLine();
        List<Integer> myTickets = new ArrayList<>();
        String tickets = br.readLine();
        for (String num : tickets.split(",")) {
            myTickets.add(Integer.parseInt(num));
        }
        br.readLine();
        br.readLine();
        nearbyTickets = new ArrayList<>();
        nearbyTickets = new ArrayList<>();
        String nearby = br.readLine();
        while (nearby != null) {
            String[] nums = nearby.split(",");
            List<Integer> rowNums = new ArrayList<>();
            for (String near : nums) {
                rowNums.add(Integer.parseInt(near));
            }
            nearbyTickets.add(rowNums);
            nearby = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(props));
        System.out.println("Part Two: " + partTwo(props, myTickets));
    }

    public static int partOne(Map<String, List<Range>> props) {
        int sum = 0;
        for (int i = 0; i < nearbyTickets.size(); i++) {
            outer: for (int j = 0; j < nearbyTickets.get(i).size(); j++) {
                int query = nearbyTickets.get(i).get(j);
                for (List<Range> ranges : props.values()) {
                    for (Range range : ranges) {
                        if (valid(range, query)) {
                            continue outer;
                        }
                    }
                }
                sum += query;
                nearbyTickets.get(i).set(j, -1);
            }
        }
        return sum;
    }

    public static boolean valid(Range range, int query) {
        return query >= range.min && query <= range.max;
    }

    public static long partTwo(Map<String, List<Range>> props, List<Integer> tickets) {
        List<String> fieldsIdentified = new ArrayList<>();
        List<List<String>> possibleFields = getPossibleFields(props, tickets);
        while (fieldsIdentified.size() != possibleFields.size()) {
            for (int i = 0; i < possibleFields.size(); i++) {
                List<String> fields = possibleFields.get(i);
                if (fields.size() == 1) {
                    if (!fieldsIdentified.contains(fields.get(0))) {
                        fieldsIdentified.add(fields.get(0));
                    }
                } else {
                    List<String> newFields = new ArrayList<>();
                    for (String field : fields) {
                        if (!fieldsIdentified.contains(field)) {
                            newFields.add(field);
                        }
                    }
                    possibleFields.set(i, newFields);
                }
            }
        }
        long product = 1;
        for (int i = 0; i < possibleFields.size(); i++) {
            if (possibleFields.get(i).get(0).startsWith("departure")) {
                product *= tickets.get(i);
            }
        }
        return product;
    }

    public static List<List<String>> getPossibleFields(Map<String, List<Range>> props, List<Integer> tickets) {
        List<List<String>> indexes = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i++) {
            List<Integer> column = new ArrayList<>();
            for (int j = 0; j < nearbyTickets.size(); j++) {
                int num = nearbyTickets.get(j).get(i);
                if (num != -1) {
                    column.add(num);
                }
            }
            indexes.add(getMatchingFields(props, column));
        }
        return indexes;
    }

    public static List<String> getMatchingFields(Map<String, List<Range>> props, List<Integer> column) {
        List<String> matches = new ArrayList<>();
        for (String key : props.keySet()) {
            List<Range> ranges = props.get(key);
            Range first = ranges.get(0);
            Range second = ranges.get(1);
            boolean possible = true;
            for (int i = 0; i < column.size(); i++) {
                int num = column.get(i);
                if (!((first.min <= num && num <= first.max) || (second.min <= num && num <= second.max))) {
                    possible = false;
                    break;
                }
            }
            if (possible) {
                matches.add(key);
            }
        }
        return matches;
    }

    public static class Range {
        public final int min;
        public final int max;

        public Range(int minimum, int maximum) {
            this.min = minimum;
            this.max = maximum;
        }
    }

}
