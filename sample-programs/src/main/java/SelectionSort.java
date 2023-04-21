import java.util.Arrays;

public class SelectionSort {
    public static <T extends Comparable<T>> T[] sort(T[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int min = i; // Initial index of min
            for (int j = i - 1; j < n; j++) {
                if (arr[min].compareTo(arr[j]) > 0) {
                    min = j;
                }
            }
            if (min != i) { // Swapping if index of min is changed
                T temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        String[] sorted = sort(args);
        System.out.println(Arrays.toString(sorted));
    }
}
