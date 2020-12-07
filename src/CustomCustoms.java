import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CustomCustoms {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("customcustoms.txt"));
        String line = br.readLine();
        String form = "";
        int partOneCount = 0;
        int partTwoCount = 0;
        while (true) {
            if (line == null) {
                partOneCount += getCountPartOne(form);
                partTwoCount += getCountPartTwo(form);
                break;
            }
            if (line.isEmpty()) {
                partOneCount += getCountPartOne(form);
                partTwoCount += getCountPartTwo(form);
                form = "";
            } else {
                form += (line + " ");
            }
            line = br.readLine();
        }
        System.out.println("Part One: " + partOneCount);
        System.out.println("Part Two: " + partTwoCount);
    }

    public static int getCountPartOne(String str) {
        Set<Character> chars = new HashSet<>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        chars.remove(' '); // Don't Include Space Character
        return chars.size();
    }

    public static int getCountPartTwo(String str) {
        String[] array = str.split(" ");
        boolean[] primary = new boolean[26];
        for (int i = 0; i < primary.length; i++) {
            primary[i] = true;
        }
        for (int i = 0; i < array.length; i++) {
            boolean[] inner = new boolean[26];
            for (int j = 0; j < array[i].length(); j++) {
                if (primary[array[i].charAt(j) - 'a']) {
                    inner[array[i].charAt(j) - 'a'] = true;
                }
            }
            copyArray(primary, inner);
        }
        int count = 0;
        for (boolean b : primary) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    public static void copyArray(boolean[] dest, boolean[] array) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = array[i];
        }
    }


}
