import java.util.*;
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
        int n = sc.nextInt(), q = sc.nextInt();
        UnionFind unionFind = new UnionFind(n + 1);

        while(q-- > 0) {
            int p = sc.nextInt(), a = sc.nextInt(), b = sc.nextInt();
            if(p == 0) {
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
        for(int i = 0; i < length; i++) {
            parents[i] = i;
        }
        Arrays.fill(counts, 1);
    }

    public int root(int x) {
        int tmp = x;
        while(tmp != parents[tmp]) {
            tmp = parents[tmp];
        }
        return parents[x] = tmp;
    }

    public void unite(int x, int y) {
        int rootX = root(x);
        int rootY = root(y);
        if (rootX == rootY) return;
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