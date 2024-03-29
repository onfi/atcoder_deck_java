import java.util.*;
import java.io.*;
import java.math.*;

class Solver {
    void solve(FScanner sc, FWriter out) {
        char[] S = sc.nextCharArray();
        BitSet combinations = new BitSet(10);
        Map<BitSet, Integer> map = new HashMap<>();
        map.merge((BitSet) combinations.clone(), 1, (a, b) -> a + b);

        for (var i = 0; i < S.length; i++) {
            combinations.set(S[i] - '0', !combinations.get(S[i] - '0'));
            map.merge((BitSet) combinations.clone(), 1, (a, b) -> a + b);
        }

        long result = 0;
        for (var cnt : map.values()) {
            result += MathLib.combinationAsLong(cnt, 2);
        }
        out.println(result);
    }
}

class XY {
    long x;
    long y;

    XY(long x, long y) {
        this.x = x;
        this.y = y;
    }

    double slope(XY b) {
        var dx = b.x - x;
        if (dx == 0) {
            if (b.y == y) {
                return 0;
            } else if (b.y - y > 0) {
                return Double.POSITIVE_INFINITY;
            }
            return Double.NEGATIVE_INFINITY;
        }
        return (1.0 * (b.y - y)) / dx;
    }

    double rad(XY b) {
        return Math.atan2(b.y - y, b.x - x);
    }

    double deg(XY b) {
        return MathLib.radToDeg(rad(b));
    }

    @Override
    public String toString() {
        return "XY [x=" + x + ", y=" + y + "]";
    }
}

class MathLib {
    static List<BigInteger> memoFactrial = new ArrayList<>(Arrays.asList(BigInteger.ONE));

    public static BigInteger factrial(int n) {
        for (int i = memoFactrial.size(); i <= n; i++) {
            memoFactrial.add(memoFactrial.get(i - 1).multiply(BigInteger.valueOf(i)));
        }
        return memoFactrial.get(n);
    }

    public static long factrialAsLong(int n) {
        return factrial(n).longValue();
    }

    static Map<Integer, List<BigInteger>> memoConbination = new TreeMap<>();

    public static BigInteger combination(int n, int r) {
        if (n < 1 || r < 1 || n < r)
            return BigInteger.ZERO;

        if (n == r)
            return BigInteger.ONE;

        // nが小さい場合は公式を使う
        if (n < 51000)
            return factrial(n).divide(factrial(r).multiply(factrial(n - r)));

        // nCr = nC(n - r)、計算量が少ない方を選ぶ
        if (r > n - r)
            r = n - r;
        List<BigInteger> result;
        if (!memoConbination.containsKey(n)) {
            // 初期値はnC0 = 0, nC1 = n
            result = new ArrayList<>(Arrays.asList(BigInteger.ZERO, BigInteger.valueOf(n)));
            memoConbination.put(n, result);
        } else {
            result = memoConbination.get(n);
        }
        for (int i = result.size(); i <= r; i++) {
            // nC(r) = nC(r - 1) * (n - r + 1) / r
            result.add(result.get(i - 1).multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i)));
        }
        return result.get(r);
    }

    public static long permutationsAsLong(int n, int r) {
        return permutations(n, r).longValue();
    }

    static Map<Integer, List<BigInteger>> memoPermutations = new TreeMap<>();

    public static BigInteger permutations(int n, int r) {
        if (n < 1 || r < 1 || n < r)
            return BigInteger.ZERO;

        if (n == r)
            return BigInteger.ONE;

        // nが小さい場合は公式を使う
        if (n < 51000)
            return factrial(n).divide(factrial(n - r));

        if (r > n - r)
            r = n - r;
        List<BigInteger> result;
        if (!memoPermutations.containsKey(n)) {
            // 初期値はnC0 = 0, nC1 = n
            result = new ArrayList<>(Arrays.asList(BigInteger.ZERO, BigInteger.valueOf(n)));
            memoPermutations.put(n, result);
        } else {
            result = memoPermutations.get(n);
        }
        for (int i = result.size(); i <= r; i++) {
            result.add(result.get(i - 1).multiply(BigInteger.valueOf(n - i + 1)));
        }
        return result.get(r);
    }

    public static long combinationAsLong(int n, int r) {
        return combination(n, r).longValue();
    }

    public static long gcd(long a, long b) {
        long c = b;
        b = a;
        do {
            a = b;
            b = c;
            if (a < b) {
                long tmp = a;
                a = b;
                b = tmp;
            }
            if (b == 0)
                return a;
            c = a % b;
        } while (c != 0);
        return b;
    }

    public static long lcm(long m, long n) {
        return m * n / gcd(m, n);
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return a.gcd(b);
    }

    public static BigInteger lcm(BigInteger m, BigInteger n) {
        return m.multiply(n).divide(gcd(m, n));
    }

    public static long sign(long x) {
        if (x == 0)
            return 0;
        if (x < 0)
            return -1;
        return 1;
    }

    public static long sign(double x) {
        if (x > -0.00001 && x < 0.00001)
            return 0;
        if (x < 0)
            return -1;
        return 1;
    }

    public static double radToDeg(double rad) {
        return rad * 180 / Math.PI;
    }

    public static double degToRad(double deg) {
        return deg / 180 * Math.PI;
    }

    // 等差数列の和
    // 初項a, 項数n, 公差d
    public static BigInteger arithmeticProgression(BigInteger a, BigInteger n, BigInteger d) {
        return (BigInteger.TWO.multiply(a).add(n.subtract(BigInteger.ONE).multiply(d))).multiply(n)
                .divide(BigInteger.TWO);
    }

    // 初項a, 末項l, 項数n
    public static BigInteger arithmeticProgression2(long a, long l, long n) {
        return (BigInteger.valueOf(a).add(BigInteger.valueOf(l))).multiply(BigInteger.valueOf(n))
                .divide(BigInteger.TWO);
    }

    // 行列乗算
    static long[][] mulMatlix(long[][] a, long[][] b) {
        return mulMatlix(a, b, Long.MAX_VALUE);
    }

    static long[][] mulMatlix(long[][] a, long[][] b, long mod) {
        int n = a.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                    res[i][j] %= mod;
                }
            }
        }
        return res;
    }

    // 行列累乗
    static long[][] powMatlix(long[][] a, long b) {
        return powMatlix(a, b, Long.MAX_VALUE);
    }

    static long[][] powMatlix(long[][] a, long b, long mod) {
        int n = a.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++)
            res[i][i] = 1;
        while (b > 0) {
            if ((b & 1) > 0)
                res = mulMatlix(res, a, mod);
            a = mulMatlix(a, a, mod);
            b >>= 1;
        }
        return res;
    }
}

