package assign05;

import java.util.ArrayList;

public class QuickSorter <E extends Comparable<? super E>> implements Sorter<E> {
	private PivotChooser<E> pivot;
	public QuickSorter(PivotChooser<E> chooser) {
		this.pivot = chooser;
	}
    /**
     * Driver method that calls Quick Sort on provided array
     * @param - Generic array to sort
     */
	@Override
	public void sort(ArrayList<E> list) {
		int max = list.size() - 1;
		sortRecursive(list, 0, max);
	}
	
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
	 * @param pivot
	 * @param list
	 * @param minIndex
	 * @param maxIndex
	 */
	private int partition(int pivot, ArrayList<E> list,
	        int minIndex, int maxIndex) {

	    E pivotValue = list.get(pivot);

	    // Move the pivot to the end.
	    E temp = list.get(maxIndex);
	    list.set(maxIndex, pivotValue);
	    list.set(pivot, temp);

	    int left = minIndex;
	    int right = maxIndex - 1;

	    while(left <= right) {

	        // Find an element on the left that belongs on the right.
	        while(left <= right &&
	                list.get(left).compareTo(pivotValue) < 0) {
	            left++;
	        }

	        // Find an element on the right that belongs on the left.
	        while(left <= right &&
	                list.get(right).compareTo(pivotValue) > 0) {
	            right--;
	        }

	        // Swap the two misplaced elements.
	        if(left <= right) {
	            temp = list.get(left);
	            list.set(left, list.get(right));
	            list.set(right, temp);

	            left++;
	            right--;
	        }
	    }

	    // Move the pivot to its final position.
	    temp = list.get(left);
	    list.set(left, pivotValue);
	    list.set(maxIndex, temp);

	    return left;
	}
}