package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyLayout {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("lobbylayout.txt"));
        List<List<Instruction>> instructions = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            List<Instruction> inner = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == 's') {
                    if (line.charAt(i + 1) == 'e') {
                        inner.add(Instruction.SE);
                        i++;
                    } else if (line.charAt(i + 1) == 'w') {
                        inner.add(Instruction.SW);
                        i++;
                    }
                } else if (line.charAt(i) == 'n') {
                    if (line.charAt(i + 1) == 'e') {
                        inner.add(Instruction.NE);
                        i++;
                    } else if (line.charAt(i + 1) == 'w') {
                        inner.add(Instruction.NW);
                        i++;
                    }
                } else if (line.charAt(i) == 'e') {
                    inner.add(Instruction.E);
                } else if (line.charAt(i) == 'w') {
                    inner.add(Instruction.W);
                }
            }
            instructions.add(inner);
            lines.add(line);
            line = br.readLine();
        }
        System.out.println("Part One: " + partOne(instructions));
        System.out.println("Part Two: " + partTwo(lines));
    }

    // Angry Coding BE WARNED
    public static int partTwo(List<String> input) {
        boolean[][] floor = new boolean[500][500];
        int xMid = floor.length / 2;
        int yMid = floor[0].length / 2;
        for (String in : input) {
            String coords = getCoordinates(in);
            int x = Integer.parseInt(coords.split(",")[0]);
            int y = Integer.parseInt(coords.split(",")[1]);
            floor[yMid + y][xMid + x] = !floor[yMid + y][xMid + x];
        }
        for (int i = 0; i < 100; i++) {
            floor = updateFloor(floor);
        }
        int count = 0;
        for (boolean[] booleans : floor) {
            for (int c = 0; c < floor[0].length; c++) {
                if (booleans[c]) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean[][] updateFloor(boolean[][] floor) {
        boolean[][] copy = new boolean[floor.length][floor[0].length];
        for (int r = 0; r < copy.length; r++) {
            System.arraycopy(floor[r], 0, copy[r], 0, copy[0].length);
        }
        for (int r = 1; r <= floor.length - 2; r += 1) {
            for (int c = (r % 2 == 0) ? 2 : 3; c <= floor[0].length - 3; c += 2) {
                int count = 0;
                if (floor[r - 1][c - 1]) {
                    count++;
                }
                if (floor[r - 1][c + 1]) {
                    count++;
                }
                if (floor[r][c - 2]) {
                    count++;
                }
                if (floor[r][c + 2]) {
                    count++;
                }
                if (floor[r + 1][c - 1]) {
                    count++;
                }
                if (floor[r + 1][c + 1]) {
                    count++;
                }
                if (floor[r][c] && (count == 0 || count > 2)) {
                    copy[r][c] = false;
                }
                if (!floor[r][c] && count == 2) {
                    copy[r][c] = true;
                }
            }
        }
        return copy;
    }

    private static String getCoordinates(String in) {
        String dir;
        int i = 0;
        int x = 0;
        int y = 0;
        while (i < in.length()) {
            if (in.substring(i, i + 1).matches("[ew]")) {
                dir = (in.substring(i, i + 1));
                i++;
            } else {
                dir = (in.substring(i, i + 2));
                i += 2;
            }
            switch (dir) {
                case "e":
                    x += 2;
                    break;
                case "w":
                    x -= 2;
                    break;
                case "ne":
                    y -= 1;
                    x += 1;
                    break;
                case "nw":
                    y -= 1;
                    x -= 1;
                    break;
                case "se":
                    y += 1;
                    x += 1;
                    break;
                case "sw":
                    y += 1;
                    x -= 1;
                    break;
            }

        }
        return x + "," + y;
    }

    private static int partOne(List<List<Instruction>> instructions) {
        // See: https://www.redblobgames.com/grids/hexagons/#coordinates
        // Cube Coordinates Implementation
        Map<String, Boolean> tiles = getFlippedTiles(instructions);
        int count = 0;
        for (boolean tile : tiles.values()) {
            count += !tile ? 1 : 0;
        }
        return count;
    }

    private static Map<String, Boolean> getFlippedTiles(List<List<Instruction>> instructions) {
        Map<String, Boolean> tiles = new HashMap<>();
        tiles.put("0 0 0", true);
        for (List<Instruction> instructionsList : instructions) {
            int currentX = 0;
            int currentY = 0;
            int currentZ = 0;
            String key = "";
            for (Instruction instruction : instructionsList) {
                switch (instruction) {
                    case E:
                        currentX++;
                        currentY--;
                        break;
                    case SE:
                        currentZ++;
                        currentY--;
                        break;
                    case SW:
                        currentX--;
                        currentZ++;
                        break;
                    case W:
                        currentX--;
                        currentY++;
                        break;
                    case NW:
                        currentZ--;
                        currentY++;
                        break;
                    case NE:
                        currentX++;
                        currentZ--;
                        break;
                }
                key = currentX + " " + currentY + " " + currentZ;
                if (!tiles.containsKey(key)) {
                    tiles.put(key, true);
                }
            }
            tiles.replace(key, !tiles.get(key));
        }
        return tiles;
    }

    private enum Instruction {
        E(), SE(), SW(), W(), NW(), NE()
    }

}