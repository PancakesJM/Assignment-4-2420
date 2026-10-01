package assign05;

import java.util.ArrayList;
import java.util.Random;

public class RandomPivotChooser <E extends Comparable<? super E>> implements PivotChooser<E> {
	
	@Override
	public int getPivotIndex(ArrayList<E> list, int leftIndex, int rightIndex) {
		Random random = new Random();
		int index = random.nextInt(leftIndex, rightIndex + 1);
		return index;
	}

}
