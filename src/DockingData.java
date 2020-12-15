import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockingData {

    public static long[] partOneIndexes;
    public static Map<String, Integer> partTwoIndexes;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("dockingdata.txt"));
        String line = br.readLine();
        String mask = line;
        partOneIndexes = new long[100000];
        partTwoIndexes = new HashMap<>();
        while (true) {
            if (line == null) {
                break;
            } else {
                if (line.startsWith("mask")) {
                    mask = line.substring(line.indexOf('=') + 2);
                } else {
                    partOne(mask, line);
                    partTwo(mask, line);
                }
            }
            line = br.readLine();
        }
        br.close();
        long partOneSum = 0L;
        long partTwoSum = 0L;
        for (int i = 0; i < 100000; i++) {
            partOneSum += partOneIndexes[i];
        }
        for (long l : partTwoIndexes.values()) {
            partTwoSum += l;
        }
        System.out.println("Part One: " + partOneSum);
        System.out.println("Part Two: " + partTwoSum);
    }

    public static void partOne(String mask, String line) {
        int index = Integer.parseInt(line.substring(line.indexOf('[') + 1, line.indexOf(']')));
        long value = Long.parseLong(line.substring(line.indexOf('=') + 2));
        for (int i = mask.length() - 1; i >= 0; i--) {
            char val = mask.charAt(i);
            long shift = mask.length() - i - 1;
            if (val != 'X') {
                long digit = Character.digit(val, 10);
                long maskIndex = (1L << shift);
                value = ((value & ~maskIndex) | ((digit << shift) & maskIndex));
            }
        }
        partOneIndexes[index] = value;
    }

    public static void partTwo(String mask, String line) {
        String address = line.substring(4, line.indexOf("]"));
        String num = line.substring(line.indexOf("=") + 2);
        List<String> addresses = decodeAddresses(address, mask);
        for (String add : addresses) {
            if (!partTwoIndexes.containsKey(add)) {
                partTwoIndexes.put(add, Integer.parseInt(num));
            } else {
                partTwoIndexes.replace(add, Integer.parseInt(num));
            }
        }
    }

    private static List<String> decodeAddresses(String address, String mask) {
        List<String> ret = new ArrayList<>(Arrays.asList(""));
        String binary = getBinaryPaddedString(address);
        for (int i = 0; i < mask.length(); i++) {
            switch (mask.charAt(i)) {
                case '0':
                    for (int a = 0; a < ret.size(); a++) {
                        ret.set(a, ret.get(a) + binary.charAt(i));
                    }
                    break;
                case '1':
                    for (int a = 0; a < ret.size(); a++) {
                        ret.set(a, ret.get(a) + "1");
                    }
                    break;
                case 'X':
                    int length = ret.size();
                    for (int a = 0; a < length; a++) {
                        String append = ret.remove(0);
                        ret.add(append + "0");
                        ret.add(append + "1");
                    }
                    break;
            }
        }
        return ret;
    }


    public static String getBinaryPaddedString(String str) {
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(Integer.parseInt(str)));
        while (sb.length() != 36) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

}
