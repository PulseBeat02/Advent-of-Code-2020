package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
                                first.commonborders.add(second.sides[jRow]);
                                second.commonborders.add(first.sides[jRow]);
                                count++;
                            }
                            if (Arrays.equals(first.sides[iRow], reverse(second.sides[jRow]))) {
                                first.commonborders.add(reverse(second.sides[jRow]));
                                second.commonborders.add(first.sides[jRow]);
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
        Tile[][] grid = new Tile[side][side];
        Set<Tile> used = new HashSet<>();
        Tile first = null;
        for (Tile t : tiles) {
            if (t.corner) {
                first = t;
                break;
            }
        }
        assert first != null;
        alignToSide(first, first.commonborders.get(0), Tile.BorderType.RIGHT)
                .flatMap(t -> alignToSide(t, t.commonborders.get(1), Tile.BorderType.BOTTOM))
                .ifPresent(t -> grid[0][0] = t);

        for (int i = 1; i < side; i++) {
            for (Tile t : tiles) {
                if (!used.contains(t)) {
                    int j = i;
                    alignToSide(t, grid[0][i - 1].getBorder(Tile.BorderType.RIGHT), Tile.BorderType.LEFT)
                            .ifPresent(align -> {
                                grid[0][j] = align;
                                used.add(align);
                            });
                }
            }
        }

        for (int i = 1; i < side; i++) {
            for (int j = 0; j < side; j++) {
                for (Tile t : tiles) {
                    if (!used.contains(t)) {
                        Optional<Tile> result = alignToSide(t, grid[i - 1][j].getBorder(Tile.BorderType.BOTTOM), Tile.BorderType.TOP);
                        if (result.isPresent()) {
                            grid[i][j] = result.get();
                            used.add(result.get());
                            break;
                        }
                    }
                }
            }
        }
        List<String> monster = Arrays.asList("                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   ");
        char[][] monsterArray = new char[monster.size()][monster.get(0).length()];
        for (int i = 0; i < monster.size(); i++) {
            monsterArray[i] = monster.get(i).toCharArray();
        }
        return waterRoughness(createBigMapPane(grid), monsterArray);
    }

    private static Tile createBigMapPane(Tile[][] panesMap) {
        int n = panesMap.length;
        List<char[]> axis = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char[][] area = panesMap[i][j].grid;
                for (int x = 1; x < area.length - 1; x++) {
                    for (int y = 1; y < area.length - 1; y++) {
                        axis.get(i * (area.length - 2) + (x - 1))[j * (area.length - 2) + (y - 1)] = area[x][y];
                    }
                }
            }
        }
        return new Tile(0, axis);
    }

    private static int waterRoughness(Tile pane, char[][] monster) {
        int monsterHash = 0;
        for (char[] chars : monster) {
            for (char aChar : chars) {
                if (aChar == '#') {
                    monsterHash++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            pane.grid = rotate(pane.grid);
            int nrOfMonsters = countMonsters(pane, monster);
            if (nrOfMonsters != 0) {
                return pane.countHashes() - nrOfMonsters * monsterHash;
            }
        }

        pane.grid = flip(pane.grid);
        for (int i = 0; i < 4; i++) {
            pane.grid = rotate(pane.grid);
            int nrOfMonsters = countMonsters(pane, monster);
            if (nrOfMonsters != 0) {
                return pane.countHashes() - nrOfMonsters * monsterHash;
            }
        }
        return 0;
    }

    private static int countMonsters(Tile pane, char[][] monster) {
        int monsterCounter = 0;
        char[][] grid = pane.grid;
        for (int i = 0; i < grid.length - monster.length; i++) {
            for (int j = 0; j < grid[0].length - monster[0].length; j++) {
                boolean allMatch = true;
                label:
                for (int x = 0; x < monster.length; x++) {
                    for (int y = 0; y < monster[0].length; y++) {
                        if (monster[x][y] == '#') {
                            if (grid[i + x][j + y] != '#') {
                                allMatch = false;
                                break label;
                            }
                        }
                    }
                }
                if (allMatch) {
                    monsterCounter++;
                }
            }
        }
        return monsterCounter;
    }

    private static Optional<Tile> alignToSide(Tile pane, char[] border, Tile.BorderType type) {
        for (int i = 0; i < 4; i++) {
            pane.grid = rotate(pane.grid);
            if (Arrays.equals(border, pane.getBorder(type))) {
                return Optional.of(pane);
            }
        }
        pane.grid = flip(pane.grid);
        for (int i = 0; i < 4; i++) {
            pane.grid = rotate(pane.grid);
            if (Arrays.equals(border, pane.getBorder(type))) {
                return Optional.of(pane);
            }
        }
        return Optional.empty();
    }

    private static char[] reverse(char[] chars) {
        char[] copy = Arrays.copyOf(chars, chars.length);
        for (int i = chars.length - 1; i >= 0; i--) {
            copy[i] = chars[chars.length - i - 1];
        }
        return copy;
    }

    private static char[][] rotate(char[][] grid) {
        final int M = grid.length;
        final int N = grid[0].length;
        char[][] rot = new char[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                rot[j][M - 1 - i] = grid[i][j];
            }
        }
        return rot;
    }

    private static char[][] flip(char[][] grid) {
        char[][] flip = new char[grid.length][grid.length];
        for (int i = 0; i < grid.length / 2; i++) {
            flip[i] = grid[grid.length - i - 1];
        }
        return flip;
    }

    private static class Tile {
        private final int id;
        private final char[][] sides;
        private final List<char[]> commonborders;
        private char[][] grid;
        private boolean corner;

        public Tile(int id, List<char[]> g) {
            char[][] grid = new char[g.size()][g.get(0).length];
            for (int i = 0; i < g.size(); i++) {
                g.set(i, g.get(i));
            }
            this.id = id;
            this.grid = grid;
            this.sides = new char[][]{getBorder(BorderType.TOP), getBorder(BorderType.BOTTOM), getBorder(BorderType.LEFT), getBorder(BorderType.RIGHT)};
            this.commonborders = new ArrayList<>();
            this.corner = false;
        }

        private int countHashes() {
            int count = 0;
            for (char[] chars : grid) {
                for (char aChar : chars) {
                    if (aChar == '#') {
                        count++;
                    }
                }
            }
            return count;
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
