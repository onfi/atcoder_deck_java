import java.util.*;
import java.io.*;
import java.math.*;

class Solver {
    void solve(FScanner sc, FWriter out) {
        // out.enableDebug();
        int n = sc.nextInt();
        int H[] = sc.nextIntArray(n);
        long A[] = sc.nextLongArray(n);

        var seg = SegmentTree.RMQ(new long[n + 1]);

        for(var i = 0; i < n; i++) {
            seg.update(H[i], seg.query(0, H[i]) + A[i]);
        }
        out.println(seg.query(0, n + 1));
    }
}

class SegmentTree {
    long[] tree;
    Long initValue;
    int size;
    java.util.function.LongBinaryOperator processingFunction;

    SegmentTree(long[] v, Long initValue, java.util.function.LongBinaryOperator processingFunction) {
        this.initValue = initValue;
        this.processingFunction = processingFunction;
        size = v.length;

        var n = 1;
        while (n < size) {
            n <<= 1;
        }

        tree = new long[2 * n];
        Arrays.fill(tree, initValue);
        
        // 直接値が入るのは、index + (n - 1)
        for (int i = 0; i < size; i++) {
            tree[i + n - 1] = v[i];
        }

        // 親：(i - 1) / 2
        // 左の子：2 * i + 1
        // 右の子：左の子 + 1
        for (int i = n - 2; i >= 0; i--) {
            updateNode(i);
        }
    }

    static SegmentTree RmQ(long[] v) {
        return new SegmentTree(v, Long.MAX_VALUE, Math::min);
    }

    static SegmentTree RMQ(long[] v) {
        return new SegmentTree(v, Long.MIN_VALUE, Math::max);
    }

    static SegmentTree RSQ(long[] v) {
        return new SegmentTree(v, 0L, (a,b) -> a + b);
    }

    static SegmentTree RSQMod(long[] v, int mod) {
        return new SegmentTree(v, 0L, (a,b) -> (a + b) % mod);
    }

    // i番目の値をvalに変更する
    void update(int i, Long val) {
        i = i + tree.length / 2 - 1;
        tree[i] = val;

        for (i = (i - 1) / 2; i > 0; i = (i - 1) / 2) {
            updateNode(i);
        }
        updateNode(i);
    }
    
    void updateNode(int i) {
        tree[i] = processingFunction.applyAsLong(tree[2 * i + 1], tree[2 * i + 2]);
    }

    Long query(int a, int b) {
        Long val_left = initValue, val_right = initValue;
        for (a += (tree.length / 2 - 1), b += (tree.length / 2 - 1); a < b; a >>= 1, b >>= 1) {
            if ((a & 1) == 0) {
                val_left = processingFunction.applyAsLong(val_left, tree[a]);
            }
            if ((b & 1) == 0) {
                val_right = processingFunction.applyAsLong(val_right, tree[--b]);
            }
        }
        return processingFunction.applyAsLong(val_left, val_right);
    }
}


class MathLib {
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
            if(b == 0) return a;
            c = a % b;
        } while (c != 0);
        return b;
    }

    public static long lcm(long m, long n) {
        return m * n / gcd(m, n);
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