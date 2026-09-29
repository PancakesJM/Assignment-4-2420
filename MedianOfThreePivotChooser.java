package assign05;

import java.util.ArrayList;

public class MedianOfThreePivotChooser<E extends Comparable<? super E>>
		implements PivotChooser<E> {

	@Override
	public int getPivotIndex(ArrayList<E> list,
			int leftIndex, int rightIndex) {

		int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;

		E left = list.get(leftIndex);
		E middle = list.get(middleIndex);
		E right = list.get(rightIndex);

		if(left.compareTo(middle) <= 0) {
			if(middle.compareTo(right) <= 0)
				return middleIndex;
			else if(left.compareTo(right) <= 0)
				return rightIndex;
			else
				return leftIndex;
		}
		else {
			if(left.compareTo(right) <= 0)
				return leftIndex;
			else if(middle.compareTo(right) <= 0)
				return rightIndex;
			else
				return middleIndex;
		}
	}
}