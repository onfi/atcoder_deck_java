import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int n = Integer.parseInt(br.readLine());
        String line = null;
        Set<String> words = new HashSet<>();
        boolean hontai = true;
        for(int i = 0; i < n; i++) {
            String newLine = br.readLine();
            boolean lose = false;
            if(line != null) {
                if(line.charAt(line.length() - 1) != newLine.charAt(0)) {
                    lose = true;
                };
            }
            if(words.contains(newLine)) lose = true;
            if(lose) {
                if(hontai) {
                    System.out.print("LOSE");
                } else {
                    System.out.print("WIN");
                }
                return;
            }
            words.add(newLine);
            line = newLine;
            hontai = !hontai;
        }
        System.out.print("DRAW");
    }
    
}