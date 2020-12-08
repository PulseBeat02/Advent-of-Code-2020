import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordPhilosophy {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("passwordphilosophy.txt"));
        List<Password> passwords = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            String[] full = line.split(" ");
            String[] nums = full[0].split("-");
            int min = Integer.parseInt(nums[0]);
            int max = Integer.parseInt(nums[1]);
            char character = full[1].charAt(0);
            String passcode = full[2];
            passwords.add(new Password(passcode, character, min, max));
            line = br.readLine();
        }
        br.close();
        System.out.println("Part One: " + partOne(passwords));
        System.out.println("Part Two: " + partTwo(passwords));
    }

    public static int partOne(List<Password> passwords) {
        int result = 0;
        for (Password pass : passwords) {
            String passcode = pass.passcode;
            int count = 0;
            for (char c : passcode.toCharArray()) {
                if (pass.c == c) {
                    count++;
                }
            }
            if (count >= pass.min && count <= pass.max) {
                result++;
            }
        }
        return result;
    }

    public static int partTwo(List<Password> passwords) {
        int result = 0;
        for (Password pass : passwords) {
            String passcode = pass.passcode;
            if (passcode.charAt(pass.min - 1) == pass.c && passcode.charAt(pass.max - 1) != pass.c) {
                result++;
            } else if (passcode.charAt(pass.max - 1) == pass.c && passcode.charAt(pass.min - 1) != pass.c) {
                result++;
            }
        }
        return result;
    }

    public static class Password {
        private final String passcode;
        private final char c;
        private final int min;
        private final int max;
        public Password(String passcode, char c, int min, int max) {
            this.passcode = passcode;
            this.c = c;
            this.min = min;
            this.max = max;
        }
    }

}
