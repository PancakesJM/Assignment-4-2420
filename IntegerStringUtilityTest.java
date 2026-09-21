package assign04;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for IntegerStringUtility
 * 
 * @author Ava Murphy & Yujia Zhao
 * @version September 20, 2026
 */
public class IntegerStringUtilityTest {
	Integer[] intArray;
	Random random = new Random();
	int randomInt;
	Comparator<Integer> numerical;
	@BeforeEach
	void setUp() throws Exception {
		intArray = new Integer[20];
		for(int i = 0; i < intArray.length; i++) {
			randomInt = random.nextInt(20);
			intArray[i] = randomInt;
		}
		numerical = new Comparator<Integer>() {
		    @Override
		    public int compare(Integer num1, Integer num2) {
		        return num1.compareTo(num2);
		    }
		};
	}
	
	@Test
	public void insertionSortReturnsNumericalSort(){
		IntegerStringUtility.insertionSort(intArray, numerical);
		assertTrue(numerical.compare(intArray[0], intArray[1]) < 0);
		System.out.print(Arrays.toString(intArray));
	}
	
	@Test
	public void maxReturnsBiggestNum() {
		Integer maxNum = IntegerStringUtility.findMax(intArray, numerical);
		for(int i = intArray.length -1; i <= 0; i--)
			assertTrue(numerical.compare(maxNum, i) > 0);
	}
	
}
