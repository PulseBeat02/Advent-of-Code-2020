import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinaryBoarding {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("binaryboarding.txt"));
        List<String> list = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            list.add(line);
            line = br.readLine();
        }
        Set<Integer> values = new HashSet<>(list.size());
        for (String str : list) {
            values.add(Integer.parseInt(str.replaceAll("[BR]", "1").replaceAll("[FL]", "0"), 2));
        }
        int max = 0;
        for (int id : values) {
            if (id > max) {
                max = id;
            }
        }
        System.out.println("Part One: " + max);
        for (int id = 0; id < 1024; id++) {
            if (!values.contains(id) && values.contains(id + 1) && values.contains(id - 1)) {
                System.out.println("Part Two: " + id);
                break;
            }
        }
    }

}
