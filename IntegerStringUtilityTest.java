package assign04;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assign04.IntegerStringUtility.StringNumericalValueComparator;

/**
 * Test class for IntegerStringUtility
 * 
 * @author Ava Murphy & Yujia Zhao
 * @version September 20, 2026
 */
public class IntegerStringUtilityTest {
	Integer[] intArray;
	String[] stringArray;
	Random random = new Random();
	int randomInt;
	Comparator<Integer> simpleNumerical;
	StringNumericalValueComparator numerical;
	@BeforeEach
	void setUp() throws Exception {
		intArray = new Integer[20];
		for(int i = 0; i < intArray.length; i++) {
			randomInt = random.nextInt(20);
			intArray[i] = randomInt;
		}
		simpleNumerical = new Comparator<Integer>() {
		    @Override
		    public int compare(Integer num1, Integer num2) {
		        return num1.compareTo(num2);
		    }
		};
		numerical = new StringNumericalValueComparator();
		stringArray = new String[4];
		stringArray[0] = "149";
		stringArray[1] = "024";
		stringArray[2] = "1578";
		stringArray[3] = "2354";
	}
	
	@Test
	public void insertionSortReturnsNumericalSort(){
		IntegerStringUtility.insertionSort(intArray, simpleNumerical);
		assertTrue(simpleNumerical.compare(intArray[0], intArray[1]) < 0);
		System.out.print(Arrays.toString(intArray));
	}
	
	@Test
	public void maxReturnsBiggestNum() {
		Integer maxNum = IntegerStringUtility.findMax(intArray, simpleNumerical);
		for(int i = intArray.length -1; i <= 0; i--)
			assertTrue(simpleNumerical.compare(maxNum, i) > 0);
	}
	
	@Test
	public void StringNumericalValueComparatorHasCorrectOrder(){
		IntegerStringUtility.insertionSort(stringArray, numerical);
		System.out.println(Arrays.toString(stringArray));
	}
}

