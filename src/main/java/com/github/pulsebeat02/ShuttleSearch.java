package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShuttleSearch {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("shuttlesearch.txt"));
        int earliest = Integer.parseInt(br.readLine());
        List<Integer> buses = new ArrayList<>();
        String parse = br.readLine();
        for (String str : parse.split(",")) {
            buses.add(str.equals("x") ? -1 : Integer.parseInt(str));
        }
        br.close();
        System.out.println("Part One: " + partOne(buses, earliest));
        System.out.println("Part Two: " + partTwo(buses));
    }

    private static long partTwo(List<Integer> buses) {

        /*
         * Good Explanation Here: https://www.youtube.com/watch?v=zIFehsBHB8o
         * Parallel Lists
         */

        // Calculate bi
        List<Integer> bi = new ArrayList<>();
        int offset = 0;
        for (int bus : buses) {
            if (bus != -1) {
                bi.add(bus - offset);
            }
            offset++;
        }

        // Clear -1
        buses.removeIf(integer -> integer == -1);

        // Calculate N
        long N = 1;
        for (int bus : buses) {
            N *= bus;
        }

        // Calculate Ni
        List<Long> Ni = new ArrayList<>();
        for (int bus : buses) {
            Ni.add(N / bus);
        }

        // Calculate xi
        List<Long> xi = new ArrayList<>();
        for (int i = 0; i < buses.size(); i++) {
            int modulo = buses.get(i);
            long coefficient = Ni.get(i) % modulo;
            long factor = 1;
            while ((coefficient * factor) % modulo != 1) {
                factor++;
            }
            xi.add(factor);
        }

        // Calculate biNixi
        List<Long> biNixi = new ArrayList<>();
        for (int i = 0; i < buses.size(); i++) {
            biNixi.add(bi.get(i) * Ni.get(i) * xi.get(i));
        }

        // Add niNixi
        long sum = 0;
        for (long num : biNixi) {
            sum += num;
        }

        return sum % N;

    }

    private static int partOne(List<Integer> buses, int earliest) {
        int min = Integer.MAX_VALUE;
        int busType = -1;
        for (int bus : buses) {
            if (bus != -1) {
                int factor = 1;
                while (factor * bus < earliest) {
                    factor++;
                }
                int difference = factor * bus - earliest;
                if (difference < min) {
                    min = difference;
                    busType = bus;
                }
            }
        }
        return min * busType;
    }

}
