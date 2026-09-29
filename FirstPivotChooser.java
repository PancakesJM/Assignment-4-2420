package assign05;

import java.util.ArrayList;

public class FirstPivotChooser<E extends Comparable<? super E>>
		implements PivotChooser<E> {

	@Override
	public int getPivotIndex(ArrayList<E> list,
			int leftIndex, int rightIndex) {

		return leftIndex;
	}
}