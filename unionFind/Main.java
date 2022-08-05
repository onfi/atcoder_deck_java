import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int cnt = 0;
        int a = 0, b = 0;
        Pattern p = Pattern.compile("AB");
        while(n-- > 0) {
            String s = sc.next();
            Matcher m = p.matcher(s);
            int index = 0;
            while(m.find(index)) {
                cnt++;
                index = m.end();
            }
            if(s.startsWith("B")) {
                b++;
            }
            if(s.endsWith("A")) {
                a++;
            }
        }
        System.out.println(cnt + Math.min(Math.min(a, b), n - 1));
    }
}
