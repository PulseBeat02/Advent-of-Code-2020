package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConwayCubes {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("conwaycubes.txt"));
        List<String> input = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            input.add(line);
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(input));
        System.out.println("Part Two: " + partTwo(input));
    }

    private static int partOne(List<String> lines) {
        int length = lines.size() + 6 * 2;
        ThreeDimensionalCube[][][] grid = new ThreeDimensionalCube[length][length][length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                for (int z = 0; z < grid[x][y].length; z++) {
                    grid[x][y][z] = new ThreeDimensionalCube(new ThreeDimensionalCoordinate(x, y, z), false);
                }
            }
        }
        for (int index = 0, i = grid.length / 2 + 1, j = 6; index < lines.size(); index++, j++) {
            String str = lines.get(index);
            for (int character = 0, k = 6; character < str.length(); character++, k++) {
                grid[i][j][k] = new ThreeDimensionalCube(new ThreeDimensionalCoordinate(i, j, k), str.charAt(character) == '#');
            }
        }
        for (int step = 0; step < 6; step++) {
            grid = performConway3D(grid);
        }
        int count = 0;
        for (ThreeDimensionalCube[][] x : grid) {
            for (ThreeDimensionalCube[] y : x) {
                for (ThreeDimensionalCube z : y) {
                    if (z.active) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static ThreeDimensionalCube[][][] performConway3D(ThreeDimensionalCube[][][] grid) {
        ThreeDimensionalCube[][][] updated = new ThreeDimensionalCube[grid.length][grid[0].length][grid[0][0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    ThreeDimensionalCube target = grid[i][j][k];
                    int count = getNeigborCountActive3D(grid, target);
                    if (target.active && (count < 2 || count > 3)) {
                        updated[i][j][k] = new ThreeDimensionalCube(new ThreeDimensionalCoordinate(i, j, k), false);
                        continue;
                    } else if (!target.active && count == 3) {
                        updated[i][j][k] = new ThreeDimensionalCube(new ThreeDimensionalCoordinate(i, j, k), true);
                        continue;
                    }
                    updated[i][j][k] = grid[i][j][k];
                }
            }
        }
        return updated;
    }

    private static int getNeigborCountActive3D(ThreeDimensionalCube[][][] grid, ThreeDimensionalCube target) {
        int count = 0;
        ThreeDimensionalCoordinate coords = target.coordinate;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (!(i == 0 && j == 0 && k == 0)) {
                        int x = coords.x + i;
                        int y = coords.y + j;
                        int z = coords.z + k;
                        if (inBounds(x, grid.length - 1) && inBounds(y, grid.length - 1)
                                && inBounds(z, grid.length - 1)) {
                            if (grid[x][y][z].active) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private static boolean inBounds(int query, int max) {
        return query >= 0 && query <= max;
    }

    private static int partTwo(List<String> lines) {
        int length = lines.size() + 6 * 2;
        FourDimensionalCube[][][][] grid = new FourDimensionalCube[length][length][length][length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                for (int z = 0; z < grid[x][y].length; z++) {
                    for (int w = 0; w < grid[x][y][z].length; w++) {
                        grid[x][y][z][w] = new FourDimensionalCube(new FourDimensionalCoordinate(x, y, z, w), false);
                    }
                }
            }
        }
        for (int index = 0, i = grid.length / 2 + 1, j = 6; index < lines.size(); index++, j++) {
            String str = lines.get(index);
            for (int character = 0, k = 6; character < str.length(); character++, k++) {
                grid[i][i][j][k] = new FourDimensionalCube(new FourDimensionalCoordinate(i, i, j, k), str.charAt(character) == '#');
            }
        }
        for (int step = 0; step < 6; step++) {
            grid = performConway4D(grid);
        }
        int count = 0;
        for (FourDimensionalCube[][][] x : grid) {
            for (FourDimensionalCube[][] y : x) {
                for (FourDimensionalCube[] z : y) {
                    for (FourDimensionalCube w : z) {
                        if (w.active) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private static FourDimensionalCube[][][][] performConway4D(FourDimensionalCube[][][][] grid) {
        FourDimensionalCube[][][][] updated = new FourDimensionalCube[grid.length][grid[0].length][grid[0][0].length][grid[0][0][0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    for (int l = 0; l < grid[i][j][k].length; l++) {
                        FourDimensionalCube target = grid[i][j][k][l];
                        int count = getNeigborCountActive4D(grid, target);
                        if (target.active && (count < 2 || count > 3)) {
                            updated[i][j][k][l] = new FourDimensionalCube(new FourDimensionalCoordinate(i, j, k, l), false);
                            continue;
                        } else if (!target.active && count == 3) {
                            updated[i][j][k][l] = new FourDimensionalCube(new FourDimensionalCoordinate(i, j, k, l), true);
                            continue;
                        }
                        updated[i][j][k][l] = grid[i][j][k][l];
                    }
                }
            }
        }
        return updated;
    }

    ///////////////////////////
    //                       //
    //        Part Two       //
    //                       //
    ///////////////////////////

    private static int getNeigborCountActive4D(FourDimensionalCube[][][][] grid, FourDimensionalCube target) {
        int count = 0;
        FourDimensionalCoordinate coords = target.coordinate;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (!(i == 0 && j == 0 && k == 0 && l == 0)) {
                            int x = coords.x + i;
                            int y = coords.y + j;
                            int z = coords.z + k;
                            int w = coords.w + l;
                            if (inBounds(x, grid.length - 1) && inBounds(y, grid.length - 1)
                                    && inBounds(z, grid.length - 1) && inBounds(w, grid.length - 1)) {
                                if (grid[x][y][z][w].active) {
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private static class ThreeDimensionalCube {
        public final ThreeDimensionalCoordinate coordinate;
        public boolean active;

        public ThreeDimensionalCube(ThreeDimensionalCoordinate coordinate, boolean active) {
            this.coordinate = coordinate;
            this.active = active;
        }
    }

    private static class ThreeDimensionalCoordinate {
        public final int x;
        public final int y;
        public final int z;

        public ThreeDimensionalCoordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class FourDimensionalCube {
        private final FourDimensionalCoordinate coordinate;
        private final boolean active;

        public FourDimensionalCube(FourDimensionalCoordinate coordinate, boolean active) {
            this.coordinate = coordinate;
            this.active = active;
        }
    }

    private static class FourDimensionalCoordinate {
        private final int x;
        private final int y;
        private final int z;
        private final int w;

        public FourDimensionalCoordinate(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }

}
