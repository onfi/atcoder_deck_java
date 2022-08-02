import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Arrays.asList(br.readLine().split(" ")).stream().map(Long::parseLong).forEach(n -> {
            System.out.println(Prime.factorize(n).entrySet().stream().map(entry -> {
                return entry.getKey() + ":" + entry.getValue();
            }).collect(Collectors.joining(",")));
        });
    }

    public static class Prime {
        private static long initialized = 0;
        private static Map<Long, LinkedHashMap<Long, Integer>> memo = new HashMap<>();
        private static List<Integer> primes = new ArrayList<>();
        private static ArrayList<Boolean> isPrimes = new ArrayList<>();

        private static void init(long n) {
            long sqrtN = (long) Math.floor(Math.sqrt(n)) * 2l;
            if(sqrtN <= initialized) return;
            isPrimes.ensureCapacity((int)sqrtN);
            for( long i = isPrimes.size(); i <= sqrtN; i++) {
                isPrimes.add(true);
            }
            for( int i = Math.max(2, (int) initialized + 1); i <= sqrtN; i++ ) {
                if( isPrimes.get(i) ) {
                    for( int j = 2 * i; j <= sqrtN; j += i ) {
                        isPrimes.set(j, false);
                    }
                    primes.add(i);
                }
            }
            initialized = sqrtN;
        }
        public static LinkedHashMap<Long, Integer> factorize(long n) {
            if(!memo.containsKey(n)) {
                init(n);
                LinkedHashMap<Long, Integer> result = new LinkedHashMap<>();
                int i = 0;
                long _n = n;
                for(int a = primes.get(i); (long)a * a <= n; a = primes.get(++i)) {
                    if(n % a != 0) continue;
                    
                    while (_n % a == 0) {
                        if(!result.containsKey((long)a)) result.put((long)a, 0);
                        int hoge = (result.get((long)a) + 1);
                        result.put((long)a, hoge);
                        _n /= a;
                    }
                }
                if(_n != 1) result.put(_n, 1);
                memo.put(n, result);
            }
            return memo.get(n);
        }
    }
}
