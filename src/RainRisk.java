import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RainRisk {

    public static int x = 0;
    public static int y = 0;
    public static int angle = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("rainrisk.txt"));
        List<Pair<Character, Integer>> translations = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            char dir = line.substring(0, 1).charAt(0);
            int value = Integer.parseInt(line.substring(1));
            translations.add(new Pair(dir, value));
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(translations));
        x = 0;
        y = 0;
        angle = 0;
        System.out.println("Part Two: " + partTwo(translations));
    }

    public static int partOne(List<Pair<Character, Integer>> translations) {
        for (Pair<Character, Integer> translation : translations) {
            int value = translation.value;
            switch (translation.key) {
                case 'N':
                    y += value;
                    break;
                case 'S':
                    y -= value;
                    break;
                case 'E':
                    x -= value;
                    break;
                case 'W':
                    x += value;
                    break;
                case 'L':
                    angle += value;
                    break;
                case 'R':
                    angle -= value;
                    break;
                case 'F':
                    getDirection(value);
                    break;
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static int partTwo(List<Pair<Character, Integer>> translations) {
        int wayX = 10;
        int wayY = 1;
        for (Pair<Character, Integer> translation : translations) {
            int value = translation.value;
            switch (translation.key) {
                case 'N':
                    wayY += value;
                    break;
                case 'S':
                    wayY -= value;
                    break;
                case 'E':
                    wayX += value;
                    break;
                case 'W':
                    wayX -= value;
                    break;
                case 'L':
                    int angL = value;
                    while (angL >= 90) {
                        int[] point = rotatePoint(wayX, wayY, false);
                        wayX = point[0];
                        wayY = point[1];
                        angL -= 90;
                    }
                    break;
                case 'R': {
                    int angR = value;
                    while (angR >= 90) {
                        int[] point = rotatePoint(wayX, wayY, true);
                        wayX = point[0];
                        wayY = point[1];
                        angR -= 90;
                    }
                    break;
                }
                case 'F': {
                    x = x + wayX * value;
                    y = y + wayY * value;
                    break;
                }
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static int[] rotatePoint(int x, int y, boolean clockwise) {
        return clockwise ? new int[]{y, -x} : new int[]{-y, x};
    }

    public static void getDirection(int value) {
        switch (Math.abs(angle) % 360) {
            case 0:
                x -= value;
                break;
            case 90:
                y -= value;
                break;
            case 180:
                x += value;
                break;
            case 270:
                y += value;
                break;
        }
    }

    public static class Pair<T, V> {
        private final T key;
        private final V value;

        public Pair(T key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
