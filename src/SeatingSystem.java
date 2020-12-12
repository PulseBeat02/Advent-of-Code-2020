import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeatingSystem {

    public static List<char[]> reference;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("seatingsystem.txt"));
        reference = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            reference.add(line.toCharArray());
            line = br.readLine();
        }
        br.close();
        System.out.println("Day One: " + partOne());
        System.out.println("Part Two: " + partTwo());
    }

    public static int partOne() {
        List<char[]> changed = deepCopy(reference);
        List<char[]> seats = deepCopy(changed);
        do {
            seats = deepCopy(changed);
            changed = applyChangePartOne(changed);
        } while (detectChange(seats, changed));
        return getOccupiedCount(changed);
    }

    public static List<char[]> applyChangePartOne(List<char[]> list) {
        List<char[]> newList = deepCopy(list);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                switch (list.get(i)[j]) {
                    case 'L':
                        if (getAdjacentOccupiedSeats(list, i, j) == 0) {
                            newList.get(i)[j] = '#';
                        }
                        break;
                    case '#':
                        if (getAdjacentOccupiedSeats(list, i, j) >= 4) {
                            newList.get(i)[j] = 'L';
                        }
                        break;
                }
            }
        }
        return newList;
    }

    public static int getAdjacentOccupiedSeats(List<char[]> list, int i, int j) {
        int count = 0;
        if (checkInBounds(i - 1, j)) {
            count += isOccupied(list, i - 1, j, '#') ? 1 : 0;
        }
        if (checkInBounds(i + 1, j)) {
            count += isOccupied(list, i + 1, j, '#') ? 1 : 0;
        }
        if (checkInBounds(i, j - 1)) {
            count += isOccupied(list, i, j - 1, '#') ? 1 : 0;
        }
        if (checkInBounds(i, j + 1)) {
            count += isOccupied(list, i, j + 1, '#') ? 1 : 0;
        }
        if (checkInBounds(i - 1, j - 1)) {
            count += isOccupied(list, i - 1, j - 1, '#') ? 1 : 0;
        }
        if (checkInBounds(i - 1, j + 1)) {
            count += isOccupied(list, i - 1, j + 1, '#') ? 1 : 0;
        }
        if (checkInBounds(i + 1, j - 1)) {
            count += isOccupied(list, i + 1, j - 1, '#') ? 1 : 0;
        }
        if (checkInBounds(i + 1, j + 1)) {
            count += isOccupied(list, i + 1, j + 1, '#') ? 1 : 0;
        }
        return count;
    }

    public static boolean checkInBounds(int i, int j) {
        return i >= 0 && i < reference.size() && j >= 0 && j < reference.get(0).length;
    }

    //////////////////////////////////
    //                              //
    //           Part Two           //
    //                              //
    //////////////////////////////////

    public static int partTwo() {
        List<char[]> changed = deepCopy(reference);
        List<char[]> seats = deepCopy(changed);
        do {
            seats = deepCopy(changed);
            changed = applyChangePartTwo(changed);
            print(changed);
            System.out.println();
        } while (detectChange(seats, changed));
        print(changed);
        return getOccupiedCount(changed);
    }

    public static List<char[]> applyChangePartTwo(List<char[]> list) {
        List<char[]> newList = deepCopy(list);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                switch (list.get(i)[j]) {
                    case 'L':
                        if (getViewedSeats(list, i, j, '#') == 0) {
                            newList.get(i)[j] = '#';
                        }
                        break;
                    case '#':
                        if (getViewedSeats(list, i, j, '#') >= 5) {
                            newList.get(i)[j] = 'L';
                        }
                        break;
                }
            }
        }
        return newList;
    }

    public static int getViewedSeats(List<char[]> list, int x, int y, char c) {

        int count = 0;

        for (int i = x; i >= 0; i--) {
            if (i != x && isOccupied(list, i, y, c)) { // Vertical (Up)
                count++;
                break;
            }
        }

        for (int i = x; i < list.size(); i++) { // Vertical (Down)
            if (i != x && isOccupied(list, i, y, c)) {
                count++;
                break;
            }
        }

        for (int i = y; i >= 0; i--) {
            if (i != y && isOccupied(list, x, i, c)) { // Horizontal (Left)
                count++;
                break;
            }
        }

        for (int i = y; i < list.get(x).length; i++) { // Horizontal (Right)
            if (i != y && isOccupied(list, x, i, c)) {
                count++;
                break;
            }
        }

        int i = x;
        int j = y;
        while (checkInBounds(i + 1, j + 1)) { // Diagonal (Bottom Right)
            i++;
            j++;
            if (isOccupied(list, i, j, c)) {
                count++;
                break;
            }
        }

        i = x;
        j = y;
        while (checkInBounds(i - 1, j - 1)) { // Diagonal (Top Left)
            i--;
            j--;
            if (isOccupied(list, i, j, c)) {
                count++;
                break;
            }
        }

        i = x;
        j = y;
        while (checkInBounds(i - 1, j + 1)) { // Diagonal (Top Right)
            i--;
            j++;
            if (isOccupied(list, i, j, c)) {
                count++;
                break;
            }
        }

        i = x;
        j = y;
        while (checkInBounds(i + 1, j - 1)) { // Diagonal (Bottom Right)
            i++;
            j--;
            if (isOccupied(list, i, j, c)) {
                count++;
                break;
            }
        }

        return count;

    }

    public static void print(List<char[]> list) {
        list.forEach(arr -> System.out.println(Arrays.toString(arr).replaceAll(",", "")));
    }

    public static int getOccupiedCount(List<char[]> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                count += isOccupied(list, i, j, '#') ? 1 : 0;
            }
        }
        return count;
    }

    public static List<char[]> deepCopy(List<char[]> other) {
        List<char[]> newList = new ArrayList<>();
        for (char[] arr : other) {
            newList.add(Arrays.copyOf(arr, arr.length));
        }
        return newList;
    }

    public static boolean isOccupied(List<char[]> list, int i, int j, char c) {
        return list.get(i)[j] == c;
    }

    public static boolean detectChange(List<char[]> before, List<char[]> after) {
        for (int i = 0; i < before.size(); i++) {
            if (!Arrays.equals(before.get(i), after.get(i))) {
                return true;
            }
        }
        return false;
    }

}
