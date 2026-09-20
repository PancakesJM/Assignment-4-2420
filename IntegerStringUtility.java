package assign04;

public class IntegerStringUtility {
  public static class StringNumericalValueComparator implements Comparator<String> {
		
	}
	
	public static class StringSimilarityComparator implements Comparator<String> {
		
	}
	
	public static class StringSimilarityGroupComparator implements Comparator<String[]> {
		
	}

	public static <E> void insertionSort(E[], Comparator<? super E>) {
		 for (int i = 1; i < array.length; i++) {
            E current = array[i];
            int j = i - 1;

            while (j >= 0 && comparator.compare(array[j], current) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = current;
        }
	}
	
	public static <E> E findMax(E[], Comparator<? super E>) {
		E[] copy = array.clone();

        insertionSort(copy, comparator);

        return copy[copy.length - 1];
	}
	
	public static String[][] getSimilarityGroups(String[]) {
		
	}
	
	public static String[] findMaximumSimilarityGroup(int[]) {
		
	}
}
