import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperationOrder {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("operationorder.txt"));
        String line = br.readLine();
        int partOneSum = 0;
        while (line != null) {
            partOneSum += partOne(line);
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOneSum);
    }

    public static int partOne(String line) {
        int innerSum = getFirstNumber(line, 0, line.length());
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '(') {
                innerSum += recurseCalculate(line, i, findClosingParenthesis(line, i));
            } else if (inBounds(line, i -2) && (inBounds(line, i + 2))) {
                int secondNumber = Character.getNumericValue(line.charAt(i + 2));
                if (c == '+') {
                    innerSum += secondNumber;
                } else if (c == '*') {
                    innerSum *= secondNumber;
                }
            }
        }
        return innerSum;
    }

    public static boolean inBounds(String line, int index) {
        return index >= 0 && index < line.length();
    }

    public static int getFirstNumber(String str, int first, int second) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str.substring(first, second));
        matcher.find();
        return Integer.valueOf(matcher.group());
    }

    private static int recurseCalculate(String line, int first, int second) {
        int sum = getFirstNumber(line, 0, line.length());
        for (int i = first + 1; i < second; i++) {
            if (line.charAt(i) == '(') {
                int index = findClosingParenthesis(line, i);
                sum += recurseCalculate(line, i, index);
                i = index;
            } else {
                int secondNumber = Character.getNumericValue(line.charAt(getOperatorIndex(line, i) + 2));
                if (line.charAt(i) == '+') {
                    sum += secondNumber;
                } else if (line.charAt(i) == '*') {
                    sum += secondNumber;
                }
            }
        }
        return sum;
    }

    private static int getOperatorIndex(String line, int start) {
        for (int i = 1; i < 5; i++) {
            if (line.charAt(i) == '+' || line.charAt(i) == '*') {
                return i;
            }
        }
        return -1;
    }

    private static int findClosingParenthesis(String text, int first) {
        int second = first;
        int counter = 1;
        while (counter > 0) {
            char c = text.charAt(++second);
            if (c == '(') {
                counter++;
            } else if (c == ')') {
                counter--;
            }
        }
        return second;
    }


}
