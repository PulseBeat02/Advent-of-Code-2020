package com.github.pulsebeat02;

import com.sun.corba.se.impl.orbutil.HexOutputStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LobbyLayout {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("lobbylayout.txt"));
        List<List<Instruction>> instructions = new ArrayList<>();
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
            line = br.readLine();
        }
        System.out.println("Part One: " + partOne(instructions));
    }

    private static int partTwo(List<List<Instruction>> instructions) {
        Map<HexagonalTile, Boolean> tiles = getTileStateMap(instructions);
        int steps = 0;
        while (steps < 100) {
            List<HexagonalTile> blackTiles = new ArrayList<>();
            for (HexagonalTile tile : tiles.keySet()) {
                if (!tile.toggled) {
                    blackTiles.add(tile);
                }
            }
            Map<HexagonalTile, Boolean> copy = new HashMap<>();
            Set<HexagonalTile> dupe = new HashSet<>();
            for (HexagonalTile blackTile : blackTiles) {
                copy.put(blackTile, true);

            }
            steps++;
        }
    }

    private static Set<HexagonalTile> getNeigborBlackTiles(Map<String, HexagonalTile> map, HexagonalTile target) {
        Set<String> coords = new HashSet<>();
        coords.add((target.x + 1) + " " + (target.y - 1) + " " + (target.z));
        coords.add((target.x) + " " + (target.y - 1) + " " + (target.z + 1));
        coords.add((target.x - 1) + " " + (target.y) + " " + (target.z + 1));
        coords.add((target.x - 1) + " " + (target.y + 1) + " " + (target.z));
        coords.add((target.x) + " " + (target.y + 1) + " " + (target.z - 1));
        coords.add((target.x + 1) + " " + (target.y) + " " + (target.z - 1));
        Set<HexagonalTile> neigbors = new HashSet<>();
        for (String key : coords) {
            if (map.containsKey(key)) {
                neigbors.add(map.get(key));
            } else {
                String[] keys = key.split(" ");
                neigbors.add(new HexagonalTile(Integer.parseInt(keys[0]), Integer.parseInt(keys[1]), Integer.parseInt(keys[2])));
            }
        }
        return neigbors;
    }

    private static int partOne(List<List<Instruction>> instructions) {
        // See: https://www.redblobgames.com/grids/hexagons/#coordinates
        // Cube Coordinates Implementation
        Map<String, HexagonalTile> tiles = getFlippedTiles(instructions);
        int count = 0;
        for (HexagonalTile tile : tiles.values()) {
            count += !tile.toggled ? 1 : 0;
        }
        return count;
    }

    private static Map<HexagonalTile, Boolean> getTileStateMap(List<List<Instruction>> instructions) {
        Map<String, HexagonalTile> tiles = getFlippedTiles(instructions);
        Map<HexagonalTile, Boolean> state = new HashMap<>();
        for (HexagonalTile tile : tiles.values()) {
            state.put(tile, tile.toggled);
        }
        return state;
    }

    private static Map<String, HexagonalTile> getFlippedTiles(List<List<Instruction>> instructions) {
        Map<String, HexagonalTile> tiles = new HashMap<>();
        tiles.put("0 0 0", new HexagonalTile(0, 0, 0));
        for (List<Instruction> instructionsList : instructions) {
            int currentX = 0;
            int currentY = 0;
            int currentZ = 0;
            String key = "";
            for (int i = 0; i < instructionsList.size(); i++) {
                switch (instructionsList.get(i)) {
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
                    HexagonalTile tile = new HexagonalTile(currentX, currentY, currentZ);
                    tiles.put(key, tile);
                }
            }
            tiles.get(key).toggled ^= true;
        }
        return tiles;
    }

    private static class HexagonalTile {
        private boolean toggled;
        private final int x;
        private final int y;
        private final int z;
        public HexagonalTile(int x, int y, int z) {
            this.toggled = true;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private enum Instruction {
        E, SE, SW, W, NW, NE
    }

}
