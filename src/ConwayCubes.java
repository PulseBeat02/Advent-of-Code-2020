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
    }

    public static int partOne(List<String> lines) {
        int length = lines.size() + 6 * 2;
        Cube[][][] grid = new Cube[length][length][length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    grid[i][j][k] = new Cube(new Coordinate(i, j, k), false);
                }
            }
        }
        int j = 6;
        for (String line : lines) {
            int k = 6;
            for (char c : line.toCharArray()) {
                int i = grid.length / 2 + 1;
                grid[i][j][k] = new Cube(new Coordinate(i, j, k), c == '#');
                k++;
            }
            j++;
        }
        for (int i = 0; i < 6; i++) {
            grid = performChange(grid);
        }
        int count = 0;
        for (Cube[][] x : grid) {
            for (Cube[] y : x) {
                for (Cube z : y) {
                    if (z.active) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static Cube[][][] performChange(Cube[][][] grid) {
        Cube[][][] updated = new Cube[grid.length][grid[0].length][grid[0][0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) {
                    Cube target = grid[i][j][k];
                    int count = getNeigborCountActive(grid, target);
                    if (target.active && (count < 2 || count > 3)) {
                        updated[i][j][k] = new Cube(new Coordinate(i, j, k), false);
                        continue;
                    } else if (!target.active && count == 3) {
                        updated[i][j][k] = new Cube(new Coordinate(i, j, k), true);
                        continue;
                    }
                    updated[i][j][k] = grid[i][j][k];
                }
            }
        }
        return updated;
    }

    public static int getNeigborCountActive(Cube[][][] grid, Cube target) {
        int count = 0;
        Coordinate coords = target.coordinate;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (!(i == 0 && j == 0 && k == 0)) {
                        int x = coords.x + i;
                        int y = coords.y + j;
                        int z = coords.z + k;
                        if (inBounds(x, 0, grid.length - 1) && inBounds(y, 0, grid.length - 1)
                                && inBounds(z, 0, grid.length - 1)) {
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

    public static boolean inBounds(int query, int min, int max) {
        return query >= min && query <= max;
    }

    public static class Cube {
        public final Coordinate coordinate;
        public boolean active;
        public Cube(Coordinate coordinate, boolean active) {
            this.coordinate = coordinate;
            this.active = active;
        }
    }

    public static class Coordinate {
        public final int x;
        public final int y;
        public final int z;
        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}
