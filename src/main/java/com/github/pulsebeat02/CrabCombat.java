package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CrabCombat {

    private static final Queue<Integer> winner = new ArrayDeque<>();

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
        System.out.println("Part Two: " + partTwo(first, second));
    }

    private static int partOne(List<Integer> first, List<Integer> second) {
        Queue<Integer> playerOne = new ArrayDeque<>(first);
        Queue<Integer> playerTwo = new ArrayDeque<>(second);
        while (playerOne.size() > 0 && playerTwo.size() > 0) {
            assert playerOne.peek() != null;
            int playerOneCard = playerOne.poll();
            assert playerTwo.peek() != null;
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

    private static int partTwo(List<Integer> first, List<Integer> second) {
        recursiveBattle(new ArrayDeque<>(first), new ArrayDeque<>(second), winner, first.size(), second.size());
        int sum = 0;
        int multipler = winner.size();
        while (winner.size() > 0) {
            sum += (winner.poll() * multipler);
            multipler--;
        }
        return sum;
    }

    private static Deque<Integer> recursiveBattle(Deque<Integer> first, Deque<Integer> second, Queue<Integer> winner, int offsetOne, int offsetTwo) {
        Deque<Integer> playerOne = new ArrayDeque<>(first);
        Deque<Integer> playerTwo = new ArrayDeque<>(second);
        for (int i = playerOne.size() - offsetOne; i > 0; i--) {
            playerOne.removeLast();
        }
        for (int i = playerTwo.size() - offsetTwo; i > 0; i--) {
            playerTwo.removeLast();
        }
        Set<String> history = new HashSet<>();
        while (playerOne.size() > 0 && playerTwo.size() > 0) {
            if (!history.add(playerOne.toString() + playerTwo.toString())) {
                return first;
            }
            assert playerOne.peek() != null;
            int playerOneCard = playerOne.poll();
            assert playerTwo.peek() != null;
            int playerTwoCard = playerTwo.poll();
            if (playerOne.size() >= playerOneCard && playerTwo.size() >= playerTwoCard) {
                if (recursiveBattle(playerOne, playerTwo, null, playerOneCard, playerTwoCard) == playerOne) {
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
        if (winner != null) {
            winner.addAll(playerTwo.isEmpty() ? playerOne : playerTwo);
        }
        return playerTwo.isEmpty() ? first : second;
    }

}