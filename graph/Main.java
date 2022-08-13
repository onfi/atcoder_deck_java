import java.util.*;
import java.io.*;

class Solver {
    static void solve(FScanner sc, FWriter out) {
        // https://atcoder.jp/contests/typical-algorithm/tasks/typical_algorithm_d
        int n = sc.nextInt(), m = sc.nextInt();
        Graph<Integer, String> g = new Graph<>();
        for(var i = 0; i < n; i++) {
            g.addNode(i);
        }
        for(var i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            long c = sc.nextLong();
            g.addOneway(u, v, c);
        }
        var dijkstra = g.dijkstra(0, n - 1);
        out.println(dijkstra.get(n - 1).cost);
    }
}

class Edge<K> {
    K to;
    long cost = 1;
    public Edge(K to, long cost) {
        this.to = to;
        this.cost = cost;
    }
}
class Route<K> implements Comparable<Route<K>> {
    K edge;
    List<Route<K>> routes;
    long cost;
    Route(K edge) {
        this.edge = edge;
        this.cost = 0L;
        this.routes = new ArrayList<Route<K>>();
        this.routes.add(this);
    }
    Route(K edge, long cost) {
        this.edge = edge;
        this.cost = cost;
    }
    Route(K edge, List<Route<K>> prevRoutes, long cost) {
        this.edge = edge;
        this.cost = cost;
        this.routes = new ArrayList<Route<K>>(prevRoutes);
        this.routes.add(this);
    }
    @Override
    public int compareTo(Route<K> o) {
        if(cost == o.cost) {
            return 0;
        } else if(cost < o.cost) {
            return -1;
        }
        return 1;
    }
}
class Graph<K extends Comparable<K>,V> {
    Map<K,V> nodes = new HashMap<>();
    Map<K,List<Edge<K>>> edges = new HashMap<>();
    public Graph(){}
    public Graph(Map<K,V> map) {
        this.nodes = map;
    }
    public V get(K key) {
        return nodes.get(key);
    }
    public Graph<K,V> addNode(K key, V value) {
        nodes.put(key, value);
        edges.put(key, new ArrayList<Edge<K>>());
        return this;
    }
    public Graph<K,V> addNode(K key) {
        return addNode(key, null);
    }
    public Graph<K,V> addOneway(K from, K to, long cost) {
        edges.computeIfAbsent(from, (f) -> new ArrayList<Edge<K>>()).add(new Edge<K>(to, cost));
        return this;
    }
    public Graph<K,V> addOneway(K from, K to) {
        return addOneway(from, to, 1);
    }
    public Graph<K,V> addTwoway(K from, K to, long cost) {
        return this.addOneway(from, to, cost).addOneway(to, from, cost);
    }
    public Graph<K,V> addTwoway(K from, K to) {
        return addTwoway(from, to, 1);
    }
    public Map<K,Route<K>> dijkstra(K from, K to, boolean enableRoute) {
        Map<K,Route<K>> result = new HashMap<>();
        Set<K> fixed = new HashSet<>();
        PriorityQueue<Route<K>> queue = new PriorityQueue<>((a, b) -> -a.compareTo(b));
        queue.add(new Route<K>(from));
        while(!queue.isEmpty()) {
            var route = queue.poll();
            if(fixed.contains(route.edge)) continue;
            fixed.add(route.edge);
            for(var target : edges.get(route.edge)) {
                if(fixed.contains(target.to)) continue;
                Route<K> newRoute;
                if(enableRoute) {
                    newRoute = new Route<K>(target.to, route.routes, route.cost + target.cost);
                } else {
                    newRoute = new Route<K>(target.to, route.cost + target.cost);
                }
                result.merge(target.to, newRoute, (a, b) -> {
                    if(a.compareTo(b) <= 0) {
                        return a;    
                    } else {
                        return b;
                    }
                });
                if(newRoute.equals(result.get(target.to))) {
                    queue.add(newRoute);
                }
            }
        }
        return result;
    }
    public Map<K,Route<K>> dijkstra(K from, K to) {
        return dijkstra(from, to, false);
    }
}

//common
public class Main {
    public static void main(String[] args) {
        FScanner sc = new FScanner(System.in);
        FWriter out = new FWriter(System.out);
        try {
            Solver.solve(sc, out);
        } catch (Throwable e) {
            out.println(e);
        }
        out.flush();
        sc.close();
    }
}

class TwoKeyMap<K,V> {
    Map<K, Map<K,V>> map = new HashMap<>();
    Set<K> key2Set = new HashSet<>();
    TwoKeyMap<K,V> put(K key1, K key2, V value) {
        key2Set.add(key2);
        map.computeIfAbsent(key1, (f) -> new HashMap<K,V>()).put(key2, value);
        return this;
    }
    TwoKeyMap<K,V> merge(K key1, K key2, V value, java.util.function.BiFunction<? super V,? super V,? extends V> remappingFunction) {
        key2Set.add(key2);
        map.computeIfAbsent(key1, (f) -> new HashMap<K,V>()).merge(key2, value, remappingFunction);
        return this;
    }
    V get(K key1, K key2) {
        var m1 = map.get(key1);
        if(m1 == null) return null;
        return m1.get(key2);
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
        println(e.getMessage());
        for (StackTraceElement el : e.getStackTrace()) {
            print("    ").println(el.toString());
        }
        if (e.getCause() != null) {
            println(e.getCause());
        }
        return this;
    }
}