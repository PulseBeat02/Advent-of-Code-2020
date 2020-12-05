import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PassportProcessing {

    private static int countPartOne;
    private static int countPartTwo;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("passportprocessing.txt"));
        String line = br.readLine();
        List<String> requirements = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
        String passport = "";
        while (true) {
            if (line == null) {
                partOneCheck(requirements, passport);
                partTwoCheck(requirements, passport);
                break;
            }
            if (line.isEmpty()) {
                partOneCheck(requirements, passport);
                partTwoCheck(requirements, passport);
                passport = "";
            } else {
                passport += (line + " ");
            }
            line = br.readLine();
        }
        System.out.println("Part One: " + countPartOne);
        System.out.println("Part Two: " + countPartTwo);
    }

    public static void partOneCheck(List<String> requirements, String passport) {
        String[] elements = passport.split(" ");
        boolean successful = true;
        for (String requirement : requirements) {
            if (!contains(elements, requirement)) {
                successful = false;
                break;
            }
        }
        countPartOne += successful ? 1 : 0;
    }

    public static void partTwoCheck(List<String> requirements, String passport) {
        String[] elements = passport.split(" ");
        boolean successful = true;
        for (String requirement : requirements) {
            if (!contains(elements, requirement)) {
                successful = false;
                break;
            }
        }
        for (String element : elements) {
            if (!matches(element, element.substring(0, 3))) {
                successful = false;
                break;
            }
        }
        countPartTwo += successful ? 1 : 0;
    }

    public static boolean contains(String[] array, String desired) {
        for (String str : array) {
            if (str.substring(0, 3).equals(desired)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matches(String mapping, String desired) {
        String value = mapping.substring(4);
        Set<Character> digits = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        switch (desired) {
            case "byr":
                int birth = Integer.parseInt(value);
                return birth >= 1920 && birth <= 2002;
            case "iyr":
                int issue = Integer.parseInt(value);
                return issue >= 2010 && issue <= 2020;
            case "eyr":
                int expiration = Integer.parseInt(value);
                return expiration >= 2020 && expiration <= 2030;
            case "hgt":
                if (value.length() <= 2) {
                    return false;
                }
                int height = Integer.parseInt(value.substring(0, value.length() - 2));
                if (value.substring(value.length() - 2).equals("cm")) {
                    return height >= 150 && height <= 193;
                } else {
                    return height >= 59 && height <= 76;
                }
            case "hcl":
                Set<Character> valid = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));
                List<Character> chars = new ArrayList<>();
                for (char c : value.toCharArray()) {
                    chars.add(c);
                }
                if (value.length() != 7) {
                    return false;
                }
                if (chars.get(0) != '#') {
                    return false;
                }
                String str = value.substring(1);
                for (int i = 0; i < str.length(); i++) {
                    if (!valid.contains(str.charAt(i))) {
                        return false;
                    }
                }
                return true;
            case "ecl":
                return new HashSet<String>(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth")).contains(value);
            case "pid":
                for (char c : value.toCharArray()) {
                    if (!digits.contains(c)) {
                        return false;
                    }
                }
                return value.length() == 9;
            case "cid":
                return true;
            default:
                return false;
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}