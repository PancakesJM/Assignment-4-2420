package assign04;

import java.util.Comparator;

/**
 * Provides utility methods and comparators for sorting, comparing,
 * and grouping positive integer values represented as strings.
 * @author Ava Murphy & Yujia Zhao
 * @version September 23, 2026
 */
public class IntegerStringUtility {
	
	
	/**
	 * Compares positive integer values represented as strings by their
	 * numerical values.
	 */
	public static class StringNumericalValueComparator implements Comparator<String> {
		
		/**
		 * Compares two strings based on the positive integer values they represent.
		 *
		 * @param string1 the first string to compare
		 * @param string2 the second string to compare
		 * @return a negative value if string1 is smaller, zero if they are equal,
		 *         or a positive value if string1 is larger
		 */
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
				if(string2.charAt(index2) == '0' && checkingZeros2) {
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
				return (string1.length() - index1) - (string2.length() - index2);
			}
			return 0;
		}
	}
	
	
	/**
	 * Compares strings by similarity. Strings are similar when their
	 * characters can be rearranged to form the same value.
	 */
	public static class StringSimilarityComparator implements Comparator<String> {

		/**
		 * Compares two strings based on similarity.
		 *
		 * @param o1 the first string to compare
		 * @param o2 the second string to compare
		 * @return a negative value if o1 comes before o2, zero if they are similar,
		 *         or a positive value if o1 comes after o2
		 */
	    @Override
	    public int compare(String o1, String o2) {
	        int firstLength = o1.length(), secondLength = o2.length();
	        if(firstLength != secondLength)
	            return (firstLength - secondLength);

	        Character[] firstChars = new Character[firstLength];
	        Character[] secondChars = new Character[secondLength];

	        //converts the strings to Character arrays
	        for(int i = 0; i < firstLength; i++) {
	            firstChars[i] = o1.charAt(i);
	            secondChars[i] = o2.charAt(i);
	        }

	        //sorts the Character arrays using natural ordering
	        insertionSort(firstChars, (char1, char2) -> char1.compareTo(char2));
	        insertionSort(secondChars, (char1, char2) -> char1.compareTo(char2));

	        //compares the sorted characters lexicographically
	        for(int i = 0; i < firstLength; i++) {
	            if(firstChars[i].compareTo(secondChars[i]) != 0)
	                return firstChars[i].compareTo(secondChars[i]);
	        }

	        return 0;
	    }   
	}
	
	
	/**
	 * Compares similarity groups by their sizes. If two groups have the
	 * same size, their largest numerical values are compared.
	 */
	public static class StringSimilarityGroupComparator implements Comparator<String[]> {

		/**
		 * Compares two similarity groups by group size and largest numerical value.
		 *
		 * @param o1 the first similarity group
		 * @param o2 the second similarity group
		 * @return a negative value if o1 is smaller, zero if the groups are equal,
		 *         or a positive value if o1 is larger
		 */
	    @Override
	    public int compare(String[] o1, String[] o2) {

	        //compares the sizes of the similarity groups
	        if(o1.length != o2.length)
	            return o1.length - o2.length;

	        //if both groups are empty, they are equal
	        if(o1.length == 0)
	            return 0;

	        StringNumericalValueComparator comparator = new StringNumericalValueComparator();

	        //finds the largest numerical value in each group
	        String max1 = findMax(o1, comparator);
	        String max2 = findMax(o2, comparator);

	        return comparator.compare(max1, max2);
	    }
	}
	
	
	/**
	 * Sorts the given array using insertion sort and the given comparator.
	 *
	 * @param <E> the type of elements in the array
	 * @param array the array to sort
	 * @param comparator the comparator used to order the elements
	 */
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
	
	
	/**
	 * Finds the largest element in the given array according to the given
	 * comparator without modifying the original array.
	 *
	 * @param <E> the type of elements in the array
	 * @param array the array to search
	 * @param comparator the comparator used to compare the elements
	 * @return the largest element in the array
	 */
	public static <E> E findMax(E[] array, Comparator<? super E> comparator) {
		E[] copy = array.clone();

        insertionSort(copy, comparator);

        return copy[copy.length - 1];
	}
	
	
	/**
	 * Groups similar strings from the given array without modifying the
	 * original array.
	 *
	 * @param array the array of strings to group
	 * @return a two-dimensional array in which each row is a similarity group
	 */
	public static String[][] getSimilarityGroups(String[] array) {
	    // Make a copy so the original array is not changed.
	    String[] copy = array.clone();

	    // Sort strings so similar strings are next to each other.
	    insertionSort(copy, new StringSimilarityComparator());

	    // Handle empty array.
	    if(copy.length == 0)
	        return new String[0][];

	    // Count the number of similarity groups.
	    int groupCount = 1;

	    StringSimilarityComparator comparator = new StringSimilarityComparator();

	    for(int i = 1; i < copy.length; i++) {
	        if(comparator.compare(copy[i - 1], copy[i]) != 0)
	            groupCount++;
	    }

	    String[][] groups = new String[groupCount][];

	    // Find the size of each group and create each row.
	    int groupIndex = 0;
	    int start = 0;

	    for(int i = 1; i <= copy.length; i++) {
	        if(i == copy.length || comparator.compare(copy[i - 1], copy[i]) != 0) {
	            int groupSize = i - start;
	            groups[groupIndex] = new String[groupSize];

	            for(int j = 0; j < groupSize; j++)
	                groups[groupIndex][j] = copy[start + j];

	            groupIndex++;
	            start = i;
	        }
	    }

	    return groups;
	}

	
	/**
	 * Finds the largest similarity group in the given array of integers
	 * without modifying the original array.
	 *
	 * @param array the array of integers
	 * @return the largest similarity group as an array of strings
	 */
	public static String[] findMaximumSimilarityGroup(int[] array) {
	    // Convert the integers into strings.
	    String[] strings = new String[array.length];

	    for(int i = 0; i < array.length; i++)
	        strings[i] = Integer.toString(array[i]);

	    // Create all similarity groups.
	    String[][] groups = getSimilarityGroups(strings);

	    // Handle empty input.
	    if(groups.length == 0)
	        return new String[0];

	    // Find and return the largest similarity group.
	    return findMax(groups, new StringSimilarityGroupComparator());
	}
}