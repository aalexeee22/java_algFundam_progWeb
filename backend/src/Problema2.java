import java.io.*;
import java.util.*;
//#1264
public class Problema2 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("uploads/statisticiordine.txt"));
        String[] firstLine = reader.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int k = Integer.parseInt(firstLine[1]);

        String[] numbers = reader.readLine().split(" ");
        long[] array = new long[n];
        for (int i = 0; i < n; i++) {
            array[i] = Long.parseLong(numbers[i]);
        }

        //folosesc randomizedQuickselect
        long result = randomizedQuickSelect(array, 0, n - 1, k - 1);

        // scriu rezultatul in fisier
        PrintWriter writer = new PrintWriter(new FileWriter("results/statisticiordine.txt"));
        writer.println(result);
        writer.close();
    }

    //definirea functiei randomizedQuickselect
    public static long randomizedQuickSelect(long[] array, int left, int right, int k) {
        if (left == right) {
            return array[left];
        }
        //aleg un pivot aleator
        int pivotIndex = left + new Random().nextInt(right - left + 1);
        pivotIndex = partition(array, left, right, pivotIndex);

        if (k == pivotIndex) {
            return array[k];
        } else if (k < pivotIndex) {
            return randomizedQuickSelect(array, left, pivotIndex - 1, k);
        } else {
            return randomizedQuickSelect(array, pivotIndex + 1, right, k);
        }
    }
    // functie de partitionare
    public static int partition(long[] array, int left, int right, int pivotIndex) {
        long pivot = array[pivotIndex];
        swap(array, pivotIndex, right);
        int i = left;

        for (int j = left; j < right; j++) {
            if (array[j] < pivot) {
                swap(array, i, j);
                i++;
            }
        }

        swap(array, i, right);
        return i;
    }
    //functie de interschimbare
    public static void swap(long[] array, int i, int j) {
        long temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
