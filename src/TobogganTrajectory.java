import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TobogganTrajectory {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("toboggantrajectory.txt"));
        List<String> input = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            input.add(line);
            line = br.readLine();
        }
        br.close();
        boolean[][] grid = new boolean[input.size()][];
        for (int r = 0; r < input.size(); r++) {
            grid[r] = new boolean[input.get(r).length()];
            for (int c = 0; c < input.get(r).length(); c++) {
                grid[r][c] = input.get(r).charAt(c) == '#';
            }
        }
        System.out.println("Part One: " + getTreeCount(grid, 3, 1));
        System.out.println("Part Two: " + getTreeCount(grid, 1, 1) * getTreeCount(grid, 3, 1) * getTreeCount(grid, 5, 1) * getTreeCount(grid, 7, 1) * getTreeCount(grid, 1, 2));
    }

    public static int getTreeCount(boolean[][] grid, int x, int y) {
        int trees = 0;
        for (int r = 0, c = 0; r < grid.length; r += y, c += x) {
            trees += grid[r][c % grid[r].length] ? 1 : 0;
        }
        return trees;
    }

}