// common

public class Main {
    public static void main(String[] args) {
        FScanner sc = new FScanner(System.in);
        FWriter out = new FWriter(System.out);
        try {
            (new Solver()).solve(sc, out);
        } catch (Throwable e) {
            out.println(e);
            out.flush();
            System.exit(1);
        }
        out.flush();
        sc.close();
    }
}

class Bsearch {
    static int search(int l, int r, java.util.function.IntPredicate func) {
        while (r - l > 1) {
            int mid = (l + r) / 2;
            if (func.test(mid)) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }
}

class TwoKeyMap<K, V> {
    Map<K, Map<K, V>> map = new HashMap<>();
    Set<K> _key2Set = new HashSet<>();

    TwoKeyMap<K, V> put(K key1, K key2, V value) {
        _key2Set.add(key2);
        map.computeIfAbsent(key1, (f) -> new HashMap<K, V>()).put(key2, value);
        return this;
    }

    TwoKeyMap<K, V> put(K[] key, V value) {
        return put(key[0], key[1], value);
    }

    TwoKeyMap<K, V> merge(K key1, K key2, V value,
            java.util.function.BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        _key2Set.add(key2);
        map.computeIfAbsent(key1, (f) -> new HashMap<K, V>()).merge(key2, value, remappingFunction);
        return this;
    }

    TwoKeyMap<K, V> merge(K[] key, V value,
            java.util.function.BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return merge(key[0], key[1], value, remappingFunction);
    }

    V get(K key1, K key2) {
        var m1 = map.get(key1);
        if (m1 == null)
            return null;
        return m1.get(key2);
    }

    Map<K, V> get(K key1) {
        return map.get(key1);
    }

    V get(K[] key) {
        return get(key[0], key[1]);
    }

    V computeIfAbsent(K key1, K key2, java.util.function.Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key1, (f) -> new HashMap<K, V>()).computeIfAbsent(key2, mappingFunction);
    }

    boolean containsKey(K key1, K key2) {
        return get(key1, key2) != null;
    }

    Set<K> key1Set() {
        return map.keySet();
    }

    Set<K> key2Set() {
        // 本来はインスタンス作るべきだが、競技プログラミング向けなのでパフォーマンス優先
        return _key2Set;
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
        return nextSB().toString();
    }

