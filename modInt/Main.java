import java.util.*;
import java.io.*;

public class Main {
    public static void solve(FScanner sc, FWriter out) {
        int n = sc.nextInt(), q = sc.nextInt();
        UnionFind unionFind = new UnionFind(n + 1);

        while (q-- > 0) {
            int p = sc.nextInt(), a = sc.nextInt(), b = sc.nextInt();
            if (p == 0) {
                unionFind.unite(a, b);
            } else {
                out.println(unionFind.same(a, b) ? "Yes" : "No");
            }
        }
    }

    public static void main(String[] args) {
        FScanner sc = new FScanner(System.in);
        FWriter out = new FWriter(System.out);
        solve(sc, out);
        out.flush();
        sc.close();
    }
}

class ModInt extends Number {
    static final int MOD = 1000000007;
    static final int MEMO_SIZE = 1024;
    static List<ModInt> memoFactrial = Arrays.asList(ModInt.valueOf(1));
    static int memoInverse[] = null;
    static ModInt memoModInt[] = new ModInt[MEMO_SIZE];
    int value;

    static {
        for (int i = 0; i < MEMO_SIZE; i++) {
            memoModInt[i] = new ModInt(i);
        }
    }

    static int add(int a, int b) {
        int result = a + b;
        return result > MOD ? result - MOD : result;
    }

    static int sub(int a, int b) {
        int result = a - b;
        return result < 0 ? result + MOD : result;
    }

    static int mul(int a, int b) {
        long result = (long) a * b;
        return (int) (result % MOD);
    }

    static int pow(int a, int b) {
        int r = 1;
        int x = a;
        while (b > 0) {
            if ((b & 1) == 1)
                r = mul(r, x);
            x = mul(x, x);
            b >>= 1;
        }
        return r;
    }

    static int inverse(int a) {
        if (memoInverse == null) {
            memoInverse = new int[MOD];
            Arrays.fill(memoInverse, -1);
        }
        if (memoInverse[a] < 0) {
            memoInverse[a] = pow(a, MOD - 2);
        }
        return memoInverse[a];
    }

    static int div(int a, int b) {
        return mul(a, inverse(b));
    }

    static ModInt factrial(int n) {
        for (int i = memoFactrial.size(); i < n; i++) {
            memoFactrial.add(memoFactrial.get(i - 1).mul(i));
        }
        return memoFactrial.get(n);
    }

    static Map<Integer, List<ModInt>> memoConbination = new TreeMap<>();

    static ModInt combination(int n, int r) {
        if (n < 1 || r < 1 || n < r)
            return ModInt.valueOf(0);

        if (n == r)
            return ModInt.valueOf(1);

        // nが小さい場合は公式を使う
        if (n < 51000)
            return factrial(n).div(factrial(r).mul(factrial(n - r)));

        // nCr = nC(n - r)、計算量が少ない方を選ぶ
        if (r > n - r)
            r = n - r;
        List<ModInt> result;
        if (!memoConbination.containsKey(n)) {
            // 初期値はnC0 = 0, nC1 = n
            result = Arrays.asList(ModInt.valueOf(0), ModInt.valueOf(n));
            memoConbination.put(n, result);
        } else {
            result = memoConbination.get(n);
        }
        for (int i = result.size(); i <= r; i++) {
            // nC(r) = nC(r - 1) * (n - r + 1) / r
            result.add(result.get(i - 1).mul(n - i + 1).div(i));
        }
        return result.get(r);
    }

    static Map<Integer, List<ModInt>> memoPermutations = new TreeMap<>();

    static ModInt permutations(int n, int r) {
        if (n < 1 || r < 1 || n < r)
            return ModInt.valueOf(0);

        if (n == r)
            return ModInt.valueOf(1);

        // nが小さい場合は公式を使う
        if (n < 51000)
            return factrial(n).div(factrial(n - r));

        // nCr = nC(n - r)、計算量が少ない方を選ぶ
        if (r > n - r)
            r = n - r;
        List<ModInt> result;
        if (!memoPermutations.containsKey(n)) {
            // 初期値はnC0 = 0, nC1 = n
            result = Arrays.asList(ModInt.valueOf(0), ModInt.valueOf(n));
            memoPermutations.put(n, result);
        } else {
            result = memoPermutations.get(n);
        }
        for (int i = result.size(); i <= r; i++) {
            // nC(r) = nC(r - 1) * (n - r + 1)
            result.add(result.get(i - 1).mul(n - i + 1));
        }
        return result.get(r);
    }

    static ModInt combination(Number n, Number r) {
        return combination(n, r);
    }

    ModInt add(int b) {
        return ModInt.valueOf(ModInt.add(value, b));
    }

    ModInt sub(int b) {
        return ModInt.valueOf(ModInt.sub(value, b));
    }

    ModInt mul(int b) {
        return ModInt.valueOf(ModInt.mul(value, b));
    }

    ModInt div(int b) {
        return ModInt.valueOf(ModInt.div(value, b));
    }

    ModInt add(Number b) {
        return add(b.intValue());
    }

    ModInt sub(Number b) {
        return sub(b.intValue());
    }

    ModInt mul(Number b) {
        return mul(b.intValue());
    }

    ModInt div(Number b) {
        return div(b.intValue());
    }

    static ModInt valueOf(int value) {
        if (value < MEMO_SIZE) {
            return memoModInt[value];
        }
        return new ModInt(value);
    }

    static ModInt valueOf(Number value) {
        return valueOf(value.intValue());
    }

    public ModInt(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return value;
    }

    public float floatValue() {
        return value;
    }

    public double doubleValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Number))
            return false;
        Number other = (Number) obj;
        return value == other.intValue();
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

    public java.util.stream.IntStream nextIntStream(int n) {
        return java.util.stream.IntStream.range(0, n).map(i -> nextInt());
    }

    public java.util.stream.LongStream nextLongStream(int n) {
        return java.util.stream.LongStream.range(0L, (long) n).map(i -> nextLong());
    }

    public java.util.stream.Stream<String> nextStream(int n) {
        return java.util.stream.IntStream.range(0, n).mapToObj(i -> next());
    }

    public java.util.stream.Stream<int[]> nextIntArrayStream(int n, int arraySize) {
        return java.util.stream.IntStream.range(0, n).mapToObj(_i -> {
            int[] ary = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                ary[i] = nextInt();
            }
            return ary;
        });
    }

    public java.util.stream.Stream<long[]> nextLongArrayStream(int n, int arraySize) {
        return java.util.stream.IntStream.range(0, n).mapToObj(_i -> {
            long[] ary = new long[arraySize];
            for (int i = 0; i < arraySize; i++) {
                ary[i] = nextLong();
            }
            return ary;
        });
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