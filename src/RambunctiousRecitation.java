import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RambunctiousRecitation {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("rambunctiousrecitation.txt"));
        String[] elements = br.readLine().split(",");
        List<Integer> nums = new ArrayList<>();
        for (String element : elements) {
            nums.add(Integer.parseInt(element));
        }
        br.close();
        System.out.println("Part One: " + calculateOutput(nums, 2020));
        System.out.println("Part Two: " + calculateOutput(nums, 30000000));
    }

    public static int calculateOutput(List<Integer> nums, int number) {
        Map<Integer, Integer> spoken = new HashMap<>();
        for (int i = 0; i < nums.size() - 1; i++) {
            spoken.put(nums.get(i), i);
        }
        int last = nums.get(nums.size() - 1);
        for (int nextIndex = spoken.size(); nextIndex < number - 1; nextIndex++){
            int recent = spoken.containsKey(last) ? nextIndex - spoken.get(last) : 0;
            spoken.put(last, nextIndex);
            last = recent;
        }
        return last;
    }

}
