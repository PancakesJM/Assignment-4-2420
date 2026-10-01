package assign05;

import java.util.ArrayList;

/**
 * Sorts an ArrayList using quicksort with a PivotChooser object
 * @author Ava Murphy & Yujia Zhao
 * @version September 30, 2026
 */
public class QuickSorter <E extends Comparable<? super E>> implements Sorter<E> {
	private PivotChooser<E> pivot;

	/**
	 * Constructs a quick sorter using the specified pivot chooser.
	 *
	 * @param chooser the strategy used to choose pivot indices
	 */
	public QuickSorter(PivotChooser<E> chooser) {
		this.pivot = chooser;
	}

     /**
     * Mthod that calls Quick Sort on provided array
     * @param - Generic array to sort
     */
	@Override
	public void sort(ArrayList<E> list) {
		int max = list.size() - 1;
		sortRecursive(list, 0, max);
	}

	/**
	 * Recursively sorts the list between min and max indeces
	 *
	 * @param list- the list being sorted
	 * @param min- the first index of sub array
	 * @param max the last index of sub array
	 */
	private void sortRecursive(ArrayList<E> list, int min, int max) {
		
		if(min >= max)
			return;
		
		//Partitions into two parts along a pivot
		int pivot = this.pivot.getPivotIndex(list, min, max);
		int value = partition(pivot, list, min, max);
		
		//Recursively sorts the first segment of array
		if(min < value)
			sortRecursive(list, min, value - 1);
		//recursively sorts second segment of array
		if(min < max - 1)
			sortRecursive(list, value + 1, max);
	}

	/**
	 * Helper method called by sortRecursive for sorting array around a pivot point
	 * @param pivot- point for array to be sorted around
	 * @param list- arraylist being sorted
	 * @param minIndex- smallest index being compared
	 * @param maxIndex- largest index being compared
	 */
	private int partition(int pivot, ArrayList<E> list, int minIndex, int maxIndex) {
	    E pivotValue = list.get(pivot);
	    // Move the pivot to the end.
	    E temp = list.get(maxIndex);
	    list.set(maxIndex, pivotValue);
	    list.set(pivot, temp);
	    int left = minIndex;
	    int right = maxIndex - 1;

	    while(left <= right) {
	        // Find an element out of place on the left
	        while(left <= right &&
	                list.get(left).compareTo(pivotValue) < 0) {
	            left++;
	        }
	        // Find an element out of place on the right
	        while(left <= right &&
	                list.get(right).compareTo(pivotValue) > 0) {
	            right--;
	        }
	        // Swap the misplaced elements.
	        if(left <= right) {
	            temp = list.get(left);
	            list.set(left, list.get(right));
	            list.set(right, temp);
	            left++;
	            right--;
	        }
	    }
	    // Move the pivot back
	    temp = list.get(left);
	    list.set(left, pivotValue);
	    list.set(maxIndex, temp);

	    return left;
	}
}
