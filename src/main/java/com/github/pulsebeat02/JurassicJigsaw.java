package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    }

    public static long partOne(List<Tile> tiles) {
        long prod = 1;
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
                    prod *= (count == 2 ? first.id : 1);
                }
            }
        }
        return prod;
    }

    private static char[] reverse(char[] chars) {
        char[] copy = Arrays.copyOf(chars, chars.length);
        for (int i = chars.length - 1; i >= 0; i--) {
            copy[i] = chars[chars.length - i - 1];
        }
        return copy;
    }

    private static class Tile {
        private char[][] grid;
        private final char[][] sides;
        private final int id;
        public Tile(int id, List<char[]> grid) {
            char[][] g = new char[grid.size()][grid.get(0).length];
            for (int i = 0; i < grid.size(); i++) {
                g[i] = grid.get(i);
            }
            this.grid = g;
            this.id = id;
            this.sides = getBorders();
        }
        private char[][] getBorders() {
            char[] top = new char[grid.length];
            for (int i = 0; i < grid.length; i++) {
                top[i] = grid[0][i];
            }
            char[] bottom = new char[grid.length];
            for (int i = 0; i < grid.length; i++) {
                bottom[i] = grid[grid.length - 1][i];
            }
            char[] left = new char[grid.length];
            for (int i = 0; i < grid.length; i++) {
                left[i] = grid[i][0];
            }
            char[] right = new char[grid.length];
            for (int i = 0; i < grid.length; i++) {
                right[i] = grid[i][grid.length - 1];
            }
            return new char[][] { top, bottom, left, right };
        }
    }

}
