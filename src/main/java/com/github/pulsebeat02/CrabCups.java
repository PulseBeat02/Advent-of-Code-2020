package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrabCups {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("crabcups.txt"));
        List<Integer> cups = new ArrayList<>();
        String line = br.readLine();
        for (char c : line.toCharArray()) {
            cups.add(Character.getNumericValue(c));
        }
        System.out.println("Part One: " + partOne(cups));
        System.out.println("Part Two: " + partTwo(cups));
    }

    private static long partTwo(List<Integer> list) {
        CircularLinkedHashList<Integer, CircularLinkedHashList.EntryNode> circle = new CircularLinkedHashList<>();
        for (int num : list) {
            circle.add(num);
        }
        int min = getMinimumLabel(list);
        int max = getMaximumLabel(list);
        for (int i = max + 1; i <= 1000000; i++) {
            circle.add(i);
        }
        CircularLinkedHashList.EntryNode current = circle.current;
        Map<Integer, CircularLinkedHashList.EntryNode> map = circle.map;
        int moves = 0;
        while (moves < 10000000) {
            CircularLinkedHashList<Integer, CircularLinkedHashList.EntryNode> removedThree = circle.popPrecedentThree(current);
            Set<Integer> removed = new HashSet<>(Arrays.asList(removedThree.current.key, removedThree.current.next.key, removedThree.current.next.next.key));
            int destCup = current.key - 1;
            destCup = destCup < min ? 1000000 : destCup;
            while (removed.contains(destCup)) {
                destCup = destCup - 1 < min ? 1000000 : destCup - 1;
            }
            circle.addAll(map.get(destCup), removedThree);
            current = current.next;
            moves++;
        }
        return (long) map.get(1).next.key * (long) map.get(1).next.next.key;
    }

    private static String partOne(List<Integer> list) {
        List<Integer> cups = new LinkedList<>(list);
        int min = getMinimumLabel(cups);
        int max = getMaximumLabel(cups);
        int moves = 0;
        int index = 0;
        int size = cups.size();
        while (moves < 100) {
            List<Integer> pickup = new ArrayList<>();
            int offset = 0;
            for (int i = 1; i <= 3; i++) {
                int pickupCupIndex = ((index + i) % size);
                pickup.add(cups.get(pickupCupIndex));
                if (pickupCupIndex < index) {
                    offset++;
                }
            }
            cups.removeAll(pickup);
            int bound = cups.get((index - offset + 1) % (size - pickup.size()));
            int destCup = cups.get(index - offset) - 1;
            destCup = destCup < min ? max : destCup;
            while (pickup.contains(destCup)) {
                destCup = destCup - 1 < min ? max : destCup - 1;
            }
            cups.addAll(cups.indexOf(destCup) + 1, pickup);
            index = cups.indexOf(bound);
            moves++;
        }
        int start = cups.indexOf(1);
        StringBuilder concatenate = new StringBuilder();
        for (int i = 1; i < size; i++) {
            concatenate.append(cups.get((start + i) % size));
        }
        return concatenate.toString();
    }

    private static int getMaximumLabel(Collection<Integer> cups) {
        int max = Integer.MIN_VALUE;
        for (int cup : cups) {
            max = Math.max(cup, max);
        }
        return max;
    }

    private static int getMinimumLabel(Collection<Integer> cups) {
        int min = Integer.MAX_VALUE;
        for (int cup : cups) {
            min = Math.min(cup, min);
        }
        return min;
    }

    private static class CircularLinkedHashList<K, V> {

        private final Map<Integer, EntryNode> map;
        private int size;
        private EntryNode head;
        private EntryNode tail;
        private EntryNode current;

        public CircularLinkedHashList() {
            this.map = new HashMap<>();
            this.size = 0;
            this.head = null;
            this.tail = null;
            this.current = null;
        }

        private void add(int num) {
            if (size == 0) {
                head = new EntryNode(num);
                tail = head;
                current = head;
                tail.next = head;
                map.put(num, current);
            } else {
                EntryNode last = tail;
                EntryNode next = new EntryNode(num);
                last.next = next;
                next.next = head;
                tail = next;
                map.put(num, next);
            }
            size++;
        }

        private CircularLinkedHashList<K, V> popPrecedentThree(EntryNode node) {
            CircularLinkedHashList<K, V> circular = new CircularLinkedHashList<>();
            EntryNode pop = node.next;
            EntryNode offer = pop;
            circular.add(pop.key);
            map.remove(pop.key);
            for (int i = 0; i < 2; i++) {
                offer = offer.next;
                circular.add(offer.key);
                map.remove(offer.key);
            }
            if (offer == tail) {
                tail = node;
                tail.next = head;
            } else if (offer == head) {
                head = head.next;
                tail = node;
                tail.next = head;
            } else if (pop == tail) {
                head = head.next.next;
                tail = node;
                tail.next = head;
            } else if (pop == head) {
                head = head.next.next.next;
                tail.next = head;
            } else {
                node.next = offer.next;
            }
            size -= 3;
            return circular;
        }

        private void addAll(EntryNode pos, CircularLinkedHashList<K, V> l) {
            if (pos == tail) {
                EntryNode oldTail = tail;
                tail = l.tail;
                tail.next = head;
                oldTail.next = l.head;
            } else {
                EntryNode oldNext = pos.next;
                pos.next = l.head;
                l.tail.next = oldNext;
            }
            map.putAll(l.map);
            size += l.size;
        }

        private static class EntryNode {
            private final int key;
            private EntryNode next;

            public EntryNode(int num) {
                this.key = num;
            }
        }

    }

}
