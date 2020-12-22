import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                id = Integer.parseInt(line.substring(5, line.length() - 2));
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

    private static long partOne(List<Tile> tiles) {
        Map<Tile, List<char[]>> corner = new HashMap<>();
        for (Tile tile : tiles) {
            List<char[]> commonBorders = new ArrayList<>();
            List<char[]> borders = tile.getAllBorders();
            for (Tile other : tiles) {
                if (other != tile) {
                    List<char[]> otherPaneBorders = other.getAllBordersWithFlips();
                    commonBorders.addAll(getCommonBorders(borders, otherPaneBorders));
                }
            }
            if (commonBorders.size() == 2) {
                corner.put(tile, commonBorders);
            }
        }
        long product = 1;
        for (Tile t : corner.keySet()) {
            product *= t.id;
        }
        return product;
    }

    private static List<char[]> getCommonBorders(List<char[]> commonBorders, List<char[]> borders) {
        List<char[]> list = new ArrayList<>();
        for (char[] i : commonBorders) {
            for (char[] j : borders) {
                if (Arrays.equals(i, j)) {
                    list.add(i);
                }
            }
        }
        return list;
    }

    private static class Tile {

        private final List<char[]> grid;
        private final int id;
        public Tile(int id, List<char[]> grid) {
            this.id = id;
            this.grid = grid;
        }

        private List<char[]> getAllBorders() {
            return new LinkedList<>(Arrays.asList(getBorder(BorderDirection.TOP), getBorder(BorderDirection.LEFT), getBorder(BorderDirection.RIGHT), getBorder(BorderDirection.BOTTOM)));
        }

        public List<char[]> getAllBordersWithFlips() {
            List<char[]> borders = getAllBorders();
            for (int i = 0; i < 3; i++) {
                char[] border = borders.get(i);
                char[] chars = Arrays.copyOf(border, grid.size());
                for (int j = 0; j < chars.length / 2; j++) {
                    char temp = border[j];
                    chars[j] = border[border.length - j - 1];
                    chars[border.length - j - 1] = temp;
                }
                borders.add(chars);
            }
            return borders;
        }

        private char[] getBorder(BorderDirection direction) {
            switch (direction) {
                case TOP:
                    char[] top = new char[grid.size()];
                    for (int i = 0; i < grid.size(); i++) {
                        top[i] = grid.get(0)[i];
                    }
                    return top;
                case BOTTOM:
                    char[] bottom = new char[grid.size()];
                    for (int i = 0; i < grid.size(); i++) {
                        bottom[i] = grid.get(grid.size() - 1)[i];
                    }
                    return bottom;
                case LEFT:
                    char[] left = new char[grid.size()];
                    for (int i = 0; i < grid.size(); i++) {
                        left[i] = grid.get(i)[0];
                    }
                    return left;
                case RIGHT:
                    char[] right = new char[grid.size()];
                    for (int i = 0; i < grid.size(); i++) {
                        right[i] = grid.get(grid.size() - 1)[grid.size() - 1];
                    }
                    return right;
            }
            return null;
        }

        private enum BorderDirection {
            TOP, BOTTOM, LEFT, RIGHT
        }

    }

}
