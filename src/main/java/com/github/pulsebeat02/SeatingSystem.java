package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeatingSystem {

    private static List<char[]> reference;

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

    private static int partOne() {
        List<char[]> changed = deepCopy(reference);
        List<char[]> seats;
        do {
            seats = deepCopy(changed);
            changed = applyChangePartOne(changed);
        } while (detectChange(seats, changed));
        return getOccupiedCount(changed);
    }

    private static List<char[]> applyChangePartOne(List<char[]> list) {
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

    private static int getAdjacentOccupiedSeats(List<char[]> list, int i, int j) {
        int count = 0;
        for (Directional dir : Directional.values()) {
            int newX = i + dir.x;
            int newY = j + dir.y;
            if (checkInBounds(newX, newY)) {
                count += getCharacterIndex(list, newX, newY) == '#' ? 1 : 0;
            }
        }
        return count;
    }

    private static boolean checkInBounds(int i, int j) {
        return i >= 0 && i < reference.size() && j >= 0 && j < reference.get(0).length;
    }

    private static int partTwo() {
        List<char[]> changed = deepCopy(reference);
        List<char[]> seats;
        do {
            seats = deepCopy(changed);
            changed = applyChangePartTwo(changed);
        } while (detectChange(seats, changed));
        return getOccupiedCount(changed);
    }

    private static List<char[]> applyChangePartTwo(List<char[]> list) {
        List<char[]> newList = deepCopy(list);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                switch (list.get(i)[j]) {
                    case 'L':
                        if (getViewedSeats(list, i, j, 'L') != -1) {
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

    private static int getViewedSeats(List<char[]> list, int x, int y, char c) {
        boolean empty = c == 'L';
        int count = 0;
        outer:
        for (Directional dir : Directional.values()) {
            int i = x + dir.x;
            int j = y + dir.y;
            while (checkInBounds(i, j)) {
                char fill = getCharacterIndex(list, i, j);
                i += dir.x;
                j += dir.y;
                switch (fill) {
                    case '#':
                        if (empty) {
                            return -1;
                        } else {
                            count++;
                            continue outer;
                        }
                    case 'L':
                        continue outer;
                    case '.':
                        break;
                }
            }
        }
        return count;
    }

    private static int getOccupiedCount(List<char[]> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                count += getCharacterIndex(list, i, j) == '#' ? 1 : 0;
            }
        }
        return count;
    }

    private static List<char[]> deepCopy(List<char[]> other) {
        List<char[]> newList = new ArrayList<>();
        for (char[] arr : other) {
            char[] characterArray = new char[arr.length];
            System.arraycopy(arr, 0, characterArray, 0, arr.length);
            newList.add(characterArray);
        }
        return newList;
    }

    private static char getCharacterIndex(List<char[]> list, int i, int j) {
        return list.get(i)[j];
    }

    private static boolean detectChange(List<char[]> before, List<char[]> after) {
        for (int i = 0; i < before.size(); i++) {
            for (int j = 0; j < before.get(i).length; j++) {
                if (before.get(i)[j] != after.get(i)[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private enum Directional {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1), UP_RIGHT(-1, 1), UP_LEFT(-1, -1), DOWN_RIGHT(1, 1),
        DOWN_LEFT(1, -1);

        final int x;
        final int y;

        Directional(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
