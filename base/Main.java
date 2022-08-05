import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), q = sc.nextInt();
        UnionFind unionFind = new UnionFind(n + 1);

        while(q-- > 0) {
            int p = sc.nextInt(), a = sc.nextInt(), b = sc.nextInt();
            if(p == 0) {
                unionFind.unite(a, b);
            } else {
                System.out.println(unionFind.same(a, b) ? "Yes" : "No");
            }
        }
        sc.close();
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