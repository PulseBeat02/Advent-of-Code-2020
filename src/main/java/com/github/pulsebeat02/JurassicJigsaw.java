package com.github.pulsebeat02;

import com.sun.deploy.net.MessageHeader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class JurassicJigsaw {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("jurassicjigsaw.txt"));
        List<Tile> tiles = new ArrayList<>();
        String line = br.readLine();
        List<char[]> tile = new ArrayList<>();
        int id = 0;
        while (true) {
            if (line == null) {
                tiles.add(new Tile(id, tile));
                tile.clear();
                break;
            }
            if (line.startsWith("Tile")) {
                id = Integer.parseInt(line.substring(5, line.length() - 1));
            } else {
                if (line.isEmpty()) {
                    tiles.add(new Tile(id, tile));
                    tile.clear();
                    id = 0;
                } else {
                    tile.add(line.toCharArray());
                }
            }
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(tiles));
        System.out.println("Part Two: " + partTwo(tiles));
    }

    public static long partOne(List<Tile> tiles) {
        long product = 1;
        for (int i = 0; i < tiles.size(); i++) {
            Tile first = tiles.get(i);
            int count = 0;
            for (int j = 0; j < tiles.size(); j++) {
                if (i != j) {
                    Tile second = tiles.get(j);
                    for (int iRow = 0; iRow < first.sides.length; iRow++) {
                        for (int jRow = 0; jRow < second.sides.length; jRow++) {
                            if (Arrays.equals(first.sides[iRow], second.sides[jRow])) {
                                count++;
                            }
                            if (Arrays.equals(first.sides[iRow], reverse(second.sides[jRow]))) {
                                count++;
                            }
                        }
                    }
                }
            }
            if (count == 2) {
                product *= first.id;
                first.corner = true;
            }
        }
        return product;
    }

    public static long partTwo(List<Tile> tiles) {
        int side = (int) Math.sqrt(tiles.size());
        Set<Tile> used = new HashSet<>();
        Tile first = null;
        for (Tile t : tiles) {
            if (t.corner) {
                first = t;
                break;
            }
        }
        assert first != null;
        Map<Tile, Set<Pair<char[], Tile>>> matches = getPossibleMatches(tiles);
        Queue<Tile> grid = new ArrayDeque<>();
        grid.add(first);
        while (grid.size() > 0) {
            for (Tile tile : matches.keySet()) {

            }
        }

    }

    // key -> which tile
    // value -> Set<Pair<char[], Tile>> -> side and which tile it's associated with
    private static Map<Tile, Set<Pair<char[], Tile>>> getPossibleMatches(List<Tile> tiles) {
        Map<Tile, Set<Pair<char[], Tile>>> matches = new HashMap<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile first = tiles.get(i);
                Tile second = tiles.get(j);
                for (char[] firstSide : tiles.get(i).sides) {
                    for (char[] secondSide : tiles.get(j).sides) {
                        if (Arrays.equals(firstSide, secondSide)) {
                            if (!matches.containsKey(first)) {
                                Set<Pair<char[], Tile>> child = new HashSet<>();
                                child.add(new Pair<>(secondSide, second));
                                matches.put(first, child);
                            } else {
                                matches.get(first).add(new Pair<>(secondSide, second));
                            }
                        }
                        if (Arrays.equals(firstSide, reverse(secondSide))) {
                            if (!matches.containsKey(first)) {
                                Set<Pair<char[], Tile>> child = new HashSet<>();
                                child.add(new Pair<>(reverse(secondSide), second));
                                matches.put(first, child);
                            } else {
                                matches.get(first).add(new Pair<>(reverse(secondSide), second));
                            }
                        }
                    }
                }
            }
        }
        return matches;
    }

    private static char[] reverse(char[] chars) {
        char[] copy = Arrays.copyOf(chars, chars.length);
        for (int i = chars.length - 1; i >= 0; i--) {
            copy[i] = chars[chars.length - i - 1];
        }
        return copy;
    }

    private char[][] getBorderless(char[][] grid) {
        char[][] borderless = new char[grid.length - 2][grid.length - 2];
        for (int i = 1; i < grid.length - 1; i++) {
            char[] change = new char[grid.length - 2];
            for (int j = 1; j < grid.length - 1; j++) {
                change[j] = grid[i][j];
            }
            borderless[i] = change;
        }
        return borderless;
    }

    private static class Pair<K, V> {
        private final K key;
        private final V value;
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class Tile {

        private final int id;
        private final char[][] sides;
        private final char[][] grid;
        private boolean corner;

        public Tile(int id, List<char[]> g) {
            char[][] grid = new char[g.size()][g.get(0).length];
            for (int i = 0; i < g.size(); i++) {
                g.set(i, g.get(i));
            }
            this.id = id;
            this.grid = grid;
            this.sides = new char[][]{getBorder(BorderType.TOP), getBorder(BorderType.BOTTOM), getBorder(BorderType.LEFT), getBorder(BorderType.RIGHT)};
            this.corner = false;
        }

        private char[] getBorder(BorderType type) {
            switch (type) {
                case TOP:
                    char[] top = new char[grid.length];
                    System.arraycopy(grid[0], 0, top, 0, grid.length);
                    return top;
                case BOTTOM:
                    char[] bottom = new char[grid.length];
                    System.arraycopy(grid[grid.length - 1], 0, bottom, 0, grid.length);
                    return bottom;
                case LEFT:
                    char[] left = new char[grid.length];
                    for (int i = 0; i < grid.length; i++) {
                        left[i] = grid[i][0];
                    }
                    return left;
                case RIGHT:
                    char[] right = new char[grid.length];
                    for (int i = 0; i < grid.length; i++) {
                        right[i] = grid[i][grid.length - 1];
                    }
                    return right;
            }
            return null;
        }

        private enum BorderType {
            TOP, BOTTOM, LEFT, RIGHT
        }

    }

}
