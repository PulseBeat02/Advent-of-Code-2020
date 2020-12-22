package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CrabCombat {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("crabcombat.txt"));
        br.readLine();
        List<Integer> first = new ArrayList<>();
        String line = br.readLine();
        while (!line.isEmpty()) {
            first.add(Integer.parseInt(line));
            line = br.readLine();
        }
        br.readLine();
        List<Integer> second = new ArrayList<>();
        line = br.readLine();
        while (line != null) {
            second.add(Integer.parseInt(line));
            line = br.readLine();
        }
        System.out.println("Part One: " + partOne(first, second));
        System.out.println("Part Two: " + partTwo(new LinkedList<>(first), new LinkedList<>(second)).sum);
    }

    private static int partOne(List<Integer> first, List<Integer> second) {
        Queue<Integer> playerOne = new ArrayDeque<>(first);
        Queue<Integer> playerTwo = new ArrayDeque<>(second);
        while (playerOne.size() > 0 && playerTwo.size() > 0) {
            int playerOneCard = playerOne.poll();
            int playerTwoCard = playerTwo.poll();
            if (playerOneCard > playerTwoCard) {
                playerOne.add(playerOneCard);
                playerOne.add(playerTwoCard);
            } else {
                playerTwo.add(playerTwoCard);
                playerTwo.add(playerOneCard);
            }
        }
        int sum = 0;
        if (playerOne.size() > playerTwo.size()) {
            int multipler = playerOne.size();
            while (playerOne.size() > 0) {
                sum += (playerOne.poll() * multipler);
                multipler--;
            }
        } else {
            int multipler = playerTwo.size();
            while (playerTwo.size() > 0) {
                sum += (playerTwo.poll() * multipler);
                multipler--;
            }
        }
        return sum;
    }

    private static Win partTwo(Queue<Integer> playerOne, Queue<Integer> playerTwo) {
        Set<Integer> previous = new HashSet<>();
        while (playerOne.size() > 0 && playerTwo.size() > 0) {
            if (!previous.add(playerOne.hashCode() * 31 + playerTwo.hashCode())) {
                playerTwo.clear();
                break;
            }
            int playerOneCard = playerOne.poll();
            int playerTwoCard = playerTwo.poll();
            if (playerOne.size() >= playerOneCard && playerTwo.size() >= playerTwoCard) {
                if (partTwo(limitQueue(playerOne, playerOneCard), limitQueue(playerTwo, playerTwoCard)).who == 1) {
                    playerOne.add(playerOneCard);
                    playerOne.add(playerTwoCard);
                } else {
                    playerTwo.add(playerTwoCard);
                    playerTwo.add(playerOneCard);
                }
            } else {
                if (playerOneCard > playerTwoCard) {
                    playerOne.add(playerOneCard);
                    playerOne.add(playerTwoCard);
                } else {
                    playerTwo.add(playerTwoCard);
                    playerTwo.add(playerOneCard);
                }
            }
        }
        int sum = 0;
        if (playerOne.size() > playerTwo.size()) {
            int multipler = playerOne.size();
            while (playerOne.size() > 0) {
                sum += (playerOne.poll() * multipler);
                multipler--;
            }
        } else {
            int multipler = playerTwo.size();
            while (playerTwo.size() > 0) {
                sum += (playerTwo.poll() * multipler);
                multipler--;
            }
        }
        return new Win(playerOne.size() > playerTwo.size() ? 1 : -1, sum);
    }

    private static Queue<Integer> limitQueue(Queue<Integer> queue, int limit) {
        Queue<Integer> copy = new ArrayDeque<>();
        int index = 0;
        while (queue.size() > 0 && index < limit) {
            if (copy.peek() == null) {
                break;
            }
            copy.add(copy.poll());
            index++;
        }
        return copy;
    }

    private static class Win {
        private final int who;
        private final int sum;
        public Win(int who, int sum) {
            this.who = who;
            this.sum = sum;
        }
    }


}
