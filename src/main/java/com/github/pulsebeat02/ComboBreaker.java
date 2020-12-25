package com.github.pulsebeat02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ComboBreaker {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("combobreaker.txt"));
        int cardPublicKey = Integer.parseInt(br.readLine());
        int doorPublicKey = Integer.parseInt(br.readLine());
        System.out.println("Part One: " + partOne(cardPublicKey, doorPublicKey));
    }

    private static long partOne(int cardPublicKey, int doorPublicKey) {
        return getEncryptionKey(cardPublicKey, getLoopSize(doorPublicKey));
    }

    private static long getLoopSize(long publicKey) {
        long value = 1;
        long count = 0;
        while (value != publicKey) {
            value *= 7;
            value %= 20201227;
            count++;
        }
        return count;
    }

    private static long getEncryptionKey(long publicKey, long count) {
        long val = 1;
        for (int i = 0; i < count; i++) {
            val *= publicKey;
            val %= 20201227;
        }
        return val;
    }

}
