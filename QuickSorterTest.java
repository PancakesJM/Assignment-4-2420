package assign05;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class QuickSorterTest {
	RandomPivotChooser<Integer> randomPivot;
	FirstPivotChooser<Integer> firstPivot;
	ArrayList<Integer> integerArray;
	QuickSorter<Integer> quickSortFirst;
	QuickSorter<Integer> quickSortRandom;
	@BeforeEach
	void setUp() throws Exception {
		randomPivot = new RandomPivotChooser<Integer>();
		firstPivot = new FirstPivotChooser<Integer>();
		integerArray = new ArrayList<>();
		quickSortFirst = new QuickSorter<Integer>(firstPivot);
		quickSortRandom = new QuickSorter<Integer>(randomPivot);
		for(int i = 0; i < 10; i++)
			integerArray.add(i);
	}
	@Test
	public void quickSortReturnsSameArrayForSorted() {
		quickSortFirst.sort(integerArray);
		ArrayList<Integer> copy = new ArrayList<>(integerArray);
		assertEquals(integerArray, copy);
	}
	@Test
	public void quickSortWorksForRandomPivot() {
		quickSortRandom.sort(integerArray);
		ArrayList<Integer> copy = new ArrayList<>(integerArray);
		assertEquals(integerArray, copy);
	}

}
