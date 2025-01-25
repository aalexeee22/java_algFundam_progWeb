import java.io.*;
import java.util.*;

public class Problema3 {
    public static void main(String[] args) {
        try {
            //se citesc datele din fisierul interclasari.txt
            BufferedReader reader = new BufferedReader(new FileReader("uploads/interclasari.txt"));
            int n = Integer.parseInt(reader.readLine().trim());
            String[] dimensiuniString = reader.readLine().trim().split("\\s+");
            reader.close();

            // coada de prioritati pentru interclasare
            int[] dimensiuni = new int[n];
            for (int i = 0; i < n; i++) {
                dimensiuni[i] = Integer.parseInt(dimensiuniString[i]);
            }

            // implementarea cozii de prioritati (Min-Heap) utilizand PriorityQueue
            PriorityQueue<Integer> heap = new PriorityQueue<>();
            for (int dim : dimensiuni) {
                heap.add(dim);
            }

            long costTotal = 0;

            //se interclaseaza sirurile pana ramane un singur sir
            while (heap.size() > 1) {
                //se extrag cele mai mici doua dimensiuni
                int d1 = heap.poll();
                int d2 = heap.poll();

                //se calculeaza costul interclasarii
                int cost = d1 + d2;
                costTotal += cost;

                //se adauga noul sir in coada
                heap.add(cost);
            }

            //se scrie rezultatul in fisierul interclasari.txt
            BufferedWriter writer = new BufferedWriter(new FileWriter("results/interclasari.txt"));
            writer.write(String.valueOf(costTotal));
            writer.close();

        } catch (IOException e) {
            System.err.println("A apărut o eroare la citirea sau scrierea fișierelor: " + e.getMessage());
        }
    }
}
