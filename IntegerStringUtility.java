package assign04;

import java.util.Comparator;

public class IntegerStringUtility {
	
	public static class StringNumericalValueComparator implements Comparator<String> {
		@Override
		public int compare(String string1, String string2) {
			//This has a lot of lines of code and should probably be simplified. Let me know if you have any 
			//questions or ideas for this one.
			int index1 = 0, index2 = 0;
			boolean checkingZeros1 = true, checkingZeros2 = true;
			while(index1 < string1.length() && index2 < string2.length()) {
				//checks if leading zeros are present and increases string index to ignore them.
				if(string1.charAt(index1) == '0' && checkingZeros1) {
					index1++;
					continue;
				}
				if(string1.charAt(index2) == '0' && checkingZeros2) {
					index2++;
					continue;
				}
				checkingZeros1 = false;
				checkingZeros2 = false;
				//if the string lengths are equal
				if((string1.length() - index1) == (string2.length() - index2)) {
					// restarts loop if characters are equal, returns if otherwise
					if(string1.charAt(index1) == string2.charAt(index2)) {
						index1++;
						index2++;
						continue;
					}
					Character firstChar = string1.charAt(index1), secondChar = string2.charAt(index2);
					return firstChar.compareTo(secondChar);
				}
				//in the case of strings being different sizes, the smaller one is less than the larger one.
				return string1.length() - string2.length();
			}
			return 0;
		}
	}
	public static class StringSimilarityComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			int firstLength = o1.length(), secondLength = o2.length();
			if(firstLength != secondLength)
				return (firstLength - secondLength);
			
			return 0;
		}	
	}
	
	public static class StringSimilarityGroupComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] o1, String[] o2) {
			
			return 0;
		}
	}

	public static <E> void insertionSort(E[] array, Comparator<? super E> comparator) {
		 for(int i = 1; i < array.length; i++) {
			 E current = array[i];
             int j = i - 1;

             while (j >= 0 && comparator.compare(array[j], current) > 0) {
                 array[j + 1] = array[j];
                 j--;
             }

             array[j + 1] = current;
         }
	}
	
	public static <E> E findMax(E[] array, Comparator<? super E> comparator) {
		E[] copy = array.clone();

        insertionSort(copy, comparator);

        return copy[copy.length - 1];
	}
	
	public static String[][] getSimilarityGroups(String[] array) {
		
	}
	
	public static String[] findMaximumSimilarityGroup(int[] array) {
		
	}
}
