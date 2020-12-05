import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportRepair {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("reportrepair.txt"));
        List<Integer> list = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            list.add(Integer.parseInt(line));
            line = br.readLine();
        }
        System.out.println("Part One: " + partOne(list));
        System.out.println("Part Two: " + partTwo(list));
    }

    public static int partOne(List<Integer> list) {
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                int first = list.get(i);
                int second = list.get(j);
                if (first + second == 2020) {
                    result = first * second;
                    break;
                }
            }
        }
        return result;
    }

    public static int partTwo(List<Integer> list) {
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                for (int k = j + 1; k < list.size(); k++) {
                    int first = list.get(i);
                    int second = list.get(j);
                    int third = list.get(k);
                    if (first + second + third == 2020) {
                        result = first * second * third;
                        break;
                    }
                }
            }
        }
        return result;
    }

}
