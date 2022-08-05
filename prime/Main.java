import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        FScanner sc = new FScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        solve(sc, out);
        out.flush();
        sc.close();
    }
    
    public static void solve(FScanner sc, PrintWriter out) {
        out.println(Prime.factorize(sc.nextLong()).entrySet().stream().map(entry -> {
            return entry.getKey() + ":" + entry.getValue();
        }).collect(Collectors.joining(",")));
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

class FScanner {
    private InputStream in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;

    FScanner(InputStream in) {
        this.in = in;
    }

    private boolean hasNextByte() {
        if (ptr < buflen) {
            return true;
        } else {
            ptr = 0;
            try {
                buflen = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (buflen <= 0) {
                return false;
            }
        }
        return true;
    }

    private int readByte() {
        if (hasNextByte())
            return buffer[ptr++];
        else
            return -1;
    }

    private static boolean isPrintableChar(int c) {
        return 33 <= c && c <= 126;
    }

    private void skipUnprintable() {
        while (hasNextByte() && !isPrintableChar(buffer[ptr]))
            ptr++;
    }

    public boolean hasNext() {
        skipUnprintable();
        return hasNextByte();
    }

    public String next() {
        if (!hasNext())
            throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }

    public int nextInt() {
        if (!hasNext())
            throw new NoSuchElementException();
        int n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while (b != -1 && isPrintableChar(b)) {
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            } else {
                throw new NumberFormatException();
            }
            b = readByte();
        }
        return minus ? -n : n;
    }

    public long nextLong() {
        if (!hasNext())
            throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while (b != -1 && isPrintableChar(b)) {
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            } else {
                throw new NumberFormatException();
            }
            b = readByte();
        }
        return minus ? -n : n;
    }

    public boolean close() {
        return true;
    }
}