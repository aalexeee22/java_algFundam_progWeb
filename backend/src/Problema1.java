import java.io.*;
import java.util.*;

public class Problema1 {
    static final int INF = 1000000000;//o valoare mare cu care este initializata distanta minima la inceput
    static int n, m;//dimensiunile matricei
    static int[][] mat;//matricea cu obstacolele
    static int[][] dist;//matricea care
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static List<int[]> points = new ArrayList<>();

    //functie BFS pentru a calcula distantele minime
    static void bfs(int x, int y) {
        for (int i = 0; i <= n; i++)
            Arrays.fill(dist[i], INF);

        Queue<int[]> queue = new LinkedList<>();
        dist[x][y] = 0;
        queue.add(new int[]{x, y});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0], cy = current[1];

            for (int d = 0; d < 4; d++) {
                int nx = cx + dx[d];//coordonatele pozitiei curente
                int ny = cy + dy[d];//coordonatele pozitiei curente

                //verifica daca pozitia (nx, ny) este valida:
                //-este in limitele matricei (1 ≤ nx ≤ n și 1 ≤ ny ≤ m).
                //-este o celula accesibila (mat[nx][ny] == 0).
                //-nu a fost deja vizitata (dist[nx][ny] == INF).
                if (nx >= 1 && nx <= n && ny >= 1 && ny <= m && mat[nx][ny] == 0 && dist[nx][ny] == INF) {
                    dist[nx][ny] = dist[cx][cy] + 1;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("uploads/lee1.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("results/lee1.txt"));

        //citire date de intrare
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);

        mat = new int[n + 1][m + 1];
        dist = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            line = br.readLine().split(" ");
            for (int j = 1; j <= m; j++) {
                mat[i][j] = Integer.parseInt(line[j - 1]);
            }
        }

        line = br.readLine().split(" ");
        int i1 = Integer.parseInt(line[0]);
        int j1 = Integer.parseInt(line[1]);
        int i2 = Integer.parseInt(line[2]);
        int j2 = Integer.parseInt(line[3]);

        int k = Integer.parseInt(br.readLine());
        points.add(new int[]{i1, j1});
        for (int i = 0; i < k; i++) {
            line = br.readLine().split(" ");
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            points.add(new int[]{x, y});
        }
        points.add(new int[]{i2, j2});

        int sz = points.size();
        int[][] d = new int[sz][sz];

        // calculez distantele intre toate perechile de puncte folosind BFS
        for (int i = 0; i < sz; i++) {
            bfs(points.get(i)[0], points.get(i)[1]);
            for (int j = 0; j < sz; j++) {
                d[i][j] = dist[points.get(j)[0]][points.get(j)[1]];
            }
        }

        //generez permutarile pentru a gasi drumul minim
        List<Integer> perm = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            perm.add(i);
        }

        int minCost = INF;
        List<Integer> bestPath = new ArrayList<>();

        //testam toate permutarile posibile
        do {
            int currentCost = d[0][perm.get(0)];
            for (int i = 0; i < k - 1; i++) {
                currentCost += d[perm.get(i)][perm.get(i + 1)];
            }
            currentCost += d[perm.get(k - 1)][sz - 1];

            if (currentCost < minCost) {
                minCost = currentCost;
                bestPath = new ArrayList<>(perm);
            }
        } while (nextPermutation(perm));

        //scriem rezultatul in fisier
        pw.println(minCost);
        pw.printf("%d,%d\n", i1, j1);
        for (int idx : bestPath) {
            pw.printf("%d,%d\n", points.get(idx)[0], points.get(idx)[1]);
        }
        pw.printf("%d,%d\n", i2, j2);

        br.close();
        pw.close();
    }

    //functie pentru a genera urmatoarea permutare lexicografica
    static boolean nextPermutation(List<Integer> perm) {
        int i = perm.size() - 2;
        while (i >= 0 && perm.get(i) >= perm.get(i + 1))
            i--;

        if (i == -1)
            return false;

        int j = perm.size() - 1;
        while (perm.get(j) <= perm.get(i))
            j--;

        Collections.swap(perm, i, j);
        Collections.reverse(perm.subList(i + 1, perm.size()));
        return true;
    }
}