    public char[] nextCharArray() {
        StringBuilder sb = nextSB();
        char[] c = new char[sb.length()];
        sb.getChars(0, sb.length(), c, 0);
        return c;
    }

    public StringBuilder nextSB() {
        if (!hasNext())
            throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb;
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

    public int[] nextIntArray(int arraySize) {
        int[] ary = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ary[i] = nextInt();
        }
        return ary;
    }

    public long[] nextLongArray(int arraySize) {
        long[] ary = new long[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ary[i] = nextLong();
        }
        return ary;
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

    FWriter print(char c) {
        write((byte) c);
        return this;
    }

    FWriter println(char c) {
        print(c);
        println();
        return this;
    }

    FWriter print(int n) {
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
        return this;
    }

    FWriter println(int n) {
        print(n);
        println();
        return this;
    }

    FWriter print(long n) {
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
        return this;
    }

    FWriter println(long n) {
        print(n);
        println();
        return this;
    }

    FWriter print(String s) {
        if (s != null) {
            byte[] b = s.getBytes();
            write(b, 0, b.length);
        }
        return this;
    }

    FWriter println(String s) {
        print(s);
        println();
        return this;
    }

    FWriter print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (i > 0)
                write(SP);
            print(a[i]);
        }
        return this;
    }

    FWriter println(int[] a) {
        print(a);
        println();
        return this;
    }

    FWriter print(long[] a) {
        for (int i = 0; i < a.length; i++) {
            if (i > 0)
                write(SP);
            print(a[i]);
        }
        return this;
    }

    FWriter println(long[] a) {
        print(a);
        println();
        return this;
    }

    FWriter print(char[] s, int from, int to) {
        for (int i = from; i < to && s[i] != '\0'; i++) {
            print(s[i]);
        }
        return this;
    }

    FWriter print(char[] s) {
        print(s, 0, s.length);
        return this;
    }

    FWriter println(char[] s, int from, int to) {
        print(s, from, to);
        println();
        return this;
    }

    FWriter println(char[] s) {
        println(s, 0, s.length);
        return this;
    }

    FWriter print(double n, int accuracy) {
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
        return this;
    }

    FWriter print(double n) {
        print(n, 10);
        return this;
    }

    FWriter println(double n) {
        print(n);
        println();
        return this;
    }

    FWriter println(double n, int accuracy) {
        print(n, accuracy);
        println();
        return this;
    }

    FWriter print(Object o) {
        if (o != null) {
            print(o.toString());
        }
        return this;
    }

    FWriter println(Object o) {
        print(o);
        println();
        return this;
    }

    FWriter print(List<?> i) {
        if (i != null) {
            boolean space = false;
            for (var o : i) {
                if (space) {
                    print(' ');
                }
                print(o);
                space = true;
            }
        }
        return this;
    }

    FWriter println(List<?> i) {
        print(i);
        println();
        return this;
    }

    FWriter println(Throwable e) {
        println(e.toString());
        for (StackTraceElement el : e.getStackTrace()) {
            print("    ").println(el.toString());
        }
        if (e.getCause() != null) {
            println(e.getCause());
        }
        return this;
    }

    private void _debug(Object o, int indent) {
        if (o == null) {
            for (var i = 0; i < indent; i++)
                print(' ');
            print("null");
        } else if (o.getClass().isArray()) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(o); i++) {
                println();
                _debug(java.lang.reflect.Array.get(o, i), indent + 2);
            }
            return;
        } else if (o instanceof Collection) {
            for (var item : (Collection<?>) o) {
                println();
                _debug(item, indent + 2);
            }
        } else if (o instanceof Map) {
            for (var i = 0; i < indent; i++)
                print(' ');
            println('{');
            for (var entry : ((Map<?, ?>) o).entrySet()) {
                for (var i = 0; i < indent + 2; i++)
                    print(' ');
                _debug(entry.getKey(), 0);
                _debug("  ", 0);
                _debug(entry.getValue(), 0);
                println();
            }
            for (var i = 0; i < indent; i++)
                print(' ');
            println('}');
            return;
        }
        for (var i = 0; i < indent; i++)
            print(' ');
        print(o);
    }

    FWriter debug(Object... os) {
        print("[DEBUG:").print(Thread.currentThread().getStackTrace()[2].getLineNumber()).print("]:  ");
        for (var o : os) {
            _debug(o, 0);
            print(' ');
        }
        print("  :[DEBUG:").print(Thread.currentThread().getStackTrace()[2].getLineNumber()).println("]");
        return this;
    }
}