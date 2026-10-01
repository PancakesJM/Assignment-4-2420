package assign05;

import java.util.ArrayList;

/**
 * Sorts an ArrayList using quicksort with a provided pivot selection strategy.
 *
 * @param <E> the type of elements in the list
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
     * Sorts the provided list into ascending order using quicksort.
     *
     * @param list the list to sort
     */
	@Override
	public void sort(ArrayList<E> list) {
		int max = list.size() - 1;
		sortRecursive(list, 0, max);
	}

	/**
	 * Recursively sorts the portion of the list between the given indices.
	 *
	 * @param list the list being sorted
	 * @param min the first index of the portion to sort
	 * @param max the last index of the portion to sort
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
	 * Partitions a portion of the list around the selected pivot value.
	 * Elements smaller than the pivot are moved before it and elements larger
	 * than the pivot are moved after it.
	 *
	 * @param pivot the index of the selected pivot
	 * @param list the list being partitioned
	 * @param minIndex the first index of the portion to partition
	 * @param maxIndex the last index of the portion to partition
	 * @return the final index of the pivot
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
