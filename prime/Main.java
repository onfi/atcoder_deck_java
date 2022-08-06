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
}

class Prime {
    private static long initialized = 0;
    private static Map<Long, LinkedHashMap<Long, Integer>> memo = new HashMap<>();
    private static List<Integer> primes = new ArrayList<>();
    private static ArrayList<Boolean> isPrimes = new ArrayList<>();

    private static void init(long n) {
        long sqrtN = (long) Math.floor(Math.sqrt(n)) * 2l;
        if (sqrtN <= initialized)
            return;
        isPrimes.ensureCapacity((int) sqrtN);
        for (long i = isPrimes.size(); i <= sqrtN; i++) {
            isPrimes.add(true);
        }
        for (int i = Math.max(2, (int) initialized + 1); i <= sqrtN; i++) {
            if (isPrimes.get(i)) {
                for (int j = 2 * i; j <= sqrtN; j += i) {
                    isPrimes.set(j, false);
                }
                primes.add(i);
            }
        }
        initialized = sqrtN;
    }

    public static LinkedHashMap<Long, Integer> factorize(long n) {
        if (!memo.containsKey(n)) {
            init(n);
            LinkedHashMap<Long, Integer> result = new LinkedHashMap<>();
            int i = 0;
            long _n = n;
            for (int a = primes.get(i); (long) a * a <= n; a = primes.get(++i)) {
                if (n % a != 0)
                    continue;

                while (_n % a == 0) {
                    if (!result.containsKey((long) a))
                        result.put((long) a, 0);
                    int hoge = (result.get((long) a) + 1);
                    result.put((long) a, hoge);
                    _n /= a;
                }
            }
            if (_n != 1)
                result.put(_n, 1);
            memo.put(n, result);
        }
        return memo.get(n);
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

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public java.math.BigDecimal nextDecimal() {
        return new java.math.BigDecimal(next());
    }

    public boolean close() {
        return true;
    }
}

class FWriter {
    OutputStream out;

    byte[] buf = new byte[1 << 16];
    byte[] ibuf = new byte[20];

    int tail = 0;

    final byte SP = (byte) ' ', LF = (byte) '\n', HYPHEN = (byte) '-';

    FWriter(OutputStream out) {
        this.out = out;
    }

    void flush() {
        try {
            out.write(buf, 0, tail);
            tail = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void write(byte b) {
        buf[tail++] = b;
        if (tail == buf.length) {
            flush();
        }
    }

    void write(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    void println() {
        write(LF);
    }

    void print(char c) {
        write((byte) c);
    }

    void println(char c) {
        print(c);
        println();
    }

    void print(int n) {
        if (n < 0) {
            n = -n;
            write(HYPHEN);
        }

        int i = ibuf.length;
        do {
            ibuf[--i] = (byte) (n % 10 + '0');
            n /= 10;
        } while (n > 0);

        write(ibuf, i, ibuf.length - i);
    }

    void println(int n) {
        print(n);
        println();
    }

    void print(long n) {
        if (n < 0) {
            n = -n;
            write(HYPHEN);
        }

        int i = ibuf.length;
        do {
            ibuf[--i] = (byte) (n % 10 + '0');
            n /= 10;
        } while (n > 0);

        write(ibuf, i, ibuf.length - i);
    }

    void println(long n) {
        print(n);
        println();
    }

    void print(String s) {
        byte[] b = s.getBytes();
        write(b, 0, b.length);
    }

    void println(String s) {
        print(s);
        println();
    }

    void print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            write(SP);
            print(a[i]);
        }
    }

    void println(int[] a) {
        print(a);
        println();
    }

    void print(char[] s, int from, int to) {
        for (int i = from; i < to && s[i] != '\0'; i++) {
            print(s[i]);
        }
    }

    void print(char[] s) {
        print(s, 0, s.length);
    }

    void println(char[] s, int from, int to) {
        print(s, from, to);
        println();
    }

    void println(char[] s) {
        println(s, 0, s.length);
    }

    void print(double n, int accuracy) {
        long longN = (long) n;
        print(longN);
        n -= (long) n;

        write((byte) '.');
        for (int j = 0; j < accuracy; j++) {
            n *= 10;
            int digit = (int) n;
            write((byte) (digit + '0'));
            n -= digit;
        }
    }

    void print(double n) {
        print(n, 10);
    }

    void println(double n) {
        print(n);
        println();
    }

    void println(double n, int accuracy) {
        print(n, accuracy);
        println();
    }

    void print(Object o) {
        if (o != null) {
            print(o.toString());
        }
    }

    void println(Object o) {
        print(o);
        println(o);
    }
}