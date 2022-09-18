import java.util.*;
import java.io.*;

class Solver {
    void solve(FScanner sc, FWriter out) {
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
}

class UnionFind {
    public int[] parents, counts;

    public UnionFind(int length) {
        this.parents = new int[length];
        this.counts = new int[length];
        for (int i = 0; i < length; i++) {
            parents[i] = i;
        }
        Arrays.fill(counts, 1);
    }

    public int root(int x) {
        int tmp = x;
        while (tmp != parents[tmp]) {
            tmp = parents[tmp];
        }
        return parents[x] = tmp;
    }

    public void unite(int x, int y) {
        int rootX = root(x);
        int rootY = root(y);
        if (rootX == rootY)
            return;
        counts[rootY] += counts[rootX];
        parents[rootX] = rootY;
    }

    public boolean same(int x, int y) {
        return root(x) == root(y);
    }

    public int count(int x) {
        return counts[root(x)];
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
            System.exit(1);
        } finally {
            out.flush();
            sc.close();
        }
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

    boolean isDebug = false;

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

    void enableDebug() {
        this.isDebug = true;
    }

    private void _debug(Object o, int indent) {
        if(o == null) {
            for(var i = 0; i < indent; i++) print(' ');
            print("null");
        } else if(o.getClass().isArray()) {
            for(int i = 0; i < java.lang.reflect.Array.getLength(o); i++) {
                println();
                _debug(java.lang.reflect.Array.get(o, i), indent + 2);
            }
            return;
        } else if(o instanceof Collection) {
            for(var item : (Collection<?>)o) {
                println();
                _debug(item, indent + 2);
            }
        } else if(o instanceof Map) {
            for(var i = 0; i < indent; i++) print(' ');
            println('{');
            for(var entry : ((Map<?,?>)o).entrySet()) {
                for(var i = 0; i < indent + 2; i++) print(' ');
                _debug(entry.getKey(), 0);
                _debug("  ", 0);
                _debug(entry.getValue(), 0);
                println();
            }
            for(var i = 0; i < indent; i++) print(' ');
            println('}');
            return;
        }
        for(var i = 0; i < indent; i++) print(' ');
        print(o);
    }

    FWriter debug(Object... os) {
        if(!isDebug) return this;
        print("[DEBUG:").print(Thread.currentThread().getStackTrace()[2].getLineNumber()).print("]:  ");
        for(var o : os) {
            _debug(o, 0);
            print(' ');
        }
        print("  :[DEBUG:").print(Thread.currentThread().getStackTrace()[2].getLineNumber()).println("]");
        return this;
    }
}