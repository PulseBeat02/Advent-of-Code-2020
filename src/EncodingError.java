import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EncodingError {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("encodingerror.txt"));
        List<Long> list = new ArrayList<>();
        List<Long> prefixSum = new ArrayList<>();
        String line = br.readLine();
        long sum = 0;
        while (line != null) {
            long l = Long.parseLong(line);
            prefixSum.add(sum);
            list.add(l);
            sum += l;
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + calculatePartOne(list, 25));
        System.out.println("Part Two: " + getContiguousSet(list, prefixSum, calculatePartOne(list, 25)));
    }

    public static long getContiguousSet(List<Long> list, List<Long> prefixSum, long target) {
        Set<Long> values = new HashSet<>();

        for (int i = 0; i < prefixSum.size(); i++) {
            for (int j = 1; j < prefixSum.size(); j++) {
                long begin = prefixSum.get(i);
                long end = prefixSum.get(j);
                if (end - begin == target) {
                    long min = Long.MAX_VALUE;
                    long max = Long.MIN_VALUE;
                    for (int k = i; k < j; k++) {
                        if (list.get(k) < min) {
                            min = list.get(k);
                        } else if (list.get(k) > max) {
                            max = list.get(k);
                        }
                    }
                    return min + max;
                }
            }
        }
        return -1;
    }

    public static long calculatePartOne(List<Long> list, int preamble) {
        for (int i = preamble; i < list.size(); i++) {
            if (!checkValidity(list, i, preamble)) {
                return list.get(i);
            }
        }
        return -1;
    }

    // Uses Hashing for an O(N) Algorithm
    public static boolean checkValidity(List<Long> list, int index, int preamble) {
        Set<Long> values = new HashSet<>();
        for (int i = index - preamble; i < index; i++) {
            if (values.contains(list.get(index) - list.get(i))) {
                return true;
            }
            values.add(list.get(i));
        }
        return false;
    }

}